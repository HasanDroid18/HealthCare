package com.example.healthcare.HospitalRetro

import retrofit2.Response
import retrofit2.http.GET

interface HospitalService {
    @GET("/api/hospitals")
    suspend fun getHospitals(): Response<Hospitals>

}