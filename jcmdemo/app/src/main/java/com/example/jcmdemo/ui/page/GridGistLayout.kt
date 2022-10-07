package com.example.jcmdemo.ui.page

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.routeTo
import kotlin.math.floor

@Composable
fun GridGistLayout() {

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        GistItem.values().forEachIndexed { i, item ->
            IconButton(
                onClick = {},
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val x = (i % 3) * (constraints.maxWidth / 3)
                        val y = floor(i / 3f).toInt() * (placeable.height + 30)
                        Log.i("tttt layout", "layout: ${i} ${x} ${y}")
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.placeRelative(x = x, y = y)
                        }
                    }
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
    }
}

@Preview
@Composable
fun GridGistLayoutPreview() {
    GridGistLayout()
}