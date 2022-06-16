package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class MyEarningResponse(
        @SerializedName("data")
        var `data`: Data? = Data(),
        @SerializedName("desc")
        var desc: String? = "",
        @SerializedName("status")
        var status: Int? = 0
) {
    data class Data(
            @SerializedName("all_debit")
            var allDebit: List<AllDebit?>? = listOf(),
            @SerializedName("all_groups")
            var allGroups: List<AllGroup?>? = listOf(),
            @SerializedName("total_amount")
            var totalAmount: List<TotalAmount?>? = listOf(),
            @SerializedName("total_payout")
            var totalPayout: List<TotalPayout?>? = listOf()
    ) {
        data class AllDebit(
                @SerializedName("total_amount")
                var totalAmount: String? = ""
        )

        data class AllGroup(
                @SerializedName("earningTask")
                var earningTask: EarningTask? = EarningTask(),
                @SerializedName("earning_task_id")
                var earningTaskId: String? = "",
                @SerializedName("total_amount")
                var totalAmount: String? = ""
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

        data class TotalAmount(
                @SerializedName("total_amount")
                var totalAmount: String? = ""
        )

        data class TotalPayout(
                @SerializedName("total_amount")
                var totalAmount: String? = ""
        )
    }
}