package com.example.hilt.repository

import com.example.hilt.api.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getEmployee() = apiService.getEmployees()

}