package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class AssignmentStatusCheckResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("desc") val desc : String,
    @SerializedName("data") val data : Int
)
