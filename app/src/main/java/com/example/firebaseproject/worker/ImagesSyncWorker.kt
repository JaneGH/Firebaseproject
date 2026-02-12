package com.example.firebaseproject.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.firebaseproject.data.local.dao.UserImagesDao
import com.example.firebaseproject.data.remote.FirebaseImagesDataSource
import com.example.firebaseproject.data.SyncStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ImagesSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dao: UserImagesDao,
    private val remote: FirebaseImagesDataSource
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val uid = inputData.getString("uid") ?: return Result.failure()

        val pending = dao.getPending(uid)
        for (img in pending) {
            try {
                dao.updateStatus(img.id, SyncStatus.UPLOADING)

                val url = remote.uploadToStorage(
                    uid = uid,
                    imageId = img.id,
                    localPath = img.localPath
                )

                remote.saveMetadataToRealtimeDb(
                    uid = uid,
                    imageId = img.id,
                    remoteUrl = url,
                    createdAt = img.createdAt
                )

                dao.markSynced(img.id, url)
            } catch (e: Exception) {
                dao.markFailed(img.id)
                return Result.retry()
            }
        }
        return Result.success()
    }
}
