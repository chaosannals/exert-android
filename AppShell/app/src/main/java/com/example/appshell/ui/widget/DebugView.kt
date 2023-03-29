package com.example.appshell.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalNavController
import com.example.appshell.ui.dp2px
import com.example.appshell.ui.shadow2
import com.example.appshell.ui.sdp



@Composable
fun DebugView(
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    val shape = RoundedCornerShape(5.sdp)
    Column (
         modifier = modifier
             .fillMaxWidth()
             .height(400.sdp)
             .shadow2(
                 color = Color(0x1A000000),
                 alpha = 0.4f,
                 cornersRadius = 5.sdp,
                 shadowBlurRadius = 4.sdp,
                 offsetY = (-1).sdp,
             )
             .background(Color.White, shape = shape),
    ) {
         Row (
             horizontalArrangement=Arrangement.SpaceBetween,
             modifier = Modifier
                 .fillMaxWidth()
                 .height(34.sdp)
                 .drawBehind {
                     drawLine(
                         color = Color.Gray,
                         start = Offset(0f, size.height),
                         end = Offset(size.width, size.height),
                         strokeWidth = 0.4.dp2px,
                     )
                 }
                 .padding(4.sdp),
         ) {
             Icon(
                 imageVector = Icons.Default.Home,
                 contentDescription = "setting",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable {

                         navController.navigate("home-page")
                     }
                 ,
             )
             Icon(
                 imageVector = Icons.Default.Build,
                 contentDescription = "setting",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable {
                         
                         navController.navigate("tbs-page")
                     }
                 ,
             )
             Icon(
                 imageVector = Icons.Default.Settings,
                 contentDescription = "setting",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable {
                         navController.navigate("conf-page")
                     }
                 ,
             )
         }
    }
}

@Preview
@Composable
fun DebugViewPreview() {
    DesignPreview (
        modifier = Modifier
            .background(Color.White)
            .padding(10.sdp)
    ) {
        DebugView()
    }
}