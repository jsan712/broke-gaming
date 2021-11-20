package com.example.brokegaming

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GamesAdapter(val games: List<Game>) : RecyclerView.Adapter<GamesAdapter.ViewHolder>() {
    //How many rows will be rendered
    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currGame = games[position]
        viewHolder.gameTitle.setText(currGame.title)
        viewHolder.description.setText(currGame.description)
        viewHolder.genre.setText(currGame.genre)
        val url = currGame.url

        if(currGame.pictureURL.isNotBlank()){
            Picasso.get().setIndicatorsEnabled(true)

            Picasso.get().load(currGame.pictureURL).into(viewHolder.thumbnail)
        }

        if(viewHolder.description.text == null){
            viewHolder.description.visibility = View.GONE
        }

        //When a card is clicked open the article in the browser
        //The following snippet is adapted from Stackoverflow user Jorgesys in
        //https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
        viewHolder.itemView.setOnClickListener {
            val context: Context = viewHolder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.game_list, parent, false)
        return ViewHolder(rootLayout)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameTitle: TextView = itemView.findViewById(R.id.gameTitle)
        val description: TextView = itemView.findViewById(R.id.description)
        val genre: TextView = itemView.findViewById(R.id.genre)
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
    }
}