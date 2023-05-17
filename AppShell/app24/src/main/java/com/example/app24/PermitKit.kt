package com.example.app24

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// 检查权限，无则申请。
fun Activity.ensurePermit(permission: String, code: Int = 1) {
    val sp = ContextCompat.checkSelfPermission(
        this,
        permission
    )
    if (sp != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission),
            code // 自定标识
        )
    }
}
