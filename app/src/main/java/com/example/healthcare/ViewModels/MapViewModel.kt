package com.example.healthcare.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.HospitalRetro.HospitalItem
import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.HospitalRetro.Hospitals
import kotlinx.coroutines.launch
import retrofit2.Response

class MapViewModel(private val hospitalService: HospitalService) : ViewModel() {

    // LiveData to hold the list of hospital items
    private val _hospitals = MutableLiveData<List<HospitalItem>>()
    val hospitals: LiveData<List<HospitalItem>> get() = _hospitals

    // LiveData to handle loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData to handle error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Function to fetch hospital data from the API
    fun fetchHospitalData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response: Response<Hospitals> = hospitalService.getHospitals()

                if (response.isSuccessful) {
                    _hospitals.value = response.body()
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
