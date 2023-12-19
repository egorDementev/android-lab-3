package com.example.androidlab3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var newsAdapter: NewsAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsAdapter = NewsAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = newsAdapter

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onGetNewsButtonClicked(view: View) {
        val query: String = findViewById<EditText>(R.id.editText).text.toString()
        if (query.isNotEmpty()) {
            GlobalScope.launch {
                try {
                    val response = NewsApiService.create().getNews("pub_35040b3c39fc6f4f6fdf22b723eb7ca05a30c", query)
                    if (response.status == "success") {
                        val simplifiedList = response.results.map { news ->
                            val truncatedDescription = if (news.description != null) {
                                val truncated = if (news.description.length > 250) {
                                    news.description.take(250) + "..."
                                } else {
                                    news.description
                                }
                                truncated
                            } else {
                                ""
                            }
                            News(news.title, truncatedDescription, news.link)
                        }
                        launch(Dispatchers.Main) {
                            newsAdapter.submitList(simplifiedList)
                            println(simplifiedList)
                        }
                    } else {
                        Log.e("MainActivity", "Error: ${response.status}")
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error fetching news", e)
                }
            }
        }
    }
}
