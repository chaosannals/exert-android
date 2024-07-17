package com.example.calendardemo.ui.page.ktlang

import androidx.compose.runtime.Composable

private inline fun callOne(one: () -> Unit) {
    one()
}

private inline fun callCross(crossinline one: () -> Unit) {
    one()
}

private inline fun callTwo(one: () -> Unit, noinline two: () -> Unit) {
    one()
    two()
}

@Composable
fun InlineDemoPage() {
    callOne {
        return // inline 导致可以在回调函数里使用 return 这种必定是 inline 的，初看误导好大。
    }
    callCross {
//        return // crossinline 禁止这种闭包里面写 return 的行为，不然代码就太乱了。
        return@callCross // 只能 return 本函数作用域。
    }
    callTwo({
        return
    }, {
//        return // 和 crossinline 一样不能 return
        return@callTwo // 而且不是内联。
    })
}