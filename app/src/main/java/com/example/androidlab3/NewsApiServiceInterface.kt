package com.example.androidlab3

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiServiceInterface {
    @GET("news")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("q") query: String
    ): NewsApiResponse
}
