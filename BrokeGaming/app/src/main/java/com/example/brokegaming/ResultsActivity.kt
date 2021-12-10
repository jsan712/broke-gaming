package com.example.brokegaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.jetbrains.anko.doAsync

class ResultsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var chipGroup: ChipGroup
    var filters = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        horizontalScrollView = findViewById(R.id.horizontalScrollView)
        chipGroup = findViewById(R.id.chipGroup)
        progressBar = findViewById(R.id.progressBar3)

        recyclerView = findViewById(R.id.recyclerView)
        //Sets the scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Get data from the Intent that launched this screen
        val intent: Intent = getIntent()

        //Set the title for the screen when coming from the skip sources button
        val title = getString(R.string.results_title)
        setTitle(title)

        //The following code snippet was adapted from
        //https://android--code.blogspot.com/2020/09/android-kotlin-chipgroup-get-selected.html
        handleSelection()
        // set checked change listener for each chip on chip group
        chipGroup.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection()
            }
        }
    }

    //Function to get chip group checked chips adapted from
    // https://android--code.blogspot.com/2020/09/android-kotlin-chipgroup-get-selected.html
    private fun handleSelection() {
        chipGroup.checkedChipIds.forEach {
            val chip = findViewById<Chip>(it)
            val gamesManager = GamesManager()
            doAsync {
                val games: List<Game> = try {
                    val filter: String = chip.text.toString()
                    filters.add(filter)
                    gamesManager.retrieveFilter(filter)
                }catch(exception: Exception){
                    Log.e("GamesActivity", "Retrieving filter failed!", exception)
                    listOf<Game>()
                }

                runOnUiThread {
                    if(games.isNotEmpty()){
                        val adapter: GamesAdapter = GamesAdapter(games)
                        recyclerView.adapter = adapter
                    }
                    else{
                        Toast.makeText(
                            this@ResultsActivity,
                            "Failed to retrieve results!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}