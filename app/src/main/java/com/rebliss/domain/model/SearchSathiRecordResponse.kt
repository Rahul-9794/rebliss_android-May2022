package com.rebliss.domain.model

data class SearchSathiRecordResponse(
    val `data`: Data,
    val desc: String,
    val status: Int
){
    data class Data (

       val id : String,
        val type : String,
       val phone_number : String
    )
}