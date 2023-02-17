package com.mhmdawad.radu.di

import android.content.Context
import com.mhmdawad.radu.utils.AudioHelper
import com.mhmdawad.radu.utils.AudioHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun bindAudioHelper(audioHelperImpl: AudioHelperImpl): AudioHelper

}