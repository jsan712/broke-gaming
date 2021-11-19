package com.example.brokegaming

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class GamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        Log.d("MainActivity", "onCreate called!")
    }
}