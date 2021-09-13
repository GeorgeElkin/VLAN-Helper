package com.example.vlanhelper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vlanhelper.model.Address

@Database(entities = [Address::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun addressDao(): AddressDao
}