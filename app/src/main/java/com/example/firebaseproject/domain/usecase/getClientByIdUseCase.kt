package com.example.firebaseproject.domain.usecase

import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientByIdUseCase @Inject constructor(private  val repository: ClientsRepository) {
    operator fun invoke(clientId: String): Flow<Client?> {
        return repository.getClientById(clientId)
    }
}