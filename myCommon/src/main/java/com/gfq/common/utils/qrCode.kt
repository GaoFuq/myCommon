package com.gfq.common.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsBuildBitmapOption

fun createQrCode(
    width: Int,
    height: Int,
    content: String,
    logo: Bitmap?=null,
    margin: Int = 1,//边距
    qrColor:Int = Color.BLACK,//码颜色
    backgroundColor: Int=Color.WHITE //码背景色
): Bitmap {
    val type = 0 //码类型。0=QR Code、1=Data Matrix、2=PDF417、3=Aztec
    val options = HmsBuildBitmapOption.Creator().setBitmapMargin(margin).setBitmapColor(qrColor)
        .setBitmapBackgroundColor(backgroundColor).setQRLogoBitmap(logo).create()
    return ScanUtil.buildBitmap(content, type, width, height, options)
}