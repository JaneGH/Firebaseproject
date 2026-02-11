package com.example.firebaseproject.data.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ImageStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageStorage {

    override suspend fun saveImage(uri: String): String =
        withContext(Dispatchers.IO) {

            val input = context.contentResolver
                .openInputStream(Uri.parse(uri))
                ?: throw IllegalArgumentException("Cannot open URI")

            val dir = File(context.filesDir, "images")
            dir.mkdirs()

            val file = File(dir, "${UUID.randomUUID()}.jpg")

            input.use { inputStream ->
                file.outputStream().use { output ->
                    inputStream.copyTo(output)
                }
            }

            file.absolutePath
        }
}
