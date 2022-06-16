package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class Category_Ref_Response(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
){
    data class Data (

            @SerializedName("all_category") val all_category : List<All_category>
    )
    {
        data class All_category (

                @SerializedName("category_id") val category_id : Int,
                @SerializedName("category_type") val category_type : String,
                @SerializedName("category_name") val category_name : String,
                @SerializedName("parent_id") val parent_id : String,
                @SerializedName("status") val status : Int
        )
    }
}
