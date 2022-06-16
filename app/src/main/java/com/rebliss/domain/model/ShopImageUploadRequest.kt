package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class ShopImageUploadRequest(
    @SerializedName("image") var image: String? = null

)
