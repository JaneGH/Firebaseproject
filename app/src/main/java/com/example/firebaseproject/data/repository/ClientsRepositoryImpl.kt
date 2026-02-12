package com.example.firebaseproject.data.repository

import android.net.Uri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.firebaseproject.data.local.dao.ClientDao
import com.example.firebaseproject.data.local.entity.ClientEntity
import com.example.firebaseproject.data.SyncStatus
import com.example.firebaseproject.data.local.dao.UserImagesDao
import com.example.firebaseproject.data.local.entity.UserImageEntity
import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.domain.repository.ClientsRepository
import com.example.firebaseproject.worker.ClientsSyncWorker
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ClientsRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val photoDao: UserImagesDao,
    private val workManager: WorkManager
) : ClientsRepository {

    override suspend fun addClient(client: Client,   galleryLocalPaths: List<String>) {

        clientDao.insert(
            ClientEntity(
                id = client.id,
                fullName = client.fullName,
                age = client.age,
                address = client.address,
                avatarUri = client.avatarUri,
                createdAt = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING
            )
        )
        insertClientPhotos(client.id, galleryLocalPaths)

        enqueueSync(client.id)
    }

    private suspend fun insertClientPhotos(
        clientId: String,
        localPaths: List<String>
    ) {
        val now = System.currentTimeMillis()

        val photos = localPaths.map {
            UserImageEntity(
                clientId = clientId,
                localPath = it,
                remoteUrl = "",
                syncStatus = SyncStatus.PENDING,
                createdAt = now,
                userId =  FirebaseAuth.getInstance().currentUser?.uid?:""
            )
        }

        photoDao.insertAll(photos)
    }

    override fun enqueueSync(clientId: String) {
        val request = OneTimeWorkRequestBuilder<ClientsSyncWorker>()
            .setInputData(workDataOf("clientId" to clientId))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(request)
    }
}
