package com.example.oxicalc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class CalculationHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val formula: String,
    val targetElement: String,
    val oxidationState: Int,
    val solution: String
)
