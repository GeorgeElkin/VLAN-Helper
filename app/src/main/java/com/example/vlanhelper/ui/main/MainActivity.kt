package com.example.vlanhelper.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vlanhelper.R
import com.example.vlanhelper.model.Address
import com.example.vlanhelper.model.Result
import com.example.vlanhelper.ui.ViewModelFactory
import com.example.vlanhelper.ui.adding.AddActivity
import com.example.vlanhelper.utils.AddressConverter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var streetEditText: EditText
    private lateinit var houseEditText: EditText
    private lateinit var buildingEditText: EditText
    private lateinit var vlanEditText: TextView
    private lateinit var getButton: Button

    //    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var addButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUi()
        setUpAdapter()
        setUpObservers()
    }

    private fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(this.applicationContext)
        ).get(MainViewModel::class.java)
        streetEditText = findViewById(R.id.mainStreetEditText)
        houseEditText = findViewById(R.id.mainHouseEditText)
        buildingEditText = findViewById(R.id.mainBuildingEditText)
        vlanEditText = findViewById(R.id.mainVlanEditText)

        progressBar = findViewById(R.id.mainProgressBar)
        progressBar.visibility = View.INVISIBLE

        spinner = findViewById(R.id.mainStreetTypeSpinner)

        getButton = findViewById(R.id.mainGetButton)
//        editButton = findViewById(R.id.mainEditButton)
        deleteButton = findViewById(R.id.mainDeleteButton)
        addButton = findViewById(R.id.addButton)
    }

    private fun setUpAdapter() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.street_type,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        getButton.setOnClickListener {
            val buildingAddress = getAddress()
            if (buildingAddress != null){
                viewModel.getPushed(buildingAddress)
            }
        }

        deleteButton.setOnClickListener {
            val buildingAddress = getAddress()
            if(buildingAddress != null){
                viewModel.deletePushed(Address(buildingAddress, vlanEditText.text.toString()))
            }
        }
    }

    private fun setUpObservers() {
        viewModel.state.observe(this) {
            when (it.status) {
                Result.Status.LOADING -> {
                    showProgress(true)
                    showButtons(false)
                }
                Result.Status.ERROR -> {
                    showProgress(false)
                    showButtons(true)
                    showError()
                }
                Result.Status.SUCCESS -> {
                    showProgress(false)
                    showButtons(true)
                    vlanEditText.text = it.data
                }
            }
        }
    }

    private fun showProgress(loading: Boolean) {
        if (loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showButtons(enabled: Boolean) {
        getButton.isEnabled = enabled
        deleteButton.isEnabled = enabled
    }

    private fun showError() {
        val toast =
            Toast.makeText(this, "Записи с таким адресом нет в базе", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun getAddress(): String? {
        if(streetEditText.text.toString().isEmpty() || houseEditText.text.toString().isEmpty() || buildingEditText.text.toString().isEmpty()){
            showRequirement()
            return null
        }
        return AddressConverter.convertAddressToString(
            spinner.selectedItem.toString(),
            streetEditText.text.toString(),
            houseEditText.text.toString(),
            buildingEditText.text.toString()
        )
    }

    private fun showRequirement(){
        val toast = Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}