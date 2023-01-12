package com.example.mockdemo

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifyPageReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context!!, "NotifyPageReceiver", Toast.LENGTH_SHORT).show()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val nid = intent!!.getIntExtra("Nid", 1)
        manager.cancel(nid)
        Toast.makeText(context!!, "NotifyPageReceiver close ${nid}", Toast.LENGTH_SHORT).show()
    }
}