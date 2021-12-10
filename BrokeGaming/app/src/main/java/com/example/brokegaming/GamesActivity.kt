package com.example.brokegaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import org.jetbrains.anko.doAsync
import com.google.android.material.chip.Chip

class GamesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var skipButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)
        skipButton = findViewById(R.id.skipButton)
        progressBar = findViewById(R.id.progressBar4)

        Log.d("GamesActivity", "onCreate called!")
        val intent: Intent = getIntent()
//        startActivity(intent)

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

        skipButton.setOnClickListener {
            Log.d("GamesActivity", "skip button clicked!")
            val intent: Intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
    }

    //Function to get chip group checked chips adapted from
    // https://android--code.blogspot.com/2020/09/android-kotlin-chipgroup-get-selected.html
//    private fun handleSelection() {
//        chipGroup.checkedChipIds.forEach {
//            val chip = findViewById<Chip>(it)
//            val gamesManager = GamesManager()
//            doAsync {
//                val games: List<Game> = try {
//                    val filter: String = chip.text.toString()
//                    filters.add(filter)
//                    gamesManager.retrieveFilter(filter)
//                }catch(exception: Exception){
//                    Log.e("GamesActivity", "Retrieving filter failed!", exception)
//                    listOf<Game>()
//                }
//
//                runOnUiThread {
//                    if(games.isNotEmpty()){
//                        val adapter: GamesAdapter = GamesAdapter(games)
//                        recyclerView.adapter = adapter
//                    }
//                    else{
//                        Toast.makeText(
//                            this@GamesActivity,
//                            "Failed to retrieve results!",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }
//        }
//    }

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