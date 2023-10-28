package com.example.hilt.data

data class Employee(
    val id: Int,
    val employee_name: String? = "",
    val employee_salary: String? = "",
    val employee_age: String? = "",
    val profile_image: String? = ""
)