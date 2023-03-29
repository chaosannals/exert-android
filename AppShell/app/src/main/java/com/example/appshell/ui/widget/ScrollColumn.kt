package com.example.appshell.ui.widget

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.appshell.LocalMainScrollSubject
import com.example.appshell.ui.dp2px
import com.example.appshell.ui.sdp
import com.example.appshell.ui.ssp
import java.util.*
import kotlin.math.abs

private val edgeMaxHeightDp = 200.sdp
private val edgeMaxHeightPx = 200.dp2px.toInt()
private val topShowHeightPx = 100.dp2px
private val animationSpec = tween<Float>(
    durationMillis = 244,
    easing = FastOutLinearInEasing,
)
private val topAnimationSpec = infiniteRepeatable(
    animation = tween<Dp>(
        durationMillis = 644,
        easing = FastOutLinearInEasing,
    )
)
private fun hinderScroll(it: Float, sa: Float, lv: Float) : Float {
    return (it * 0.4f * (edgeMaxHeightPx - abs(sa - lv)) / edgeMaxHeightPx)
}

interface ScrollColumnScope {
    val scrollY: Float
    val surplusHeight: Int
    fun toAnchor(tag: String): Float?

    @Stable
    fun Modifier.anchorTag(tag: String): Modifier
}
class ScrollColumnImpl(
    val tag: String
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return this@ScrollColumnImpl
    }
}

@Composable
fun ScrollColumn(
    modifier: Modifier = Modifier,
    pushUpTip: String? = null,
    onPullDownFinal: (() -> Unit)? = null,
    onPushUpFinal: (() -> Unit)? = null,
    onAfterAnchor: ((String) -> Unit)? = null,
    isControlFloating: Boolean = true,
    content: (@Composable ScrollColumnScope.() -> Unit)? = null,
) {
    val mss = LocalMainScrollSubject.current

    var contentHeight by remember {
        mutableStateOf(0)
    }
    var contentSurplusHeight by remember {
        mutableStateOf(0)
    }

    var scrollAll by remember {
        mutableStateOf(0f)
    }

    var pullPushLastAt: Long by remember {
        mutableStateOf(0L)
    }
    var pullPushAnimating by remember {
        mutableStateOf(false)
    }

    var afterAnchor: String? by remember {
        mutableStateOf(null)
    }
    val anchors = mutableMapOf<String, Float>()
    val scope = object: ScrollColumnScope {
        override val scrollY: Float
            get() = scrollAll
        override val surplusHeight: Int
            get() = contentSurplusHeight

        override fun toAnchor(tag: String): Float? {
            if (anchors.containsKey(tag)) {
                scrollAll = -anchors[tag]!!
                return -anchors[tag]!!
            }
            return null
        }

        override fun Modifier.anchorTag(tag: String): Modifier {
            return this.then(ScrollColumnImpl(tag))
        }
    }

    val scrollState = rememberScrollableState {
        val sv = when {
            !pullPushAnimating && scrollAll > 0f -> {
                if (pullPushLastAt == 0L) {
                    pullPushLastAt = Calendar.getInstance().timeInMillis
                }
                hinderScroll(it, scrollAll, 0f)
            }
            !pullPushAnimating && scrollAll < contentSurplusHeight -> {
                if (pullPushLastAt == 0L) {
                    pullPushLastAt = Calendar.getInstance().timeInMillis
                }
                hinderScroll(it, scrollAll, contentSurplusHeight.toFloat())
            }
            else -> it
        }
        scrollAll += sv
        sv
    }

    if (isControlFloating) {
        LaunchedEffect(scrollAll) {
            mss.onNext(abs(scrollAll) / (abs(contentSurplusHeight) + 1))
        }
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (!scrollState.isScrollInProgress && !pullPushAnimating) {
            val now = Calendar.getInstance().timeInMillis
            val duration = now - pullPushLastAt

            when {
                scrollAll > 0f -> {
                    onPullDownFinal?.let {
                        if (duration > 1000L && pullPushLastAt > 0L && scrollAll > topShowHeightPx) {
                            it()
                        }
                    }
                    pullPushAnimating = true
                }
                scrollAll < contentSurplusHeight -> {
                    onPushUpFinal?.let {
                        if (duration > 1000L && pullPushLastAt > 0L) {
                            it()
                        }
                    }
                    pullPushAnimating = true
                }
            }
        }
        pullPushLastAt = 0L
    }

    LaunchedEffect(pullPushAnimating) {
        if (pullPushAnimating) {
            val sy = when {
                scrollAll > 0f -> 0f - scrollAll
                scrollAll < contentSurplusHeight -> contentSurplusHeight.toFloat() - scrollAll
                else -> 0f
            }
            if (abs(sy) > 0.1f) {
                scrollState.animateScrollBy(sy, animationSpec)
            }
            pullPushAnimating = false
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .scrollable(
                state = scrollState,
                enabled = !pullPushAnimating,
                orientation = Orientation.Vertical,
            ),
    ) {
        Layout(
            modifier = Modifier
                .clip(RectangleShape),
            content = {
                if (abs(scrollAll) > topShowHeightPx) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .layoutId("top")
                            .fillMaxWidth()
                            .height(edgeMaxHeightDp),
                    ) {
                        val infiniteTransition = rememberInfiniteTransition()
                        val bottom by infiniteTransition.animateValue(
                            initialValue = 0.sdp,
                            targetValue = 24.sdp,
                            typeConverter = Dp.VectorConverter,
                            animationSpec = topAnimationSpec,
                        )
                        Image(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "图标",
                            modifier = Modifier
                                .padding(bottom = bottom)
                                .size(40.sdp),
                        )
                    }
                }
                content?.invoke(scope)
                pushUpTip?.let {
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .layoutId("bottom")
                            .fillMaxWidth()
                            .height(edgeMaxHeightDp),
                    ) {
                        Text(
                            text = it,
                            color = Color.Gray,
                            fontSize = 13.ssp,
                            modifier = androidx.compose.ui.Modifier.padding(4.sdp)
                        )
                    }
                }},
        ) { ms, cs ->
            val sai = scrollAll.toInt()
            val c = cs.copy(maxHeight = Constraints.Infinity)
            layout(cs.maxWidth, cs.maxHeight) {
                var heightSum = 0
                var tp :  Placeable? = null
                var bp :  Placeable? = null
                for (m in ms) {
                    val p = m.measure(c)
                    val x = (cs.maxWidth - p.width) / 2
                    val ppd = p.parentData
                    if (ppd is ScrollColumnImpl) {
                        anchors[ppd.tag] = heightSum.toFloat()
                    }
                    when (m.layoutId) {
                        "top" -> tp = p
                        "bottom" -> bp = p
                        else -> {
                            val y = heightSum + sai
                            heightSum += p.height
                            p.place(x, y)
                        }
                    }
                }

                onAfterAnchor?.let { after ->
                    var afterY = if (anchors.containsKey(afterAnchor)) {
                        anchors[afterAnchor]
                    } else {
                        null
                    }

                    for((t, y) in anchors) {
                        if (afterAnchor == null) {
                            afterAnchor = t
                            afterY = y
                            after(t)
                        } else if (afterAnchor != t) {
                            val ad = afterY!! + scrollAll
                            val d = y + scrollAll
                            if (d <= 1 && (ad > 0 || d >= ad)) {
                                afterAnchor = t
                                afterY = y
                                after(t)
                            }
                        }
                    }
                }

                contentHeight = heightSum
                contentSurplusHeight = if (cs.maxHeight > contentHeight) 0 else cs.maxHeight - contentHeight
                tp?.run {
                    place(0, sai - edgeMaxHeightPx)
                }
                bp?.run {
                    place(0, sai + contentHeight)
                }
            }
        }
    }
}

@Preview
@Composable
fun ScrollColumnPreview() {
    val context = LocalContext.current
    var boxCount by remember {
        mutableStateOf(0)
    }

    DesignPreview() {

        ScrollColumn(
            pushUpTip = "上拉加载更多",
        ) {
            Text(
                text = "添加一些内容",
                modifier = Modifier
                    .border(1.dp, Color.Cyan)
                    .padding(10.dp)
                    .clickable {
                        if (boxCount < 10) {
                            Toast
                                .makeText(
                                    context,
                                    "添加了",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            ++boxCount
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "不能再添加了",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
            )
            for (i in 0..boxCount) {
                Box(
                    modifier = Modifier
                        .background(Color.Green)
                        .border(1.dp, Color.Cyan)
                        .size((i * 10).dp, (i * 100).dp),
                ) {
                    Text(text = i.toString())
                }
            }
            Text(
                text = "减少一些内容",
                modifier = Modifier
                    .border(1.dp, Color.Cyan)
                    .padding(10.dp)
                    .clickable {
                        if (boxCount > 0) {
                            Toast
                                .makeText(
                                    context,
                                    "减少了",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            --boxCount
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "不能再减少了",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
            )
        }
    }
}