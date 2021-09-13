package com.example.vlanhelper.data

import android.content.Context
import androidx.room.Room

object RoomBuilder {

    fun provideAppDatabase(applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "Posts Database")
            .fallbackToDestructiveMigration()
            .build()
}