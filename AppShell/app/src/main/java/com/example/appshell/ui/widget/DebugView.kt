package com.example.appshell.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.*
import com.example.appshell.ui.widget.form.Form
import com.example.appshell.ui.widget.form.FormTextInput


@Composable
fun DebugView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val totalStatus = LocalTotalStatus.current
    val scaffoldStatus = LocalX5ScaffoldStatus.current

    val subject = LocalX5ScaffoldRxSubject.current

    var isLoading by remember {
        mutableStateOf(false)
    }

    DisposableEffect(subject) {
        val s = subject.subscribe {
            isLoading = it.isShowLoadingPane
        }
        onDispose {
            s.dispose()
        }
    }

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
                     .clickable
                     {
//                         navController.navigate("home-page")
                         totalStatus.router.routeTop("home-page")
                     }
                 ,
             )
             Icon(
                 imageVector = Icons.Default.Build,
                 contentDescription = "setting",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable
                     {
//                         navController.navigate("tbs-page")
                         totalStatus.router.routeTop("tbs-page")
                     }
                 ,
             )
             Icon(
                 imageVector = Icons.Default.Settings,
                 contentDescription = "setting",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable
                     {
//                         navController.navigate("conf-page")
                         totalStatus.router.routeTop("conf-page")
                     }
                 ,
             )
             Icon(
                 imageVector = Icons.Default.Menu,
                 contentDescription = "toggle",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable {
                         //scaffoldStatus.isShowNavbar = !scaffoldStatus.isShowNavbar
                     }
             )

             Icon(
                 imageVector = if (isLoading) Icons.Default.Lock else Icons.Default.Done,
                 contentDescription = "loading",
                 modifier = Modifier
                     .aspectRatio(1f)
                     .fillMaxSize(0.84f)
                     .clickable {
                         subject.onNext(
                             X5ScaffoldStatus(
                                 isShowLoadingPane = true
                             )
                         )
                     }
             )
         }

        val otherConf by remember {
            val conf = context.LoadOtherConf()
            mutableStateOf(conf)
        }

        Form() {
            FormTextInput(
                title = "字符串1",
                value = otherConf.stringValue1,
                onValueChange =
                {
                    otherConf.stringValue1 = it!!
                    context.SaveOtherConf(otherConf)
                },
                isRequired = true,
                isNullable = false,
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