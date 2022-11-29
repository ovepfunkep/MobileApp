package com.example.mobileapp

import DBHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class activity_view : AppCompatActivity() {
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewSurname = findViewById<TextView>(R.id.textViewSurname)
        val textViewDateOfBirth = findViewById<TextView>(R.id.textViewDateOfBirth)
        val textViewPhone = findViewById<TextView>(R.id.textViewPhone)

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonChange = findViewById<Button>(R.id.buttonChange)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        var id = intent.getStringExtra("LINE_ID")?.toInt()
        if (id == null) id = 0
        val name = intent.getStringExtra("NAME")
        val surname = intent.getStringExtra("SURNAME")
        val dateOfBirth = intent.getStringExtra("DATEOFBIRTH")
        val phoneNumber = intent.getStringExtra("PHONENUMBER")

        textViewName.setText("${textViewName.text} ${name}")
        textViewSurname.setText("${textViewSurname.text} ${surname}")
        textViewDateOfBirth.setText("${textViewDateOfBirth.text} ${dateOfBirth}")
        textViewPhone.setText("${textViewPhone.text} ${phoneNumber}")

        val returnIntent = Intent()

        buttonBack.setOnClickListener {
            returnIntent.putExtra("RESULT", "NOCHANGES")
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }

        buttonDelete.setOnClickListener {
            dbHelper.removeLine(id)
            returnIntent.putExtra("RESULT", "UPDATE")
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val result = it?.data?.extras?.get("RESULT") as String
            returnIntent.putExtra("RESULT", result)
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }

        buttonChange.setOnClickListener {
            val intent = Intent(this, activity_modify::class.java)
            intent.putExtra("LINE_ID", id)
            intent.putExtra("NAME", name)
            intent.putExtra("SURNAME", surname)
            intent.putExtra("DATEOFBIRTH", dateOfBirth)
            intent.putExtra("PHONENUMBER", phoneNumber)
            getResult.launch(intent)
        }
    }
}