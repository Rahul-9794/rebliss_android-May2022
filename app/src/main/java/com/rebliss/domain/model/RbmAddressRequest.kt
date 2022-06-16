package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class RbmAddressRequest(

@SerializedName("user_id") val user_id : String,
@SerializedName("address_id") val address_id : String,
@SerializedName("address1") val address1 : String,
@SerializedName("address2") val address2 : String,
@SerializedName("city") val city : String,
@SerializedName("pincode") val pincode : String,
@SerializedName("state") val state : String,
@SerializedName("landmark") val landmark : String,
@SerializedName("latitude") val latitude : Double,
@SerializedName("longitude ") val longitude : Double

)
