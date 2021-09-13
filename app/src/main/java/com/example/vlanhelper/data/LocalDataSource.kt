package com.example.vlanhelper.data

import android.content.Context
import com.example.vlanhelper.model.Address
import com.example.vlanhelper.model.Result

class LocalDataSource(applicationContext: Context) {

    private val addressDao: AddressDao = RoomBuilder.provideAppDatabase(applicationContext).addressDao()

    fun getVlan(address: String): Result<String>{
        val vlan = addressDao.getVlan(address)
        if (vlan.isNullOrEmpty()){
            return Result.error("Address doesn't exist, or VLAN is null")
        }
        return Result.success(vlan)
    }

    fun addAddress(address: Address) = addressDao.insertAddress(address)

    fun deleteAddress(address: Address) = addressDao.deleteAddress(address)

    fun getCount() = addressDao.getRowCount()
}