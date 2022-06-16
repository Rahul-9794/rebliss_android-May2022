package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class TrainingResponse(
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
            @SerializedName("id")
            var id: Int? = 0,
            @SerializedName("image_path")
            var imagePath: String? = "",
            @SerializedName("is_active")
            var isActive: Int? = 0,
            @SerializedName("mediaType")
            var mediaType: String? = "",
            @SerializedName("task_for")
            var taskFor: Int? = 0,
            @SerializedName("training_description")
            var trainingDescription: String? = "",
            @SerializedName("training_title")
            var trainingTitle: String? = "",
            @SerializedName("video_link")
            var videoLink: String? = ""
        )
    }
}