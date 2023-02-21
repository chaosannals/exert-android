package com.example.backpdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import java.util.*

// 判断是否被电源优化忽略。
fun isIgnoringBatteryOptimizations(
    context: Context
): Boolean {
    var isIgnoring = false
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
    powerManager?.let {
        Toast.makeText(context, context.packageName, Toast.LENGTH_SHORT)
        isIgnoring = it.isIgnoringBatteryOptimizations(context.packageName)
    }
    return isIgnoring
}

// 请求电源优化忽略后台程序
fun requestIgnoreBatteryOptimizations(
    context: Context
) : Boolean{
    try {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.setData(Uri.parse("package:${context.packageName}"))
        context.startActivity(intent)
        return true
    } catch(e: Exception) {
        return false
    }
}

// 转到平台的应用商店设置后台允许
fun showActivity(
    context: Context,
    packageName: String,
    activityDir: String? = null,
) {
    val intent =  if (activityDir == null) {
        context.packageManager.getLaunchIntentForPackage(packageName)
    } else {
        val intent = Intent()
        intent.setComponent(ComponentName(packageName, activityDir))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent
    }
    context.startActivity(intent)
}

class BrandSystemManagerActivityInfo(
    val packageName: String,
    val activityDir: String? = null,
)

class BrandSystemManager(
    val tags: List<String>,
    val activityInfos: List<BrandSystemManagerActivityInfo>,
) {
    // 通过 brand 判断厂商
    fun matchTags(): Boolean {
        if (Build.BRAND == null) {
            return false
        }
        val brand = Build.BRAND.lowercase(Locale.getDefault())
        for (tag in tags) {
            if (brand.contains(tag)) {
                return true
            }
        }
        return false
    }

    // 打开系统管理
    fun openSystemManager(context: Context) : Boolean {
        for (activityInfo in activityInfos) {
            try {
                showActivity(
                    context,
                    activityInfo.packageName,
                    activityInfo.activityDir,
                )
                return true
            } catch(e: Exception) {

            }
        }
        return false
    }
}

val brandSystemManagers = listOf(
    // 华为
    BrandSystemManager(
        tags = listOf("huawei", "honor"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName="com.huawei.systemmanager",
                activityDir="com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity",
            ),
            BrandSystemManagerActivityInfo(
                packageName="com.huawei.systemmanager",
                activityDir="com.huawei.systemmanager.optimize.bootstart.BootStartActivity",
            ),
        ),
    ),
    // 小米
    BrandSystemManager(
        tags = listOf("xiaomi"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName="com.miui.securitycenter",
                activityDir="com.miui.permcenter.autostart.AutoStartManagementActivity",
            ),
        ),
    ),
    // OPPO
    BrandSystemManager(
        tags = listOf("oppo"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName = "com.coloros.phonemanager",
            ),
            BrandSystemManagerActivityInfo(
                packageName = "com.oppo.safe",
            ),
            BrandSystemManagerActivityInfo(
                packageName = "com.coloros.oppoguardelf",
            ),
            BrandSystemManagerActivityInfo(
                packageName = "com.coloros.safecenter",
            ),
        ),
    ),
    // VIVO
    BrandSystemManager(
        tags = listOf("vivo"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName = "com.iqoo.secure",
            ),
        ),
    ),
    // 魅族
    BrandSystemManager(
        tags = listOf("meizu"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName = "com.meizu.safe",
            ),
        ),
    ),
    // 三星
    BrandSystemManager(
        tags = listOf("samsung"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName = "com.samsung.android.sm_cn",
            ),
            BrandSystemManagerActivityInfo(
                packageName = "com.samsung.android.sm",
            ),
        ),
    ),
    // 乐视
    BrandSystemManager(
        tags = listOf("letv"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName = "com.letv.android.letvsafe",
                activityDir = "com.letv.android.letvsafe.AutobootManageActivity"
            ),
        ),
    ),
    // 锤子
    BrandSystemManager(
        tags = listOf("smartisan"),
        activityInfos = listOf(
            BrandSystemManagerActivityInfo(
                packageName = "com.smartisanos.security",
            ),
        ),
    ),
)

fun openMatchBrandSystemManager(context: Context): String? {
    for (brand in brandSystemManagers) {
        if (brand.matchTags() && brand.openSystemManager(context)) {
            return brand.tags[0]
        }
    }
    return null
}