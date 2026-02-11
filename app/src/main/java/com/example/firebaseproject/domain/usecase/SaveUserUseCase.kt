package com.example.firebaseproject.domain.usecase

import com.example.firebaseproject.domain.model.User
import com.example.firebaseproject.domain.repository.UserRepository
import javax.inject.Inject


class SaveUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}