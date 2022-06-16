package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class OpportunityDetailResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("desc")
    var desc: String? = "",
    @SerializedName("status")
    var status: Int? = 0
) {
    data class Data(
        @SerializedName("all_groups")
        var allGroups: List<AllGroup?>? = listOf()
    ) {
        data class AllGroup(
            @SerializedName("created_at")
            var createdAt: String? = "",
            @SerializedName("description")
            var description: String? = "",
            @SerializedName("id")
            var id: Int? = 0,
            @SerializedName("image")
            var image: String? = "",
            @SerializedName("media_title")
            var mediaTitle: String? = "",
            @SerializedName("mediaType")
            var mediaType: String? = "",
            @SerializedName("opportunity_title")
            var opportunityTitle: String? = "",
            @SerializedName("valid_till_date")
            var validTillDate: String? = "",
            @SerializedName("video_link")
            var videoLink: String? = ""
        )
    }
}