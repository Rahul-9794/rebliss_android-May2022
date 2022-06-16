package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class ShopUploadResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("desc") val desc : String,
    @SerializedName("data") val data : Data
)
{
    data class Data (

        @SerializedName("all_groups") val all_groups : String
    )
}
