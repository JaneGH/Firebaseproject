package com.example.firebaseproject.data.repository

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.firebaseproject.data.local.dao.ClientDao
import com.example.firebaseproject.data.local.entity.ClientEntity
import com.example.firebaseproject.domain.SyncStatus
import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.domain.repository.ClientsRepository
import com.example.firebaseproject.worker.ClientsSyncWorker
import javax.inject.Inject

class ClientsRepositoryImpl @Inject constructor(
    private val dao: ClientDao,
    private val workManager: WorkManager
) : ClientsRepository {

    override suspend fun addClient(client: Client) {

        dao.insert(
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

        enqueueSync(client.id)
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
