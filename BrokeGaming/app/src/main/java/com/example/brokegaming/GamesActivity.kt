package com.example.brokegaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import org.jetbrains.anko.doAsync

class GamesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var horizontalScrollView: HorizontalScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)
        horizontalScrollView = findViewById(R.id.horizontalScrollView)

        Log.d("GamesActivity", "onCreate called!")
        val intent: Intent = getIntent()

        recyclerView = findViewById(R.id.recyclerView)
        //Sets the scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(this, R.array.sorting,
            android.R.layout.simple_spinner_item).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }

    private fun getGames(sortBy: String){
        val gamesManager = GamesManager()
        doAsync {
            val games: List<Game> = try{
                gamesManager.retrieveGames(sortBy)
            }catch(exception: Exception){
                Log.e("GamesActivity", "Retrieving games failed!", exception)
                listOf<Game>()
            }

            runOnUiThread {
                if(games.isNotEmpty()){
                    val adapter: GamesAdapter = GamesAdapter(games)
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

    //The following functions provided by https://developer.android.com/guide/topics/ui/controls/spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id:Long){
        val sortBy = parent.getItemAtPosition(pos).toString()
        getGames(sortBy)
        Log.i("GamesActivity", "New spinner item selected!")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }
}