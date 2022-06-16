package com.rebliss.domain.model

import com.google.gson.annotations.SerializedName

data class service_output_response(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
){
    data class Data (

            @SerializedName("all_groups") val all_groups : All_groups
    ) {
        data class All_groups (

                @SerializedName("id") val id : Int,
                @SerializedName("user_id") val user_id : Int,
                @SerializedName("gender") val gender : String,
                @SerializedName("address") val address : String,
                @SerializedName("photo") val photo : String,
                @SerializedName("bday") val bday : String,
                @SerializedName("location") val location : String,
                @SerializedName("marital_status") val marital_status : String,
                @SerializedName("cellphone") val cellphone : String,
                @SerializedName("web_page") val web_page : String,
                @SerializedName("gst_no") val gst_no : String,
                @SerializedName("dob") val dob : String,
                @SerializedName("landline_no") val landline_no : String,
                @SerializedName("personal_email_id") val personal_email_id : String,
                @SerializedName("official_email_id") val official_email_id : String,
                @SerializedName("cp_firm_name") val cp_firm_name : String,
                @SerializedName("communication_address_id") val communication_address_id : String,
                @SerializedName("bussiness_address_id") val bussiness_address_id : String,
                @SerializedName("bank_holder_name") val bank_holder_name : String,
                @SerializedName("account_number") val account_number : String,
                @SerializedName("ifsc_code") val ifsc_code : String,
                @SerializedName("bank_name") val bank_name : String,
                @SerializedName("upload_type_option") val upload_type_option : Int,
                @SerializedName("aadhar_no") val aadhar_no : String,
                @SerializedName("company_pan_no") val company_pan_no : String,
                @SerializedName("pan_no") val pan_no : String,
                @SerializedName("pos_father_name") val pos_father_name : String,
                @SerializedName("driving_license_no") val driving_license_no : String,
                @SerializedName("em_contact_person_name") val em_contact_person_name : String,
                @SerializedName("em_person_contact_no") val em_person_contact_no : String,
                @SerializedName("partner_id") val partner_id : String,
                @SerializedName("id_proof") val id_proof : String,
                @SerializedName("cp_adhar_proof") val cp_adhar_proof : String,
                @SerializedName("cp_pan_proof") val cp_pan_proof : String,
                @SerializedName("firm_pan_proof") val firm_pan_proof : String,
                @SerializedName("gst_proof") val gst_proof : String,
                @SerializedName("address_proof") val address_proof : String,
                @SerializedName("business_type") val business_type : String,
                @SerializedName("manpower_strength") val manpower_strength : String,
                @SerializedName("business_locations") val business_locations : String,
                @SerializedName("turnover_business") val turnover_business : String,
                @SerializedName("nature_of_business") val nature_of_business : String,
                @SerializedName("cheque_proof") val cheque_proof : String,
                @SerializedName("driving_license_proof") val driving_license_proof : String,
                @SerializedName("passport_size_photo") val passport_size_photo : String,
                @SerializedName("signed_form") val signed_form : String,
                @SerializedName("shop_photo") val shop_photo : String,
                @SerializedName("shop_inside_photo") val shop_inside_photo : String,
                @SerializedName("ib_da_code") val ib_da_code : String,
                @SerializedName("ib_da_proof") val ib_da_proof : String,
                @SerializedName("shop_latitude") val shop_latitude : String,
                @SerializedName("shop_longitude") val shop_longitude : String,
                @SerializedName("industry_type_other") val industry_type_other : String,
                @SerializedName("education") val education : String,
                @SerializedName("current_service") val current_service : String,
                @SerializedName("interested_service") val interested_service : String,
                @SerializedName("created") val created : String,
                @SerializedName("modified") val modified : String
        )
    }
}
