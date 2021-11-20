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
import org.json.JSONTokener

class GamesManager {
    val okHttpClient: OkHttpClient

    init{
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }

    fun retrieveGames(sortBy: String): List<Game>{
        val games: MutableList<Game> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://www.freetogame.com/api/games?sort-by=$sortBy")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrBlank()){
            //The following code was adapted from https://johncodeos.com/how-to-parse-json-in-android-using-kotlin/
            val arr: JSONArray = JSONTokener(responseBody).nextValue() as JSONArray

            for(i in 0 until arr.length()) {
                val title: String = arr.getJSONObject(i).getString("title")
                val description: String = arr.getJSONObject(i).getString("short_description")
                val genre: String = arr.getJSONObject(i).getString("genre")
                val pictureUrl: String = arr.getJSONObject(i).getString("thumbnail")
                val url: String = arr.getJSONObject(i).getString("game_url")

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