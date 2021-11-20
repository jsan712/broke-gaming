package com.example.brokegaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class GamesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        Log.d("MainActivity", "onCreate called!")

        recyclerView = findViewById(R.id.recyclerView)
        //Sets the scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        val intent: Intent = getIntent()

        val gamesManager = GamesManager()
        doAsync {
            val results: List<Game> = try{
                gamesManager.getGames()
            }catch(exception: Exception){
                Log.e("GamesActivity", "Retrieving games failed!", exception)
                listOf<Game>()
            }

            runOnUiThread {
                if(results.isNotEmpty()){
                    val adapter: GamesAdapter = GamesAdapter(results)
                    recyclerView.adapter = adapter
                }
                else{
                    Toast.makeText(
                        this@GamesActivity,
                        "Failed to retrieve results!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}