package com.example.hilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.hilt.data.Employee
import com.example.hilt.ui.MainViewModel
import com.example.hilt.ui.theme.HiltWithRetrofitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getEmployees()

        setContent {
            HiltWithRetrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(mainViewModel.employeeList)
                }
            }
        }
    }
}

@Composable
fun Greeting(employeeList: List<Employee>, modifier: Modifier = Modifier) {
    LazyColumn() {
        itemsIndexed(items = employeeList) { index, item ->
            Text(
                text = "${item.id} - ${item.employee_name} - ${item.employee_age} years old - ${item.employee_salary}$",
                modifier = modifier
            )
            AsyncImage(
                model = "https://randomuser.me/api/portraits/men/${item.id}.jpg",
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val employeeList = listOf<Employee>(
        Employee(
            id = 0,
            employee_name = "Mr. Smith",
            employee_salary = "1000",
            employee_age = "49"
        )
    )
    HiltWithRetrofitTheme {
        Greeting(employeeList)
    }
}