package com.example.firebaseproject.domain.repository

import com.example.firebaseproject.domain.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientsRepository {

    suspend fun addClient(client: Client,  galleryLocalPaths: List<String>)

    fun enqueueSync(clientId: String )

    fun getAllClients(): Flow<List<Client>>
}