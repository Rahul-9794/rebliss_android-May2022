package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class update_status_rbmlist_response(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
)
{
    data class Data (

            @SerializedName("all_groups") val all_groups : All_groups
    )
    {
        data class All_groups (
            @SerializedName("profile_status") val profile_status : String
        )
    }

}
