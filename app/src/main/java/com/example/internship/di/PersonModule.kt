package com.example.internship.di

import com.example.internship.person.utility.CustomLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PersonModule {

    @Provides
    @Singleton
    fun provideCustomLogger(): CustomLogger.Companion = CustomLogger.Companion

}