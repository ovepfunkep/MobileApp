package com.example.mobileapp

import DBHelper
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class activity_modify : AppCompatActivity() {

    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        val textInputName = findViewById<TextInputEditText>(R.id.textInputName)
        val textInputSurname = findViewById<TextInputEditText>(R.id.textInputSurname)
        val textInputDate = findViewById<TextInputEditText>(R.id.textInputDate)
        val textInputPhone = findViewById<TextInputEditText>(R.id.textInputPhone)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)

        val id = intent.getIntExtra("LINE_ID", -1)

        val returnIntent = Intent()

        fun IsAllCorrect(): Boolean {
            return !textInputName.text.isNullOrEmpty() &&
                    !textInputSurname.text.isNullOrEmpty() &&
                    !textInputDate.text.isNullOrEmpty() &&
                    !textInputPhone.text.isNullOrEmpty()
        }

        if (id != -1) {
            textInputName.setText(intent.getStringExtra("NAME"))
            textInputSurname.setText(intent.getStringExtra("SURNAME"))
            textInputDate.setText(intent.getStringExtra("DATEOFBIRTH"))
            textInputPhone.setText(intent.getStringExtra("PHONENUMBER"))
        }

        buttonCancel.setOnClickListener {
            returnIntent.putExtra("RESULT", "NOCHANGES")
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }

        buttonSave.setOnClickListener {
            if (!IsAllCorrect()) Toast.makeText(this, "Fill all fields correctly", Toast.LENGTH_SHORT).show()
            else {
                val name = textInputName.text.toString()
                val surname = textInputSurname.text.toString()
                val dateOfBirth = textInputDate.text.toString()
                val phoneNumber = textInputPhone.text.toString()
                if (id == -1) {
                    dbHelper.addLine(name, surname, dateOfBirth, phoneNumber)
                    returnIntent.putExtra("RESULT", "UPDATE")
                    setResult(RESULT_OK, returnIntent)
                }
                else {
                    dbHelper.updateLine(id, name, surname, dateOfBirth, phoneNumber)
                    returnIntent.putExtra("RESULT", "UPDATE")
                    setResult(RESULT_OK, returnIntent)
                }
                this.finish()
            }
        }
    }
}
