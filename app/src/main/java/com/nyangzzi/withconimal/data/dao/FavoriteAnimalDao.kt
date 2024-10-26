package com.nyangzzi.withconimal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nyangzzi.withconimal.domain.model.db.FavoriteAnimal
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimalDao {
    @Insert
    suspend fun insert(animal: FavoriteAnimal)

    @Query("SELECT * FROM favorite_animals")
    fun getAllAnimals(): Flow<List<FavoriteAnimal>>

    @Update
    suspend fun update(animal: FavoriteAnimal)

    @Delete
    suspend fun delete(animal: FavoriteAnimal)
}