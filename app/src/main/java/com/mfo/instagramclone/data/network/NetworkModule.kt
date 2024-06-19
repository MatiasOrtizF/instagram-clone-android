package com.mfo.instagramclone.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mfo.instagramclone.data.RepositoryImpl
import com.mfo.instagramclone.domain.Repository
import com.mfo.instagramclone.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideInstagramCloneApiService(retrofit: Retrofit): InstagramCloneApiService {
        return retrofit.create(InstagramCloneApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(teamBalancerApiService: InstagramCloneApiService): Repository {
        return RepositoryImpl(teamBalancerApiService)
    }
}