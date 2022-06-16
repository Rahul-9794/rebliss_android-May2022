package com.rebliss.domain.model.assignment_review

import com.google.gson.annotations.SerializedName

data class AssignmentModel(
    @SerializedName("data") val data : Data
){
    data class Data (

        @SerializedName("all_groups") val all_groups : List<All_groups>
    ){
        data class All_groups (

            @SerializedName("activity_detail_id") val activity_detail_id : String,
            @SerializedName("fos_id") val fos_id : String,
            @SerializedName("shop_name") val shop_name : String,
            @SerializedName("shop_id") val shop_id : String,
            @SerializedName("mobile") val mobile : String,
            @SerializedName("shop_category") val shop_category : String,
            @SerializedName("inside_photo") val inside_photo : String,
            @SerializedName("outside_photo") val outside_photo : String,
            @SerializedName("activity_photo") val activity_photo : String,
            @SerializedName("inside_photo2") val inside_photo2 : String,
            @SerializedName("inside_photo3") val inside_photo3 : String,
            @SerializedName("inside_photo4") val inside_photo4 : String,
            @SerializedName("inside_photo5") val inside_photo5 : String,
            @SerializedName("outside_photo2") val outside_photo2 : String,
            @SerializedName("outside_photo3") val outside_photo3 : String,
            @SerializedName("outside_photo4") val outside_photo4 : String,
            @SerializedName("outside_photo5") val outside_photo5 : String,
            @SerializedName("activity_photo2") val activity_photo2 : String,
            @SerializedName("activity_photo3") val activity_photo3 : String,
            @SerializedName("activity_photo4") val activity_photo4 : String,
            @SerializedName("activity_photo5") val activity_photo5 : String,
            @SerializedName("proof_id") val proof_id : String,
            @SerializedName("pan_photo") val pan_photo : String,
            @SerializedName("pan_number") val pan_number : String,
            @SerializedName("pincode") val pincode : Int,
            @SerializedName("address") val address : String,
            @SerializedName("city") val city : String,
            @SerializedName("state") val state : String,
            @SerializedName("lat") val lat : Double,
            @SerializedName("long") val long : Double,
            @SerializedName("gst_number") val gst_number : String,
            @SerializedName("gst_photo") val gst_photo : String,
            @SerializedName("category_id") val category_id : Int,
            @SerializedName("sub_category_id") val sub_category_id : String,
            @SerializedName("sub_category1_id") val sub_category1_id : String,
            @SerializedName("status") val status : String,
            @SerializedName("decline_list") val decline_list : String,
            @SerializedName("reason_to_decline") val reason_to_decline : String,
            @SerializedName("client_review") val client_review : String,
            @SerializedName("activity_status") val activity_status : String,
            @SerializedName("order_id") val order_id : String,
            @SerializedName("created_at") val created_at : String
        )
    }
}
