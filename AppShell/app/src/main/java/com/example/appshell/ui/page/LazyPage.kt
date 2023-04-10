package com.example.appshell.ui.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerDefaults.flingBehavior
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.sdp
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.ScrollColumn


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyPage() {
    val infos = remember {
        val v = mutableStateListOf<String>(
            "aaaa",
            "bbbb"
        )
        for(i in 1..1000) {
            v.add("text- ${i}")
        }
        v
    }
    val lazyState = rememberLazyListState()

    // 无效
//    val pagerState = rememberPagerState()

    // 不能加，LazyColumn 就用了这个。冲突。
//    val scrollState = rememberScrollState()

    // rememberScrollableState 无效
//    var scrollY by remember {
//        mutableStateOf(0f)
//    }

    LaunchedEffect(Unit) {
        for(i in 1..10000) {
            infos.add("text- ${i}")
        }
        Log.d("lazypage", "count: ${infos.size}")
    }

    Column(
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Text(
//                text = "Y: ${scrollY}",
//                text= "Y: ${scrollState.value}",
//                text="Y: ${pagerState.currentPage}",
                text="顶部最接近的索引: ${lazyState.firstVisibleItemIndex}",
                color = Color.White,
                fontSize = 24.ssp,
            )
            Text(
                text="顶部重复利用的 Y : ${lazyState.firstVisibleItemScrollOffset}",
                color = Color.White,
                fontSize = 24.ssp,
            )
            Text(
                text="可前滚 : ${lazyState.canScrollForward}",
                color = Color.White,
                fontSize = 24.ssp,
            )
            Text(
                text="可后滚 : ${lazyState.canScrollBackward}",
                color = Color.White,
                fontSize = 24.ssp,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F8F8)),
            ) {
                /*val scrollScope = rememberScrollableState() {
                    scrollY += it
                    it
                }*/

                LazyColumn(
                    state = lazyState,
                    // padding 有显示效果，但是 lazyState firstVisibleItemIndex firstVisibleItemScrollOffset 却不会没有因此变化
//                    contentPadding = PaddingValues(top= 40.sdp, bottom = 40.sdp),
                    modifier = Modifier
//                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                    // 无效
//                        .scrollable(
//                            state = scrollScope,
//                            orientation = Orientation.Vertical,
//                        ),
                ) {
                    itemsIndexed(infos) { i, info ->
                        Text(
                            text = info,
                            fontSize = 24.ssp,
                        )
                    }
                    item {
                        Text(
                            text ="底部",
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LazyPagePreview(){
    DesignPreview {
        LazyPage()
    }
}