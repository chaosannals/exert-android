package com.example.appshell.ui.page.demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import com.jakewharton.threetenabp.AndroidThreeTen

// 使用以下替代 java.time.Instant
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun Date310Page() {
    val now by remember {
        mutableStateOf(Instant.now())
    }
    val zonedNow by remember(now) {
        derivedStateOf {
            now.atZone(ZoneId.systemDefault())
        }
    }
    val formatters = remember {
//        val dtfc = DateTimeFormatter::class.createType()
        DateTimeFormatter::class.java.declaredFields
            .filter {
                val m = it.modifiers
                java.lang.reflect.Modifier.isStatic(m)
                        && java.lang.reflect.Modifier.isPublic(m)
            }
            .map { Pair(it.name, it.get(null) as? DateTimeFormatter) }
            .filter { it.second != null }
    }
    var formatter by remember(formatters) {
        mutableStateOf(
            formatters.get(0)
        )
    }
    val text by remember(formatter, zonedNow) {
        derivedStateOf {
            zonedNow.format(formatter.second)
        }
    }
    var showEnum by remember {
        mutableStateOf(false)
    }
    val lazyState = rememberLazyListState()

    var showPopup by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        state = lazyState,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .border(1.sdp, Color.Yellow),
    ) {
        item {
            Text(text = "${now.epochSecond}s")
        }
        item {
            Box() {
                Text(
                    text = text,
                    modifier = Modifier
                        .clickable {
                            showPopup = true
                        },
                )
                if (showPopup) {
                    Popup( // 锚点是父 Composable
                        onDismissRequest = { showPopup = false },
                        popupPositionProvider = object : PopupPositionProvider {
                            override fun calculatePosition(
                                anchorBounds: IntRect,
                                windowSize: IntSize,
                                layoutDirection: LayoutDirection,
                                popupContentSize: IntSize
                            ): IntOffset {
                                Log.d(
                                    "date310",
                                    "${anchorBounds} ${windowSize} ${popupContentSize}"
                                )
                                val x = 0
                                val y = anchorBounds.bottom
                                return IntOffset(x, y)
                            }
                        },
                        properties = PopupProperties(
                            focusable = true, // 允许获得焦点
                            clippingEnabled=false, // 允许超出屏幕
                            excludeFromSystemGesture=false, // 排除系统手势
                        ),
                    ) {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.sdp)
                                .verticalScroll(scrollState)
                                .background(Color.White)
                                .padding(4.sdp)
                        ) {
                            formatters.forEachIndexed { i, it ->
                                Text(
                                    text = "${i} ${it.first}",
                                    modifier = Modifier
                                        .clickable {
                                            formatter = it
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            Box() {
                Text(
                    text = formatter.first,
                    modifier = Modifier
                        .clickable {
                            showEnum = true
                        }
                        .border(1.sdp, Color.Green),
                )

                DropdownMenu(
                    expanded = showEnum,
                    onDismissRequest = { showEnum = false },
                    properties = PopupProperties(
                        focusable = true,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.sdp)
                        .border(1.sdp, Color.Cyan)
                ) {
                    formatters.forEachIndexed { i, it ->
                        DropdownMenuItem(
                            onClick = { formatter = it },
                        ) {
                            Text(
                                text = "${i} ${it.first}",
                                modifier = Modifier
                                    .clickable {
                                        formatter = it
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Date310PagePreview() {
    AndroidThreeTen.init(LocalContext.current)
    DesignPreview {
        Date310Page()
    }
}