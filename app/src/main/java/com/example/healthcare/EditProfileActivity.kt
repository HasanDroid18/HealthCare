package com.example.healthcare
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
//            showDatePickerDialog()
        }
    }


//    private fun showDatePickerDialog() {
//        val da = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker, null)
//        val dialogBinding = DialogDatePickerBinding.bind(dialogView)
//
//        val dialogBuilder = AlertDialog.Builder(this)
//            .setView(dialogView)
//            .setCancelable(false)
//
//        val dialog = dialogBuilder.create()
//        dialog.show()
//
//        dialogBinding.calendarView.setOnClickListener{}
//
//        dialogBinding.btnCancel.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialogBinding.btnReset.setOnClickListener {
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            val formattedDate = dateFormat.format(calendar.time)
//            binding.dateOfBirthDisplay.setText(formattedDate)
//            dialog.dismiss()
//        }
//    }
}
