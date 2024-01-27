package com.example.jcm3wv.ui.page.demo

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.lazyTableDimensions
import eu.wewox.lazytable.lazyTablePinConfiguration
import eu.wewox.lazytable.rememberLazyTableState
import io.github.serpro69.kfaker.faker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

data class LazyTableDemoItem(
    val name: String,
    val age: Int,
    val address: String,
    val email: String,
)

// 这个 组件 在 连接调试器的时候会很卡，断开调试器后流畅。不要使用调试器调试
// 他只维护索引，视图（Compose）和数据由使用者维护
@Composable
fun LazyTablePage() {
    val rows = remember {
        mutableStateListOf<LazyTableDemoItem>()
    }
    val columns by remember {
        derivedStateOf {
            val names = LazyTableDemoItem::class.primaryConstructor!!.parameters.map {
                it.name
            }
            LazyTableDemoItem::class.memberProperties.filter {
                it.visibility == KVisibility.PUBLIC
            }.map {
                it
            }.sortedBy {
                names.indexOf(it.name)
            }
        }
    }
    val columnCount by remember(columns) {
        derivedStateOf {
            columns.size + 1
        }
    }

    val fake by remember {
        mutableStateOf(faker {})
    }

    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            for (j in 0 .. 100) {
                rows.addAll((0 .. 100).toList().map {
                    LazyTableDemoItem(
                        name = fake.name.name(),
                        age = Random.nextInt(2, 100),
                        address = fake.address.fullAddress(),
                        email = fake.internet.email(),
                    )
                })
                delay(10000)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyTable(
            state = rememberLazyTableState(),
            dimensions = lazyTableDimensions(
                columnSize = {
                    when (it) {
                        0 -> 60.dp
                        1 -> 120.dp
                        2 -> 40.dp
                        3 -> 500.dp
                        4 -> 500.dp
                        else -> 200.dp
                    }
                },
                rowSize = {
                    when (it) {
                        0 -> 40.dp
                        else -> 32.dp
                    }
                }
            ),
            pinConfiguration = lazyTablePinConfiguration(
                columns = 1, // 列起始，没有列头用 0
                rows = 1, // 行的起始， 没有表头时用 0
            ),
        ) {
            // 原角
            items(
                count = 1,
                layoutInfo = {
                    LazyTableItem(
                        column = 0,
                        row = 0,
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .zIndex(100f)
                        .fillMaxSize()
                        .background(Color.White)
                ) {

                }
            }

            // 列头
            items(
                count = columns.size,
                layoutInfo = {
                    LazyTableItem(
                        column = it % columnCount + 1,
                        row = 0,
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .drawBehind {
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height),
                                end= Offset(size.width, size.height),
                                strokeWidth = 1f,
                            )
                        }
                ) {
                    Text(
                        text = columns[it].name,
                    )
                }
            }

            // 行头
            items(
                count = rows.size,
                layoutInfo = {
                    LazyTableItem(
                        column = 0,
                        row = it + 1,
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .drawBehind {
                            drawLine(
                                color = Color.Gray,
                                start = Offset(size.width, 0f),
                                end= Offset(size.width, size.height),
                                strokeWidth = 1f,
                            )
                        }
                ) {
                    Text(
                        text = "${it + 1}",
                    )
                }
            }

            // 内容
            items(
                count = rows.size * columns.size,
                layoutInfo = {
                    LazyTableItem(
                        column = it % columns.size + 1,
                        row = it / columns.size + 1,
                    )
                }
            ) {
                val item = rows[it / columns.size]
                val kp = columns[it % columns.size]
                Box(
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    Text(
                        text = kp.getter.call(item).toString(),
                    )
                }
            }
        }
    }
}