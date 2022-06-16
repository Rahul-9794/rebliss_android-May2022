package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class RefrenceRequest(
        @SerializedName("user_id") val user_id : String,
        @SerializedName("name") val name : String,
        @SerializedName("mobile") val mobile : String,
        @SerializedName("category") val category : String,
        @SerializedName("pincode") val pincode : String
)
