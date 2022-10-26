package com.example.mobileapp

import DBHelper
import Line
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val list = mutableListOf<String>()
    private lateinit var adapter: RecyclerAdapter
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (line in dbHelper.getLines()){
            list.add(line.title)
        }

        adapter = RecyclerAdapter(list){
            list.removeAt(it)
            adapter.notifyItemRemoved(it)
        }
        val RecView = findViewById<RecyclerView>(R.id.recyclerView)
        RecView.layoutManager = LinearLayoutManager(this)
        RecView.adapter = adapter

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val textInput = findViewById<TextInputEditText>(R.id.textInput)

        buttonAdd.setOnClickListener {
            if ((textInput.text.toString() != "") && !(list.any{ elem -> elem == textInput.text.toString()}))
            {
                list.add(textInput.text.toString())
                adapter.notifyItemInserted(list.lastIndex)
            }
        }
    }
}