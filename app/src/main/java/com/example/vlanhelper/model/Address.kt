package com.example.vlanhelper.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    @PrimaryKey
    val address: String,
    val vlan: String
)