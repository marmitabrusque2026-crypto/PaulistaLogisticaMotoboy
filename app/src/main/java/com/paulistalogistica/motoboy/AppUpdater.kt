package com.paulistalogistica.motoboy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

object AppUpdater {

    private const val APK_FILE_NAME = "PaulistaLogisticaMotoboy-update.apk"

    fun installApk(activity: Activity, apkFile: File) {
        val apkUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                apkUri,
                "application/vnd.android.package-archive"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        activity.startActivity(intent)
    }

    fun getUpdateFile(activity: Activity): File {
        val directory = activity.cacheDir
        return File(directory, APK_FILE_NAME)
    }
}
