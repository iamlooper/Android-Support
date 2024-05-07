package com.looper.android.support.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.io.File
import java.security.MessageDigest

object AppUtils {

    /**
     * Calculates the SHA-256 hash of the app's signatures and returns it as a Base64-encoded string.
     *
     * @param context, the context of the application.
     * @return The SHA-256 hash of the app's signatures as a Base64-encoded string.
     */
    @SuppressLint("PackageManagerGetSignatures")
    fun getSignatureHash(context: Context): String {
        var signatureHash = ""

        try {
            // Get the package info.
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            }

            // Get the app's signatures.
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }

            // Iterate over all signatures and update the MessageDigest with SHA-256 algorithm.
            val md = MessageDigest.getInstance("SHA-256")
            for (signature in signatures) {
                md.update(signature.toByteArray())
            }

            // Get the hash bytes and convert them to Base64-encoded string.
            val hashBytes = md.digest()
            signatureHash = Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return signatureHash
    }

    /**
     * Retrieves the version name of the application.
     *
     * @param context, the context of the application.
     * @return The version name of the application.
     */
    fun getVersion(context: Context): String {
        var version = ""
        try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    /**
     * Checks if a specific package is installed on the device.
     *
     * @param context, the context of the application.
     * @param packageName, the package name to check for installation.
     * @return True if the package is installed, false otherwise.
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Gets the full path of a native library file.
     *
     * @param context, the application context.
     * @param libraryName, the name of the native library (without any prefix or extension).
     * @return The absolute path of the native library file.
     */
    fun getNativeLibraryPath(context: Context, libraryName: String): String {
        // Get the directory where native libraries are stored for the current application.
        val libraryFolderPath = context.applicationInfo.nativeLibraryDir

        // Map the library name to the appropriate platform-specific file name.
        val libraryFileName = System.mapLibraryName(libraryName)

        // Create a File object representing the library file inside the library folder path.
        val libraryFile = File(libraryFolderPath, libraryFileName)

        // Return the absolute path of the library file.
        return libraryFile.absolutePath
    }
}
