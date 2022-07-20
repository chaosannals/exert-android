package com.example.jcmdemo.ui.page.tool

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.jcmdemo.R

class InfoItem(var title: String,var info: String) {

}

@Composable
fun ListingItem() {
    val shape = RoundedCornerShape(10.dp)
    Column(
        modifier = Modifier
            .aspectRatio(1.0f)
            .padding(10.dp)
            .clip(shape)
            .border(
                BorderStroke(1.dp, colorResource(id = R.color.gray_2)),
                shape
            )
            .background(colorResource(id = R.color.white)),
    ) {
//        var context = LocalContext.current
//        var fff = suspend {
//            var a : ImageRequest = ImageRequest.Builder(context)
//                .data("https://pic2.zhimg.com/80/v2-f6b1f64a098b891b4ea1e3104b5b71f6_720w.png")
//                .size(Size.ORIGINAL) // Set the target size to load the image at.
//                .build()
//
//            val imageLoader = ImageLoader(context)
//            var d = imageLoader.execute(a).drawable
//
//        }
        Text(text = "标题")
        var infos = listOf(
            InfoItem("信息1：", "123213"),
            InfoItem("信息2：", "1"),
            InfoItem("信息3：", "3"),
            InfoItem("信息4：", "sdafsd"),
            InfoItem("信息5：", "34"),
        )
        infos.forEach {
            Row(
                //horizontalArrangement = Arrangement.SpaceBetween
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp, 1.dp, 10.dp, 1.dp)
            ) {
                Text(it.title, modifier = Modifier.weight(1.0f), fontSize = 10.sp)
                Text(it.info, modifier = Modifier.weight(1.0f), fontSize = 10.sp)
            }
        }


        var painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://pic2.zhimg.com/80/v2-f6b1f64a098b891b4ea1e3104b5b71f6_720w.png")
                .size(Size.ORIGINAL) // Set the target size to load the image at.
                .build()
        )
        if (painter.state is AsyncImagePainter.State.Success) {
            Image(painter = painter, contentDescription = "图片")
        }
        else {
            Icon(painterResource(id = R.drawable.ic_gist), contentDescription="加载失败")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ListingPage() {
    var messages = listOf("123", "23432")
    Column() {}
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
    ) {
        items(89) {
            ListingItem()
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun ListingPagePreview() {
    ListingPage()
}