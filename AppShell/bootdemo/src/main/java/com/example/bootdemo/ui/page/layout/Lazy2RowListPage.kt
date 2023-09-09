package com.example.bootdemo.ui.page.layout

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bootdemo.ui.px2dp
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

private val animateSpec = spring(
    dampingRatio = Spring.DampingRatioHighBouncy,
    stiffness = Spring.StiffnessMedium,
    visibilityThreshold = 1f,
)

enum class ScrollLazyColumnFling {
    Top,
    Bottom,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollLazyColumn2(
    status: LazyGridState = rememberLazyGridState(),
    columnCount: Int = 2,
    flingMs: Int = 600,
    spaceMaxHeight: Dp = 200.dp,
    onFling: (ScrollLazyColumnFling) -> Unit = {},
    content: LazyGridScope.() -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val spaceMaxHeightPx = with(density) { spaceMaxHeight.roundToPx().toFloat() }
    val coroutineScope = rememberCoroutineScope()

    val topDragChange = remember {
        PublishSubject.create<Boolean>()
    }
    var isTopDragging by remember {
        mutableStateOf(false)
    }
    var topLastDraggingAt by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    val bottomDragChange = remember {
        PublishSubject.create<Boolean>()
    }
    var isBottomDragging by remember {
        mutableStateOf(false)
    }
    var bottomLastDraggingAt by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    val topPx = remember {
        Animatable(
            initialValue = 0f,
            visibilityThreshold = 1f,
        )
    }
    val bottomPx = remember {
        Animatable(
            initialValue = 0f,
            visibilityThreshold = 1f,
        )
    }
    val index by remember {
        derivedStateOf {
            status.firstVisibleItemIndex
        }
    }

    DisposableEffect(topDragChange) {
        val dragRxDisposable = topDragChange.subscribe {
            val now = System.currentTimeMillis()
            val diff = now - topLastDraggingAt
            Log.d("ScrollLazyColumn2", "isDragging: $it")
            if (diff > flingMs && isTopDragging && !it && abs(topPx.value) >= spaceMaxHeightPx) {
                Toast.makeText(context, "Top Loose: $it", Toast.LENGTH_SHORT).show()
                onFling(ScrollLazyColumnFling.Top)
            }
            topLastDraggingAt = now
            if (isTopDragging != it) {
                isTopDragging = it
            }
        }
        onDispose {
            dragRxDisposable.dispose()
        }
    }

    DisposableEffect(bottomDragChange) {
        val dragRxDisposable = bottomDragChange.subscribe {
            val now = System.currentTimeMillis()
            val diff = now - bottomLastDraggingAt
            Log.d("ScrollLazyColumn2", "isDragging: $it")
            if (diff > flingMs && isBottomDragging && !it && abs(bottomPx.value) >= spaceMaxHeightPx) {
                Toast.makeText(context, "Bottom Loose: $it", Toast.LENGTH_SHORT).show()
                onFling(ScrollLazyColumnFling.Bottom)
            }
            bottomLastDraggingAt = now
            if (isBottomDragging != it) {
                isBottomDragging = it
            }
        }
        onDispose {
            dragRxDisposable.dispose()
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val endIndex = status.layoutInfo.totalItemsCount - index - status.layoutInfo.visibleItemsInfo.size
                val topOffset = if (index == 0) {
                    if (!isTopDragging) {
                        topDragChange.onNext(true)
                    }
                    topPx.value + delta
                } else {
                    if (isTopDragging) {
                        topDragChange.onNext(false)
                    }
                    0f
                }
                val bottomOffset = if (endIndex == 0) {
                    if (!isBottomDragging) {
                        bottomDragChange.onNext(true)
                    }
                    val veOffset = status.layoutInfo.viewportEndOffset
                    val ei = status.layoutInfo.visibleItemsInfo.last()
                    val eiOffset = ei.offset
                    val eiHeight = ei.size

                    // 每行不止一个元素，行高需要遍历该行所有元素
                    var eirBottom = ei.offset.y + ei.size.height
                    val eirOthers = status.layoutInfo.visibleItemsInfo.subList(
                        max(0, status.layoutInfo.visibleItemsInfo.size - columnCount),
                        status.layoutInfo.visibleItemsInfo.size - 1
                    )
                    eirOthers.forEach {
                        if (it.row == ei.row) {
                            val jBottom = it.offset.y + it.size.height
                            if (jBottom > eirBottom) {
                                eirBottom = jBottom
                            }
                        }
                    }
                    Log.d("ScrollLazyColumn2", "veo: $veOffset eio: $eiOffset eih: $eiHeight eirb: $eirBottom")
                    bottomPx.value + delta
                } else {
                    if (isBottomDragging) {
                        bottomDragChange.onNext(false)
                    }
                    0f
                }

                coroutineScope.launch {
                    topPx.snapTo(topOffset.coerceIn(0f, spaceMaxHeightPx))
                    bottomPx.snapTo(bottomOffset.coerceIn(-spaceMaxHeightPx, 0f))
                }
//                Log.d("lazy-nested-pre-column", "delta: ${delta} no: ${topOffset} top: {$topPx} bottom: {$bottomPx}")
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
//                val r = super.onPostScroll(consumed, available, source)
//                Log.d("lazy-nested-pre-column", "post r: ${r}")
                return Offset.Zero
            }

            // 结束开始
            override suspend fun onPreFling(available: Velocity): Velocity {
//                Log.d("lazy-nested-pre-column", "pre fling")
                // pre fling 和 post fling 之间可能会有 post pre scroll
                return super.onPreFling(available)
            }

            // 结束
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
//                Log.d("lazy-nested-pre-column", "post fling")
                if (isTopDragging) {
                    topDragChange.onNext(false)
                }
                if (isBottomDragging) {
                    bottomDragChange.onNext(false)
                }
                Log.d("ScrollLazyColumn2", "post fling: ${topPx.value}, ${bottomPx.value}")
                topPx.animateTo(0f, animateSpec)
                bottomPx.animateTo(0f, animateSpec)
                return super.onPostFling(consumed, available)
            }
        }
    }

    CompositionLocalProvider(
        // https://developer.android.com/about/versions/12/behavior-changes-all?hl=zh-cn#overscroll
        LocalOverscrollConfiguration provides null, // 关闭 Overscroll Effect （拉伸滚动效果），安卓 12 （SDK 31 以上）嵌套滚动错误。
    ) {
        val padding by remember(topPx, bottomPx) {
            derivedStateOf {
                PaddingValues(
                    top = abs(topPx.value).px2dp,
                    bottom = abs(bottomPx.value).px2dp,
                )
            }
        }
        Box(
            modifier = Modifier
                .nestedScroll(nestedScrollConnection)
        ) {
            LazyVerticalGrid(
                state = status,
                contentPadding = padding,
                columns = GridCells.Fixed(columnCount),
                content = content,
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .background(Color.Gray),
            )
        }
    }
}

@Composable
fun Lazy2RowListPage() {
    val boxes = remember {
        mutableStateListOf(*(0.. 100).toList().toTypedArray())
    }
    val state = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    ScrollLazyColumn2 {
        itemsIndexed(boxes) { i, box ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1.0f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1.0f)
                        .background(Color.White)
                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                        .clickable {
                            coroutineScope.launch {
                                state.scrollToItem(i, 0)
                            }
                        }
                ) {
                    Text(
                        text = "$box",
                        fontSize = 44.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Lazy2RowListPagePreview() {
    Lazy2RowListPage()
}