package com.nyangzzi.withconimal.data.di

import android.content.Context
import androidx.room.Room
import com.nyangzzi.withconimal.data.dao.FavoriteAnimalDao
import com.nyangzzi.withconimal.data.dao.FavoriteAnimalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FavoriteAnimalDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteAnimalDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideFavoriteAnimalDao(database: FavoriteAnimalDatabase): FavoriteAnimalDao {
        return database.favoriteAnimalDao()
    }
}