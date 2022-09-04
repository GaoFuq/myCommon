package com.gfq.common.utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.gfq.common.toast.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//保存bitmap到相册
fun FragmentActivity?.saveBitmap2Gallery(
    bitmap: Bitmap,
    success: () -> Unit = { toast("保存成功！") },
    failed: () -> Unit = { toast("保存失败！") },
) {
    this?.lifecycleScope?.launch(Dispatchers.IO) {
        val insertUri = contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
        ) ?: kotlin.run {
            failed()
            return@launch
        }
        //使用use可以自动关闭流
        contentResolver?.openOutputStream(insertUri).use {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                success()
            } else {
              failed()
            }
        }
    }
}

fun Fragment?.saveBitmap2Gallery(bitmap: Bitmap) {
    this?.activity?.saveBitmap2Gallery(bitmap)
}
