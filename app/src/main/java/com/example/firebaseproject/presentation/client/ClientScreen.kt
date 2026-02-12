package com.example.firebaseproject.presentation.client

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.firebaseproject.R
import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.domain.usecase.GetClientByIdUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


@Composable
fun ClientScreen(
    clientId: String?,
    isEditMode: Boolean = false,
    onSaveClick: (fullName: String, age: Int, address: String, avatarUri: Uri?, galleryUris: List<Uri>) -> Unit
) {

    val viewModel: ClientViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(clientId) {
        clientId?.let {
            viewModel.getClient(it)
        }
    }

    var fullName by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var selectedGalleryUris by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }

    var selectionMode by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(setOf<Uri>()) }

    LaunchedEffect(uiState) {
        if (uiState is ClientUiState.Success) {
            val client = (uiState as ClientUiState.Success).client
            fullName = client.fullName
            age = client.age.toString()
            address = client.address
        }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            selectedImageUri = uri
        }

    val multiImagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(10)
        ) { uris: List<Uri> ->
            selectedGalleryUris =
                (selectedGalleryUris + uris).distinct().take(10)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Surface(
                shape = CircleShape,
                shadowElevation = 8.dp,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = isEditMode) {
                        imagePickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
            ) {
                when {
                    selectedImageUri != null -> {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isEditMode) {

            Text(
                text = "Client Information",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onSaveClick(
                        fullName,
                        age.toIntOrNull() ?: 0,
                        address,
                        selectedImageUri,
                        selectedGalleryUris

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Save Client")
            }

        } else {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    HorizontalDivider()

                    Text(
                        text = "Age",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = age,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Address",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                if (selectedGalleryUris.size < 10) {
                    multiImagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Photos (${selectedGalleryUris.size}/10)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectionMode) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "${selectedItems.size} selected",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row {

                    TextButton(
                        onClick = {
                            selectedGalleryUris =
                                selectedGalleryUris.filterNot {
                                    selectedItems.contains(it)
                                }

                            selectedItems = emptySet()
                            selectionMode = false
                        }
                    ) {
                        Text(
                            "Delete",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    TextButton(
                        onClick = {
                            selectedItems = emptySet()
                            selectionMode = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            items(selectedGalleryUris) { uri ->
                val isSelected = selectedItems.contains(uri)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .combinedClickable(
                            onClick = {
                                if (selectionMode) {
                                    selectedItems =
                                        if (isSelected)
                                            selectedItems - uri
                                        else
                                            selectedItems + uri
                                }
                            },
                            onLongClick = {
                                selectionMode = true
                                selectedItems = selectedItems + uri
                            }
                        )
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }
        }
    }

}
