package com.example.hilt.data

data class EmployeeResponse(
    val data: List<Employee>? = null,
    val status: String? = "",
    val message: String? = ""
)