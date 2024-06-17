package com.myapp.skinsavvy.data.response

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class ArticlesItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("body")
    val body: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null
)

data class Data(

    @field:SerializedName("articles")
    val articles: List<ArticlesItem?>? = null
)
