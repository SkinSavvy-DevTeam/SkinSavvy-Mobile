package com.myapp.skinsavvy.data.retrofit

import com.myapp.skinsavvy.ResponseSolution
import com.myapp.skinsavvy.data.response.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>

    @GET("severity-level-solutions/{level}")
    fun getSolution(@Path("level") level: Int): Call<ResponseSolution>
}