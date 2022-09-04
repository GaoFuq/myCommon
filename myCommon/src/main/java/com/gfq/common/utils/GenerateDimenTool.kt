package com.gfq.common.utils

import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

/**
 * 屏幕尺寸适配工具类
 * 1.运行 main 方法，生成values/dimens.xml文件
 * 2.运行 ScreenMatch 插件
 */
object GenerateDimenTool {
    @JvmStatic
    fun main(args: Array<String>) {
        val sw360 = StringBuilder()
        //添加xml开始的标签
        val xmlStart = """<?xml version="1.0" encoding="utf-8"?>
<resources>
"""
        sw360.append(xmlStart)
        //添加内容
        for (i in 0..1920) {
//            此处name后的标签名可以自定义"margin_"随意更改
            val start = "<dimen name=\"dp$i\">"
            val end = "dp</dimen>"
            sw360.append(start).append(i).append(end).append("\n")
        }
        for (i in 0..1920) {
            // 负的dp
            val start = "<dimen name=\"dpNeg$i\">"
            val end = "dp</dimen>"
            sw360.append(start).append(-i).append(end).append("\n")
        }

        for (i in 0..50) {
            // 小数dp
            val start = "<dimen name=\"dp${i}_5\">"
            val end = "dp</dimen>"
            sw360.append(start).append("${i}.5").append(end).append("\n")
        }
        for (i in 0..50) {
            // 负的小数dp
            val start = "<dimen name=\"dpNeg${i}_5\">"
            val end = "dp</dimen>"
            sw360.append(start).append("-${i}.5").append(end).append("\n")
        }

        for (i in 0..100) {
            // sp
            val start = "<dimen name=\"sp$i\">"
            val end = "sp</dimen>"
            sw360.append(start).append(i).append(end).append("\n")
        }
        //添加xml的尾标签
        sw360.append("</resources>")
        val sw360file = "./app/src/main/res/values/dimens.xml"
        writeFile(sw360file, sw360.toString())
    }

    fun writeFile(file: String?, text: String?) {
        var out: PrintWriter? = null
        try {
            out = PrintWriter(BufferedWriter(FileWriter(file)))
            out.println(text)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        out!!.close()
    }



}