package com.example.firebaseproject.presentation.images

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.platform.LocalContext
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.firebaseproject.data.storage.CameraImageStorage
import com.google.firebase.auth.FirebaseAuth

import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
//    onLogout: () -> Unit
    onAddClientClick: () -> Unit
) {
    val vm: ImagesViewModel = hiltViewModel()
//    val images  by vm.images.collectAsState()
    val clients by vm.clients.collectAsState()

    val uid = FirebaseAuth.getInstance().currentUser?.uid?:""

    val context = LocalContext.current
    val cameraStorage = remember { CameraImageStorage(context) }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val path = cameraStorage.latestLocalPath()
                if (path != null) vm.onCameraSaved(uid, path)
            }
        }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddClientClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Client"
                )
            }
        }
    ) { padding ->

        Column(Modifier.fillMaxSize().padding(12.dp)) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val uri = cameraStorage.createImageUri()
                    takePictureLauncher.launch(uri)
                }) { Text("Take photo") }

                OutlinedButton(onClick = { vm.sync(uid) }) { Text("Sync") }

//                OutlinedButton(onClick = onAddClientClick) {
//                    Text("Add Client")
//                }

//            OutlinedButton(onClick = onLogout) { Text("Logout") }
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(clients) { client ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                Text(
                                    text = client.fullName,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Age: ${client.age}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}
