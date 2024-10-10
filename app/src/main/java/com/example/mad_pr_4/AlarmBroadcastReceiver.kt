package com.example.mad_pr_4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmBroadcastReceiver", "onReceive: Received event")
        if(intent != null && context!=null){
            val str1 = intent.getStringExtra("Service1")
            if(str1 == null){}
            else if(str1 == "Start" || str1 == "Stop"){
                val intentService = Intent(context, AlarmService::class.java)
                intentService.putExtra("Service1", intent.getStringExtra("Service1"))
                if(str1 == "Start"){
                    context.startService(intentService)
                }
                else if(str1=="Stop"){
                    context.stopService(intentService)
                }
            }
        }
    }
}
