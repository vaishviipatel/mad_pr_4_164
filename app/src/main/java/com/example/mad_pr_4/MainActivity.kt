package com.example.mad_pr_4

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.view.View
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Date
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val createAlarmBtn = findViewById<Button>(R.id.createAlarmBtn)
        createAlarmBtn.setOnClickListener {
            showTimerDialog()
        }
        val cardView =
            findViewById<com.google.android.material.card.MaterialCardView>(R.id.materialCardView2)
        cardView.visibility = View.GONE
        findViewById<Button>(R.id.cancelAlarm).setOnClickListener {
            setAlarm(0, "Stop")
            cardView.visibility = View.GONE
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun showTimerDialog()
    {
        val cldr: Calendar = Calendar.getInstance()
        val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = cldr.get(Calendar.MINUTE)
        val picker=TimePickerDialog(
            this,
            { tp,sHour,sMinute -> sendDialogDataToActivity(sHour,sMinute)
            },hour,minutes,false
        )
        picker.show()
    }
    @RequiresApi(Build.VERSION_CODES.S)
    private fun sendDialogDataToActivity(hour:Int, minute:Int){
        val alarmCalender = Calendar.getInstance()
        val year: Int = alarmCalender.get(Calendar.YEAR)
        val month: Int = alarmCalender.get(Calendar.MONTH)
        val day: Int = alarmCalender.get(Calendar.DATE)
        alarmCalender.set(year, month, day, hour, minute, 0)
        val textAlarmTime = findViewById<TextView>(R.id.setTime)
        textAlarmTime.text = SimpleDateFormat("hh:mm ss a").format(alarmCalender.time)
        setAlarm(alarmCalender.timeInMillis, "Start")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setAlarm(millisTime: Long, str:String)
    {
        val intent= Intent(this,AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1",str)
        val
                pendingIntent=PendingIntent.getBroadcast(applicationContext,234324243,intent,PendingIntent.FLAG_MUTABLE
                )
        val alarmManager= getSystemService(ALARM_SERVICE) as AlarmManager
        if(str=="Start")
        {
            if(alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    millisTime,
                    pendingIntent
                )
                val cardView =
                    findViewById<com.google.android.material.card.MaterialCardView>(R.id.materialCardView2)
                cardView.visibility = View.VISIBLE
            }
            else{
// Ask users to go to exact alarm page in system settings.
                startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                Toast.makeText(this, "Can't schedule Alarm", Toast.LENGTH_SHORT).show()
            }
        }
        else if (str=="Stop")
        {
            alarmManager.cancel(pendingIntent);
            sendBroadcast(intent)
        }
    }
}