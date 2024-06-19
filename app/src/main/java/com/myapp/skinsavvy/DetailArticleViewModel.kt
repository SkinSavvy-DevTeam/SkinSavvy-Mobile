package com.myapp.skinsavvy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.skinsavvy.data.response.Article
import com.myapp.skinsavvy.data.response.DetailArticleResponse
import com.myapp.skinsavvy.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArticleViewModel : ViewModel() {
    private val _detailArticleResponse = MutableLiveData<Article?>()
    val detailArticleResponse: MutableLiveData<Article?> = _detailArticleResponse

    private val _responseMessage = MutableLiveData<String?>()
    val  responseMessage: LiveData<String?> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryDetail(id: String) {
        _isLoading.value = true

        val data = ApiConfig.getApiService().getDetailArticles(id)
        data.enqueue(object : Callback<DetailArticleResponse> {
            override fun onResponse(
                call: Call<DetailArticleResponse>,
                response: Response<DetailArticleResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailArticleResponse.value = response.body()?.data?.article
                } else {
                    _responseMessage.value = "Failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailArticleResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = "Failed: ${t.message.toString()}"
            }
        })
    }
}