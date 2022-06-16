package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class servicesRequest(

@SerializedName("user_id") val user_id : String,
@SerializedName("interested_service") val interested_service : String,
@SerializedName("current_service") val current_service : String,
)
