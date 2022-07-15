package com.example.jcmdemo.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.routeTo

enum class GistItem(var route: String, var icon: Int) {
    Home("home", R.drawable.ic_home),
    Conf("conf", R.drawable.ic_conf),
    Camera("camera", R.drawable.ic_camera),
//    Conf1("conf", R.drawable.ic_conf),
//    Conf2("conf", R.drawable.ic_conf),
//    Conf3("conf", R.drawable.ic_conf),
}

@Composable
fun GistPage (navController: NavController) {
    val items = GistItem.values()
    val columnCount = 3

    Layout(
        // 修改器会影响到子空间的测量，使得子修改器失效
        // modifier = Modifier.fillMaxSize(),
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
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed{ index, placeable ->
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
    val navController = rememberNavController()
    return GistPage(navController)
}