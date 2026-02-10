package com.example.firebaseproject.hilt

import android.content.Context
import com.example.firebaseproject.data.remote.AuthRepositoryImpl
import com.example.firebaseproject.data.remote.GoogleAuthManager
import com.example.firebaseproject.domain.AuthRepository
import com.example.firebaseproject.domain.EmailLoginUseCase
import com.example.firebaseproject.domain.GoogleSignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(
        googleAuthManager: GoogleAuthManager
    ): AuthRepository {
        return AuthRepositoryImpl(googleAuthManager)
    }

    @Provides
    fun provideGoogleSignInUseCase(
        repository: AuthRepository
    ) = GoogleSignInUseCase(repository)

    @Provides
    fun provideEmailLoginUseCase(
        repository: AuthRepository
    ) = EmailLoginUseCase(repository)
}
