package com.example.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textViewCounter = findViewById<TextView>(R.id.textViewCounter)
        val buttonAdd = findViewById<TextView>(R.id.buttonAdd)
        val buttonSubstract = findViewById<TextView>(R.id.buttonSubstract)
        val textInputCounterVal = findViewById<TextInputEditText>(R.id.textInputCounterVal)
        var inputValue = textInputCounterVal.text.toString().toInt()

        textInputCounterVal.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val temp = textInputCounterVal.text.toString().toIntOrNull()
                if (temp != null) inputValue = temp
                else textInputCounterVal.setText("0")
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        buttonAdd.setOnClickListener {
            counter += inputValue
            textViewCounter.text = (counter).toString()
        }

        buttonSubstract.setOnClickListener {
            if (counter - inputValue >= 0) {
                counter -= textInputCounterVal.text.toString().toInt()
                textViewCounter.text = (counter).toString()
            }
            else {
                val toast = Toast.makeText(applicationContext, "Be positive!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}