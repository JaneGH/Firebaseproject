package com.example.firebaseproject.presentation.images

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.platform.LocalContext
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.firebaseproject.data.storage.CameraImageStorage
import com.google.firebase.auth.FirebaseAuth

import java.io.File

@Composable
fun ImagesScreen(
//    onLogout: () -> Unit
    onAddClientClick: () -> Unit
) {
    val vm: ImagesViewModel = hiltViewModel()
    val images by vm.images.collectAsState()

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

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Column (Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val uri = cameraStorage.createImageUri()
                takePictureLauncher.launch(uri)
            }) { Text("Take photo") }

            OutlinedButton(onClick = { vm.sync(uid) }) { Text("Sync") }

            OutlinedButton(onClick = onAddClientClick) {
                Text("Add Client")
            }

//            OutlinedButton(onClick = onLogout) { Text("Logout") }
        }

        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(images) { img ->
                val model = img.remoteUrl ?: File(img.localPath)
                AsyncImage(
                    model = model,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}
