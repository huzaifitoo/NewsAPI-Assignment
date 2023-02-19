package com.example.newsapi


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.newsapi.databinding.ActivityMainBinding
import retrofit2.Call
import androidx.appcompat.widget.SearchView
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mArticle: ArrayList<ArticlesItem> = arrayListOf()
    private lateinit var searchView: SearchView
    private lateinit var adapter: NewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                filterList(newText)
                return true
            }

        })

        binding.swipeToRefresh.setOnRefreshListener {

            gettingData()
            //Toast.makeText(this, "refreshing ", Toast.LENGTH_SHORT).show()

        }
          gettingData()
    }

    fun gettingData(){
        binding.swipeToRefresh.isRefreshing = true

        val res = RetrofitInstance.getInstance()
            .create(APIInterface::class.java)

        res.getArticleData().enqueue(object : Callback<ArticlesModel> {
            override fun onResponse(
                call: Call<ArticlesModel>,
                response: Response<ArticlesModel>
            ) {
                binding.swipeToRefresh.isRefreshing = false

                mArticle.addAll(response.body()?.articles!! as ArrayList<ArticlesItem>)

                val recyclerView = binding.recyclerView
                adapter = NewsAdapter(this@MainActivity, mArticle)
                recyclerView.adapter = adapter

                adapter.setOnItemClickListener(object : NewsAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                        val builder = CustomTabsIntent.Builder()
                        val customTabsIntent = builder.build()
                        customTabsIntent.launchUrl(
                            this@MainActivity,
                            Uri.parse(mArticle.get(position).url))
                    }
                })
                Log.d("Data", response.body().toString())
            }

            override fun onFailure(call: Call<ArticlesModel>, t: Throwable) {
                binding.swipeToRefresh.isRefreshing = false

                t.message?.let { Log.d("error something wrong", it) }
            }
        })
    }
    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<ArticlesItem>()
            for (i in mArticle) {
                if (i.title!!.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}