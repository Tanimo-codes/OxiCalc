package com.example.oxicalc.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oxicalc.Model.CalculationHistoryItem
import com.example.oxicalc.Model.historyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: historyRepo
) : ViewModel() {

    val historyItems = repo.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addHistory(item: CalculationHistoryItem) = viewModelScope.launch {
        repo.addHistory(item)
    }

    fun deleteHistory(item: CalculationHistoryItem) = viewModelScope.launch {
        repo.deleteHistory(item)
    }
}
