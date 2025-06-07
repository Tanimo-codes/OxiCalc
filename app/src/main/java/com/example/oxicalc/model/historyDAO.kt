package com.example.oxicalc.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistory(): Flow<List<CalculationHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CalculationHistoryItem)

    @Delete
    suspend fun delete(item: CalculationHistoryItem)
}
