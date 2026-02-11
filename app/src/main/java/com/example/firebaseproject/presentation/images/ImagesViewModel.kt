package com.example.firebaseproject.presentation.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseproject.domain.model.UserImage
import com.example.firebaseproject.domain.usecase.AddImageFromPathUseCase
import com.example.firebaseproject.domain.usecase.EnqueueSyncUseCase
import com.example.firebaseproject.domain.usecase.ObserveImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val observeImages: ObserveImagesUseCase,
    private val addFromPath: AddImageFromPathUseCase,
    private val enqueueSync: EnqueueSyncUseCase
) : ViewModel() {

    private val uidFlow = MutableStateFlow<String?>(null)

    val images: StateFlow<List<UserImage>> =
        uidFlow.filterNotNull()
            .flatMapLatest { observeImages(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setUid(uid: String) {
        uidFlow.value = uid
    }

    fun onCameraSaved(uid: String, localPath: String) {
        viewModelScope.launch {
            addFromPath(uid, localPath)
            enqueueSync(uid)
        }
    }

    fun sync(uid: String) {
        enqueueSync(uid)
    }
}
