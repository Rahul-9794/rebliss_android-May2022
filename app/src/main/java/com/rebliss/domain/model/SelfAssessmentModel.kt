package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class SelfAssessmentModel(
        @SerializedName("user_id")
        var userId: String? = "",
        @SerializedName("question_id")
        var questionId: String? = "",
        @SerializedName("answer")
        var answer: String? = ""
)