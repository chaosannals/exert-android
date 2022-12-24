package com.example.anidemo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.net.NetworkInterface
import java.util.*

class SystemInfo(
    var lang: String?=null,
    var version: String?=null,
    var model: String?=null,
    var brand: String?=null,
    var imei: String?=null,
    var errors: MutableList<String>,
    var manufacturer: String? = null,
    var serial: String? = null,
    var macAddress: String? = null,
    var androidId: String? = null,

    // 移动安全联盟
    var oaid: String? = null,
    var aaid: String? = null,
    var vaid: String? = null,
)

object SystemInfoUtil {
    /**
     * zh-CN 这种本地语言标志
     */
    fun getSystemLanguage() : String {
        return Locale.getDefault().language
    }

    fun listSystemlanguages() : List<String> {
        return Locale.getAvailableLocales().map { it.language }.toList()
    }

    /**
     * 系统版本
     */
    fun getSystemVersion() : String {
        return Build.VERSION.RELEASE
    }

    /**
     * 手机型号
     */
    fun getSystemModel() :String {
        return Build.MODEL
    }

    /**
     * 手机厂商
     */
    fun getDeviceBrand():String {
        return Build.BRAND
    }

    fun getIMEI(context: Context) :String? {
        val ts = context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ts.imei
        } else {
            return ts.deviceId
        }
    }

    fun getMacAddress(context: Context) : String? {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val wfs = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wfs.connectionInfo.macAddress
        } else {
            // 需要文件权限
            // return BufferedReader(FileReader(File("/sys/class/net/wlan0/address"))).readLine()

            val all = NetworkInterface.getNetworkInterfaces()
            for(nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) {
                    continue
                }
                if (nif.hardwareAddress == null) {
                    return null
                }
                val r = mutableListOf<String>()
                for (b in nif.hardwareAddress) {
                    r.add("%02X".format(b))
                }
                return r.joinToString(separator = ":")
            }
            return null
        }
    }

    fun getAndroidId(context: Context) : String {
        return Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID);
    }

    fun catchError(r: SystemInfo, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            r.errors.add(e.message!!)
        }
    }

    fun getPermission(context: Context, permissionTag: String) {
        val sp = ContextCompat.checkSelfPermission(
            context,
            permissionTag
        )

        if (sp != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(permissionTag),
                11 // 自定标识
            )
        }
    }

    fun getInfo(context: Context): SystemInfo {
        val r = SystemInfo(errors = mutableListOf())
        try {
            getPermission(context, android.Manifest.permission.READ_PHONE_STATE)
            getPermission(context, android.Manifest.permission.ACCESS_WIFI_STATE)
            getPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE)
            r.lang = getSystemLanguage()
            r.version = getSystemVersion()
            r.brand = getDeviceBrand()
            r.model = getSystemModel()
            catchError(r) {
                r.imei = getIMEI(context)
            }
            r.manufacturer = Build.MANUFACTURER
            catchError(r) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    r.serial = Build.getSerial()
                } else {
                    r.serial = Build.SERIAL
                }
            }
            catchError(r){
                r.macAddress = getMacAddress(context)
            }
            r.androidId = getAndroidId(context)
        } catch (e: Exception) {
            r.errors.add(e.message!!)
        }
        return r
    }
}