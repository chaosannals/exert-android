package com.example.jcm3demo.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jcm3demo.LocalNavController
import com.example.jcm3demo.R
import com.example.jcm3demo.ui.routeTo

@Composable
fun ToolPage() {
    val navController = LocalNavController.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            //.padding(0.dp, 0.dp, 0.dp, 60.dp)
            .navigationBarsPadding(),
    ) {
        items(ToolItem.values()) {
            val shape = RoundedCornerShape(8)
            IconButton(
                onClick = { navController.routeTo(it.route) },
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .padding(10.dp)
                    .clip(shape)
                    .background(colorResource(id = R.color.light_sky_blue)),
            ) {
                Icon(
                    painter = painterResource(id = it.iconRid),
                    contentDescription = it.title,
                    modifier = Modifier.size(40.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToolPagePreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        ToolPage()
    }
}