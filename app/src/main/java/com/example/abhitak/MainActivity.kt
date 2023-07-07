package com.example.abhitak

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private var mJsonRequest: JsonObjectRequest? = null
    private var mAdapter: NewsListAdapter? = null
    private val url = "https://saurav.tech/NewsAPI/everything/cnn.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter( this)
        recyclerView.adapter = mAdapter

    }

    private fun fetchData() {
        // String Request initialized
        mJsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until  newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter?.updateNews(newsArray)

            }
        ) { error -> Log.d(ContentValues.TAG, "Error :$error") }
        MySingleTon.getInstance(this).addToRequestQueue(mJsonRequest!!)
    }

    override fun onItemClicked(item: News) {
        val url = item.url
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(this@MainActivity, Uri.parse(url))
    }
}