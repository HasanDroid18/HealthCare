package com.example.healthcare.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.Models.HospitalItem
import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.HospitalRetro.Hospitals
import kotlinx.coroutines.launch
import retrofit2.Response

class HospitalViewModel(private val hospitalService: HospitalService) : ViewModel() {

    // LiveData to hold the list of hospitals
    private val _hospitals = MutableLiveData<List<HospitalItem>>()
    val hospitals: LiveData<List<HospitalItem>> get() = _hospitals

    // LiveData to hold the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData to hold error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Function to fetch hospital data from the API
    fun fetchHospitalData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response: Response<Hospitals> = hospitalService.getHospitals()

                if (response.isSuccessful) {
                    val hospitalsList = response.body()?.listIterator()

                    val hospitalsDataList = mutableListOf<HospitalItem>()

                    hospitalsList?.forEach { hospitalsItem ->
                        val name = hospitalsItem.name ?: ""
                        val type = hospitalsItem.type ?: ""
                        val status = hospitalsItem.status ?: ""

                        hospitalsDataList.add(HospitalItem(name, type, status))
                    }

                    _hospitals.value = hospitalsDataList
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
