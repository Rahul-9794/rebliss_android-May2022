package com.rebliss.domain

import com.google.gson.annotations.SerializedName

data class personal_data_response(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
){
    data class Data (

            @SerializedName("all_groups") val all_groups : All_groups
    ) {
        data class All_groups (

                @SerializedName("id") val id : Int,
                @SerializedName("first_name") val first_name : String,
                @SerializedName("last_name") val last_name : String,
                @SerializedName("email") val email : String,
                @SerializedName("auth_key") val auth_auth_key : String,
                @SerializedName("password_hash") val password_hash : String,
                @SerializedName("password_reset_token") val password_reset_token : String,
                @SerializedName("phone_number") val phone_number : String,
                @SerializedName("group_id") val group_id : Int,
                @SerializedName("group_detail_id") val group_detail_id : Int,
                @SerializedName("img_path") val img_path : String,
                @SerializedName("accept_tnc") val accept_tnc : String,
                @SerializedName("invite_user_id") val invite_user_id : Int,
                @SerializedName("status") val status : Int,
                @SerializedName("group") val group : String,
                @SerializedName("email_verified") val email_verified : Int,
                @SerializedName("sms_verified") val sms_verified : Int,
                @SerializedName("profile_verified") val profile_verified : Int,
                @SerializedName("device_type") val device_type : String,
                @SerializedName("device_id") val device_id : String,
                @SerializedName("last_login") val last_login : String,
                @SerializedName("by_admin") val by_admin : Int,
                @SerializedName("code") val code : String,
                @SerializedName("age_range") val age_range : String,
                @SerializedName("occupation") val occupation : String,
                @SerializedName("fos_type") val fos_type : String,
                @SerializedName("fos_shop_name") val fos_shop_name : String,
                @SerializedName("location_zipcode") val location_zipcode : Int,
                @SerializedName("location_city") val location_city : String,
                @SerializedName("location_state") val location_state : String,
                @SerializedName("cp_type") val cp_type : String,
                @SerializedName("created") val created : String,
                @SerializedName("modified") val modified : String,
                @SerializedName("notification_count") val notification_count : Int,
                @SerializedName("unique_ref_code") val unique_ref_code : String,
                @SerializedName("decline_message") val decline_message : String,
                @SerializedName("app_version") val app_version : String
        )
    }
}
