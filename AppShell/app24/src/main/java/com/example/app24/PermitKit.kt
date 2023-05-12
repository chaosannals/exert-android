package com.example.app24

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// 检查权限，无则申请。
fun ensurePermit(context: Activity, permission: String, code: Int = 1) {
    val sp = ContextCompat.checkSelfPermission(
        context,
        permission
    )
    if (sp != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(permission),
            code // 自定标识
        )
    }
}
