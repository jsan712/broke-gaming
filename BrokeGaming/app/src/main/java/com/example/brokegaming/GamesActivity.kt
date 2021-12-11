package com.example.brokegaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import java.util.*
import android.os.Build


class GamesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var filterSearch: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)
        filterSearch = findViewById(R.id.filterSearch)
        progressBar = findViewById(R.id.progressBar4)

        Log.d("GamesActivity", "onCreate called!")

        //Set the title for the screen when coming from the skip sources button
        val title = getString(R.string.games_title)
        setTitle(title)

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

        filterSearch.setOnClickListener {
            Log.d("GamesActivity", "skip button clicked!")
            val intent: Intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id:Long){
        var sortBy = parent.getItemAtPosition(pos).toString()
        if(sortBy == "fecha-de-lanzamiento"){
            sortBy = "release-date"
        }
        else if(sortBy == "alfabético"){
            sortBy = "alphabetical"
        }
        else if(sortBy == "relevancia"){
            sortBy = "relevance"
        }
        else{
            sortBy = "popularity"
        }
        getGames(sortBy)
        Log.i("GamesActivity", "New spinner item selected!")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }
}