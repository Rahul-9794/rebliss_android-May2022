package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class rbmAddressResponse(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
){
    data class Data (

            @SerializedName("all_groups") val all_groups : All_groups
    ) {
        data class All_groups (

                @SerializedName("id") val id : Int,
                @SerializedName("address1") val address1 : String,
                @SerializedName("address2") val address2 : String,
                @SerializedName("city") val city : Int,
                @SerializedName("district") val district : String,
                @SerializedName("state") val state : Int,
                @SerializedName("country") val country : String,
                @SerializedName("zipcode") val zipcode : Int,
                @SerializedName("land_mark") val land_mark : String,
                @SerializedName("latitude") val latitude : Double,
                @SerializedName("longitude") val longitude : Double
        )
    }
}
