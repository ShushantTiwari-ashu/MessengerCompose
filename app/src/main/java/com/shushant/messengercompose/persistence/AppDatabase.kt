package com.shushant.messengercompose.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.model.Messages

@Database(
  entities = [(Data::class), (Messages::class)],
  version = 13, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun movieDao(): MessengerDao
}
