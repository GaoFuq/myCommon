package com.gfq.common.net

import com.gfq.common.system.loge
import com.gfq.common.utils.mainThread
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * 1.上传进度回调<br>
 * 2.间隔30ms，防止频繁回调，上层无用的刷新<br>
 */
class RequestBodyProgress(
    private val delegate: RequestBody?,
    private val intervals: Long = 30L,
    private val onProgress: ((progress: Long) -> Unit),
) : RequestBody() {
    constructor(delegate: RequestBody, onProgress: (progress: Long) -> Unit) :
            this(delegate, 30L, onProgress)


    override fun contentType(): MediaType? = delegate?.contentType()

    override fun writeTo(sink: BufferedSink) {
        delegate?.let {
            val countingSink = CountingSink(sink)
            val bufferedSink = countingSink.buffer()
            delegate.writeTo(bufferedSink)
            bufferedSink.flush()
        }

    }

    /**
     * 重写调用实际的响应体的contentLength
     */
    override fun contentLength(): Long {
        return try {
            delegate?.contentLength() ?: -1
        } catch (e: IOException) {
            loge(e.message)
            -1
        }
    }


    inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
        private var bytesWritten: Long = 0
        private var contentLength: Long = 0 //总字节长度，避免多次调用contentLength()方法
        private var lastRefreshUiTime: Long = 0//最后一次刷新的时间

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (contentLength <= 0) contentLength = contentLength() //获得contentLength的值，后续不再调用
            //增加当前写入的字节数
            bytesWritten += byteCount
            val curTime = System.currentTimeMillis()
            //每 intervals 毫秒刷新一次数据,防止频繁无用的刷新
            if (curTime - lastRefreshUiTime >= intervals || bytesWritten == contentLength) {
                val progress = bytesWritten * 100 / contentLength
                mainThread {
                    onProgress(progress)
                }
                lastRefreshUiTime = System.currentTimeMillis()
            }
        }
    }

}