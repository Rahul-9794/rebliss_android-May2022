package com.rebliss.domain.model
import com.google.gson.annotations.SerializedName


data class SeachMerchantData (

		@SerializedName("status") val status : Int,
		@SerializedName("desc") val desc : String,
		@SerializedName("data") val data : Data
)
{
	data class Data (

			@SerializedName("all_groups") val all_groups : List<All_groups>
	)
	{
		data class All_groups (

				@SerializedName("id") val id : Int,
				@SerializedName("first_name") val first_name : String,
				@SerializedName("last_name") val last_name : String,
				@SerializedName("fos_shop_name") val fos_shop_name : String,
				@SerializedName("phone_number") val phone_number : String,
				@SerializedName("location_zipcode") val location_zipcode : Int,
				@SerializedName("location_city") val location_city : String,
				@SerializedName("location_state") val location_state : String,
				@SerializedName("profile_status") val profile_status : String
		)
	}
}