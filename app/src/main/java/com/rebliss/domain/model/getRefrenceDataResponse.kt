package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class getRefrenceDataResponse(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
) {
    data class Data (

            @SerializedName("all_groups") val all_groups : List<All_groups>
    )
    {
        data class All_groups (

                @SerializedName("id") val id : Int,
                @SerializedName("user_id") val user_id : Int,
                @SerializedName("name") val name : String,
                @SerializedName("mobile") val mobile : String,
                @SerializedName("category") val category : String,
                @SerializedName("pincode") val pincode : Int,
                @SerializedName("created_at") val created_at : String
        )
    }
}
