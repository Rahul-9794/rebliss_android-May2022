package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class EarningSummaryResponse(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("desc")
    var desc: String? = "",
    @SerializedName("status")
    var status: Int? = 0
) {
    data class Data(
        @SerializedName("all_groups")
        var allGroups: List<AllGroup?>? = listOf(),
        var total_amount: List<Total_amount?>? = listOf()
    ) {
        data class AllGroup(
            @SerializedName("activity_id")
            var activityId: String? = "",
            @SerializedName("amount")
            var amount: String? = "",
            @SerializedName("created_at")
            var createdAt: String? = "",
            @SerializedName("date")
            var date: String? = "",
            @SerializedName("earningTask")
            var earningTask: EarningTask? = EarningTask(),
            @SerializedName("earning_task_id")
            var earningTaskId: String? = "",
            @SerializedName("earning_type")
            var earningType: String? = "",
            @SerializedName("id")
            var id: String? = "",
            @SerializedName("payout_status")
            var payoutStatus: String? = "",
            @SerializedName("status")
            var status: String? = "",
            @SerializedName("user_id")
            var userId: String? = ""
        ) {
            data class EarningTask(
                @SerializedName("amount")
                var amount: String? = "",
                @SerializedName("city_id")
                var cityId: String? = "",
                @SerializedName("cp_code")
                var cpCode: String? = "",
                @SerializedName("created_at")
                var createdAt: String? = "",
                @SerializedName("id")
                var id: String? = "",
                @SerializedName("price_type")
                var priceType: String? = "",
                @SerializedName("state_id")
                var stateId: String? = "",
                @SerializedName("task_for")
                var task_for: String? = "",
                @SerializedName("taskFor")
                var taskFor: TaskFor? = TaskFor(),
                @SerializedName("type")
                var type: String? = ""
            ) {
                data class TaskFor(
                    @SerializedName("category_id")
                    var categoryId: String? = "",
                    @SerializedName("category_name")
                    var categoryName: String? = "",
                    @SerializedName("category_type")
                    var categoryType: String? = "",
                    @SerializedName("parent_id")
                    var parentId: String? = ""
                )

            }

        }

      data  class Total_amount(
              @SerializedName("total_amount")
              var totalAmount: String? = ""
      )

    }

}