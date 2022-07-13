package com.example.jcmdemo.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcmdemo.R
import kotlin.math.floor

sealed class GistItem(var route: String, var icon: Int) {

}

@Composable
fun GistPage () {

    Layout(
        modifier = Modifier.fillMaxSize(),
        content = @Composable() {
            IconButton(
                onClick = { /*TODO*/ },
                //modifier = Modifier.width(10.dp)//.size(10.dp, 10.dp)//.fillMaxWidth(0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_gist),
                    contentDescription = "gist"
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
                //modifier = Modifier.width(10.dp)//.size(10.dp, 10.dp)//.fillMaxWidth(0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_gist),
                    contentDescription = "gist"
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
                //modifier = Modifier.width(10.dp)//.size(10.dp, 10.dp)//.fillMaxWidth(0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_gist),
                    contentDescription = "gist"
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
                //modifier = Modifier.width(10.dp)//.size(10.dp, 10.dp)//.fillMaxWidth(0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_gist),
                    contentDescription = "gist"
                )
            }
        },
    ) { measurables, constraints ->
        var placeables = measurables.map { measurable -> 
            measurable.measure(constraints)
        }

        var ps = placeables.size
        var iw = constraints.maxWidth / 3.0
        var ph = (floor(ps / 3.0).toInt() * iw).toInt()

        layout(constraints.maxWidth, ph) {
            placeables.forEachIndexed{ index, placeable ->
                var x = ((index % 3) * constraints.maxWidth / 3).toInt()
                var y = floor(index / 3.0).toInt() * placeable.width
                placeable.placeRelative(x = x, y = y)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GistPagePreview () {
    return GistPage()
}