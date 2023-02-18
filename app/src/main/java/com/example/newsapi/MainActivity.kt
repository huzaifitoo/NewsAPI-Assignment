package com.example.newsapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.newsapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mArticle: ArrayList<ArticlesItem> = arrayListOf()
    lateinit var news: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val res = RetrofitInstance.getInstance()
            .create(APIInterface::class.java)

        res.getArticleData().enqueue(object : Callback<ArticlesModel> {
            override fun onResponse(
                call: Call<ArticlesModel>,
                response: Response<ArticlesModel>
            ) {
                mArticle.addAll(response.body()?.articles!! as ArrayList<ArticlesItem>)

                val recyclerView = binding.recyclerView
                val adapter = NewsAdapter(this@MainActivity, mArticle)
                recyclerView.adapter = adapter

                adapter.setOnItemClickListener(object : NewsAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {

                        Toast.makeText(this@MainActivity, "you clicked on item no.: $position", Toast.LENGTH_SHORT).show()
                    }

                })

                Log.d("Data",response.body().toString())
            }

            override fun onFailure(call: Call<ArticlesModel>, t: Throwable) {
                t.message?.let { Log.d("error something wrong", it) }
            }

        })
    }
}