package com.example.mobileapp

import DBHelper
import com.example.mobileapp.Line
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
    private val list = mutableListOf<Line>()
    private lateinit var adapter: RecyclerAdapter
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addAll(dbHelper.getLines())

        adapter = RecyclerAdapter(list, {
            dbHelper.removeLine(list[it].id.toInt())
            list.removeAt(it)
            adapter.notifyItemRemoved(it)
        })

        val RecView = findViewById<RecyclerView>(R.id.recyclerView)
        RecView.layoutManager = LinearLayoutManager(this)
        RecView.adapter = adapter

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val textInput = findViewById<TextInputEditText>(R.id.textInput)

        buttonAdd.setOnClickListener {
            val value = textInput.text.toString()
            if ((value != "") && !(list.any{ elem -> elem.title == value}))
            {
                val id = dbHelper.addLine(value)
                list.add(Line(id, value))
                adapter.notifyItemInserted(list.lastIndex)
            }
        }
    }
}