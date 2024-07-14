package com.looper.android.support.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the TextView in the layout
        val textView: TextView = findViewById(R.id.textView)

        // Set the text to "Hello, World!"
        textView.text = "Hello, World!"
    }
}