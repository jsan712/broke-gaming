package com.example.brokegaming

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.jetbrains.anko.doAsync

class ResultsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var chipGroup: ChipGroup
    private lateinit var platformSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        horizontalScrollView = findViewById(R.id.horizontalScrollView)
        chipGroup = findViewById(R.id.chipGroup)
        progressBar = findViewById(R.id.progressBar3)

        recyclerView = findViewById(R.id.recyclerView)
        //Sets the scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set the title for the screen when coming from the filter search button
        val title = getString(R.string.results_title)
        setTitle(title)

        platformSpinner = findViewById(R.id.platformSpinner)
        ArrayAdapter.createFromResource(this, R.array.platforms,
            android.R.layout.simple_spinner_item).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            platformSpinner.adapter = adapter
        }

        platformSpinner.onItemSelectedListener = this

        getGames("relevance")

        //The following code snippet was adapted from
        //https://android--code.blogspot.com/2020/09/android-kotlin-chipgroup-get-selected.html
        handleSelection(platformSpinner.selectedItem.toString())
        // set checked change listener for each chip on chip group
        chipGroup.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(platformSpinner.selectedItem.toString())
            }
        }
    }

    //Function to get chip group checked chips adapted from
    // https://android--code.blogspot.com/2020/09/android-kotlin-chipgroup-get-selected.html
    private fun handleSelection(platform: String) {
        chipGroup.checkedChipIds.forEach {
            val chip = findViewById<Chip>(it)
            val gamesManager = GamesManager()
            doAsync {
                val games: List<Game> = try {
                    var filter: String = chip.text.toString()
                    if(filter == "juego-de-disparos"){
                        filter = "shooter"
                    }
                    else if(filter == "estrategia"){
                        filter = "strategy"
                    }
                    else if(filter == "carreras"){
                        filter = "racing"
                    }
                    else if(filter == "deportes"){
                        filter = "sports"
                    }
                    else if(filter == "caja-de-arena"){
                        filter = "sandbox"
                    }
                    else if(filter == "mundo-abierto"){
                        filter = "open-world"
                    }
                    else if(filter == "supervivencia"){
                        filter = "survival"
                    }
                    else if(filter == "zombi"){
                        filter = "zombie"
                    }
                    else if(filter == "por-turnos"){
                        filter = "turn-based"
                    }
                    else if(filter == "primera-persona"){
                        filter = "first-person"
                    }
                    else if(filter == "tercera-persona"){
                        filter = "third-person"
                    }
                    else if(filter == "de-arriba-hacia-abajo"){
                        filter = "top-down"
                    }
                    else if(filter == "tanque"){
                        filter = "tank"
                    }
                    else if(filter == "espacio"){
                        filter = "space"
                    }
                    else if(filter == "navegación"){
                        filter = "sailing"
                    }
                    else if(filter == "desplazamiento-lateral"){
                        filter = "side-scroller"
                    }
                    else if(filter == "superhéroe"){
                        filter = "superhero"
                    }
                    else if(filter == "muerte-permanente"){
                        filter = "permadeath"
                    }
                    else if(filter == "carta"){
                        filter = "card"
                    }
                    else if(filter == "batalla-real"){
                        filter = "battle-royale"
                    }
                    else if(filter == "fantasía"){
                        filter = "fantasy"
                    }
                    else if(filter == "ciencia- ficción"){
                        filter = "sci-fi"
                    }
                    else if(filter == "luchando"){
                        filter = "fighting"
                    }
                    else if(filter == "acción-rpg"){
                        filter = "action-rpg"
                    }
                    else if(filter == "acción"){
                        filter = "action"
                    }
                    else if(filter == "militar"){
                        filter = "military"
                    }
                    else if(filter == "artes-marciales"){
                        filter = "martial-arts"
                    }
                    else if(filter == "vuelo"){
                        filter = "flight"
                    }
                    else if(filter == "baja-especificación"){
                        filter = "low-spec"
                    }
                    else if(filter == "torre-de-defensa"){
                        filter = "tower-defense"
                    }
                    else if(filter == "terror"){
                        filter = "horror"
                    }
                    gamesManager.retrieveFilter(filter, platform)
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
                        this@ResultsActivity,
                        "Failed to retrieve results!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id:Long){
        var platform = parent.getItemAtPosition(pos).toString()
        if(platform == "navegador web"){
            platform = "browser"
        }
        handleSelection(platform)
        Log.i("GamesActivity", "New spinner item selected!")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }
}