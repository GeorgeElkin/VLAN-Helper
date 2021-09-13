package com.example.vlanhelper.ui.adding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vlanhelper.data.LocalDataSource
import com.example.vlanhelper.model.Address
import com.example.vlanhelper.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddingViewModel(private val localDataSource: LocalDataSource): ViewModel() {

    private val _state: MutableLiveData<Result<String>> = MutableLiveData()
    val state: LiveData<Result<String>>
        get() = _state

    private val _warning: MutableLiveData<Boolean> = MutableLiveData(false)
    val warning: LiveData<Boolean>
        get() = _warning

    fun addPushed(buildingAddress: String, vlan: String){
        val address = Address(buildingAddress, vlan)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            _state.postValue(Result.loading())
            if (isExist(buildingAddress)){
                _warning.postValue(true)
            } else {
                writeAddress(address)
                _state.postValue(Result.success(null))
//                if(result.isNullOrEmpty())
//                {
//                    _state.postValue(Result.error("Unknown Error"))
//                } else {
//                    _state.postValue(Result.success(null))
//                }
            }
        }
    }

    private fun isExist(address: String): Boolean{
        val result = localDataSource.getVlan(address)
        if (result.status == Result.Status.SUCCESS){
            return true
        }
        return false
    }

    private fun writeAddress(address: Address): Long{
        return localDataSource.addAddress(address)
    }

//    private fun showWarning(){
//        _warning.postValue(true)
//    }

    fun noClicked(){

    }

    fun yesClicked(buildingAddress: String, vlan: String){
        val address = Address(buildingAddress, vlan)
        CoroutineScope(Dispatchers.IO).launch {
            writeAddress(address)
            _state.postValue(Result.success(null))
        }
    }
}