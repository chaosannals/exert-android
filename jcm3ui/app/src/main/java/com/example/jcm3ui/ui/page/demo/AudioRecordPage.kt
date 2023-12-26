package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID

@Composable
fun AudioRecordPage() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.RequestPermission()
    ) {

    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }


    val audioMp4Path = remember() {
        "${context.externalCacheDir}/${UUID.randomUUID()}.m4a"
    }
    val audioUri = remember(audioMp4Path) {
        Uri.parse(audioMp4Path)
    }
    var audioSize by remember {
        mutableLongStateOf(0L)
    }
    var audioDuration by remember {
        mutableLongStateOf(0L)
    }

    var recorder: MediaRecorder? by remember(context) {
        mutableStateOf(null)
    }
    val startRecord by rememberUpdatedState {
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS)
//            } else {
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            }
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioMp4Path)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e("record", "prepar() failed: ${e.message}")
            }
            start()
        }
    }
    val stopRecord by rememberUpdatedState {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        audioSize = File(audioMp4Path).length()
        audioDuration = MediaMetadataRetriever().run {
            setDataSource(context, Uri.parse(audioMp4Path))
            extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()?: 0
        }
    }

    var player:MediaPlayer? by remember(context) {
        mutableStateOf(null)
    }
    val startPlay by rememberUpdatedState {
        player = MediaPlayer().apply {
            try {
                setDataSource(audioMp4Path)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("play", "prepare() failed: ${e.message}")
            }
        }
    }
    val stopPlay by rememberUpdatedState {
        player?.release()
        player = null
    }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
    ) {
        Text("path: $audioMp4Path")
        Text("uri: $audioUri")
        Text("size: $audioSize")
        Text("duration: $audioDuration")

        Button(
            onClick = {
                if (recorder != null) {
                    stopRecord()
                } else {
                    startRecord()
                }
            }
        ) {
            Text("录音")
        }

        Button(
            onClick = {
                if (player != null) {
                    stopPlay()
                } else {
                    startPlay()
                }
            }
        ) {
            Text("播放")
        }
    }
}