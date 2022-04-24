package com.shushant.messengercompose.di

import android.content.Context
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.shushant.messengercompose.network.Api
import com.shushant.messengercompose.network.RequestInterceptor
import com.shushant.messengercompose.network.service.MessengerService
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.okHttpClient(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okhHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okhHttpClient)
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MessengerService {
        return retrofit.create(MessengerService::class.java)
    }
}
