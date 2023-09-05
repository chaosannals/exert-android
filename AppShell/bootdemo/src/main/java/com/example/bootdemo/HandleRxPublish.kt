package com.example.bootdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.reactivex.rxjava3.subjects.PublishSubject

@Composable
fun <T> HandleRxPublish(subject: PublishSubject<T>, action: (T) -> Unit) {
    DisposableEffect(subject) {
        val d = subject.subscribe(action)
        onDispose {
            d.dispose()
        }
    }
}