package com.example.anidemo

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import java.util.Collections

object ShortcutUtil {
    fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortScan = ShortcutInfoCompat.Builder(context, "scan2")
                .setShortLabel(context.getString(R.string.shortcut_scan_2))
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_scan))
                .setIntent(Intent(Intent.ACTION_MAIN, null, context, ScanActivity::class.java))
                .build()
            ShortcutManagerCompat.addDynamicShortcuts(context, mutableListOf(shortScan))
        }
    }

    fun init3(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val info = ShortcutInfo.Builder(context, "scan3")
                .setShortLabel(context.getString(R.string.shortcut_scan_3))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_scan))
                .setIntent(Intent(Intent.ACTION_MAIN, null, context, ScanActivity::class.java))
                .build()
            // 这里是替换，所以会把动态的 scan2 给清了。
            context.getSystemService(ShortcutManager::class.java)
                .dynamicShortcuts = mutableListOf(info)
        }
    }

    fun update(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortScan = ShortcutInfoCompat.Builder(context, "scan2")
                .setShortLabel(context.getString(R.string.shortcut_scan_2))
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_scan))
                .setIntent(Intent(Intent.ACTION_MAIN, null, context, ScanActivity::class.java))
                .build()
            ShortcutManagerCompat.updateShortcuts(context, mutableListOf(shortScan))
        }
    }

    fun update3(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val info = ShortcutInfo.Builder(context, "scan3")
                .setShortLabel(context.getString(R.string.shortcut_scan_3))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_scan))
                .setIntent(Intent(Intent.ACTION_MAIN, null, context, ScanActivity::class.java))
                .build()
            context.getSystemService(ShortcutManager::class.java)
                .updateShortcuts(listOf(info))
        }
    }

    fun remove(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManagerCompat.removeDynamicShortcuts(
                context,
                Collections.singletonList("scan2")
            )
        }
    }

    fun remove3(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.getSystemService(ShortcutManager::class.java)
                .removeDynamicShortcuts(listOf("scan3"))
        }
    }
}