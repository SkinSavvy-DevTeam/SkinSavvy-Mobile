package com.myapp.skinsavvy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.skinsavvy.data.response.ArticlesItem
import com.myapp.skinsavvy.data.response.ArticlesResponse
import com.myapp.skinsavvy.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listArticle = MutableLiveData<List<ArticlesItem>>()
    val listArticle: LiveData<List<ArticlesItem>> = _listArticle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchAllArticles()
    }

    private fun fetchAllArticles() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getArticles()
        client.enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listArticle.value = responseBody.data?.articles?.filterNotNull()
                    }
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}