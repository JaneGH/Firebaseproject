package com.example.firebaseproject.domain.usecase

import com.example.firebaseproject.domain.repository.ImagesRepository
import javax.inject.Inject


class AddImageFromPathUseCase @Inject constructor(
    private val repo: ImagesRepository
) {
    suspend operator fun invoke(uid: String, localPath: String) =
        repo.addImageFromLocalPath(uid, localPath)
}