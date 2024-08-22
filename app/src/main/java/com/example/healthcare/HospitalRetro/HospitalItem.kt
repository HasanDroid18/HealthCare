package com.example.healthcare.HospitalRetro

import com.google.gson.annotations.SerializedName

data class HospitalItem(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("info")
    val info: Info?=null
)

data class Info(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)
