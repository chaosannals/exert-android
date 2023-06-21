package com.example.appimop.ui.page

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.ui.DesignPreview
import io.reactivex.rxjava3.subjects.BehaviorSubject

private val count: BehaviorSubject<Int> = BehaviorSubject.create()

@Composable
fun ReentryPage() {
    val c by count.subscribeAsState(initial = 0)

    LaunchedEffect(Unit) {
        count.onNext(c + 1)
        Log.d("reentry", "count launch: $c")
    }
}

@Preview
@Composable
fun ReentryPagePreview() {
    DesignPreview {
        ReentryPage()
    }
}