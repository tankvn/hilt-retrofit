package com.example.hilt.api

import com.example.hilt.data.EmployeeResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("employees")
    suspend fun getEmployees(): Response<EmployeeResponse>

}