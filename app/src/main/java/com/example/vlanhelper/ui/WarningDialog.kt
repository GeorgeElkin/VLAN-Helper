package com.example.vlanhelper.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.vlanhelper.ui.adding.AddActivity

class WarningDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Предупреждение")
                .setMessage("Запись с таким адресом уже существует.\nПерезаписать?")
                .setNegativeButton("Нет") { dialog, id ->
                    (it as AddActivity).noClicked()
                }
                .setPositiveButton("Да"){ dialog, id ->
                    (it as AddActivity).yesClicked()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}