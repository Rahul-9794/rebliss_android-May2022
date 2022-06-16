package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class update_status_rbmlist_request(
        @SerializedName("profile_status") val status : String,
        @SerializedName("rbm_id") val desc : String,

)

