package com.example.vlanhelper.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vlanhelper.data.LocalDataSource
import com.example.vlanhelper.ui.main.MainViewModel
import com.example.vlanhelper.ui.adding.AddingViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AddingViewModel::class.java) -> {
                AddingViewModel(LocalDataSource(applicationContext)) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(LocalDataSource(applicationContext)) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown class name")
            }
        }
    }
}