# Material 3 示例

## 切换图标

通过 activity-alias 切换图标，会导致程序关闭。
当切换的是 activity-alias 而不是主 activity 时，调试器无法识别（切回主activity 就可以调试了）。

所以需要选择好时机切换，一般是用户使用的时候切出去了，就直接换掉，用户发现应用没了就会去重开。