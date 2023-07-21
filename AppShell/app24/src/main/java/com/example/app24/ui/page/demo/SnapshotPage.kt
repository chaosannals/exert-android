package com.example.app24.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
/// 快照是底层实现
/// 好像不能在 compose 层使用，会扰乱底层。
@Composable
fun SnapshotPage() {
    var n by remember {
        mutableStateOf(1)
    }

    val ss = remember {
        Snapshot.takeSnapshot()
    }
    val sms = remember {
        Snapshot.takeMutableSnapshot()
    }

    var ssn by remember(ss) {
        mutableStateOf(ss.enter { n })
    }
    var smsn by remember {
        mutableStateOf(sms.enter { n })
    }

    Column() {
        Button(
            onClick =
            {
                n++
            },
        ) {
            Text("n: $n")
        }
        Button(
            onClick =
            {
                ss.enter {
                    ssn = n
                }
            },
        ) {
            Text("ssn: $ssn")
        }
        Button(
            onClick =
            {
                sms.enter {
                    n += 4
                }
                sms.apply()
            }
        ) {
            Text("smsn: $smsn")
        }
    }
}

@Preview
@Composable
fun SnapshotPagePreview() {
    DesignPreview {
        SnapshotPage()
    }
}