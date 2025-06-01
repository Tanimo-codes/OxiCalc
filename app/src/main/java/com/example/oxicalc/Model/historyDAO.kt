package com.example.oxicalc.Model

import androidx.room.*
import com.example.oxicalc.Model.CalculationHistoryItem
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
