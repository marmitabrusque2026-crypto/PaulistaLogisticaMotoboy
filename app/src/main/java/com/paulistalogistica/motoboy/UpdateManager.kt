package com.paulistalogistica.motoboy

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object UpdateManager {

    private const val VERSION_URL =
        "https://raw.githubusercontent.com/marmitabrusque2026-crypto/PaulistaLogisticaMotoboy/master/version.json"

    fun verificar(context: Context) {
        Thread {
            try {
                val connection = URL(VERSION_URL).openConnection() as HttpURLConnection
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                connection.requestMethod = "GET"

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    connection.disconnect()
                    return@Thread
                }

                val json = connection.inputStream.bufferedReader().use { it.readText() }
                connection.disconnect()

                val remoteVersionCode =
                    Regex("\"versionCode\"\\s*:\\s*(\\d+)").find(json)
                        ?.groupValues?.get(1)?.toIntOrNull()
                        ?: return@Thread

                val apkUrl =
                    Regex("\"apkUrl\"\\s*:\\s*\"([^\"]+)\"").find(json)
                        ?.groupValues?.get(1)
                        ?: return@Thread

                val currentVersionCode =
                    context.packageManager
                        .getPackageInfo(context.packageName, 0)
                        .longVersionCode
                        .toInt()

                if (remoteVersionCode <= currentVersionCode) {
                    return@Thread
                }

                Handler(Looper.getMainLooper()).post {
                    if (context is Activity && !context.isFinishing) {
                        AlertDialog.Builder(context)
                            .setTitle("Nova versão disponível")
                            .setMessage("Existe uma atualização do Paulista Logística. Deseja instalar agora?")
                            .setCancelable(false)
                            .setNegativeButton("Depois", null)
                            .setPositiveButton("Atualizar") { _, _ ->
                                baixarEInstalar(context, apkUrl)
                            }
                            .show()
                    }
                }

            } catch (_: Exception) {
            }
        }.start()
    }

    private fun baixarEInstalar(activity: Activity, apkUrl: String) {
        Thread {
            try {
                val connection = URL(apkUrl).openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 30000
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    connection.disconnect()
                    return@Thread
                }

                val apkFile = AppUpdater.getUpdateFile(activity)

                FileOutputStream(apkFile).use { output ->
                    connection.inputStream.use { input ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                        }
                    }
                }

                connection.disconnect()

                Handler(Looper.getMainLooper()).post {
                    AppUpdater.installApk(activity, apkFile)
                }

            } catch (_: Exception) {
            }
        }.start()
    }
}