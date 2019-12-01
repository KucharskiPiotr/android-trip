package com.agh.trip.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadcastHandler : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val name = intent?.getStringExtra("name")
        Toast.makeText(context, "Payment of $name added.", Toast.LENGTH_SHORT).show()
    }
}