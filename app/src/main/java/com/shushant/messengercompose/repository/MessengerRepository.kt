
package com.shushant.messengercompose.repository

import androidx.annotation.WorkerThread
import com.shushant.messengercompose.network.service.MessengerService
import com.shushant.messengercompose.persistence.MessengerDao
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber

class MessengerRepository constructor(
  private val discoverService: MessengerService,
  private val messengerDao: MessengerDao,
) : Repository {

  init {
    Timber.d("Injection DiscoverRepository")
  }

  @WorkerThread
  fun loadUsers(page: Int, success: () -> Unit, error: () -> Unit) = flow {
    /*var movies = messengerDao.getStoryList(page)
    if (movies.isEmpty()) {*/
      val response = discoverService.getUsers(page)
      response.suspendOnSuccess {
        val movies = data.data?: emptyList()
        movies.forEachIndexed { index, data ->
          if (index == 0){
            data.picture="https://i.ibb.co/1959D7F/Your-Story.png"
            data.firstName="Your Story"
            data.lastName =""
          }
          data.page = page
        }
        messengerDao.insertStories(movies)
        emit(movies)
      }.onError {
        error()
      }.onException { error() }
    //} /*else {
     // emit(movies)
    //}*/
  }.onCompletion { success() }.flowOn(Dispatchers.IO)
}
