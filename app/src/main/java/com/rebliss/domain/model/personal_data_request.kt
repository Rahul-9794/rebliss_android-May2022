package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class personal_data_request(

@SerializedName("user_id") val user_id : String,
@SerializedName("first_name") val first_name : String,
@SerializedName("last_name") val last_name : String,
@SerializedName("phone_number") val phone_number : String,
@SerializedName("age_range") val age_range : String,
@SerializedName("gender") val gender : String,
@SerializedName("education") val education : String,
)
