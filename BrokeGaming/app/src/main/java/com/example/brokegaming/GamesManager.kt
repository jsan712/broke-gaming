package com.example.brokegaming

import org.json.JSONArray
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor

class GamesManager {
    val okHttpClient: OkHttpClient

    init{
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }

    fun getGames(): List<Game>{
        val games: MutableList<Game> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://www.freetogame.com/api/games")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrBlank()){
            val json: JSONObject = JSONObject(responseBody)
            val statuses: JSONArray = json.getJSONArray("sources")

            for(i in 0 until statuses.length()){
                val curr: JSONObject = statuses.getJSONObject(i)
                val title: String = curr.getString("title")
                val description: String = curr.getString("short_description")
                val genre: String = curr.getString("genre")
                val pictureUrl: String = curr.getString("thumbnail")
                val url: String = curr.getString("game_url")

                val game: Game = Game(
                    title = title,
                    description = description,
                    genre = genre,
                    pictureURL = pictureUrl,
                    url = url,
                )

                games.add(game)
            }
        }
        return games
    }
}