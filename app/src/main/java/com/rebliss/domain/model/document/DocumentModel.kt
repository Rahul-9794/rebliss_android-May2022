package com.rebliss.domain.model.document

import com.google.gson.annotations.SerializedName


/*
 # @Author: Bhavesh Chand
 # @Date: 3/21/2022
 */

data class DocumentModel(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("status")
    var status: Int? = 0
){
    data class Data(
        @SerializedName("document")
        var document: List<Docs?>? = listOf(),

        )
    {
        data class Docs(
            @SerializedName("document_name")
            var document_name: String? = "",

            @SerializedName("doc_id")
        var doc_id: Int? = 0
        )
    }


}
