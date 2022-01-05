package com.gfq.common.system

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 *  2022/1/5 16:16
 * @auth gaofuq
 * @description
 */
class ActivityManagerProvider:ContentProvider() {
    override fun onCreate(): Boolean {
        var context = context
        if (context == null) return false
        context = context.applicationContext
        return if (context is Application) {
            ActivityManager.init(context)
            true
        } else {
            false
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
       return 0
    }
}