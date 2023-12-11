package com.example.mpl_base.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mpl_base.R
import com.example.mpl_base.util.CalcUtil

class MainActivity : AppCompatActivity()
{
    private lateinit var randomNumberTv: TextView
    private lateinit var randomizeBtn: Button
    private lateinit var notifyBtn: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //         NotificationUtil.createNotificationChannel(this)

        setUI()
    }

    private fun setUI(){
        randomNumberTv = findViewById(R.id.main_random_number)
        randomizeBtn = findViewById(R.id.main_btn_randomize)
        notifyBtn = findViewById(R.id.main_btn_notify)

        updateRandomNumber()

        randomizeBtn.setOnClickListener {
            updateRandomNumber()
        }

        notifyBtn.setOnClickListener{
//            NotificationUtil.sendNotification(this, title, text, icon, i)
        }
    }

    private fun updateRandomNumber(){
        val randomNumber = CalcUtil.rng()
        randomNumberTv.text = randomNumber.toString()
    }

}