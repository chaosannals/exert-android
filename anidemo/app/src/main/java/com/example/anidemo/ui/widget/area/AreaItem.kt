package com.example.anidemo.ui.widget.area

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

@Serializable
data class AreaItem(
    val id: Int,
    val parentId: Int,
    val name: String,
)

val defaultAreaItems = listOf(
        AreaItem(
            id = 1000,
            parentId = 0,
            name="海外地区",
        ),



        AreaItem(
            id = 10000,
            parentId = 1000,
            name="亚洲",
        ),



        AreaItem(
            id = 10001,
            parentId = 10000,
            name="阿富汗",
        ),



        AreaItem(
            id = 10002,
            parentId = 10000,
            name="阿联酋",
        ),



        AreaItem(
            id = 10003,
            parentId = 10000,
            name="阿曼",
        ),



        AreaItem(
            id = 10004,
            parentId = 10000,
            name="阿塞拜疆",
        ),



        AreaItem(
            id = 10005,
            parentId = 10000,
            name="巴基斯坦",
        ),



        AreaItem(
            id = 10006,
            parentId = 10000,
            name="巴勒斯坦",
        ),



        AreaItem(
            id = 10007,
            parentId = 10000,
            name="巴林",
        ),



        AreaItem(
            id = 10008,
            parentId = 10000,
            name="不丹",
        ),



        AreaItem(
            id = 10009,
            parentId = 10000,
            name="朝鲜",
        ),



        AreaItem(
            id = 10010,
            parentId = 10000,
            name="东帝汶",
        ),



        AreaItem(
            id = 10011,
            parentId = 10000,
            name="菲律宾",
        ),



        AreaItem(
            id = 10012,
            parentId = 10000,
            name="格鲁吉亚",
        ),



        AreaItem(
            id = 10013,
            parentId = 10000,
            name="哈萨克斯坦",
        ),



        AreaItem(
            id = 10014,
            parentId = 10000,
            name="韩国",
        ),



        AreaItem(
            id = 10015,
            parentId = 10000,
            name="吉尔吉斯斯坦",
        ),



        AreaItem(
            id = 10016,
            parentId = 10000,
            name="柬埔寨",
        ),



        AreaItem(
            id = 10017,
            parentId = 10000,
            name="卡塔尔",
        ),



        AreaItem(
            id = 10018,
            parentId = 10000,
            name="科威特",
        ),



        AreaItem(
            id = 10019,
            parentId = 10000,
            name="老挝",
        ),



        AreaItem(
            id = 10020,
            parentId = 10000,
            name="黎巴嫩",
        ),



        AreaItem(
            id = 10021,
            parentId = 10000,
            name="马尔代夫",
        ),



        AreaItem(
            id = 10022,
            parentId = 10000,
            name="马来西亚",
        ),



        AreaItem(
            id = 10023,
            parentId = 10000,
            name="蒙古",
        ),



        AreaItem(
            id = 10024,
            parentId = 10000,
            name="孟加拉国",
        ),



        AreaItem(
            id = 10025,
            parentId = 10000,
            name="缅甸",
        ),



        AreaItem(
            id = 10026,
            parentId = 10000,
            name="尼泊尔",
        ),



        AreaItem(
            id = 10027,
            parentId = 10000,
            name="日本",
        ),



        AreaItem(
            id = 10028,
            parentId = 10000,
            name="沙特阿拉伯",
        ),



        AreaItem(
            id = 10029,
            parentId = 10000,
            name="斯里兰卡",
        ),



        AreaItem(
            id = 10030,
            parentId = 10000,
            name="塔吉克斯坦",
        ),



        AreaItem(
            id = 10031,
            parentId = 10000,
            name="泰国",
        ),



        AreaItem(
            id = 10032,
            parentId = 10000,
            name="土耳其",
        ),



        AreaItem(
            id = 10033,
            parentId = 10000,
            name="土库曼斯坦",
        ),



        AreaItem(
            id = 10034,
            parentId = 10000,
            name="文莱",
        ),



        AreaItem(
            id = 10035,
            parentId = 10000,
            name="乌兹别克斯坦",
        ),



        AreaItem(
            id = 10036,
            parentId = 10000,
            name="新加坡",
        ),



        AreaItem(
            id = 10037,
            parentId = 10000,
            name="叙利亚",
        ),



        AreaItem(
            id = 10038,
            parentId = 10000,
            name="亚美尼亚",
        ),



        AreaItem(
            id = 10039,
            parentId = 10000,
            name="也门",
        ),



        AreaItem(
            id = 10040,
            parentId = 10000,
            name="伊拉克",
        ),



        AreaItem(
            id = 10041,
            parentId = 10000,
            name="伊朗",
        ),



        AreaItem(
            id = 10042,
            parentId = 10000,
            name="以色列",
        ),



        AreaItem(
            id = 10043,
            parentId = 10000,
            name="印度",
        ),



        AreaItem(
            id = 10044,
            parentId = 10000,
            name="印度尼西亚",
        ),



        AreaItem(
            id = 10045,
            parentId = 10000,
            name="约旦",
        ),



        AreaItem(
            id = 10046,
            parentId = 10000,
            name="越南",
        ),



        AreaItem(
            id = 11000,
            parentId = 1000,
            name="非洲",
        ),



        AreaItem(
            id = 11001,
            parentId = 11000,
            name="阿尔及利亚",
        ),



        AreaItem(
            id = 11002,
            parentId = 11000,
            name="埃及",
        ),



        AreaItem(
            id = 11003,
            parentId = 11000,
            name="埃塞俄比亚",
        ),



        AreaItem(
            id = 11004,
            parentId = 11000,
            name="安哥拉",
        ),



        AreaItem(
            id = 11005,
            parentId = 11000,
            name="贝宁",
        ),



        AreaItem(
            id = 11006,
            parentId = 11000,
            name="博茨瓦纳",
        ),



        AreaItem(
            id = 11007,
            parentId = 11000,
            name="布基纳法索",
        ),



        AreaItem(
            id = 11008,
            parentId = 11000,
            name="布隆迪",
        ),



        AreaItem(
            id = 11009,
            parentId = 11000,
            name="赤道几内亚",
        ),



        AreaItem(
            id = 11010,
            parentId = 11000,
            name="多哥",
        ),



        AreaItem(
            id = 11011,
            parentId = 11000,
            name="厄立特里亚",
        ),



        AreaItem(
            id = 11012,
            parentId = 11000,
            name="佛得角",
        ),



        AreaItem(
            id = 11013,
            parentId = 11000,
            name="冈比亚",
        ),



        AreaItem(
            id = 11014,
            parentId = 11000,
            name="刚果(布)",
        ),



        AreaItem(
            id = 11015,
            parentId = 11000,
            name="刚果(金)",
        ),



        AreaItem(
            id = 11016,
            parentId = 11000,
            name="吉布提",
        ),



        AreaItem(
            id = 11017,
            parentId = 11000,
            name="几内亚",
        ),



        AreaItem(
            id = 11018,
            parentId = 11000,
            name="几内亚比绍",
        ),



        AreaItem(
            id = 11019,
            parentId = 11000,
            name="加纳",
        ),



        AreaItem(
            id = 11020,
            parentId = 11000,
            name="加蓬",
        ),



        AreaItem(
            id = 11021,
            parentId = 11000,
            name="津巴布韦",
        ),



        AreaItem(
            id = 11022,
            parentId = 11000,
            name="喀麦隆",
        ),



        AreaItem(
            id = 11023,
            parentId = 11000,
            name="科摩罗",
        ),



        AreaItem(
            id = 11024,
            parentId = 11000,
            name="科特迪瓦",
        ),



        AreaItem(
            id = 11025,
            parentId = 11000,
            name="肯尼亚",
        ),



        AreaItem(
            id = 11026,
            parentId = 11000,
            name="莱索托",
        ),



        AreaItem(
            id = 11027,
            parentId = 11000,
            name="利比里亚",
        ),



        AreaItem(
            id = 11028,
            parentId = 11000,
            name="利比亚",
        ),



        AreaItem(
            id = 11029,
            parentId = 11000,
            name="卢旺达",
        ),



        AreaItem(
            id = 11030,
            parentId = 11000,
            name="马达加斯加",
        ),



        AreaItem(
            id = 11031,
            parentId = 11000,
            name="马拉维",
        ),



        AreaItem(
            id = 11032,
            parentId = 11000,
            name="马里",
        ),



        AreaItem(
            id = 11033,
            parentId = 11000,
            name="毛里求斯",
        ),



        AreaItem(
            id = 11034,
            parentId = 11000,
            name="毛里塔尼亚",
        ),



        AreaItem(
            id = 11035,
            parentId = 11000,
            name="摩洛哥",
        ),



        AreaItem(
            id = 11036,
            parentId = 11000,
            name="莫桑比克",
        ),



        AreaItem(
            id = 11037,
            parentId = 11000,
            name="纳米比亚",
        ),



        AreaItem(
            id = 11038,
            parentId = 11000,
            name="南非",
        ),



        AreaItem(
            id = 11039,
            parentId = 11000,
            name="南苏丹",
        ),



        AreaItem(
            id = 11040,
            parentId = 11000,
            name="尼日尔",
        ),



        AreaItem(
            id = 11041,
            parentId = 11000,
            name="尼日利亚",
        ),



        AreaItem(
            id = 11042,
            parentId = 11000,
            name="塞拉利昂",
        ),



        AreaItem(
            id = 11043,
            parentId = 11000,
            name="塞内加尔",
        ),



        AreaItem(
            id = 11044,
            parentId = 11000,
            name="塞舌尔",
        ),



        AreaItem(
            id = 11045,
            parentId = 11000,
            name="圣多美和普林西比",
        ),



        AreaItem(
            id = 11046,
            parentId = 11000,
            name="斯威士兰",
        ),



        AreaItem(
            id = 11047,
            parentId = 11000,
            name="苏丹",
        ),



        AreaItem(
            id = 11048,
            parentId = 11000,
            name="索马里",
        ),



        AreaItem(
            id = 11049,
            parentId = 11000,
            name="坦桑尼亚",
        ),



        AreaItem(
            id = 11050,
            parentId = 11000,
            name="突尼斯",
        ),



        AreaItem(
            id = 11051,
            parentId = 11000,
            name="乌干达",
        ),



        AreaItem(
            id = 11052,
            parentId = 11000,
            name="赞比亚",
        ),



        AreaItem(
            id = 11053,
            parentId = 11000,
            name="乍得",
        ),



        AreaItem(
            id = 11054,
            parentId = 11000,
            name="中非",
        ),



        AreaItem(
            id = 12000,
            parentId = 1000,
            name="欧洲",
        ),



        AreaItem(
            id = 12001,
            parentId = 12000,
            name="阿尔巴尼亚",
        ),



        AreaItem(
            id = 12002,
            parentId = 12000,
            name="爱尔兰",
        ),



        AreaItem(
            id = 12003,
            parentId = 12000,
            name="爱沙尼亚",
        ),



        AreaItem(
            id = 12004,
            parentId = 12000,
            name="安道尔",
        ),



        AreaItem(
            id = 12005,
            parentId = 12000,
            name="奥地利",
        ),



        AreaItem(
            id = 12006,
            parentId = 12000,
            name="白俄罗斯",
        ),



        AreaItem(
            id = 12007,
            parentId = 12000,
            name="保加利亚",
        ),



        AreaItem(
            id = 12008,
            parentId = 12000,
            name="比利时",
        ),



        AreaItem(
            id = 12009,
            parentId = 12000,
            name="冰岛",
        ),



        AreaItem(
            id = 12010,
            parentId = 12000,
            name="波黑",
        ),



        AreaItem(
            id = 12011,
            parentId = 12000,
            name="波兰",
        ),



        AreaItem(
            id = 12012,
            parentId = 12000,
            name="丹麦",
        ),



        AreaItem(
            id = 12013,
            parentId = 12000,
            name="德国",
        ),



        AreaItem(
            id = 12014,
            parentId = 12000,
            name="俄罗斯",
        ),



        AreaItem(
            id = 12015,
            parentId = 12000,
            name="法国",
        ),



        AreaItem(
            id = 12016,
            parentId = 12000,
            name="梵蒂冈",
        ),



        AreaItem(
            id = 12017,
            parentId = 12000,
            name="芬兰",
        ),



        AreaItem(
            id = 12018,
            parentId = 12000,
            name="荷兰",
        ),



        AreaItem(
            id = 12019,
            parentId = 12000,
            name="黑山",
        ),



        AreaItem(
            id = 12020,
            parentId = 12000,
            name="捷克",
        ),



        AreaItem(
            id = 12021,
            parentId = 12000,
            name="克罗地亚",
        ),



        AreaItem(
            id = 12022,
            parentId = 12000,
            name="拉脱维亚",
        ),



        AreaItem(
            id = 12023,
            parentId = 12000,
            name="立陶宛",
        ),



        AreaItem(
            id = 12024,
            parentId = 12000,
            name="列支敦士登",
        ),



        AreaItem(
            id = 12025,
            parentId = 12000,
            name="卢森堡",
        ),



        AreaItem(
            id = 12026,
            parentId = 12000,
            name="罗马尼亚",
        ),



        AreaItem(
            id = 12027,
            parentId = 12000,
            name="马耳他",
        ),



        AreaItem(
            id = 12028,
            parentId = 12000,
            name="马其顿",
        ),



        AreaItem(
            id = 12029,
            parentId = 12000,
            name="摩尔多瓦",
        ),



        AreaItem(
            id = 12030,
            parentId = 12000,
            name="摩纳哥",
        ),



        AreaItem(
            id = 12031,
            parentId = 12000,
            name="挪威",
        ),



        AreaItem(
            id = 12032,
            parentId = 12000,
            name="葡萄牙",
        ),



        AreaItem(
            id = 12033,
            parentId = 12000,
            name="瑞典",
        ),



        AreaItem(
            id = 12034,
            parentId = 12000,
            name="瑞士",
        ),



        AreaItem(
            id = 12035,
            parentId = 12000,
            name="塞尔维亚",
        ),



        AreaItem(
            id = 12036,
            parentId = 12000,
            name="塞浦路斯",
        ),



        AreaItem(
            id = 12037,
            parentId = 12000,
            name="圣马力诺",
        ),



        AreaItem(
            id = 12038,
            parentId = 12000,
            name="斯洛伐克",
        ),



        AreaItem(
            id = 12039,
            parentId = 12000,
            name="斯洛文尼亚",
        ),



        AreaItem(
            id = 12040,
            parentId = 12000,
            name="乌克兰",
        ),



        AreaItem(
            id = 12041,
            parentId = 12000,
            name="西班牙",
        ),



        AreaItem(
            id = 12042,
            parentId = 12000,
            name="希腊",
        ),



        AreaItem(
            id = 12043,
            parentId = 12000,
            name="匈牙利",
        ),



        AreaItem(
            id = 12044,
            parentId = 12000,
            name="意大利",
        ),



        AreaItem(
            id = 12045,
            parentId = 12000,
            name="英国",
        ),



        AreaItem(
            id = 13000,
            parentId = 1000,
            name="北美洲",
        ),



        AreaItem(
            id = 13001,
            parentId = 13000,
            name="安提瓜和巴布达",
        ),



        AreaItem(
            id = 13002,
            parentId = 13000,
            name="巴巴多斯",
        ),



        AreaItem(
            id = 13003,
            parentId = 13000,
            name="巴哈马",
        ),



        AreaItem(
            id = 13004,
            parentId = 13000,
            name="巴拿马",
        ),



        AreaItem(
            id = 13005,
            parentId = 13000,
            name="伯利兹",
        ),



        AreaItem(
            id = 13006,
            parentId = 13000,
            name="多米尼加",
        ),



        AreaItem(
            id = 13007,
            parentId = 13000,
            name="多米尼克",
        ),



        AreaItem(
            id = 13008,
            parentId = 13000,
            name="格林纳达",
        ),



        AreaItem(
            id = 13009,
            parentId = 13000,
            name="哥斯达黎加",
        ),



        AreaItem(
            id = 13010,
            parentId = 13000,
            name="古巴",
        ),



        AreaItem(
            id = 13011,
            parentId = 13000,
            name="海地",
        ),



        AreaItem(
            id = 13012,
            parentId = 13000,
            name="洪都拉斯",
        ),



        AreaItem(
            id = 13013,
            parentId = 13000,
            name="加拿大",
        ),



        AreaItem(
            id = 13014,
            parentId = 13000,
            name="美国",
        ),



        AreaItem(
            id = 13015,
            parentId = 13000,
            name="墨西哥",
        ),



        AreaItem(
            id = 13016,
            parentId = 13000,
            name="尼加拉瓜",
        ),



        AreaItem(
            id = 13017,
            parentId = 13000,
            name="萨尔瓦多",
        ),



        AreaItem(
            id = 13018,
            parentId = 13000,
            name="圣基茨和尼维斯",
        ),



        AreaItem(
            id = 13019,
            parentId = 13000,
            name="圣卢西亚",
        ),



        AreaItem(
            id = 13020,
            parentId = 13000,
            name="圣文森特和格林纳丁斯",
        ),



        AreaItem(
            id = 13021,
            parentId = 13000,
            name="特立尼达和多巴哥",
        ),



        AreaItem(
            id = 13022,
            parentId = 13000,
            name="危地马拉",
        ),



        AreaItem(
            id = 13023,
            parentId = 13000,
            name="牙买加",
        ),



        AreaItem(
            id = 14000,
            parentId = 1000,
            name="南美洲",
        ),



        AreaItem(
            id = 14001,
            parentId = 14000,
            name="阿根廷",
        ),



        AreaItem(
            id = 14002,
            parentId = 14000,
            name="巴拉圭",
        ),



        AreaItem(
            id = 14003,
            parentId = 14000,
            name="巴西",
        ),



        AreaItem(
            id = 14004,
            parentId = 14000,
            name="秘鲁",
        ),



        AreaItem(
            id = 14005,
            parentId = 14000,
            name="玻利维亚",
        ),



        AreaItem(
            id = 14006,
            parentId = 14000,
            name="厄瓜多尔",
        ),



        AreaItem(
            id = 14007,
            parentId = 14000,
            name="哥伦比亚",
        ),



        AreaItem(
            id = 14008,
            parentId = 14000,
            name="圭亚那",
        ),



        AreaItem(
            id = 14009,
            parentId = 14000,
            name="苏里南",
        ),



        AreaItem(
            id = 14010,
            parentId = 14000,
            name="委内瑞拉",
        ),



        AreaItem(
            id = 14011,
            parentId = 14000,
            name="乌拉圭",
        ),



        AreaItem(
            id = 14012,
            parentId = 14000,
            name="智利",
        ),



        AreaItem(
            id = 15000,
            parentId = 1000,
            name="大洋洲",
        ),



        AreaItem(
            id = 15001,
            parentId = 15000,
            name="澳大利亚",
        ),



        AreaItem(
            id = 15002,
            parentId = 15000,
            name="巴布亚新几内亚",
        ),



        AreaItem(
            id = 15003,
            parentId = 15000,
            name="斐济",
        ),



        AreaItem(
            id = 15004,
            parentId = 15000,
            name="基里巴斯",
        ),



        AreaItem(
            id = 15005,
            parentId = 15000,
            name="库克群岛",
        ),



        AreaItem(
            id = 15006,
            parentId = 15000,
            name="马绍尔群岛",
        ),



        AreaItem(
            id = 15007,
            parentId = 15000,
            name="密克罗尼西亚联邦",
        ),



        AreaItem(
            id = 15008,
            parentId = 15000,
            name="瑙鲁",
        ),



        AreaItem(
            id = 15009,
            parentId = 15000,
            name="纽埃",
        ),



        AreaItem(
            id = 15010,
            parentId = 15000,
            name="帕劳",
        ),



        AreaItem(
            id = 15011,
            parentId = 15000,
            name="萨摩亚",
        ),



        AreaItem(
            id = 15012,
            parentId = 15000,
            name="所罗门群岛",
        ),



        AreaItem(
            id = 15013,
            parentId = 15000,
            name="汤加",
        ),



        AreaItem(
            id = 15014,
            parentId = 15000,
            name="图瓦卢",
        ),



        AreaItem(
            id = 15015,
            parentId = 15000,
            name="瓦努阿图",
        ),



        AreaItem(
            id = 15016,
            parentId = 15000,
            name="新西兰",
        ),



        AreaItem(
            id = 110000,
            parentId = 0,
            name="北京市",
        ),



        AreaItem(
            id = 110100,
            parentId = 110000,
            name="北京市",
        ),



        AreaItem(
            id = 110101,
            parentId = 110100,
            name="东城区",
        ),



        AreaItem(
            id = 110102,
            parentId = 110100,
            name="西城区",
        ),



        AreaItem(
            id = 110105,
            parentId = 110100,
            name="朝阳区",
        ),



        AreaItem(
            id = 110106,
            parentId = 110100,
            name="丰台区",
        ),



        AreaItem(
            id = 110107,
            parentId = 110100,
            name="石景山区",
        ),



        AreaItem(
            id = 110108,
            parentId = 110100,
            name="海淀区",
        ),



        AreaItem(
            id = 110109,
            parentId = 110100,
            name="门头沟区",
        ),



        AreaItem(
            id = 110111,
            parentId = 110100,
            name="房山区",
        ),



        AreaItem(
            id = 110112,
            parentId = 110100,
            name="通州区",
        ),



        AreaItem(
            id = 110113,
            parentId = 110100,
            name="顺义区",
        ),



        AreaItem(
            id = 110114,
            parentId = 110100,
            name="昌平区",
        ),



        AreaItem(
            id = 110115,
            parentId = 110100,
            name="大兴区",
        ),



        AreaItem(
            id = 110116,
            parentId = 110100,
            name="怀柔区",
        ),



        AreaItem(
            id = 110117,
            parentId = 110100,
            name="平谷区",
        ),



        AreaItem(
            id = 110118,
            parentId = 110100,
            name="密云区",
        ),



        AreaItem(
            id = 110119,
            parentId = 110100,
            name="延庆区",
        ),



        AreaItem(
            id = 120000,
            parentId = 0,
            name="天津市",
        ),



        AreaItem(
            id = 120100,
            parentId = 120000,
            name="天津市",
        ),



        AreaItem(
            id = 120101,
            parentId = 120100,
            name="和平区",
        ),



        AreaItem(
            id = 120102,
            parentId = 120100,
            name="河东区",
        ),



        AreaItem(
            id = 120103,
            parentId = 120100,
            name="河西区",
        ),



        AreaItem(
            id = 120104,
            parentId = 120100,
            name="南开区",
        ),



        AreaItem(
            id = 120105,
            parentId = 120100,
            name="河北区",
        ),



        AreaItem(
            id = 120106,
            parentId = 120100,
            name="红桥区",
        ),



        AreaItem(
            id = 120110,
            parentId = 120100,
            name="东丽区",
        ),



        AreaItem(
            id = 120111,
            parentId = 120100,
            name="西青区",
        ),



        AreaItem(
            id = 120112,
            parentId = 120100,
            name="津南区",
        ),



        AreaItem(
            id = 120113,
            parentId = 120100,
            name="北辰区",
        ),



        AreaItem(
            id = 120114,
            parentId = 120100,
            name="武清区",
        ),



        AreaItem(
            id = 120115,
            parentId = 120100,
            name="宝坻区",
        ),



        AreaItem(
            id = 120116,
            parentId = 120100,
            name="滨海新区",
        ),



        AreaItem(
            id = 120117,
            parentId = 120100,
            name="宁河区",
        ),



        AreaItem(
            id = 120118,
            parentId = 120100,
            name="静海区",
        ),



        AreaItem(
            id = 120119,
            parentId = 120100,
            name="蓟州区",
        ),



        AreaItem(
            id = 130000,
            parentId = 0,
            name="河北省",
        ),



        AreaItem(
            id = 130100,
            parentId = 130000,
            name="石家庄市",
        ),



        AreaItem(
            id = 130102,
            parentId = 130100,
            name="长安区",
        ),



        AreaItem(
            id = 130104,
            parentId = 130100,
            name="桥西区",
        ),



        AreaItem(
            id = 130105,
            parentId = 130100,
            name="新华区",
        ),



        AreaItem(
            id = 130107,
            parentId = 130100,
            name="井陉矿区",
        ),



        AreaItem(
            id = 130108,
            parentId = 130100,
            name="裕华区",
        ),



        AreaItem(
            id = 130109,
            parentId = 130100,
            name="藁城区",
        ),



        AreaItem(
            id = 130110,
            parentId = 130100,
            name="鹿泉区",
        ),



        AreaItem(
            id = 130111,
            parentId = 130100,
            name="栾城区",
        ),



        AreaItem(
            id = 130121,
            parentId = 130100,
            name="井陉县",
        ),



        AreaItem(
            id = 130123,
            parentId = 130100,
            name="正定县",
        ),



        AreaItem(
            id = 130125,
            parentId = 130100,
            name="行唐县",
        ),



        AreaItem(
            id = 130126,
            parentId = 130100,
            name="灵寿县",
        ),



        AreaItem(
            id = 130127,
            parentId = 130100,
            name="高邑县",
        ),



        AreaItem(
            id = 130128,
            parentId = 130100,
            name="深泽县",
        ),



        AreaItem(
            id = 130129,
            parentId = 130100,
            name="赞皇县",
        ),



        AreaItem(
            id = 130130,
            parentId = 130100,
            name="无极县",
        ),



        AreaItem(
            id = 130131,
            parentId = 130100,
            name="平山县",
        ),



        AreaItem(
            id = 130132,
            parentId = 130100,
            name="元氏县",
        ),



        AreaItem(
            id = 130133,
            parentId = 130100,
            name="赵县",
        ),



        AreaItem(
            id = 130181,
            parentId = 130100,
            name="辛集市",
        ),



        AreaItem(
            id = 130183,
            parentId = 130100,
            name="晋州市",
        ),



        AreaItem(
            id = 130184,
            parentId = 130100,
            name="新乐市",
        ),



        AreaItem(
            id = 130200,
            parentId = 130000,
            name="唐山市",
        ),



        AreaItem(
            id = 130202,
            parentId = 130200,
            name="路南区",
        ),



        AreaItem(
            id = 130203,
            parentId = 130200,
            name="路北区",
        ),



        AreaItem(
            id = 130204,
            parentId = 130200,
            name="古冶区",
        ),



        AreaItem(
            id = 130205,
            parentId = 130200,
            name="开平区",
        ),



        AreaItem(
            id = 130207,
            parentId = 130200,
            name="丰南区",
        ),



        AreaItem(
            id = 130208,
            parentId = 130200,
            name="丰润区",
        ),



        AreaItem(
            id = 130209,
            parentId = 130200,
            name="曹妃甸区",
        ),



        AreaItem(
            id = 130223,
            parentId = 130200,
            name="滦州市",
        ),



        AreaItem(
            id = 130224,
            parentId = 130200,
            name="滦南县",
        ),



        AreaItem(
            id = 130225,
            parentId = 130200,
            name="乐亭县",
        ),



        AreaItem(
            id = 130227,
            parentId = 130200,
            name="迁西县",
        ),



        AreaItem(
            id = 130229,
            parentId = 130200,
            name="玉田县",
        ),



        AreaItem(
            id = 130230,
            parentId = 130200,
            name="芦台区",
        ),



        AreaItem(
            id = 130281,
            parentId = 130200,
            name="遵化市",
        ),



        AreaItem(
            id = 130283,
            parentId = 130200,
            name="迁安市",
        ),



        AreaItem(
            id = 130300,
            parentId = 130000,
            name="秦皇岛市",
        ),



        AreaItem(
            id = 130302,
            parentId = 130300,
            name="海港区",
        ),



        AreaItem(
            id = 130303,
            parentId = 130300,
            name="山海关区",
        ),



        AreaItem(
            id = 130304,
            parentId = 130300,
            name="北戴河区",
        ),



        AreaItem(
            id = 130306,
            parentId = 130300,
            name="抚宁区",
        ),



        AreaItem(
            id = 130321,
            parentId = 130300,
            name="青龙满族自治县",
        ),



        AreaItem(
            id = 130322,
            parentId = 130300,
            name="昌黎县",
        ),



        AreaItem(
            id = 130324,
            parentId = 130300,
            name="卢龙县",
        ),



        AreaItem(
            id = 130400,
            parentId = 130000,
            name="邯郸市",
        ),



        AreaItem(
            id = 130402,
            parentId = 130400,
            name="邯山区",
        ),



        AreaItem(
            id = 130403,
            parentId = 130400,
            name="丛台区",
        ),



        AreaItem(
            id = 130404,
            parentId = 130400,
            name="复兴区",
        ),



        AreaItem(
            id = 130406,
            parentId = 130400,
            name="峰峰矿区",
        ),



        AreaItem(
            id = 130407,
            parentId = 130400,
            name="肥乡区",
        ),



        AreaItem(
            id = 130408,
            parentId = 130400,
            name="永年区",
        ),



        AreaItem(
            id = 130423,
            parentId = 130400,
            name="临漳县",
        ),



        AreaItem(
            id = 130424,
            parentId = 130400,
            name="成安县",
        ),



        AreaItem(
            id = 130425,
            parentId = 130400,
            name="大名县",
        ),



        AreaItem(
            id = 130426,
            parentId = 130400,
            name="涉县",
        ),



        AreaItem(
            id = 130427,
            parentId = 130400,
            name="磁县",
        ),



        AreaItem(
            id = 130430,
            parentId = 130400,
            name="邱县",
        ),



        AreaItem(
            id = 130431,
            parentId = 130400,
            name="鸡泽县",
        ),



        AreaItem(
            id = 130432,
            parentId = 130400,
            name="广平县",
        ),



        AreaItem(
            id = 130433,
            parentId = 130400,
            name="馆陶县",
        ),



        AreaItem(
            id = 130434,
            parentId = 130400,
            name="魏县",
        ),



        AreaItem(
            id = 130435,
            parentId = 130400,
            name="曲周县",
        ),



        AreaItem(
            id = 130481,
            parentId = 130400,
            name="武安市",
        ),



        AreaItem(
            id = 130500,
            parentId = 130000,
            name="邢台市",
        ),



        AreaItem(
            id = 130502,
            parentId = 130500,
            name="桥东区",
        ),



        AreaItem(
            id = 130503,
            parentId = 130500,
            name="桥西区",
        ),



        AreaItem(
            id = 130521,
            parentId = 130500,
            name="邢台县",
        ),



        AreaItem(
            id = 130522,
            parentId = 130500,
            name="临城县",
        ),



        AreaItem(
            id = 130523,
            parentId = 130500,
            name="内丘县",
        ),



        AreaItem(
            id = 130524,
            parentId = 130500,
            name="柏乡县",
        ),



        AreaItem(
            id = 130525,
            parentId = 130500,
            name="隆尧县",
        ),



        AreaItem(
            id = 130526,
            parentId = 130500,
            name="任县",
        ),



        AreaItem(
            id = 130527,
            parentId = 130500,
            name="南和县",
        ),



        AreaItem(
            id = 130528,
            parentId = 130500,
            name="宁晋县",
        ),



        AreaItem(
            id = 130529,
            parentId = 130500,
            name="巨鹿县",
        ),



        AreaItem(
            id = 130530,
            parentId = 130500,
            name="新河县",
        ),



        AreaItem(
            id = 130531,
            parentId = 130500,
            name="广宗县",
        ),



        AreaItem(
            id = 130532,
            parentId = 130500,
            name="平乡县",
        ),



        AreaItem(
            id = 130533,
            parentId = 130500,
            name="威县",
        ),



        AreaItem(
            id = 130534,
            parentId = 130500,
            name="清河县",
        ),



        AreaItem(
            id = 130535,
            parentId = 130500,
            name="临西县",
        ),



        AreaItem(
            id = 130581,
            parentId = 130500,
            name="南宫市",
        ),



        AreaItem(
            id = 130582,
            parentId = 130500,
            name="沙河市",
        ),



        AreaItem(
            id = 130600,
            parentId = 130000,
            name="保定市",
        ),



        AreaItem(
            id = 130602,
            parentId = 130600,
            name="竞秀区",
        ),



        AreaItem(
            id = 130606,
            parentId = 130600,
            name="莲池区",
        ),



        AreaItem(
            id = 130607,
            parentId = 130600,
            name="满城区",
        ),



        AreaItem(
            id = 130608,
            parentId = 130600,
            name="清苑区",
        ),



        AreaItem(
            id = 130609,
            parentId = 130600,
            name="徐水区",
        ),



        AreaItem(
            id = 130623,
            parentId = 130600,
            name="涞水县",
        ),



        AreaItem(
            id = 130624,
            parentId = 130600,
            name="阜平县",
        ),



        AreaItem(
            id = 130626,
            parentId = 130600,
            name="定兴县",
        ),



        AreaItem(
            id = 130627,
            parentId = 130600,
            name="唐县",
        ),



        AreaItem(
            id = 130628,
            parentId = 130600,
            name="高阳县",
        ),



        AreaItem(
            id = 130629,
            parentId = 130600,
            name="容城县",
        ),



        AreaItem(
            id = 130630,
            parentId = 130600,
            name="涞源县",
        ),



        AreaItem(
            id = 130631,
            parentId = 130600,
            name="望都县",
        ),



        AreaItem(
            id = 130632,
            parentId = 130600,
            name="安新县",
        ),



        AreaItem(
            id = 130633,
            parentId = 130600,
            name="易县",
        ),



        AreaItem(
            id = 130634,
            parentId = 130600,
            name="曲阳县",
        ),



        AreaItem(
            id = 130635,
            parentId = 130600,
            name="蠡县",
        ),



        AreaItem(
            id = 130636,
            parentId = 130600,
            name="顺平县",
        ),



        AreaItem(
            id = 130637,
            parentId = 130600,
            name="博野县",
        ),



        AreaItem(
            id = 130638,
            parentId = 130600,
            name="雄县",
        ),



        AreaItem(
            id = 130681,
            parentId = 130600,
            name="涿州市",
        ),



        AreaItem(
            id = 130682,
            parentId = 130600,
            name="定州市",
        ),



        AreaItem(
            id = 130683,
            parentId = 130600,
            name="安国市",
        ),



        AreaItem(
            id = 130684,
            parentId = 130600,
            name="高碑店市",
        ),



        AreaItem(
            id = 130700,
            parentId = 130000,
            name="张家口市",
        ),



        AreaItem(
            id = 130702,
            parentId = 130700,
            name="桥东区",
        ),



        AreaItem(
            id = 130703,
            parentId = 130700,
            name="桥西区",
        ),



        AreaItem(
            id = 130705,
            parentId = 130700,
            name="宣化区",
        ),



        AreaItem(
            id = 130706,
            parentId = 130700,
            name="下花园区",
        ),



        AreaItem(
            id = 130708,
            parentId = 130700,
            name="万全区",
        ),



        AreaItem(
            id = 130709,
            parentId = 130700,
            name="崇礼区",
        ),



        AreaItem(
            id = 130722,
            parentId = 130700,
            name="张北县",
        ),



        AreaItem(
            id = 130723,
            parentId = 130700,
            name="康保县",
        ),



        AreaItem(
            id = 130724,
            parentId = 130700,
            name="沽源县",
        ),



        AreaItem(
            id = 130725,
            parentId = 130700,
            name="尚义县",
        ),



        AreaItem(
            id = 130726,
            parentId = 130700,
            name="蔚县",
        ),



        AreaItem(
            id = 130727,
            parentId = 130700,
            name="阳原县",
        ),



        AreaItem(
            id = 130728,
            parentId = 130700,
            name="怀安县",
        ),



        AreaItem(
            id = 130730,
            parentId = 130700,
            name="怀来县",
        ),



        AreaItem(
            id = 130731,
            parentId = 130700,
            name="涿鹿县",
        ),



        AreaItem(
            id = 130732,
            parentId = 130700,
            name="赤城县",
        ),



        AreaItem(
            id = 130800,
            parentId = 130000,
            name="承德市",
        ),



        AreaItem(
            id = 130802,
            parentId = 130800,
            name="双桥区",
        ),



        AreaItem(
            id = 130803,
            parentId = 130800,
            name="双滦区",
        ),



        AreaItem(
            id = 130804,
            parentId = 130800,
            name="鹰手营子矿区",
        ),



        AreaItem(
            id = 130821,
            parentId = 130800,
            name="承德县",
        ),



        AreaItem(
            id = 130822,
            parentId = 130800,
            name="兴隆县",
        ),



        AreaItem(
            id = 130824,
            parentId = 130800,
            name="滦平县",
        ),



        AreaItem(
            id = 130825,
            parentId = 130800,
            name="隆化县",
        ),



        AreaItem(
            id = 130826,
            parentId = 130800,
            name="丰宁满族自治县",
        ),



        AreaItem(
            id = 130827,
            parentId = 130800,
            name="宽城满族自治县",
        ),



        AreaItem(
            id = 130828,
            parentId = 130800,
            name="围场满族蒙古族自治县",
        ),



        AreaItem(
            id = 130881,
            parentId = 130800,
            name="平泉市",
        ),



        AreaItem(
            id = 130900,
            parentId = 130000,
            name="沧州市",
        ),



        AreaItem(
            id = 130902,
            parentId = 130900,
            name="新华区",
        ),



        AreaItem(
            id = 130903,
            parentId = 130900,
            name="运河区",
        ),



        AreaItem(
            id = 130921,
            parentId = 130900,
            name="沧县",
        ),



        AreaItem(
            id = 130922,
            parentId = 130900,
            name="青县",
        ),



        AreaItem(
            id = 130923,
            parentId = 130900,
            name="东光县",
        ),



        AreaItem(
            id = 130924,
            parentId = 130900,
            name="海兴县",
        ),



        AreaItem(
            id = 130925,
            parentId = 130900,
            name="盐山县",
        ),



        AreaItem(
            id = 130926,
            parentId = 130900,
            name="肃宁县",
        ),



        AreaItem(
            id = 130927,
            parentId = 130900,
            name="南皮县",
        ),



        AreaItem(
            id = 130928,
            parentId = 130900,
            name="吴桥县",
        ),



        AreaItem(
            id = 130929,
            parentId = 130900,
            name="献县",
        ),



        AreaItem(
            id = 130930,
            parentId = 130900,
            name="孟村回族自治县",
        ),



        AreaItem(
            id = 130981,
            parentId = 130900,
            name="泊头市",
        ),



        AreaItem(
            id = 130982,
            parentId = 130900,
            name="任丘市",
        ),



        AreaItem(
            id = 130983,
            parentId = 130900,
            name="黄骅市",
        ),



        AreaItem(
            id = 130984,
            parentId = 130900,
            name="河间市",
        ),



        AreaItem(
            id = 131000,
            parentId = 130000,
            name="廊坊市",
        ),



        AreaItem(
            id = 131002,
            parentId = 131000,
            name="安次区",
        ),



        AreaItem(
            id = 131003,
            parentId = 131000,
            name="广阳区",
        ),



        AreaItem(
            id = 131022,
            parentId = 131000,
            name="固安县",
        ),



        AreaItem(
            id = 131023,
            parentId = 131000,
            name="永清县",
        ),



        AreaItem(
            id = 131024,
            parentId = 131000,
            name="香河县",
        ),



        AreaItem(
            id = 131025,
            parentId = 131000,
            name="大城县",
        ),



        AreaItem(
            id = 131026,
            parentId = 131000,
            name="文安县",
        ),



        AreaItem(
            id = 131028,
            parentId = 131000,
            name="大厂回族自治县",
        ),



        AreaItem(
            id = 131081,
            parentId = 131000,
            name="霸州市",
        ),



        AreaItem(
            id = 131082,
            parentId = 131000,
            name="三河市",
        ),



        AreaItem(
            id = 131100,
            parentId = 130000,
            name="衡水市",
        ),



        AreaItem(
            id = 131102,
            parentId = 131100,
            name="桃城区",
        ),



        AreaItem(
            id = 131103,
            parentId = 131100,
            name="冀州区",
        ),



        AreaItem(
            id = 131121,
            parentId = 131100,
            name="枣强县",
        ),



        AreaItem(
            id = 131122,
            parentId = 131100,
            name="武邑县",
        ),



        AreaItem(
            id = 131123,
            parentId = 131100,
            name="武强县",
        ),



        AreaItem(
            id = 131124,
            parentId = 131100,
            name="饶阳县",
        ),



        AreaItem(
            id = 131125,
            parentId = 131100,
            name="安平县",
        ),



        AreaItem(
            id = 131126,
            parentId = 131100,
            name="故城县",
        ),



        AreaItem(
            id = 131127,
            parentId = 131100,
            name="景县",
        ),



        AreaItem(
            id = 131128,
            parentId = 131100,
            name="阜城县",
        ),



        AreaItem(
            id = 131182,
            parentId = 131100,
            name="深州市",
        ),



        AreaItem(
            id = 140000,
            parentId = 0,
            name="山西省",
        ),



        AreaItem(
            id = 140100,
            parentId = 140000,
            name="太原市",
        ),



        AreaItem(
            id = 140105,
            parentId = 140100,
            name="小店区",
        ),



        AreaItem(
            id = 140106,
            parentId = 140100,
            name="迎泽区",
        ),



        AreaItem(
            id = 140107,
            parentId = 140100,
            name="杏花岭区",
        ),



        AreaItem(
            id = 140108,
            parentId = 140100,
            name="尖草坪区",
        ),



        AreaItem(
            id = 140109,
            parentId = 140100,
            name="万柏林区",
        ),



        AreaItem(
            id = 140110,
            parentId = 140100,
            name="晋源区",
        ),



        AreaItem(
            id = 140121,
            parentId = 140100,
            name="清徐县",
        ),



        AreaItem(
            id = 140122,
            parentId = 140100,
            name="阳曲县",
        ),



        AreaItem(
            id = 140123,
            parentId = 140100,
            name="娄烦县",
        ),



        AreaItem(
            id = 140181,
            parentId = 140100,
            name="古交市",
        ),



        AreaItem(
            id = 140200,
            parentId = 140000,
            name="大同市",
        ),



        AreaItem(
            id = 140212,
            parentId = 140200,
            name="新荣区",
        ),



        AreaItem(
            id = 140213,
            parentId = 140200,
            name="平城区",
        ),



        AreaItem(
            id = 140214,
            parentId = 140200,
            name="云冈区",
        ),



        AreaItem(
            id = 140215,
            parentId = 140200,
            name="云州区",
        ),



        AreaItem(
            id = 140221,
            parentId = 140200,
            name="阳高县",
        ),



        AreaItem(
            id = 140222,
            parentId = 140200,
            name="天镇县",
        ),



        AreaItem(
            id = 140223,
            parentId = 140200,
            name="广灵县",
        ),



        AreaItem(
            id = 140224,
            parentId = 140200,
            name="灵丘县",
        ),



        AreaItem(
            id = 140225,
            parentId = 140200,
            name="浑源县",
        ),



        AreaItem(
            id = 140226,
            parentId = 140200,
            name="左云县",
        ),



        AreaItem(
            id = 140300,
            parentId = 140000,
            name="阳泉市",
        ),



        AreaItem(
            id = 140302,
            parentId = 140300,
            name="城区",
        ),



        AreaItem(
            id = 140303,
            parentId = 140300,
            name="矿区",
        ),



        AreaItem(
            id = 140311,
            parentId = 140300,
            name="郊区",
        ),



        AreaItem(
            id = 140321,
            parentId = 140300,
            name="平定县",
        ),



        AreaItem(
            id = 140322,
            parentId = 140300,
            name="盂县",
        ),



        AreaItem(
            id = 140400,
            parentId = 140000,
            name="长治市",
        ),



        AreaItem(
            id = 140402,
            parentId = 140400,
            name="潞州区",
        ),



        AreaItem(
            id = 140421,
            parentId = 140400,
            name="上党区",
        ),



        AreaItem(
            id = 140423,
            parentId = 140400,
            name="襄垣县",
        ),



        AreaItem(
            id = 140424,
            parentId = 140400,
            name="屯留区",
        ),



        AreaItem(
            id = 140425,
            parentId = 140400,
            name="平顺县",
        ),



        AreaItem(
            id = 140426,
            parentId = 140400,
            name="黎城县",
        ),



        AreaItem(
            id = 140427,
            parentId = 140400,
            name="壶关县",
        ),



        AreaItem(
            id = 140428,
            parentId = 140400,
            name="长子县",
        ),



        AreaItem(
            id = 140429,
            parentId = 140400,
            name="武乡县",
        ),



        AreaItem(
            id = 140430,
            parentId = 140400,
            name="沁县",
        ),



        AreaItem(
            id = 140431,
            parentId = 140400,
            name="沁源县",
        ),



        AreaItem(
            id = 140481,
            parentId = 140400,
            name="潞城区",
        ),



        AreaItem(
            id = 140500,
            parentId = 140000,
            name="晋城市",
        ),



        AreaItem(
            id = 140502,
            parentId = 140500,
            name="城区",
        ),



        AreaItem(
            id = 140521,
            parentId = 140500,
            name="沁水县",
        ),



        AreaItem(
            id = 140522,
            parentId = 140500,
            name="阳城县",
        ),



        AreaItem(
            id = 140524,
            parentId = 140500,
            name="陵川县",
        ),



        AreaItem(
            id = 140525,
            parentId = 140500,
            name="泽州县",
        ),



        AreaItem(
            id = 140581,
            parentId = 140500,
            name="高平市",
        ),



        AreaItem(
            id = 140600,
            parentId = 140000,
            name="朔州市",
        ),



        AreaItem(
            id = 140602,
            parentId = 140600,
            name="朔城区",
        ),



        AreaItem(
            id = 140603,
            parentId = 140600,
            name="平鲁区",
        ),



        AreaItem(
            id = 140621,
            parentId = 140600,
            name="山阴县",
        ),



        AreaItem(
            id = 140622,
            parentId = 140600,
            name="应县",
        ),



        AreaItem(
            id = 140623,
            parentId = 140600,
            name="右玉县",
        ),



        AreaItem(
            id = 140681,
            parentId = 140600,
            name="怀仁市",
        ),



        AreaItem(
            id = 140700,
            parentId = 140000,
            name="晋中市",
        ),



        AreaItem(
            id = 140702,
            parentId = 140700,
            name="榆次区",
        ),



        AreaItem(
            id = 140721,
            parentId = 140700,
            name="榆社县",
        ),



        AreaItem(
            id = 140722,
            parentId = 140700,
            name="左权县",
        ),



        AreaItem(
            id = 140723,
            parentId = 140700,
            name="和顺县",
        ),



        AreaItem(
            id = 140724,
            parentId = 140700,
            name="昔阳县",
        ),



        AreaItem(
            id = 140725,
            parentId = 140700,
            name="寿阳县",
        ),



        AreaItem(
            id = 140726,
            parentId = 140700,
            name="太谷县",
        ),



        AreaItem(
            id = 140727,
            parentId = 140700,
            name="祁县",
        ),



        AreaItem(
            id = 140728,
            parentId = 140700,
            name="平遥县",
        ),



        AreaItem(
            id = 140729,
            parentId = 140700,
            name="灵石县",
        ),



        AreaItem(
            id = 140781,
            parentId = 140700,
            name="介休市",
        ),



        AreaItem(
            id = 140800,
            parentId = 140000,
            name="运城市",
        ),



        AreaItem(
            id = 140802,
            parentId = 140800,
            name="盐湖区",
        ),



        AreaItem(
            id = 140821,
            parentId = 140800,
            name="临猗县",
        ),



        AreaItem(
            id = 140822,
            parentId = 140800,
            name="万荣县",
        ),



        AreaItem(
            id = 140823,
            parentId = 140800,
            name="闻喜县",
        ),



        AreaItem(
            id = 140824,
            parentId = 140800,
            name="稷山县",
        ),



        AreaItem(
            id = 140825,
            parentId = 140800,
            name="新绛县",
        ),



        AreaItem(
            id = 140826,
            parentId = 140800,
            name="绛县",
        ),



        AreaItem(
            id = 140827,
            parentId = 140800,
            name="垣曲县",
        ),



        AreaItem(
            id = 140828,
            parentId = 140800,
            name="夏县",
        ),



        AreaItem(
            id = 140829,
            parentId = 140800,
            name="平陆县",
        ),



        AreaItem(
            id = 140830,
            parentId = 140800,
            name="芮城县",
        ),



        AreaItem(
            id = 140881,
            parentId = 140800,
            name="永济市",
        ),



        AreaItem(
            id = 140882,
            parentId = 140800,
            name="河津市",
        ),



        AreaItem(
            id = 140900,
            parentId = 140000,
            name="忻州市",
        ),



        AreaItem(
            id = 140902,
            parentId = 140900,
            name="忻府区",
        ),



        AreaItem(
            id = 140921,
            parentId = 140900,
            name="定襄县",
        ),



        AreaItem(
            id = 140922,
            parentId = 140900,
            name="五台县",
        ),



        AreaItem(
            id = 140923,
            parentId = 140900,
            name="代县",
        ),



        AreaItem(
            id = 140924,
            parentId = 140900,
            name="繁峙县",
        ),



        AreaItem(
            id = 140925,
            parentId = 140900,
            name="宁武县",
        ),



        AreaItem(
            id = 140926,
            parentId = 140900,
            name="静乐县",
        ),



        AreaItem(
            id = 140927,
            parentId = 140900,
            name="神池县",
        ),



        AreaItem(
            id = 140928,
            parentId = 140900,
            name="五寨县",
        ),



        AreaItem(
            id = 140929,
            parentId = 140900,
            name="岢岚县",
        ),



        AreaItem(
            id = 140930,
            parentId = 140900,
            name="河曲县",
        ),



        AreaItem(
            id = 140931,
            parentId = 140900,
            name="保德县",
        ),



        AreaItem(
            id = 140932,
            parentId = 140900,
            name="偏关县",
        ),



        AreaItem(
            id = 140981,
            parentId = 140900,
            name="原平市",
        ),



        AreaItem(
            id = 141000,
            parentId = 140000,
            name="临汾市",
        ),



        AreaItem(
            id = 141002,
            parentId = 141000,
            name="尧都区",
        ),



        AreaItem(
            id = 141021,
            parentId = 141000,
            name="曲沃县",
        ),



        AreaItem(
            id = 141022,
            parentId = 141000,
            name="翼城县",
        ),



        AreaItem(
            id = 141023,
            parentId = 141000,
            name="襄汾县",
        ),



        AreaItem(
            id = 141024,
            parentId = 141000,
            name="洪洞县",
        ),



        AreaItem(
            id = 141025,
            parentId = 141000,
            name="古县",
        ),



        AreaItem(
            id = 141026,
            parentId = 141000,
            name="安泽县",
        ),



        AreaItem(
            id = 141027,
            parentId = 141000,
            name="浮山县",
        ),



        AreaItem(
            id = 141028,
            parentId = 141000,
            name="吉县",
        ),



        AreaItem(
            id = 141029,
            parentId = 141000,
            name="乡宁县",
        ),



        AreaItem(
            id = 141030,
            parentId = 141000,
            name="大宁县",
        ),



        AreaItem(
            id = 141031,
            parentId = 141000,
            name="隰县",
        ),



        AreaItem(
            id = 141032,
            parentId = 141000,
            name="永和县",
        ),



        AreaItem(
            id = 141033,
            parentId = 141000,
            name="蒲县",
        ),



        AreaItem(
            id = 141034,
            parentId = 141000,
            name="汾西县",
        ),



        AreaItem(
            id = 141081,
            parentId = 141000,
            name="侯马市",
        ),



        AreaItem(
            id = 141082,
            parentId = 141000,
            name="霍州市",
        ),



        AreaItem(
            id = 141100,
            parentId = 140000,
            name="吕梁市",
        ),



        AreaItem(
            id = 141102,
            parentId = 141100,
            name="离石区",
        ),



        AreaItem(
            id = 141121,
            parentId = 141100,
            name="文水县",
        ),



        AreaItem(
            id = 141122,
            parentId = 141100,
            name="交城县",
        ),



        AreaItem(
            id = 141123,
            parentId = 141100,
            name="兴县",
        ),



        AreaItem(
            id = 141124,
            parentId = 141100,
            name="临县",
        ),



        AreaItem(
            id = 141125,
            parentId = 141100,
            name="柳林县",
        ),



        AreaItem(
            id = 141126,
            parentId = 141100,
            name="石楼县",
        ),



        AreaItem(
            id = 141127,
            parentId = 141100,
            name="岚县",
        ),



        AreaItem(
            id = 141128,
            parentId = 141100,
            name="方山县",
        ),



        AreaItem(
            id = 141129,
            parentId = 141100,
            name="中阳县",
        ),



        AreaItem(
            id = 141130,
            parentId = 141100,
            name="交口县",
        ),



        AreaItem(
            id = 141181,
            parentId = 141100,
            name="孝义市",
        ),



        AreaItem(
            id = 141182,
            parentId = 141100,
            name="汾阳市",
        ),



        AreaItem(
            id = 150000,
            parentId = 0,
            name="内蒙古自治区",
        ),



        AreaItem(
            id = 150100,
            parentId = 150000,
            name="呼和浩特市",
        ),



        AreaItem(
            id = 150102,
            parentId = 150100,
            name="新城区",
        ),



        AreaItem(
            id = 150103,
            parentId = 150100,
            name="回民区",
        ),



        AreaItem(
            id = 150104,
            parentId = 150100,
            name="玉泉区",
        ),



        AreaItem(
            id = 150105,
            parentId = 150100,
            name="赛罕区",
        ),



        AreaItem(
            id = 150121,
            parentId = 150100,
            name="土默特左旗",
        ),



        AreaItem(
            id = 150122,
            parentId = 150100,
            name="托克托县",
        ),



        AreaItem(
            id = 150123,
            parentId = 150100,
            name="和林格尔县",
        ),



        AreaItem(
            id = 150124,
            parentId = 150100,
            name="清水河县",
        ),



        AreaItem(
            id = 150125,
            parentId = 150100,
            name="武川县",
        ),



        AreaItem(
            id = 150200,
            parentId = 150000,
            name="包头市",
        ),



        AreaItem(
            id = 150202,
            parentId = 150200,
            name="东河区",
        ),



        AreaItem(
            id = 150203,
            parentId = 150200,
            name="昆都仑区",
        ),



        AreaItem(
            id = 150204,
            parentId = 150200,
            name="青山区",
        ),



        AreaItem(
            id = 150205,
            parentId = 150200,
            name="石拐区",
        ),



        AreaItem(
            id = 150206,
            parentId = 150200,
            name="白云鄂博矿区",
        ),



        AreaItem(
            id = 150207,
            parentId = 150200,
            name="九原区",
        ),



        AreaItem(
            id = 150221,
            parentId = 150200,
            name="土默特右旗",
        ),



        AreaItem(
            id = 150222,
            parentId = 150200,
            name="固阳县",
        ),



        AreaItem(
            id = 150223,
            parentId = 150200,
            name="达尔罕茂明安联合旗",
        ),



        AreaItem(
            id = 150300,
            parentId = 150000,
            name="乌海市",
        ),



        AreaItem(
            id = 150302,
            parentId = 150300,
            name="海勃湾区",
        ),



        AreaItem(
            id = 150303,
            parentId = 150300,
            name="海南区",
        ),



        AreaItem(
            id = 150304,
            parentId = 150300,
            name="乌达区",
        ),



        AreaItem(
            id = 150400,
            parentId = 150000,
            name="赤峰市",
        ),



        AreaItem(
            id = 150402,
            parentId = 150400,
            name="红山区",
        ),



        AreaItem(
            id = 150403,
            parentId = 150400,
            name="元宝山区",
        ),



        AreaItem(
            id = 150404,
            parentId = 150400,
            name="松山区",
        ),



        AreaItem(
            id = 150421,
            parentId = 150400,
            name="阿鲁科尔沁旗",
        ),



        AreaItem(
            id = 150422,
            parentId = 150400,
            name="巴林左旗",
        ),



        AreaItem(
            id = 150423,
            parentId = 150400,
            name="巴林右旗",
        ),



        AreaItem(
            id = 150424,
            parentId = 150400,
            name="林西县",
        ),



        AreaItem(
            id = 150425,
            parentId = 150400,
            name="克什克腾旗",
        ),



        AreaItem(
            id = 150426,
            parentId = 150400,
            name="翁牛特旗",
        ),



        AreaItem(
            id = 150428,
            parentId = 150400,
            name="喀喇沁旗",
        ),



        AreaItem(
            id = 150429,
            parentId = 150400,
            name="宁城县",
        ),



        AreaItem(
            id = 150430,
            parentId = 150400,
            name="敖汉旗",
        ),



        AreaItem(
            id = 150500,
            parentId = 150000,
            name="通辽市",
        ),



        AreaItem(
            id = 150502,
            parentId = 150500,
            name="科尔沁区",
        ),



        AreaItem(
            id = 150521,
            parentId = 150500,
            name="科尔沁左翼中旗",
        ),



        AreaItem(
            id = 150522,
            parentId = 150500,
            name="科尔沁左翼后旗",
        ),



        AreaItem(
            id = 150523,
            parentId = 150500,
            name="开鲁县",
        ),



        AreaItem(
            id = 150524,
            parentId = 150500,
            name="库伦旗",
        ),



        AreaItem(
            id = 150525,
            parentId = 150500,
            name="奈曼旗",
        ),



        AreaItem(
            id = 150526,
            parentId = 150500,
            name="扎鲁特旗",
        ),



        AreaItem(
            id = 150581,
            parentId = 150500,
            name="霍林郭勒市",
        ),



        AreaItem(
            id = 150600,
            parentId = 150000,
            name="鄂尔多斯市",
        ),



        AreaItem(
            id = 150602,
            parentId = 150600,
            name="东胜区",
        ),



        AreaItem(
            id = 150603,
            parentId = 150600,
            name="康巴什区",
        ),



        AreaItem(
            id = 150621,
            parentId = 150600,
            name="达拉特旗",
        ),



        AreaItem(
            id = 150622,
            parentId = 150600,
            name="准格尔旗",
        ),



        AreaItem(
            id = 150623,
            parentId = 150600,
            name="鄂托克前旗",
        ),



        AreaItem(
            id = 150624,
            parentId = 150600,
            name="鄂托克旗",
        ),



        AreaItem(
            id = 150625,
            parentId = 150600,
            name="杭锦旗",
        ),



        AreaItem(
            id = 150626,
            parentId = 150600,
            name="乌审旗",
        ),



        AreaItem(
            id = 150627,
            parentId = 150600,
            name="伊金霍洛旗",
        ),



        AreaItem(
            id = 150700,
            parentId = 150000,
            name="呼伦贝尔市",
        ),



        AreaItem(
            id = 150702,
            parentId = 150700,
            name="海拉尔区",
        ),



        AreaItem(
            id = 150703,
            parentId = 150700,
            name="扎赉诺尔区",
        ),



        AreaItem(
            id = 150721,
            parentId = 150700,
            name="阿荣旗",
        ),



        AreaItem(
            id = 150722,
            parentId = 150700,
            name="莫力达瓦达斡尔族自治旗",
        ),



        AreaItem(
            id = 150723,
            parentId = 150700,
            name="鄂伦春自治旗",
        ),



        AreaItem(
            id = 150724,
            parentId = 150700,
            name="鄂温克族自治旗",
        ),



        AreaItem(
            id = 150725,
            parentId = 150700,
            name="陈巴尔虎旗",
        ),



        AreaItem(
            id = 150726,
            parentId = 150700,
            name="新巴尔虎左旗",
        ),



        AreaItem(
            id = 150727,
            parentId = 150700,
            name="新巴尔虎右旗",
        ),



        AreaItem(
            id = 150781,
            parentId = 150700,
            name="满洲里市",
        ),



        AreaItem(
            id = 150782,
            parentId = 150700,
            name="牙克石市",
        ),



        AreaItem(
            id = 150783,
            parentId = 150700,
            name="扎兰屯市",
        ),



        AreaItem(
            id = 150784,
            parentId = 150700,
            name="额尔古纳市",
        ),



        AreaItem(
            id = 150785,
            parentId = 150700,
            name="根河市",
        ),



        AreaItem(
            id = 150800,
            parentId = 150000,
            name="巴彦淖尔市",
        ),



        AreaItem(
            id = 150802,
            parentId = 150800,
            name="临河区",
        ),



        AreaItem(
            id = 150821,
            parentId = 150800,
            name="五原县",
        ),



        AreaItem(
            id = 150822,
            parentId = 150800,
            name="磴口县",
        ),



        AreaItem(
            id = 150823,
            parentId = 150800,
            name="乌拉特前旗",
        ),



        AreaItem(
            id = 150824,
            parentId = 150800,
            name="乌拉特中旗",
        ),



        AreaItem(
            id = 150825,
            parentId = 150800,
            name="乌拉特后旗",
        ),



        AreaItem(
            id = 150826,
            parentId = 150800,
            name="杭锦后旗",
        ),



        AreaItem(
            id = 150900,
            parentId = 150000,
            name="乌兰察布市",
        ),



        AreaItem(
            id = 150902,
            parentId = 150900,
            name="集宁区",
        ),



        AreaItem(
            id = 150921,
            parentId = 150900,
            name="卓资县",
        ),



        AreaItem(
            id = 150922,
            parentId = 150900,
            name="化德县",
        ),



        AreaItem(
            id = 150923,
            parentId = 150900,
            name="商都县",
        ),



        AreaItem(
            id = 150924,
            parentId = 150900,
            name="兴和县",
        ),



        AreaItem(
            id = 150925,
            parentId = 150900,
            name="凉城县",
        ),



        AreaItem(
            id = 150926,
            parentId = 150900,
            name="察哈尔右翼前旗",
        ),



        AreaItem(
            id = 150927,
            parentId = 150900,
            name="察哈尔右翼中旗",
        ),



        AreaItem(
            id = 150928,
            parentId = 150900,
            name="察哈尔右翼后旗",
        ),



        AreaItem(
            id = 150929,
            parentId = 150900,
            name="四子王旗",
        ),



        AreaItem(
            id = 150981,
            parentId = 150900,
            name="丰镇市",
        ),



        AreaItem(
            id = 152200,
            parentId = 150000,
            name="兴安盟",
        ),



        AreaItem(
            id = 152201,
            parentId = 152200,
            name="乌兰浩特市",
        ),



        AreaItem(
            id = 152202,
            parentId = 152200,
            name="阿尔山市",
        ),



        AreaItem(
            id = 152221,
            parentId = 152200,
            name="科尔沁右翼前旗",
        ),



        AreaItem(
            id = 152222,
            parentId = 152200,
            name="科尔沁右翼中旗",
        ),



        AreaItem(
            id = 152223,
            parentId = 152200,
            name="扎赉特旗",
        ),



        AreaItem(
            id = 152224,
            parentId = 152200,
            name="突泉县",
        ),



        AreaItem(
            id = 152500,
            parentId = 150000,
            name="锡林郭勒盟",
        ),



        AreaItem(
            id = 152501,
            parentId = 152500,
            name="二连浩特市",
        ),



        AreaItem(
            id = 152502,
            parentId = 152500,
            name="锡林浩特市",
        ),



        AreaItem(
            id = 152522,
            parentId = 152500,
            name="阿巴嘎旗",
        ),



        AreaItem(
            id = 152523,
            parentId = 152500,
            name="苏尼特左旗",
        ),



        AreaItem(
            id = 152524,
            parentId = 152500,
            name="苏尼特右旗",
        ),



        AreaItem(
            id = 152525,
            parentId = 152500,
            name="东乌珠穆沁旗",
        ),



        AreaItem(
            id = 152526,
            parentId = 152500,
            name="西乌珠穆沁旗",
        ),



        AreaItem(
            id = 152527,
            parentId = 152500,
            name="太仆寺旗",
        ),



        AreaItem(
            id = 152528,
            parentId = 152500,
            name="镶黄旗",
        ),



        AreaItem(
            id = 152529,
            parentId = 152500,
            name="正镶白旗",
        ),



        AreaItem(
            id = 152530,
            parentId = 152500,
            name="正蓝旗",
        ),



        AreaItem(
            id = 152531,
            parentId = 152500,
            name="多伦县",
        ),



        AreaItem(
            id = 152900,
            parentId = 150000,
            name="阿拉善盟",
        ),
        AreaItem(
            id = 152921,
            parentId = 152900,
            name="阿拉善左旗",
        ),
        AreaItem(
            id = 152922,
            parentId = 152900,
            name="阿拉善右旗",
        ),
        AreaItem(
            id = 152923,
            parentId = 152900,
            name="额济纳旗",
        ),
)

fun LoadAreaItems(context: Context): List<AreaItem> {
    val stream = context.assets.open("area.json")
    val writer = StringWriter()
    val buffer = CharArray(1024)
    try {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    } finally {
        stream.close()
    }

    val jsonString = writer.toString()
    return Json.decodeFromString(jsonString)
}