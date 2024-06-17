package com.myapp.skinsavvy.data.retrofit

import com.myapp.skinsavvy.data.response.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>
}