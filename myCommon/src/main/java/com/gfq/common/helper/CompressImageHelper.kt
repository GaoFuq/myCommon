package com.gfq.common.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.gfq.common.system.loge
import com.gfq.common.utils.cacheDir
import com.gfq.common.utils.formatSizeInMB
import com.gfq.common.utils.getFileName
import com.gfq.common.utils.getMimeType
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 *  2022/5/6 11:42
 * @auth gaofuq
 * @description
 */
object CompressImageHelper {

    @JvmStatic
    fun compress(
        path: String,
        saveDir: String = cacheDir.path,
        fileSaveName: String = "compressed_${path.getFileName()}",
    ): String {

        if (!File(path).exists()) throw RuntimeException("源文件不存在，path = $path")

        val mimeType = path.getMimeType() ?: return path

        if (mimeType == "image/gif") {
            return path
        }
        if (!mimeType.contains("image")) {
            return path
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = computeSize(options.outWidth, options.outHeight)
        options.inJustDecodeBounds = false
        val tagBitmap = BitmapFactory.decodeFile(path, options)
        val stream = ByteArrayOutputStream()
        val focusAlpha = false
        tagBitmap.compress(if (focusAlpha) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
            70,
            stream)
        tagBitmap.recycle()
        val compressPath = "$saveDir/$fileSaveName"
        val dir = File(saveDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        loge("file compressed size = ${
            formatSizeInMB(stream.size().toLong())
        } , compressPath = $compressPath")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(compressPath)
            fos.write(stream.toByteArray())
            fos.flush()
            fos.close()
            stream.close()
        } catch (e: Exception) {
            try {
                fos?.close()
                stream.close()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        } finally {
            try {
                fos?.close()
                stream.close()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
        return compressPath
    }

    @JvmStatic
    private fun computeSize(srcWidth: Int, srcHeight: Int): Int {
        var _srcWidth = srcWidth
        var _srcHeight = srcHeight
        _srcWidth = if (_srcWidth % 2 == 1) _srcWidth + 1 else _srcWidth
        _srcHeight = if (_srcHeight % 2 == 1) _srcHeight + 1 else _srcHeight
        val longSide = Math.max(_srcWidth, _srcHeight)
        val shortSide = Math.min(_srcWidth, _srcHeight)
        val scale = shortSide.toFloat() / longSide
        return if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                1
            } else if (longSide < 4990) {
                2
            } else if (longSide > 4990 && longSide < 10240) {
                4
            } else {
                if (longSide / 1280 == 0) 1 else longSide / 1280
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (longSide / 1280 == 0) 1 else longSide / 1280
        } else {
            Math.ceil(longSide / (1280.0 / scale)).toInt()
        }
    }


}