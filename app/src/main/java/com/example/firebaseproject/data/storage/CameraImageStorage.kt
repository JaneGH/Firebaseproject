package com.example.firebaseproject.data.storage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class CameraImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var latestFile: File? = null

    fun createImageUri(): Uri {
        val dir = File(context.filesDir, "camera_images").apply { mkdirs() }
        val file = File(dir, "${UUID.randomUUID()}.jpg")
        file.createNewFile()
        latestFile = file

        Log.d("CameraPath", file.absolutePath)

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun latestLocalPath(): String? = latestFile?.absolutePath
}
