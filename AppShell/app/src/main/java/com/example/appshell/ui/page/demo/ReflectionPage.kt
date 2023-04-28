package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class RKClass(
    val i1: Int,
    var vari1: Int,
    var s1: String = "aaaaa",
    val d1: Double = 1 / 3.0,
) {
    fun f1() { "f1 result" }
}

@Composable
fun ReflectionPage() {
    val rk = RKClass(1,2)
    Column {
        Text(text = "qualifiedName: ${rk::class.qualifiedName} psize: ${rk::class.declaredMemberProperties.size}")

        // 属性是 虚拟机函数，所以一下方式类似方法。
        for (m in rk::class.declaredMemberProperties) {
            m.isAccessible = true // 开放访问权限
            Text("${m.returnType} ${m.name} = ${m.getter.call(rk)}")
        }
    }
}

@Preview
@Composable
fun ReflectionPagePreview() {
    DesignPreview {
        ReflectionPage()
    }
}