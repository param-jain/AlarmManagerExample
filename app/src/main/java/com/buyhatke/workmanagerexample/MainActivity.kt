package com.buyhatke.workmanagerexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.buyhatke.workmanagerexample.helper.Constants
import com.buyhatke.workmanagerexample.helper.NotificationHandler

import java.util.Calendar
import java.util.UUID

import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data

class MainActivity : AppCompatActivity() {

    private var alertInputValue: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alertInputValue = findViewById<View>(R.id.alert_time) as EditText

        val alertButton = findViewById<View>(R.id.set_alert_button) as Button
        alertButton.setOnClickListener {
            val inputValue = alertInputValue!!.text.toString()
            if (inputValue == "") {
                Toast.makeText(
                    this@MainActivity,
                    "Input value must not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }

            //Generate notification string tag
            val tag = generateKey()

            //Get time before alarm
            val minutesBeforeAlert = Integer.valueOf(inputValue)
            val alertTime = getAlertTime(minutesBeforeAlert) - System.currentTimeMillis()
            val current = System.currentTimeMillis()

            Log.d(TAG, "Alert time - " + alertTime + "Current time " + current)

            val random = (Math.random() * 50 + 1).toInt()

            //Data
            val data = createWorkInputData(Constants.TITLE, Constants.TEXT, random)

            NotificationHandler.scheduleReminder(alertTime, data, tag)
        }
    }


    private fun createWorkInputData(title: String, text: String, id: Int): Data {
        return Data.Builder()
            .putString(Constants.EXTRA_TITLE, title)
            .putString(Constants.EXTRA_TEXT, text)
            .putInt(Constants.EXTRA_ID, id)
            .build()
    }


    private fun getAlertTime(userInput: Int): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, userInput)
        return cal.timeInMillis
    }


    private fun generateKey(): String {
        return UUID.randomUUID().toString()
    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }

}
