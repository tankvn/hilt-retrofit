package com.example.hilt.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hilt.data.Employee
import com.example.hilt.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    //private val employeeData = MutableLiveData<Resource<EmployeeResponse>>()

    var employeeList: List<Employee> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")

    init {
        //getEmployees()
    }

    fun getEmployees() = viewModelScope.launch {
        val employeeResponse = mainRepository.getEmployee()

        when (employeeResponse.isSuccessful) {
            true -> {
                employeeList = employeeResponse.body()?.data!!
            }
            else -> {
                //errorMessage = employeeResponse.message()
                errorMessage = employeeResponse.body()?.message.toString()
            }
        }
    }
}