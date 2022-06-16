package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class ViewInvoiceResponse(
        @SerializedName("status") val status : Int,
        @SerializedName("desc") val desc : String,
        @SerializedName("data") val data : Data
) {
    data class Data (

            @SerializedName("all_groups") val all_groups : List<All_groups>
    ) {
        data class All_groups (

                @SerializedName("id") val id : Int,
                @SerializedName("earning_task_id") val earning_task_id : Int,
                @SerializedName("user_id") val user_id : Int,
                @SerializedName("amount") val amount : Int,
                @SerializedName("date") val date : String,
                @SerializedName("status") val status : String,
                @SerializedName("created_at") val created_at : String,
                @SerializedName("earningTask") val earningTask : EarningTask
        ) {
            data class EarningTask (

                    @SerializedName("id") val id : Int,
                    @SerializedName("task_for") val task_for : Int,
                    @SerializedName("state_id") val state_id : Int,
                    @SerializedName("type") val type : String,
                    @SerializedName("cp_code") val cp_code : String,
                    @SerializedName("price_type") val price_type : String,
                    @SerializedName("city_id") val city_id : Int,
                    @SerializedName("amount") val amount : Int,
                    @SerializedName("created_at") val created_at : String,
                    @SerializedName("taskFor") val taskFor : TaskFor
            ) {
                data class TaskFor (

                        @SerializedName("category_id") val category_id : Int,
                        @SerializedName("category_type") val category_type : String,
                        @SerializedName("category_name") val category_name : String,
                        @SerializedName("parent_id") val parent_id : Int
                )
            }
        }
    }
}