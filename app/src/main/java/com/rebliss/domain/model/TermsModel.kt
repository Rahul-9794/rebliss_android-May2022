package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class TermsModel(
        @SerializedName("user_id")
    var userId: String? = "",
        @SerializedName("terms")
        var terms: Int? = 0
)