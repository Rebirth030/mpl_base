package com.example.mpl_base.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mpl_base.R
import com.example.mpl_base.util.IS_PRIME
import com.example.mpl_base.util.RANDOM_NUMBER

class FalseActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var textField: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_false)

        setUI()
    }

    /**
     * Sets the UI elements and their listeners
     */
    private fun setUI(){
        val number = this.intent.getIntExtra(RANDOM_NUMBER, 0)
        val isPrime = this.intent.getBooleanExtra(IS_PRIME, false)

        backButton = findViewById(R.id.backBtn)
        textField = findViewById(R.id.anwserView)

        if (isPrime){
            textField.text = String.format(getString(R.string.answer_text), number, getString(R.string.is_text))
        } else {
            textField.text = String.format(getString(R.string.answer_text), number, getString(R.string.is_not_text))
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}