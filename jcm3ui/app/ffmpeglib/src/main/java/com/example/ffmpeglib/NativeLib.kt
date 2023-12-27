package com.example.ffmpeglib

class NativeLib {

    /**
     * A native method that is implemented by the 'ffmpeglib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'ffmpeglib' library on application startup.
        init {
            System.loadLibrary("ffmpeglib")
        }
    }
}