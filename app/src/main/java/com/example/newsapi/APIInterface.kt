package com.example.newsapi

import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {

    @GET("top-headlines?country=us&apiKey=0367ac796da046f08cd019763a183971")
    fun getArticleData(): Call<ArticlesModel>
}