package com.shushant.messengercompose.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.model.Stories

@Database(
  entities = [Data::class],
  version = 4, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun movieDao(): MessengerDao
}
