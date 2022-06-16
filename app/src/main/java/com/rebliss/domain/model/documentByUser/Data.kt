package com.rebliss.domain.model.documentByUser

data class Data(
    val document_path: String,
    val document_title: String,
    val status: String,
    val user_id: String,
    val file_type: String
)