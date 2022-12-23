package com.example.jcm3demo.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Process
import androidx.annotation.StringDef
import androidx.compose.material3.ExperimentalMaterial3Api

@ExperimentalMaterial3Api
object LauncherSwitch {
    const val APP = "com.example.jcm3demo.MainActivity"
    const val APP_2 = "com.example.jcm3demo.launcher2"
    const val APP_3 = "com.example.jcm3demo.launcher3"
    val APP_SET = arrayOf(APP, APP_2, APP_3)

    @StringDef(value=[APP, APP_2, APP_3])
    @Retention(AnnotationRetention.SOURCE)
    annotation class AppLauncher

    fun enableLauncher(context: Context, @AppLauncher name: String) {
        context.packageManager.run {
            APP_SET.forEach {
                if (name != it) {
                    setComponentEnabledSetting(
                        ComponentName(context, it),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            setComponentEnabledSetting(
                ComponentName(context, name),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        // 重启无效，重启后会被再次杀掉。
//        restart(context, name, false)
    }

    fun currentLauncher(context: Context) :String? {
        context.packageManager.run {
            APP_SET.forEach {
                val state = getComponentEnabledSetting(
                    ComponentName(context, it)
                )
                if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                    return it
                }
            }
        }
        return null
    }

    fun restart(context: Context,@AppLauncher name: String, killSelf: Boolean=false) {
        val intent = Intent()
        intent.component = ComponentName(context, name)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (killSelf) {
            Process.killProcess(Process.myPid())
        }
    }
}