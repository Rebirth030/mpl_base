package com.example.mpl_base.activities


import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.mpl_base.R
import com.example.mpl_base.util.APP_WIDGET_ID
import com.example.mpl_base.util.CalcUtil
import com.example.mpl_base.util.IS_PRIME
import com.example.mpl_base.util.MyAppWidget
import com.example.mpl_base.util.RANDOM_NUMBER
import com.example.mpl_base.util.WidgetActionEnum


class MainActivity : AppCompatActivity()
{
    private lateinit var randomNumberTv: TextView
    private lateinit var randomizeBtn: Button
    private lateinit var trueBtn: ImageButton
    private lateinit var falseBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Prime or Not?"

        setUI()
    }

    /**
     * Sets the UI elements and their listeners
     */
    private fun setUI(){
        randomNumberTv = findViewById(R.id.main_random_number)
        randomizeBtn = findViewById(R.id.main_btn_randomize)
        trueBtn = findViewById(R.id.buttonTrue)
        falseBtn = findViewById(R.id.buttonFalse)

        randomNumberTv.text = this.intent.getIntExtra(RANDOM_NUMBER, 0).toString()

        randomizeBtn.setOnClickListener {
            updateRandomNumber()
            updateWidget()
        }

        trueBtn.setOnClickListener {
            onKlick(true)
        }

        falseBtn.setOnClickListener {
            onKlick(false)
        }
    }

    /**
     * Handles the logic for the true and false buttons
     * @param trueOrFalse true if the true button was pressed, false if the false button was pressed
     */
    private fun onKlick(trueOrFalse : Boolean){
        val number = randomNumberTv.text.toString().toInt()
        val isPrime = CalcUtil.checkIfPrime(number)

        if (isPrime == trueOrFalse)
        {
            val intent = Intent(this, TrueActivity::class.java)
            intent.putExtra(RANDOM_NUMBER, number)
            intent.putExtra(IS_PRIME, isPrime)
            startActivity(intent)
        }
        else
        {
            val intent = Intent(this, FalseActivity::class.java)
            intent.putExtra(RANDOM_NUMBER, number)
            intent.putExtra(IS_PRIME, isPrime)
            startActivity(intent)
        }
    }

    /**
     * Updates all the widgets with the current random number
     */
    private fun updateWidget() {
        for (widget in AppWidgetManager.getInstance(this).getAppWidgetIds(
            ComponentName(this, MyAppWidget::class.java)
        )) {
            val intent = Intent(this, MyAppWidget::class.java)
            intent.action = WidgetActionEnum.SYNC.toString()
            intent.putExtra(APP_WIDGET_ID, widget)
            intent.putExtra(RANDOM_NUMBER, randomNumberTv.text.toString().toInt())
            sendBroadcast(intent)
        }
    }

    /**
     * Updates the random number
     */
    private fun updateRandomNumber(){
        val randomNumber = CalcUtil.rng()
        randomNumberTv.text = randomNumber.toString()
    }
}

