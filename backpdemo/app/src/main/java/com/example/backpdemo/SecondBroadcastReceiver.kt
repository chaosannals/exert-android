package com.example.backpdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SecondBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val uri = intent?.toUri(Intent.URI_INTENT_SCHEME)
        Toast.makeText(context, "second action: ${action} uri: ${uri}", Toast.LENGTH_LONG).show()
    }
}