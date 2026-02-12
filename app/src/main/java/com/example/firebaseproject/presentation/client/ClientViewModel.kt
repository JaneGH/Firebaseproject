package com.example.firebaseproject.presentation.client

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseproject.data.storage.ImageStorage
import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.domain.repository.ClientsRepository
import com.example.firebaseproject.domain.usecase.GetClientByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ClientViewModel @Inject constructor(
    private val imageStorage: ImageStorage,
    private  val getClientByIdUseCase: GetClientByIdUseCase,
    private val repository: ClientsRepository): ViewModel() {

    private val _uiState = MutableStateFlow<ClientUiState>(ClientUiState.Idle)
    val uiState: StateFlow<ClientUiState> = _uiState

    fun getClient(clientId: String) {
        viewModelScope.launch {

            _uiState.value = ClientUiState.Loading

            getClientByIdUseCase(clientId)
                .catch { e ->
                    _uiState.value =
                        ClientUiState.Error(e.message ?: "Unknown error")
                }
                .collect { client ->

                    if (client != null) {
                        _uiState.value =
                            ClientUiState.Success(client)
                    } else {
                        _uiState.value =
                            ClientUiState.Error("Client not found")
                    }
                }
        }
    }

    fun saveClient(
        fullName: String,
        age: Int,
        address: String,
        avatarUri: Uri?,
        galleryUris: List<Uri>
    ) {
        viewModelScope.launch {

            val savedPath = avatarUri?.let {
                imageStorage.saveImage(it.toString())
            }

            val client = Client(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                age = age,
                address = address,
                avatarUri = savedPath?:""
            )

            val savedGalleryPaths = galleryUris.mapNotNull { uri ->
                try {
                    imageStorage.saveImage(uri.toString())
                } catch (e: Exception) {
                    null
                }
            }

            repository.addClient(
                client = client,
                galleryLocalPaths = savedGalleryPaths
            )
        }
    }
}