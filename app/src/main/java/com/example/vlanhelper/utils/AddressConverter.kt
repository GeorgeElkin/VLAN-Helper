package com.example.vlanhelper.utils

import com.example.vlanhelper.model.Address

object AddressConverter {

    fun convertAddressToString(streetType: String, street: String, house: String, building: String): String {
        return "$streetType;$street;$house;$building".toLowerCase()
    }

    fun convertStringToList(address: String): List<String> {
        return address.split(";")
    }

//    fun convertStreetType(streetType: String): Int{
//
//    }
//
//    fun convertStreetType(streetType: Int): String{
//        return
//    }

}