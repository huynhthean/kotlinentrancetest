package com.nexlesoft.ket.di.modules

import com.nexlesoft.ket.data.repository.IRepository
import com.nexlesoft.ket.data.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesRepository(repository: Repository): IRepository
}