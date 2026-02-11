package com.example.firebaseproject.hilt

import com.example.firebaseproject.data.repository.AuthRepositoryImpl
import com.example.firebaseproject.data.remote.GoogleAuthManager
import com.example.firebaseproject.domain.repository.AuthRepository
import com.example.firebaseproject.domain.usecase.EmailLoginUseCase
import com.example.firebaseproject.domain.usecase.GoogleSignInUseCase
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
