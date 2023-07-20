package com.example.app24.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

// 以下两种方式都无法实现基本类型和自定义对象 union 的效果。
// 即：
// "string"  和  { ... } 的直接多态不可行
// {  "a": "string..." } 和 { "a": { ... } } 必须整体是个对象
// 原因在于，此方式使用了 ”继承“ 而无法让 string 等已有类继承后定义的数据结构。

// 此类方式需要主动识别数据。
// 实例是通过 type 字段区分，或者可以自行判断 json 对象的字段。
object TypeDataSerializer : JsonContentPolymorphicSerializer<TypeData>(TypeData::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<out TypeData> {

        return when (val type = element.jsonObject["type"]?.jsonPrimitive?.contentOrNull) {
            "object"    -> TypeObjectData.serializer()
            "string"    -> TypeStringData.serializer()
            else -> error("unknown Item type $type")
        }
    }
}

@Serializable(with = TypeDataSerializer::class)
sealed class TypeData {
    abstract val type: String
}

@Serializable
data class TypeObjectData @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val type: String = "object",
    val id: Int,
    val name: String,
) :TypeData()

@Serializable
data class TypeStringData @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val type: String = "string",
    val value: String,
):TypeData()


//////////////////////////////////////////////////////////////
// 此种方式非常繁琐，IntResult 并非 Int ，而是 { "value": 123 } 这样的对象。
@Serializable
data class WrapColor(
    @Polymorphic val color: ColorData
)


// JsonClassDiscriminator 其实就是 typeData 的 type 字段。
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class ColorData {
    @Serializable
    data class IntResult(val value: Int): ColorData()
    @Serializable
    data class StringResult(val value: String): ColorData()
}

@Serializable
data class ColorInfo(
    val name: String,
    val value: Int,
): ColorData()

@Composable
fun ColorText(
    color: ColorData,
) {
    when(color) {
        is ColorInfo -> {
            Text("name: ${color.name} value: ${color.value}")
        }
        is ColorData.IntResult -> {
            Text("Int: ${color.value}")
        }
        is ColorData.StringResult -> {
            Text("String: ${color.value}")
        }
    }
}

@Composable
fun TypeText(
    type: TypeData,
) {
    when(type) {
        is TypeObjectData -> {
            Text("type: ${type.id} name: ${type.name}")
        }
        is TypeStringData -> {
            Text("type: ${type.value}")
        }
    }
}

@Composable
fun JsonPage() {
    val colorJsoner = remember {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            allowStructuredMapKeys = true
            encodeDefaults = true
//            classDiscriminator = "#class" // 不知道这个表示法是什么意思.
            //  @JsonClassDiscriminator 时好时坏。
            serializersModule = SerializersModule {
                polymorphic(ColorData::class) {
                    subclass(ColorInfo::class, ColorInfo.serializer())
                    subclass(ColorData.IntResult::class, ColorData.IntResult.serializer())
                    subclass(ColorData.StringResult::class, ColorData.StringResult.serializer())
                }
            }
        }
    }

    val colorsJson = remember {
        """
            [
                {
                    "kind": "com.example.app24.ui.page.demo.ColorInfo",
                    "name": "red",
                    "value": 4
                },
                {
                    "kind": "com.example.app24.ui.page.demo.ColorData.IntResult",
                    "value": 1234
                },
                {
                    "kind": "com.example.app24.ui.page.demo.ColorData.StringResult",
                    "value": "1234ttttt"
                }
            ]
        """.trimIndent()
    }
    val colors by remember {
        derivedStateOf {
            colorJsoner.decodeFromString<List<ColorData>>(colorsJson)
        }
    }

    val colorJson = remember {
        """
            {
                "color": {
                    "kind": "com.example.app24.ui.page.demo.ColorInfo",
                    "name": "red",
                    "value": 4
                }
            }
        """.trimIndent()
    }
    val color by remember {
        derivedStateOf {
            colorJsoner.decodeFromString<WrapColor>(colorJson)
        }
    }

    val typesJson = remember {
        """
            [
                {
                    "type": "string",
                    "value": "123455"
                },
                {
                    "type": "object",
                    "id": 123,
                    "name": "objectType"
                },
                {
                    "type": "string",
                    "value": "tttttt"
                }
            ]
        """.trimIndent()
    }
    val types by remember {
        derivedStateOf {
            Json.decodeFromString<List<TypeData>>(typesJson)
        }
    }


    // 完全弱化类型。
    val mapJsoner = remember {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            allowStructuredMapKeys = true
            encodeDefaults = true
        }
    }
    val mapJson = remember {
        """
            {
                "a": 123,
                "b": "tttt",
                "c": {
                    "c1": 123,
                    "c2": {}
                },
                "d": null
            }
        """.trimIndent()
    }
    val map by remember {
        derivedStateOf {
            mapJsoner.decodeFromString<Map<String, JsonElement?>>(mapJson)
        }
    }

    // encode
    // 此序列化 JSON 库有自己一套节点类型
    // JsonNull  null
    // JsonElement 基类
    // JsonObject 字典 map
    // JsonArray 数组 array
    // JsonPrimitive 基础类型
    val anyJsoner = remember {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            allowStructuredMapKeys = true
            encodeDefaults = true
            classDiscriminator = "#class"
        }
    }
    val any = remember {
        JsonObject(
            mapOf(
                "aaa" to JsonPrimitive(123),
                "bbb" to JsonPrimitive("string"),
                "ccc" to JsonObject(
                    mapOf(
                        "ccc1" to JsonPrimitive(123),
                        "ccc2" to JsonPrimitive(null),
                    )
                )
            )
        )
    }
    val anyJson by remember {
        derivedStateOf {
            anyJsoner.encodeToString(any)
        }
    }

    Column() {
        ColorText(color.color)
        for (c in colors) {
            ColorText(c)
        }
        for (t in types) {
            TypeText(type = t)
        }

        Text("map.a: ${map["a"]}")
        Text("map.b: ${map["b"]}")
        Text("map.c: ${map["c"]}")
        Text("map.d: ${map["d"]}")

        Text("any: ${anyJson}")
    }
}

@Preview
@Composable
fun JsonPagePreview() {
    DesignPreview {
        JsonPage()
    }
}