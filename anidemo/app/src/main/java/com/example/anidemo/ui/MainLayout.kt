package com.example.anidemo.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.LocalMainScrollerPercentage
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@ExperimentalMaterial3Api
@Composable
fun MainLayout() {
    val context = LocalContext.current
    var sd by remember {
        mutableStateOf(0f)
    }

    val sp by remember {
        mutableStateOf(ScrollPercentage(0.0f){})
    }

    Observable.create {
        sp.onChange = { f ->
            it.onNext(f)
        }
        it.setCancellable {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
    }//.subscribeOn(AndroidSchedulers.mainThread())
        .throttleLast(400, TimeUnit.MILLISECONDS)
        .subscribe { sd = it }

    CompositionLocalProvider(
        LocalMainScrollerPercentage provides sp,
    ) {
        Scaffold(
            topBar = {},
            bottomBar = {
                MainBottomBar()
            },
            floatingActionButton={
                if (sd < 0.99) {
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = { },
                    ) {

                    }
                }
            },

            modifier = Modifier.fillMaxSize()
        ) {
            Row() {
                //Text(text = scroller.maxValue.toString())
                Text(text = sd.toString())
            }
            NavGraphRoutes(paddingValues = it)
        }
    }
}

@ExperimentalMaterial3Api
@Preview()
@Composable
fun MainLayoutPreview() {
    MainLayout()
}