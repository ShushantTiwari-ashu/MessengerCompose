package com.shushant.chatties.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shushant.chatties.model.Data
import com.shushant.chatties.model.Messages

@Database(
  entities = [(Data::class), (Messages::class)],
  version = 13, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun movieDao(): MessengerDao
}
