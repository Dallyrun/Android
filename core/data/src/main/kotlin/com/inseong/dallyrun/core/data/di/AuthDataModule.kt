package com.inseong.dallyrun.core.data.di

import com.inseong.dallyrun.core.data.auth.AuthRepositoryImpl
import com.inseong.dallyrun.core.data.auth.TokenManagerImpl
import com.inseong.dallyrun.core.domain.auth.AuthRepository
import com.inseong.dallyrun.core.network.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthDataModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: TokenManagerImpl): TokenProvider

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
