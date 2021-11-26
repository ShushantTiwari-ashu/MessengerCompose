package com.shushant.messengercompose.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.model.Messages

@Dao
interface MessengerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Messages)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(message: List<Data>)

    @Update
    suspend fun updateMessage(message: Messages)

    @Query("SELECT * FROM Messages WHERE id = :id_")
    suspend fun getMessage(id_: String): Messages

    @Query("SELECT * FROM Messages WHERE id =:id_ ORDER BY date DESC ")
    suspend fun getMessageList(id_: String): List<Messages>

}
