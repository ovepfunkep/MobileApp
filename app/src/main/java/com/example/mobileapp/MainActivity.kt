package com.example.mobileapp

import DBHelper
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ContentInfoCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    private val listMain = mutableListOf<Line>()
    private val listStorage = mutableListOf<Line>()
    private lateinit var adapter: RecyclerAdapter
    private val dbHelper = DBHelper(this)

    fun addLineToListMain(line: Line) {
        listMain.add(line)
        adapter.notifyItemInserted(listMain.lastIndex)
    }

    fun removeLineFromListMain(line: Line) {
        adapter.notifyItemRemoved(listMain.indexOf(line))
        listMain.remove(line)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val textInput = findViewById<TextInputEditText>(R.id.textInput)

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val result = it?.data?.extras?.get("RESULT") as String
            if (result == "UPDATE") {
                textInput.setText("")
                for (line in listStorage)
                    removeLineFromListMain(line)
                listStorage.clear()
                listStorage.addAll(dbHelper.getLines())
                for (line in listStorage)
                    addLineToListMain(line)
            }
        }

        listStorage.addAll(dbHelper.getLines())
        listMain.addAll(listStorage)

        adapter = RecyclerAdapter(listMain, {
            val intent = Intent(this, activity_view::class.java)
            intent.putExtra("LINE_ID", listMain[it].id.toString())
            intent.putExtra("NAME", listMain[it].name)
            intent.putExtra("SURNAME", listMain[it].surname)
            intent.putExtra("DATEOFBIRTH", listMain[it].dateOfBirth)
            intent.putExtra("PHONENUMBER", listMain[it].phoneNumber)
            getResult.launch(intent)
        })

        val RecView = findViewById<RecyclerView>(R.id.recyclerView)
        RecView.layoutManager = LinearLayoutManager(this)
        RecView.adapter = adapter

        textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var text = textInput.text.toString()
                if (text.isEmpty()) text = " "
                for (line in listStorage)
                    if (!"${line.name} ${line.surname} ${line.dateOfBirth} ${line.phoneNumber}".contains(text)) {
                        if (listMain.contains(line))
                            removeLineFromListMain(line)
                    } else if (!listMain.contains(line))
                        addLineToListMain(line)
            }
        })

        buttonAdd.setOnClickListener {
            val intent = Intent(this, activity_modify::class.java)
            intent.putExtra("LINE_ID", -1)
            getResult.launch(intent)
        }
    }
}