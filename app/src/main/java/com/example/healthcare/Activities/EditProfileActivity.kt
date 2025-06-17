package com.example.healthcare.Activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.healthcare.R
import com.example.healthcare.databinding.ActivityEditProfileBinding
import com.example.healthcare.databinding.DialogDatePickerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateOfBirthDisplay.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker, null)
        val dialogBinding = DialogDatePickerBinding.bind(dialogView)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Variable to store the selected date
        val selectedDate = Calendar.getInstance()

        dialogBinding.calendarView.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, dayOfMonth ->
            selectedDate.set(year, month, dayOfMonth)
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            // Set the selected date to the EditText only when "Save" is clicked
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.dateOfBirthDisplay.setText(dateFormat.format(selectedDate.time))
            dialog.dismiss()
        }
    }
}
