package com.example.firebaseproject.hilt

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.firebaseproject.data.local.AppDatabase
import com.example.firebaseproject.data.local.dao.UserImagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "upload_images_db").build()

    @Provides
    fun provideDao(db: AppDatabase): UserImagesDao = db.userImagesDao()

    @Provides @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}