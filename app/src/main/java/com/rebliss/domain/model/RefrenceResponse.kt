package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class RefrenceResponse(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
){
    data class Data (

            @SerializedName("all_groups") val all_groups : All_groups
    ) {
        data class All_groups (

                @SerializedName("user_id") val user_id : Int,
                @SerializedName("name") val name : String,
                @SerializedName("mobile") val mobile : String,
                @SerializedName("category") val category : String,
                @SerializedName("pincode") val pincode : Int,
                @SerializedName("id") val id : Int
        )
    }
}
