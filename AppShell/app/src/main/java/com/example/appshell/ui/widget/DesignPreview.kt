package com.example.appshell.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.appshell.*
import com.example.appshell.ui.*

@Composable
fun DesignPreview(
    modifier: Modifier = Modifier,
    isLimit: Boolean = true,
    content: @Composable () -> Unit,
) {
    val m = if (isLimit) {
        modifier
            .sizeIn(
                minWidth = 0.dp,
                maxWidth = 375.sdp,
                minHeight = 0.dp,
                maxHeight = 667.sdp,
            )
    } else {
        modifier.width(375.sdp)
    }

    CompositionLocalProvider(
        LocalTotalStatus provides rememberTotalStatus(),
        LocalRouteStatus provides rememberRouteStatus(),
        LocalFormContext provides rememberFormContext(),
        LocalX5ScaffoldStatus provides rememberX5ScaffoldStatus(),
        LocalTipQueue provides rememberTipQueue(),
        LocalLoadingPaneSubject provides rememberLoadingPaneSubject(),
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier=m,
        ){
            content()
        }
    }
}