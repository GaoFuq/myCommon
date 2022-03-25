package com.gfq.common.utils

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.gfq.common.system.ActivityManager
import com.gfq.common.system.log
import okhttp3.*
import java.io.*

/**
 *  2022/1/28 15:16
 * @auth gaofuq
 * @description
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
 * @param onProgress 下载进度回调
 * @param success 下载成功回调
 * @param failed 下载失败回调
 */
fun downloadFile(
    url: String,
    saveDir: String? = externalDownload?.path,
    saveFileName: String? = url.getFileName(),
    autoOpen: Boolean = false,
    onProgress: ((Int) -> Unit)? = null,
    success: ((file: File) -> Unit)? = null,
    failed: (() -> Unit)? = null,
) {
    val oldFile = File(saveDir + File.separator + saveFileName)
    if (oldFile.exists() && autoOpen) {
        log("downloadFile exist oldFile = ${oldFile.path}")
        success?.invoke(oldFile)
        oldFile.open()
        return
    }
    val startTime = System.currentTimeMillis()
    log("downloadFile startTime = $startTime")
    val okHttpClient = OkHttpClient()
    val request: Request = Request.Builder().url(url).build()
    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            log("downloadFile failed ")
            mainThread { failed?.invoke() }
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
                val file = File(saveDir, saveFileName)
                fos = FileOutputStream(file)
                var sum: Long = 0
                while (`is`.read(buf).also { len = it } != -1) {
                    fos.write(buf, 0, len)
                    sum += len.toLong()
                    val progress = (sum * 1.0f / total * 100).toInt()
                    // 下载中
                    mainThread { onProgress?.invoke(progress) }
                }
                fos.flush()
                mainThread { success?.invoke(file) }

                log("downloadFile success cost time = ${(System.currentTimeMillis() - startTime)}")

                if (file.canInsertMediaStore()) {
                    file.insertMediaStore()
                }
                if (autoOpen) {
                    file.open()
                }
            } catch (e: Exception) {
                log("downloadFile failed onResponse handle failed")
                mainThread { failed?.invoke() }
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                } catch (e: IOException) {
                }
                try {
                    fos?.close()
                } catch (e: IOException) {
                }
            }
        }
    })
}

fun getFileNameFromUrl(url: String): String {
    return url.getFileName() ?: ""
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


