package com.nexlesoft.ket.di.modules

import android.content.Context
import com.nexlesoft.ket.data.local.LocalDataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataService(@ApplicationContext context: Context): LocalDataService {
        return LocalDataService(context)
    }
}