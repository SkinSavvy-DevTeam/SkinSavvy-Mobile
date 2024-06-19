package com.myapp.skinsavvy.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.skinsavvy.data.response.ResponseSolution
import com.myapp.skinsavvy.data.retrofit.ApiConfig
import com.myapp.skinsavvy.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectionResultViewModel : ViewModel() {
    private val _solution = MutableLiveData<String?>()
    val solution: LiveData<String?> = _solution

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val apiService: ApiService = ApiConfig.getApiService()

    fun fetchSolution(level: Int) {
        _isLoading.value = true
        apiService.getSolution(level).enqueue(object : Callback<ResponseSolution> {
            override fun onResponse(
                call: Call<ResponseSolution>,
                response: Response<ResponseSolution>
            ) {
                if (response.isSuccessful) {
                    val explanation = response.body()?.data?.severityLevelSolution?.solution?.explanation
                    _solution.value = explanation
                } else {
                    _solution.value = "Explanation not found"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseSolution>, t: Throwable) {
                _solution.value = "Failed to load explanation"
                _isLoading.value = false
            }
        })
    }
}