package com.example.healthcare.Util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String):Boolean{
    return ContextCompat.checkSelfPermission(this,permission)== PackageManager.PERMISSION_GRANTED
}

inline fun Context.cameraPermissionRequest(crossinline positive:() -> Unit){
    AlertDialog.Builder(this)
        .setTitle("Camera Permission Required")
        .setMessage("Without accessing the camera it is not possible to scan QR codes")
        .setPositiveButton("Allow Camera"){dialog, which ->
            positive.invoke()
        }.setNegativeButton("Cancle"){dialog,which->

        }.show()
}

fun Context.openPermissionSetting(){
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also{
        val uri: Uri = Uri.fromParts("package",packageName,null)
        it.data=uri
        startActivity(it)
    }
}