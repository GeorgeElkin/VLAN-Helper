package com.example.vlanhelper.data

import androidx.room.*
import com.example.vlanhelper.model.Address

@Dao
interface AddressDao {

    @Query("SELECT vlan FROM address WHERE address = :address")
    fun getVlan(address: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address): Long

    @Delete
    fun deleteAddress(address: Address): Int

    @Query("SELECT COUNT(`vlan`) FROM address")
    fun getRowCount(): Int?
}