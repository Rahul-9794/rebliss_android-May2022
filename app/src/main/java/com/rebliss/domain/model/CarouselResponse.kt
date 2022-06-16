package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class CarouselResponse(
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
            var createdAt: Any? = Any(),
            @SerializedName("id")
            var id: Int? = 0,
            @SerializedName("image")
            var image: String? = "",
            @SerializedName("is_active")
            var isActive: Int? = 0,
            @SerializedName("media_type")
            var mediaType: String? = "",
            @SerializedName("position")
            var position: String? = "",
            @SerializedName("redirect_link")
            var redirectLink: String? = "",
            @SerializedName("redirection_type")
            var redirection_type: String? = "",
            @SerializedName("img_redirect_link")
            var img_redirect_link: String? = "",
            @SerializedName("show_type")
            var show_type: String? = "",
            @SerializedName("screen_redirection_value")
            var screen_redirection_value: String? = "",
            @SerializedName("type")
            var type: Int? = 0,
            @SerializedName("video_link")
            var videoLink: String? = ""
        )
    }
}