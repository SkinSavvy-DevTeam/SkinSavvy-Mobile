package com.myapp.skinsavvy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.skinsavvy.data.retrofit.ApiConfig
import com.myapp.skinsavvy.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectionResultViewModel : ViewModel() {
    private val _solutionExplanation = MutableLiveData<String?>()
    val solutionExplanation: LiveData<String?> = _solutionExplanation

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
                    _solutionExplanation.value = explanation
                } else {
                    // Handle error case
                    _solutionExplanation.value = "Explanation not found"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseSolution>, t: Throwable) {
                // Handle failure case
                _solutionExplanation.value = "Failed to load explanation"
                _isLoading.value = false
            }
        })
    }
}