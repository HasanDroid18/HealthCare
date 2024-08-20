package com.example.healthcare.HospitalRetro

import com.google.gson.annotations.SerializedName

data class HospitalItem(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("status")
    val status: String?
)