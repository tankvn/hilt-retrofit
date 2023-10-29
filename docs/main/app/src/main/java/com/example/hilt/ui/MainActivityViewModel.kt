package com.androidfactory.onequote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidfactory.onequote.AppState.Navigation.Page
import com.androidfactory.onequote.network.QuoteRepository
import com.androidfactory.onequote.network.QuoteService
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private val _appState = MutableStateFlow(AppState.initial())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    fun selectPage(page: Page) {
        _appState.update {
            return@update it.copy(
                navigation = it.navigation.copy(
                    selectedPage = page
                )
            )
        }
    }

    fun fetchData() = viewModelScope.launch {
        val quoteOfTheDayResponse = quoteRepository.getQuoteOfTheDay()
        Log.e("RESPONSE", quoteOfTheDayResponse?.toString() ?: "failed to fetch")
    }
}