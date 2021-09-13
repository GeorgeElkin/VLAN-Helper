package com.example.vlanhelper.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vlanhelper.data.LocalDataSource
import com.example.vlanhelper.model.Address
import com.example.vlanhelper.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val localDataSource: LocalDataSource): ViewModel() {

    private val _state: MutableLiveData<Result<String>> = MutableLiveData()
    val state: LiveData<Result<String>>
        get() = _state

    fun getPushed(buildingAddress: String){
        _state.value = Result.loading()
        CoroutineScope(Dispatchers.IO).launch {
            val result = localDataSource.getVlan(buildingAddress)
            _state.postValue(result)
        }
    }

    fun deletePushed(address: Address){
        _state.value = Result.loading()
        CoroutineScope(Dispatchers.IO).launch {
            val result = localDataSource.deleteAddress(address)
            if(result > 0){
                _state.postValue(Result.success(""))
            } else if(result == 0){
                _state.postValue(Result.error(""))
            }
        }
    }
}