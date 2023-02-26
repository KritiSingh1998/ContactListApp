package com.example.contactlistapp.di

import com.example.contactlistapp.network.APIClient
import com.example.contactlistapp.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideContactRepository(apiClient: APIClient): ContactRepository {
        return ContactRepository(apiClient)
    }
}