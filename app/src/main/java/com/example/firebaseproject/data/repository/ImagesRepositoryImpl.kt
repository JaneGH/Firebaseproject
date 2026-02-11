package com.example.firebaseproject.data.repository


import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.firebaseproject.data.local.dao.UserImagesDao
import com.example.firebaseproject.data.local.entity.UserImageEntity
import com.example.firebaseproject.data.mappers.toDomain
import com.example.firebaseproject.domain.SyncStatus
import com.example.firebaseproject.domain.repository.ImagesRepository
import com.example.firebaseproject.worker.ImagesSyncWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.firebaseproject.domain.model.UserImage

class ImagesRepositoryImpl @Inject constructor(
    private val dao: UserImagesDao,
    private val workManager: WorkManager
) : ImagesRepository {

    override fun observeImages(uid: String): Flow<List<UserImage>> =
        dao.observe(uid).map { list -> list.map { it.toDomain() } }

    override suspend fun addImageFromLocalPath(uid: String, localPath: String) {
        dao.insert(
            UserImageEntity(
                userId = uid,
                localPath = localPath,
                remoteUrl = null,
                syncStatus = SyncStatus.PENDING,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override fun enqueueSync(uid: String) {
        val request = OneTimeWorkRequestBuilder<ImagesSyncWorker>()
            .setInputData(workDataOf("uid" to uid))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            "sync_images_$uid",
            ExistingWorkPolicy.KEEP,
            request
        )
    }
}
