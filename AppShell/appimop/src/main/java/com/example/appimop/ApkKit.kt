package com.example.appimop


import android.content.Context
import androidx.core.content.PackageManagerCompat

object ApkKit {
    fun Context.isInstalled(packageName: String): Boolean {
        val ps = packageManager.getInstalledPackages(0)
        for (p in ps) {
            if (p.packageName.equals(packageName)) {
                return true
            }
        }
        return false
    }

    fun Context.isInstalled2(packageName: String): Boolean {
        val ps = packageManager.getInstalledApplications(0)
        for (p in ps) {
            if (p.packageName.equals(packageName)) {
                return true
            }
        }
        return false
    }
}