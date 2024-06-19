package com.myapp.skinsavvy.data.retrofit

import com.myapp.skinsavvy.data.response.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>

    @GET("articles/{id}")
    fun getDetailArticles(
        @Path("id") id: String
    ): Call<ArticlesResponse>
}