package com.example.appshell

import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appshell.db.AppDatabase
import com.example.appshell.db.rememberAppDatabase
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

val LocalTotalStatus = staticCompositionLocalOf<TotalStatus> {
    error("no total status !!")
}

// by remember 记录 @Parcelize 都会在编译期导致编译错误
// 采用 remember 阶段重组的方式解决
@Parcelize
data class TotalStatus(
    val token: String,

) : Parcelable {
    // 此种方式要好过整体 rx 传递
    @IgnoredOnParcel
    val scrollOffset: PublishSubject<Float> = PublishSubject.create()

    @IgnoredOnParcel
    val exceptionQueue: PublishSubject<Throwable> = PublishSubject.create()

    @IgnoredOnParcel
    var navController: NavHostController? = null

    @IgnoredOnParcel
    var appDatabase: AppDatabase? = null

    @IgnoredOnParcel
    var coroutineScope: CoroutineScope? = null

    val router: NavHostController get() = navController!!
    val database: AppDatabase get() = appDatabase!!
    val scope: CoroutineScope get() = coroutineScope!!

    fun routeTo(path: String, isClear: Boolean=false) {
        try {
            if (isClear) {
                router.backQueue.clear()
            }
            router.navigate(path)
        } catch (t: Throwable) {
            exceptionQueue.onNext(t)
        }
    }
}

val TotalStatusSaver = run {
    val tokenKey = "token"
    mapSaver(
        save =
        {
            mapOf(
                tokenKey to it.token,
            )
        },
        restore =
        {
            TotalStatus(it[tokenKey] as String)
        }
    )
}

@Composable
fun rememberTotalStatus() : TotalStatus {
    val navController = rememberNavController()
    val appDatabase = rememberAppDatabase()
    val coroutineScope = rememberCoroutineScope()

    // 使用 @Parcelize
    val result by rememberSaveable() {
        val r = TotalStatus("test")
        mutableStateOf(r)
    }

    // 替代 @Parcelize 的方案。
//    var result by rememberSaveable(stateSaver = TotalStatusSaver) {
//        mutableStateOf(TotalStatus("test"))
//    }
//    return result

    // 因为 TotalStatus 要保留状态 @Parcelize 或者 Saver
    // 只是简单粗暴地重组，相当于建了个变量合并存。
    // 以下的字段都是公开的，因为需要重组赋值。
    // 会被 LocalTotalStatus.current 取
    result.navController = navController
    result.appDatabase = appDatabase
    result.coroutineScope = coroutineScope

    return result
}