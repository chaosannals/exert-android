package com.example.appshell

import android.util.Log
import com.example.appshell.ui.toHex

data class MimeInfo (
    val head: ByteArray,
    val type: String,
    val suffix: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MimeInfo

        if (!head.contentEquals(other.head)) return false
        if (type != other.type) return false
        if (suffix != other.suffix) return false

        return true
    }

    override fun hashCode(): Int {
        var result = head.contentHashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + suffix.hashCode()
        return result
    }

    fun matchHead(two: ByteArray) : Boolean {
        if (two.size < head.size) return false
        Log.d("file-page", "compare ${head.toHex()} ${two.copyOfRange(0, head.size).toHex()}")
        for (i in 0 until head.size) {
            if (head[i] != two[i]) {
                return false
            }
        }
        return true
    }
}

val ByteArray.guessSuffix: String? get() = run {
    Log.d("file-page", "guessSuffix")
    for (mime in MIME_INFO) {
        if (mime.matchHead(this)) {
            return mime.suffix
        }
    }
    null
}

val MIME_INFO = listOf(
    MimeInfo (
        byteArrayOf(0xff.toByte(), 0xd8.toByte()),
        "image/jpeg",
        "jpg"
    ),
    MimeInfo (
        byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4e.toByte(), 0x47.toByte(), 0x0D.toByte(), 0x0A.toByte(), 0x1A.toByte(), 0x0A.toByte()),
        "image/png",
        "png",
    ),
    MimeInfo (
        byteArrayOf(0x47.toByte(), 0x49.toByte(), 0x46.toByte(), 0x38.toByte()),
        "image/gif",
        "gif",
    )
)
