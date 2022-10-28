package com.gfq.common.utils

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.loge
import okhttp3.*
import java.io.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 *  2022/1/28 15:16
 * @auth gaofuq
 * @description
 */
/**
externalCacheDir    : /storage/emulated/0/Android/data/com.mmj.android/cache
cacheDir            : /data/user/0/com.mmj.android/cache
filesDir            : /data/user/0/com.mmj.android/files
externalMusic       : /storage/emulated/0/Android/data/com.mmj.android/files/Music
externalDCIM        : /storage/emulated/0/Android/data/com.mmj.android/files/DCIM
externalDownload    : /storage/emulated/0/Android/data/com.mmj.android/files/Download
externalPicture     : /storage/emulated/0/Android/data/com.mmj.android/files/Pictures
externalMovies      : /storage/emulated/0/Android/data/com.mmj.android/files/Movies
 */
val externalCacheDir by lazy { ActivityManager.application.externalCacheDir }
val cacheDir by lazy { ActivityManager.application.cacheDir }
val filesDir by lazy { ActivityManager.application.filesDir }

val externalMusic by lazy { ActivityManager.application.getExternalFilesDir(Environment.DIRECTORY_MUSIC) }
val externalDCIM by lazy { ActivityManager.application.getExternalFilesDir(Environment.DIRECTORY_DCIM) }
val externalDownload by lazy { ActivityManager.application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) }
val externalPicture by lazy { ActivityManager.application.getExternalFilesDir(Environment.DIRECTORY_PICTURES) }
val externalMovies by lazy { ActivityManager.application.getExternalFilesDir(Environment.DIRECTORY_MOVIES) }

/**
 * 下载文件。如果文件是 图片/视频/音频 ，下载完成后会自动插入MediaStore媒体库中，并刷新图库。
 * @param url 文件地址
 * @param saveDir 文件保存的目录路径，默认[externalDownload]
 * @param saveFileName 文件保存名称，默认原名称
 * @param autoOpen 文件下载完成后，是否自动打开
 * @param isInsertMediaStore 如果下载的文件是图片、音频、视频，是否插入到图库
 * @param onProgress 下载进度回调
 * @param success 下载成功回调
 * @param failed 下载失败回调
 */
fun downloadFile(
    url: String,
    saveDir: String? = externalDownload?.path,
    saveFileName: String? = url.getFileName(),
    autoOpen: Boolean = false,
    isOverrideOldFile: Boolean = true,
    isInsertMediaStore: Boolean = true,
    onProgress: ((Int) -> Unit)? = null,
    success: ((file: File) -> Unit)? = null,
    failed: (() -> Unit)? = null,
) {
    val TAG = "【downloadFile】"
    val saveDirTp = if (saveDir.isNullOrEmpty()) {
        Log.d(TAG, "saveDir isNullOrEmpty , changed to default dir")
        externalDownload?.path
    } else {
        saveDir
    }

    if (saveFileName.isNullOrEmpty()) {
        Log.d(TAG, "saveFileName isNullOrEmpty , $saveFileName")
        failed?.let { mainThread { it.invoke() } }
        return
    }

    val oldFile = File(saveDirTp + File.separator + saveFileName)

    if (oldFile.exists() && !isOverrideOldFile) {
        Log.d(TAG, "exist oldFile = ${oldFile.path}")
        success?.invoke(oldFile)
        if (autoOpen) {
            oldFile.open()
        }
        return
    }
    saveDirTp?.let {
        val dir = File(it)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    Log.d(TAG, "start url = $url")
    val startTime = System.currentTimeMillis()
    val okHttpClient = OkHttpClient()
    val request: Request = Request.Builder().url(url).build()
    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d(TAG, "failed ${e.message}")
            failed?.let { mainThread { it.invoke() } }
            e.printStackTrace()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            var `is`: InputStream? = null
            val buf = ByteArray(2048)
            var len = 0
            var fos: FileOutputStream? = null
            try {
                `is` = response.body!!.byteStream()
                val total = response.body!!.contentLength()
                val file = File(saveDirTp, saveFileName)
                fos = FileOutputStream(file)
                var sum: Long = 0
                while (`is`.read(buf).also { len = it } != -1) {
                    fos.write(buf, 0, len)
                    sum += len.toLong()
                    val progress = (sum * 1.0f / total * 100).toInt()
                    // 下载中
                    onProgress?.let { mainThread { it.invoke(progress) } }
                }
                fos.flush()
                mainThread { success?.invoke(file) }

                Log.d(TAG,
                    "success cost time = ${(System.currentTimeMillis() - startTime)}, save path = ${file.path}")

                if (file.canInsertMediaStore() && isInsertMediaStore) {
                    file.insertMediaStore()
                }
                if (autoOpen) {
                    file.open()
                }
            } catch (e: Exception) {
                Log.d(TAG, "failed onResponse handle failed ${e.message}")
                mainThread { failed?.invoke() }
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    })
}

fun getFileNameFromUrl(url: String): String? {
    return url.getFileName()
}

fun String?.getFileName(): String? = this?.substring(this.lastIndexOf("/") + 1)

fun File?.canInsertMediaStore(): Boolean {
    this ?: return false
    val mimeType = getMimeType() ?: return false
    return mimeType.contains("audio")
            || mimeType.contains("video")
            || mimeType.contains("image")
}

fun File?.insertMediaStore() {
    this ?: return
    val mimeType = getMimeType() ?: return
    val tableUri = when {
        mimeType.contains("image") -> {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        mimeType.contains("video") -> {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
        mimeType.contains("audio") -> {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        else -> null
    } ?: return

    var os: OutputStream? = null
    val values = ContentValues()
    values.put(MediaStore.Images.Media.DISPLAY_NAME, name)
    // 将该索引信息插入数据表，获得图片的Uri
    val newUri = ActivityManager.application.contentResolver.insert(tableUri, values) ?: return
    try {
        os = ActivityManager.application.contentResolver.openOutputStream(newUri);
        os?.write(this.readBytes())
        os?.flush()
    } catch (e: Exception) {
        e.printStackTrace();
    } finally {
        try {
            os?.close()
        } catch (e: IOException) {
        }
    }
}


/**
 * 获取扩展名
 */
fun String?.getExtension(): String = MimeTypeMap.getFileExtensionFromUrl(this)

/**
 * 获取扩展名
 */
fun Uri?.getExtension(): String = this?.path.getExtension()

/**
 * 获取扩展名
 */
fun File?.getExtension(): String = this?.path.getExtension()

/**
 * 获取 MimeType
 */
fun String?.getMimeType(): String? =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension())

/**
 * 获取 MimeType
 */
fun File?.getMimeType(): String? = this?.path.getMimeType()

/**
 * 获取 MimeType
 */
fun Uri?.getMimeType(): String? = this?.path.getMimeType()


fun File?.open() {
    this ?: return
    val ctx = ActivityManager.application
    val intent = Intent().apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        action = Intent.ACTION_VIEW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri =
                FileProvider.getUriForFile(ctx, ctx.packageName + ".fileProvider", this@open)
            setDataAndType(contentUri, getMimeType())
        } else {
            setDataAndType(Uri.fromFile(this@open), getMimeType())
        }
    }
    ActivityManager.application.startActivity(intent);
}


fun formatSizeInMB(sizeInBytes: Long): Float {
    val df = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
    df.applyPattern("0.0")
    var result = df.format((sizeInBytes.toFloat() / 1024 / 1024).toDouble())
    loge("getSizeInMB: $result M")
    val resultKB = df.format((sizeInBytes.toFloat() / 1024).toDouble())
    loge("getSizeInKB: $resultKB KB")
    result = result.replace(",".toRegex(), ".") // in some case , 0.0 will be 0,0
    return java.lang.Float.valueOf(result)
}

fun formatSizeInKB(sizeInBytes: Long): Float {
    val df = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
    df.applyPattern("0.0")
    var result = df.format((sizeInBytes.toFloat() / 1024).toDouble())
    loge("getSizeInKB: $result KB")
    result = result.replace(",".toRegex(), ".") // in some case , 0.0 will be 0,0
    return java.lang.Float.valueOf(result)
}