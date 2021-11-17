package com.shushant.messengercompose.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.model.Stories

@Dao
interface MessengerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(movies: List<Data>)

    @Update
    suspend fun updateStories(movie: Data)

    @Query("SELECT * FROM Data WHERE id = :id_")
    suspend fun getStory(id_: Long): Data

    @Query("SELECT * FROM Data WHERE page = :page_")
    suspend fun getStoryList(page_: Int): List<Data>
}
