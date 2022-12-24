# Demo

## shortcuts（快捷方式）

就是桌面图标长按后会出来的菜单，不是常规PC的快捷方式。
所以是一个菜单。
需要 minSDK 25 （安卓 7.1）以上。

### 静态方式

- res/xml/shortcuts.xml 添加文件
- app/src/AndroidManifest.xml 入口 Activity 下添加 meta-data 标签引入 shortcuts.xml


### 动态方式

ShortcutUtil

## IMEI 

注: 
- 安卓9及以前需要 android.permission.READ_PHONE_STATE
- 安卓10 开始需要系统权限 android.permission.READ_PRIVILEGED_PHONE_STATE 这个只有系统应用才有。

安卓 10 之后只能通过替代方案的ID 来区分设备。
