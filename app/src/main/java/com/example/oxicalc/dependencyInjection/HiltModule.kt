// HiltModule.kt - Corrected
package com.example.oxicalc.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.oxicalc.model.AppDatabase // Assuming you have an AppDatabase class
import com.example.oxicalc.model.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Provides
    @Singleton // To ensure only one instance of the database and DAO
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "history_database" //  name for database
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase): HistoryDao { // Provides the DAO
        return appDatabase.historyDao()
    }
}