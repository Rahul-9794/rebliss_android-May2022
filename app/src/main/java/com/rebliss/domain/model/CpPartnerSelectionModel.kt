package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class CpPartnerSelectionModel(
        @SerializedName("user_id")
        var userId: String? = "",
        @SerializedName("cp_type")
        var cpID: String? = ""
)