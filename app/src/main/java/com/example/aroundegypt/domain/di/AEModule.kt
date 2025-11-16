package com.example.aroundegypt.domain.di

import com.example.aroundegypt.data.datasource.AERemoteDSImpl
import com.example.aroundegypt.data.repo.AERepositoryImpl
import com.example.aroundegypt.domain.datasource.AERemoteDS
import com.example.aroundegypt.domain.repo.AERepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//
// Created by Dina Mounib on 14/11/2025.
//
@Module
@InstallIn(SingletonComponent::class)
class AEModule {

    @Provides
    fun providesAERepositoryImpl(aerRepositoryImpl: AERepositoryImpl): AERepository =
        aerRepositoryImpl

    @Provides
    fun providesAEDataSourceImpl(aeRemoteDSImpl: AERemoteDSImpl): AERemoteDS =
        aeRemoteDSImpl
}