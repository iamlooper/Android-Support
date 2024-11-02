package com.looper.android.support.provider

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.looper.android.support.R
import com.looper.android.support.util.AppUtil
import com.looper.android.support.util.IntentUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AppUpdateProvider {

    private data class UpdateInfo(
        val version: String,
        val versionCode: Int,
        val apkUrl: String,
        val changelog: String
    )

    companion object {
        suspend fun checkForUpdate(context: Context, updateUrl: String) {
            withContext(Dispatchers.IO) {
                try {
                    val updateJson = fetchUpdateJson(updateUrl)
                    val updateInfo = parseUpdateInfo(updateJson)

                    if (updateInfo.versionCode > AppUtil.getAppVersionCode(context)) {
                        withContext(Dispatchers.Main) {
                            showUpdateDialog(context, updateInfo)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun fetchUpdateJson(updateUrl: String): String {
            val url = URL(updateUrl)
            val connection = url.openConnection() as HttpURLConnection

            return try {
                connection.apply {
                    requestMethod = "GET"
                    connectTimeout = 15000
                    readTimeout = 15000
                }.inputStream.bufferedReader().use { it.readText() }
            } finally {
                connection.disconnect()
            }
        }

        private fun parseUpdateInfo(jsonString: String): UpdateInfo {
            val json = JSONObject(jsonString)
            return UpdateInfo(
                version = json.getString("version"),
                versionCode = json.getInt("versionCode"),
                apkUrl = json.getString("apkUrl"),
                changelog = json.getString("changelog")
            )
        }

        private fun showUpdateDialog(context: Context, updateInfo: UpdateInfo) {
            MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.app_update_title))
                .setMessage(
                    context.getString(
                        R.string.app_update_message,
                        updateInfo.version.removePrefix("v")
                    )
                )
                .setPositiveButton(context.getString(R.string.update)) { _, _ ->
                    IntentUtil.openURL(context, updateInfo.apkUrl)
                }
                .setNegativeButton(context.getString(R.string.later), null)
                .setNeutralButton(context.getString(R.string.changelog)) { _, _ ->
                    IntentUtil.openURL(context, updateInfo.changelog)
                }
                .show()
        }
    }
}