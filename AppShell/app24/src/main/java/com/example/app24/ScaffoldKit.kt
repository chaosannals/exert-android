package com.example.app24

import io.reactivex.rxjava3.subjects.BehaviorSubject

object ScaffoldKit {
    val isShowWebView: BehaviorSubject<Boolean> = BehaviorSubject.create()
}