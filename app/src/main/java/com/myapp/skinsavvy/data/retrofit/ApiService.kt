package com.myapp.skinsavvy.data.retrofit

import com.myapp.skinsavvy.ResponseSolution
import com.myapp.skinsavvy.data.response.ArticlesResponse
import com.myapp.skinsavvy.data.response.DetailArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>

    @GET("severity-level-solutions/{level}")
    fun getSolution(@Path("level") level: Int): Call<ResponseSolution>

    @GET("articles/{id}")
    fun getDetailArticles(
        @Path("id") id: String
    ): Call<DetailArticleResponse>
}