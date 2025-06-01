// HiltModule.kt - Corrected
package com.example.oxicalc.DependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.oxicalc.Model.AppDatabase // Assuming you have an AppDatabase class
import com.example.oxicalc.Model.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule { // It's common to use an object for Hilt modules if all @Provides methods are static

    @Provides
    @Singleton // To ensure only one instance of the database and DAO
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "history_database" // Choose a name for your database
        ).build()
    }

    @Provides
    @Singleton // Often DAOs are also singletons tied to the database instance
    fun provideDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.historyDao()
    }
}