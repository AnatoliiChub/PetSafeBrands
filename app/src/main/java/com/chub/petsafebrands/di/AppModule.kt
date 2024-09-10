package com.chub.petsafebrands.di

import android.content.Context
import android.content.res.AssetManager
import com.chub.petsafebrands.BuildConfig
import com.chub.petsafebrands.config.DebugConfig
import com.chub.petsafebrands.data.FixerApi
import com.chub.petsafebrands.data.debug.FakeFxRatesRepository
import com.chub.petsafebrands.data.debug.FakeResponseInterceptor
import com.chub.petsafebrands.data.debug.JsonReader
import com.chub.petsafebrands.data.debug.MockApiService
import com.chub.petsafebrands.data.repo.FixerFxRatesRepository
import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.retrofit.FixerCallAdapterFactory
import com.chub.petsafebrands.di.qualifiers.WorkDispatcher
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFakeResponseInterceptor(jsonReader: JsonReader): FakeResponseInterceptor {
        return FakeResponseInterceptor(jsonReader)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(fakeResponseInterceptor: FakeResponseInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.MOCK_API) {
                this.addInterceptor(fakeResponseInterceptor)
            } else {
                val loggingInterceptor = okhttp3.logging.HttpLoggingInterceptor()
                loggingInterceptor.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
                val host = "data.fixer.io"
                val pin = "sha256/4q3CllWNIW9XcXbtJFQCHjSrnqH8Wak0jbwMhRkGP0U="
                val pinner = okhttp3.CertificatePinner.Builder().add(host, pin).build()
                this.connectionSpecs(listOf(okhttp3.ConnectionSpec.MODERN_TLS))
                    .certificatePinner(pinner)
                    .addInterceptor(loggingInterceptor)
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideMockApiService(client: OkHttpClient, callAdapterFactory: FixerCallAdapterFactory): MockApiService {
        return Retrofit.Builder()
            .baseUrl(DebugConfig.BASE_URL)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MockApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFixerApiService(client: OkHttpClient, callAdapterFactory: FixerCallAdapterFactory): FixerApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(FixerApi::class.java)
    }

    @Provides
    fun provideFxRatesRepository(
        fixerApi: FixerApi,
        mockApiService: MockApiService,
    ): FxRatesRepository {
        return if (BuildConfig.MOCK_API) {
            FakeFxRatesRepository(mockApiService)
        } else {
            FixerFxRatesRepository(fixerApi)
        }
    }

    @Provides
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @WorkDispatcher
    fun provideWorkDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }
}