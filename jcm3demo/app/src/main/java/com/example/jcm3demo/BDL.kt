package com.example.jcm3demo

import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.example.jcm3demo.ui.page.tool.writeLog
import java.util.*


object BDL {
    private var client: LocationClient? = null

    private val listeners = Hashtable<UUID, (BDLocation) -> Unit>()
    private var application: Application? = null
    private var activity: Activity? = null

    fun attach(listener: (BDLocation) -> Unit) : UUID {
        val key = UUID.randomUUID()
        listeners[key] = listener
        return key
    }

    fun detach(key: UUID) {
        if (listeners.containsKey(key)) {
            listeners.remove(key)
        }
    }

    fun init(application: Application, activity: Activity) {
        writeLog(application, "bdl init")
        this.application = application
        this.activity = activity
        client = LocationClient(application)
        client!!.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onLocDiagnosticMessage(p0: Int, p1: Int, p2: String?) {
                writeLog(application, "on bdl ldsm: ${p0} ${p1}, ${p2}")
            }

            override fun onReceiveLocation(location: BDLocation?) {
                writeLog(application, "on bdl receivel")
                listeners.forEach { it.value.invoke(location!!) }

                // 地址信息 --------------------------------
                val addr = location!!.addrStr //获取详细地址信息

                val country = location!!.country //获取国家

                val province = location!!.province //获取省份

                val city = location!!.city //获取城市

                val district = location!!.district //获取区县

                val street = location!!.street //获取街道信息

                val adcode = location!!.adCode //获取adcode

                val town = location!!.town //获取乡镇信息

                // 位置描述 -----------------------
                val locationDescribe = location.locationDescribe //获取位置描述信息

                // 周边信息
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取周边POI信息相关的结果
                //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取周边POI信息相关的结果
                //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
                if (location.poiList != null && location.poiList.isNotEmpty()) {
                    val poi = location.poiList[0]
                    val poiName = poi.name //获取POI名称

                    // val poiTags: String = poi.getTag() //获取POI类型

                    val poiAddr = poi.addr //获取POI地址 //获取周边POI信息

                    if (location.poiRegion != null) {
                        val poiRegion = location.poiRegion
                        val poiDerectionDesc = poiRegion.derectionDesc //获取PoiRegion位置关系

                        val poiRegionName = poiRegion.name //获取PoiRegion名称

                        val poiTags = poiRegion.tags //获取PoiRegion类型
                    }
                }


                // 室内定位 --------------------------------------------
                //这种只有特定地点有。有这个信息才能被使用。
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                if (location.floor != null) {
                    // 当前支持高精度室内定位
                    val buildingID = location.buildingID // 百度内部建筑物ID
                    val buildingName = location.buildingName // 百度内部建筑物缩写
                    val floor = location.floor // 室内定位的楼层信息，如 f1,f2,b1,b2
                    client!!.startIndoorMode() // 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
                }
            }
        })
        var option = LocationClientOption()

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy)
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        //LocationMode.Fuzzy_Locating, 模糊定位模式；v9.2.8版本开始支持，可以降低API的调用频率，但同时也会降低定位精度；

        option.setCoorType("bd09ll")
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        // 老版没有
        // option.setFirstLocType(FirstLocTypefirstLocType)
        //可选，首次定位时可以选择定位的返回是准确性优先还是速度优先，默认为速度优先
        //可以搭配setOnceLocation(Boolean isOnceLocation)单次定位接口使用，当设置为单次定位时，setFirstLocType接口中设置的类型即为单次定位使用的类型
        //FirstLocType.SPEED_IN_FIRST_LOC:速度优先，首次定位时会降低定位准确性，提升定位速度；
        //FirstLocType.ACCUARACY_IN_FIRST_LOC:准确性优先，首次定位时会降低速度，提升定位准确性；

        option.setScanSpan(2000)
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.isOpenGps = true
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.isLocationNotify = true
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        //option.setIgnoreKillProcess(false)
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        //option.SetIgnoreCacheException(false)
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        //option.setWifiCacheTimeOut(5*60*1000)
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false)
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true)
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true


        // 地址 ----------------------------------------------
        option.setIsNeedAddress(true)
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true)
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true


        // 位置描述 --------------------------------------------------
        option.setIsNeedLocationDescribe(true)
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true

        // 周边信息 -----------------------------------
        option.setIsNeedLocationPoiList(true)
        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true

        client!!.locOption = option
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    @ExperimentalMaterial3Api
    fun start() {
        writeLog(application!!, "on bdl start: ${client == null}")
        val builder = Notification.Builder(activity!!)
        val nfIntent = Intent(activity, MainActivity::class.java)
        builder.setContentIntent(
            PendingIntent.getActivity(
                activity,
                0,
                nfIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        ) // 设置PendingIntent
            .setContentTitle("正在进行后台定位") // 设置下拉列表里的标题
            .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
            .setContentText("后台定位通知") // 设置上下文内容
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

        var notification = builder.build()
        notification.defaults = Notification.DEFAULT_SOUND //设置为默认的声音

        client?.enableLocInForeground(1001, notification) // 调起前台定位

        client?.start()
    }

    fun stop() {
        writeLog(application!!, "on bdl stop: ${client == null}")
        client?.disableLocInForeground(true)
        client?.stop()
    }

    fun restart() {
        client?.restart()
    }

    fun isOpenGPS(): Boolean {
        val locManager = application!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun toOpenGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //context!!.startActivityForResult(intent, GPS_REQUEST_CODE);
    }
}