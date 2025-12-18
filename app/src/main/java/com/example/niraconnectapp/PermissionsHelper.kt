
package com.example.niraconnectapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsHelper {

    // For Android 13 (API 33) and above
    private val galleryPermissionsForAndroid13AndAbove = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    // For Android 12 (API 32) and below
    private val galleryPermissionsForAndroid12AndBelow = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val cameraPermissions = arrayOf(
        Manifest.permission.CAMERA
    )

    // Check if gallery permissions are granted
    fun hasGalleryPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 12 and below
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Check if camera permissions are granted
    fun hasCameraPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request gallery permissions
    fun requestGalleryPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            ActivityCompat.requestPermissions(
                activity,
                galleryPermissionsForAndroid13AndAbove,
                requestCode
            )
        } else {
            // Android 12 and below
            ActivityCompat.requestPermissions(
                activity,
                galleryPermissionsForAndroid12AndBelow,
                requestCode
            )
        }
    }

    // Request camera permissions
    fun requestCameraPermissions(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            cameraPermissions,
            requestCode
        )
    }

    // Check if permission was granted in onRequestPermissionsResult
    fun isPermissionGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    // Should we show permission rationale?
    fun shouldShowPermissionRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    // Get permission name for display
    fun getPermissionName(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "Camera"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "Storage"
            Manifest.permission.READ_MEDIA_IMAGES -> "Photos & Media"
            else -> "Permission"
        }
    }
}
