package com.ildus.mytsd1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "Broadcast"
//private const val IntentFilterAction = Intent.ACTION_TIME_TICK
private const val IntentFilterAction = "hsm.RECVRBI"
//private const val IntentFilterAction = "com.honeywell.intent.action.SCAN_RESULT"
class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private val tickReceiver by lazy { makeBroadcastReceiver() }

    companion object {
        private fun getCurrentTimeStamp(): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val now = Date()
            return simpleDateFormat.format(now)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

//        Toast.makeText(this, "ildus", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
//        dateTimeTextView.text = getCurrentTimeStamp()
        registerReceiver(tickReceiver, IntentFilter(IntentFilterAction))
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Time tick Receiver not registered", e)
        }
    }


    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                // https://support.honeywellaidc.com/s/article/Scanning-using-a-Broadcast-Data-Intent-on-CT50-and-CN51
                Log.d(TAG, "onReceive")
//                Toast.makeText(this@MainActivity, "onReceive", Toast.LENGTH_SHORT).show()
//                if (intent?.action == Intent.ACTION_TIME_TICK) {
//                    dateTimeTextView.text = getCurrentTimeStamp()
//                    Log.d(TAG, "" + dateTimeTextView.text)
//                }

                Log.d(TAG, "codeId: \t" + intent?.getStringExtra("codeId"))
                Log.d(TAG, "dataBytes\t" + Arrays.toString(intent?.getByteArrayExtra("dataBytes")))
                val d = intent?.getStringExtra("data")
                Log.d(TAG, "data: \t" + intent?.getStringExtra("data"))
                Log.d(TAG, "timestamp: \t" + intent?.getStringExtra("timestamp"))
                Log.d(TAG, "aimId: \t" + intent?.getStringExtra("aimId"))
                Log.d(TAG, "version: \t" + intent?.getIntExtra("version", -1))
                Log.d(TAG, "charset: \t" + intent?.getStringExtra("charset"))
                Log.d(TAG, "scanner: \t" + intent?.getStringExtra("scanner"))
                if (d != null && d.length > 0) {
                    textView.text = d
                }

                Toast.makeText(this@MainActivity, d, Toast.LENGTH_SHORT).show()

            }
        }
    }
}