package com.example.oxicalc.Model

import javax.inject.Inject

class historyRepo @Inject constructor(private val dao: HistoryDao) {
    val allHistory = dao.getAllHistory()

    suspend fun addHistory(item: CalculationHistoryItem) = dao.insert(item)
    suspend fun deleteHistory(item: CalculationHistoryItem) = dao.delete(item)
}