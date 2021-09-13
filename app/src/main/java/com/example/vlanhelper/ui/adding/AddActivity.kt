package com.example.vlanhelper.ui.adding

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
import com.example.vlanhelper.ui.WarningDialog
import com.example.vlanhelper.ui.ErrorDialog
import com.example.vlanhelper.utils.AddressConverter

class AddActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var streetEditText: EditText
    private lateinit var houseEditText: EditText
    private lateinit var buildingEditText: EditText
    private lateinit var vlanEditText: EditText
    private lateinit var addButton: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: AddingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding)

        initUi()
        setUpAdapter()
        setUpObservers()
    }

    private fun initUi() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(this.applicationContext)
        ).get(AddingViewModel::class.java)
        streetEditText = findViewById(R.id.addingStreetEditText)
        houseEditText = findViewById(R.id.addingHouseEditText)
        buildingEditText = findViewById(R.id.addingBuildingEditText)
        vlanEditText = findViewById(R.id.addingVlanEditText)

        progressBar = findViewById(R.id.addingProgressBar)
        progressBar.visibility = View.INVISIBLE

        addButton = findViewById(R.id.addingButtonAdd)
        addButton.setOnClickListener {
            if(checkEditFields()){
                viewModel.addPushed(
                    AddressConverter.convertAddressToString(
                        spinner.selectedItem.toString(),
                        streetEditText.text.toString(),
                        houseEditText.text.toString(),
                        buildingEditText.text.toString()
                    ),
                    vlanEditText.text.toString()
                )
            }
        }

        spinner = findViewById(R.id.addingStreetTypeSpinner)
    }

    private fun setUpAdapter() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.street_type,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setUpObservers() {
        viewModel.warning.observe(this) {
            if (it) {
                showLoading(false)
                addButton.isEnabled = true
                val dialog = WarningDialog()
                dialog.show(supportFragmentManager, "warningDialog")
            }
        }

        viewModel.state.observe(this){
            when(it.status){
                Result.Status.ERROR -> {
                    showLoading(false)
                    addButton.isEnabled = true
                    showError()
                }
                Result.Status.LOADING -> {
                    showLoading(true)
                    addButton.isEnabled = false
                }
                Result.Status.SUCCESS -> {
                    showLoading(false)
                    addButton.isEnabled = true
                    showSuccess()
                }
            }
        }
    }

    fun noClicked() {
        viewModel.noClicked()
    }

    fun yesClicked() {
        val buildingAddress = AddressConverter.convertAddressToString(
            spinner.selectedItem.toString(),
            streetEditText.text.toString(),
            houseEditText.text.toString(),
            buildingEditText.text.toString()
        )
        viewModel.yesClicked(buildingAddress, vlanEditText.text.toString())
    }


    private fun showError() {
        val dialog = ErrorDialog()
        dialog.show(supportFragmentManager, "errorDialog")
    }

    private fun showSuccess() {
        val toast = Toast.makeText(this, "Запись успешно сохранена", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun showLoading(loading: Boolean){
        if(loading){
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun checkEditFields(): Boolean{
        if(streetEditText.text.toString().isEmpty() || houseEditText.text.toString().isEmpty() || buildingEditText.text.toString().isEmpty() || vlanEditText.text.toString().isEmpty()){
            showRequirement()
            return false
        }
        return true
    }

    private fun showRequirement(){
        val toast = Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}