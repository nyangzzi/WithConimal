package com.nyangzzi.withconimal.data.di

import com.nyangzzi.withconimal.data.repository.NetworkRepositoryImpl
import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryProvideModule {
    @Singleton
    @Provides
    fun bindNetworkRepository(
    ): NetworkRepository {
        return NetworkRepositoryImpl()
    }


}