
package com.shushant.chatties.di

import com.shushant.chatties.network.service.MessengerService
import com.shushant.chatties.persistence.MessengerDao
import com.shushant.chatties.repository.MessengerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

  @Provides
  @ViewModelScoped
  fun provideDiscoverRepository(
    discoverService: MessengerService,
    messengerDao: MessengerDao,
  ): MessengerRepository {
    return MessengerRepository(discoverService, messengerDao)
  }
}
