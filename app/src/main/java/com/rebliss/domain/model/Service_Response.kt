package com.rebliss.domain.model
import com.google.gson.annotations.SerializedName


data class Service_Response (

		@SerializedName("status") val status : Int,
		@SerializedName("data") val data : Data
)
{
	data class Data (

			@SerializedName("industry") val industry : List<Industry>
	)
	{
		data class Industry (

				@SerializedName("id") val id : Int,
				@SerializedName("type") val type : String,
				@SerializedName("scenario_type") val scenario_type : Int,
				@SerializedName("value") val value : String,
				@SerializedName("text") val text : String,
				@SerializedName("status") val status : Int,
				@SerializedName("created") val created : String,
				@SerializedName("created_by") val created_by : Int,
				@SerializedName("modified_by") val modified_by : String,
				@SerializedName("modified") val modified : String,
				@SerializedName("deleted") val deleted : Int
		)
	}
}