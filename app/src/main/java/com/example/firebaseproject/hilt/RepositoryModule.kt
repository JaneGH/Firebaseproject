package com.example.firebaseproject.hilt

import com.example.firebaseproject.data.repository.ImagesRepositoryImpl
import com.example.firebaseproject.data.repository.UserRepositoryImpl
import com.example.firebaseproject.domain.repository.ImagesRepository

import com.example.firebaseproject.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds @Singleton
    abstract fun provideImagesRepo(
        impl: ImagesRepositoryImpl
    ): ImagesRepository
}
