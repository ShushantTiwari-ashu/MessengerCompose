
package com.shushant.chatties.di

import android.content.Context
import androidx.room.Room
import com.shushant.chatties.persistence.AppDatabase
import com.shushant.chatties.persistence.MessengerDao
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
      .build()
  }

  @Provides
  @Singleton
  fun provideMovieDao(appDatabase: AppDatabase): MessengerDao {
    return appDatabase.movieDao()
  }
}
