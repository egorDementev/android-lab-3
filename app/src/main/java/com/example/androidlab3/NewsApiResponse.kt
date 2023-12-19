package com.example.androidlab3

data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val results: List<News>
)
