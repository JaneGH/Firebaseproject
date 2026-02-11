package com.example.firebaseproject.domain.usecase

import com.example.firebaseproject.domain.repository.ImagesRepository
import javax.inject.Inject

class EnqueueSyncUseCase @Inject constructor(
    private val repo: ImagesRepository
) {
    operator fun invoke(uid: String) = repo.enqueueSync(uid)
}