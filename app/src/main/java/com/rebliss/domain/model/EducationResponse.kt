package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class EducationResponse(
        @SerializedName("data")
        var `data`: Data? = Data(),
        @SerializedName("desc")
        var desc: String? = "",
        @SerializedName("status")
        var status: Int? = 0
) {
    data class Data(
            @SerializedName("all_groups")
            var allGroups: ArrayList<AllGroup?>? = arrayListOf()
    ) {
        data class AllGroup(
                @SerializedName("created_at")
                var createdAt: Any? = Any(),
                @SerializedName("education_name")
                var educationName: String? = "",
                @SerializedName("education_status")
                var educationStatus: String? = "",
                @SerializedName("id")
                var id: Int? = 0
        )
    }
}