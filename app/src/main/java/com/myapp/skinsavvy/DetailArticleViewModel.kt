package com.myapp.skinsavvy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.skinsavvy.data.response.ArticlesItem
import com.myapp.skinsavvy.data.response.ArticlesResponse
import com.myapp.skinsavvy.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArticleViewModel : ViewModel() {
    private val _detailArticleResponse = MutableLiveData<ArticlesItem?>()
    val detailArticleResponse: MutableLiveData<ArticlesItem?> = _detailArticleResponse

    private val _responseMessage = MutableLiveData<String?>()
    val  responseMessage: LiveData<String?> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryDetail(id: String) {
        _isLoading.value = true

        val data = ApiConfig.getApiService().getDetailArticles(id)
        data.enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailArticleResponse.value = response.body()?.data?.articles?.firstOrNull { it?.id == id }
                } else {
                    Log.e("story detail", "onResponse: ${response.message()}, ${response.code()}")
                    _responseMessage.value = "Failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = "Failed: ${t.message.toString()}"
            }
        })
    }

    companion object {
        const val DETAIL_ARTICLE = "detail_article"
    }
}