
package com.shushant.messengercompose.di

import com.shushant.messengercompose.network.service.MessengerService
import com.shushant.messengercompose.persistence.MessengerDao
import com.shushant.messengercompose.repository.MessengerRepository
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
