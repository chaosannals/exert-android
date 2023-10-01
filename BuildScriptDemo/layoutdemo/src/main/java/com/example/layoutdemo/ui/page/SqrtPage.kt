package com.example.layoutdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.log
import kotlin.math.sqrt

// 连分数法
// r² = a² + b （ a 远大于 b）
// r = a + b / (2a + b / (2a + ...))
// lv0: r ≈ a
// lv1: r ≈ a + b / 2a
// lv2: r ≈ a + b / (2a + b / 2a)
// 证：
// r² - a² = b
// (r + a)(r - a) = b
// r - a = b / (r + a)
// r = a + b / (a + r)
// 此时：右边 r 可递归展开：
// lv1： r = a + b / (a + [a + b / (a + r)]) = a + b / (2a + b / (a + r))
private fun sqrt1(v: Double, times: Int=10) {

}

// 长除法
// r² = (10b + a)²
// r = 10b + a
// 此时： 10b + a 是十进制表示法： ba，dcba 则如下：
// r² = (10³d + 10²c + 10b + a)²
// r = 10³d + 10²c + 10b + a
// 这样会很复制，所以：
// 进行分段，是为了控制在 100 以内 ba 形式，百进制。
// r² = （10³b1 + 10²a1 + 10b0 + a0)²
// r = 10³b1 + 10²a1 + 10b0 + a0
// 此时每段单独计算后，余数贴回下一段计算，就可以递归计算 10b + a
// 证：
// r² = 100b² + 20ab + a²
// r² - 100b² = (20b + a)a
private fun sqrt2(v: Double, times: Int=10) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SqrtPage() {
    var numberText by remember {
        mutableStateOf("100.100")
    }
    var sqrt1Times by remember {
        mutableStateOf(10)
    }
    var sqrt2Times by remember {
        mutableStateOf(10)
    }

    val sqrtResult by remember {
        derivedStateOf {
            numberText.toDoubleOrNull()?.let { sqrt(it) }?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = numberText,
            onValueChange = {numberText = it}
        )
        Text(sqrtResult)
        TextField(
            value = sqrt1Times.toString(),
            onValueChange = { sqrt1Times = it.toIntOrNull() ?: 0}
        )
        TextField(
            value = sqrt2Times.toString(),
            onValueChange = { sqrt2Times = it.toIntOrNull() ?: 0}
        )
    }
}

@Preview
@Composable
fun SqrtPagePreview() {
    SqrtPage()
}