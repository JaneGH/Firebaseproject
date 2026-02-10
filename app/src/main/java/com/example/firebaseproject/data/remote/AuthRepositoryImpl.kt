package com.example.firebaseproject.data.remote

import com.example.firebaseproject.domain.AuthRepository
import com.example.firebaseproject.domain.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val googleAuthManager: GoogleAuthManager
) : AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    override suspend fun signInWithGoogle(): Result<User> {

        val token = googleAuthManager.getGoogleToken()
            ?: return Result.failure(Exception("Token is null"))

        val credential = GoogleAuthProvider.getCredential(token, null)

        return try {
            val result = auth.signInWithCredential(credential).await()

            val firebaseUser = result.user
                ?: return Result.failure<User>(Exception("User is null"))

            val user = firebaseUser.toDomain()

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
