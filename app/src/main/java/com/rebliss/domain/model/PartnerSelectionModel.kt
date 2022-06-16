package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class PartnerSelectionModel(
        @SerializedName("user_id")
        var userId: String? = "",
        @SerializedName("group_id")
        var groupId: String? = ""
)