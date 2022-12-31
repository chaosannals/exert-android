package com.example.jcmdemo.ui.page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcmdemo.LocalNavController
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.routeTo

@Composable
fun GistPage () {
    val items = GistItem.values()
    val columnCount = 3

    val navController = LocalNavController.current
    val scrollState = rememberScrollState()

    Layout(
        // 修改器会影响到子空间的测量，使得子修改器失效
        // modifier = Modifier.fillMaxSize(),
        //modifier = Modifier.background(colorResource(id = R.color.gray)),
        modifier = Modifier.verticalScroll(scrollState),
        content = {
            items.forEach{ item ->
                IconButton(
                    onClick = {
                        navController.routeTo(item.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .aspectRatio(1.0f)
                        .border(BorderStroke(1.dp, colorResource(id = R.color.black)))
                        .background(colorResource(id = R.color.light_sky_blue))
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.route
                    )
                }
            }
        },
    ) { ms, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {
            val ps = ms.map { it.measure(constraints) }
            ps.forEachIndexed{ index, placeable ->
                val i = index % columnCount
                val j = index.floorDiv(columnCount)
                val span = (constraints.maxWidth - placeable.width * columnCount).floorDiv(columnCount + 1)
                val x = span + i * (placeable.width + span)
                val y = span + j * (placeable.height + span)
                placeable.placeRelative(x = x, y = y)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GistPagePreview () {
    DesignPreview() {
        GistPage()
    }
}