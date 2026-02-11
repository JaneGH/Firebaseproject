package com.example.firebaseproject.data.remote

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class FirebaseImagesDataSource @Inject constructor(
    private val storage: FirebaseStorage,
    private val realtimeDb: FirebaseDatabase
) {
    suspend fun uploadToStorage(uid: String, imageId: Long, localPath: String): String {
        val ref = storage.reference.child("users/$uid/images/$imageId.jpg")
        val uri = Uri.fromFile(File(localPath))

        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun saveMetadataToRealtimeDb(uid: String, imageId: Long, remoteUrl: String, createdAt: Long) {
        val ref = realtimeDb.reference
            .child("users")
            .child(uid)
            .child("images")
            .child(imageId.toString())

        val payload = mapOf(
            "imageId" to imageId,
            "remoteUrl" to remoteUrl,
            "createdAt" to createdAt
        )

        ref.setValue(payload).await()
    }
}
