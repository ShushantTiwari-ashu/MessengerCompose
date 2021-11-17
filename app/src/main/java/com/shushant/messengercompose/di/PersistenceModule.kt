
package com.shushant.messengercompose.di

import android.content.Context
import androidx.room.Room
import com.shushant.messengercompose.persistence.AppDatabase
import com.shushant.messengercompose.persistence.MessengerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

  @Provides
  @Singleton
  fun provideRoomDataBase(@ApplicationContext context: Context): AppDatabase {
    return Room
      .databaseBuilder(context, AppDatabase::class.java, "MessengerCompose.db")
      .fallbackToDestructiveMigration()
      .allowMainThreadQueries()
      .build()
  }

  @Provides
  @Singleton
  fun provideMovieDao(appDatabase: AppDatabase): MessengerDao {
    return appDatabase.movieDao()
  }
}
