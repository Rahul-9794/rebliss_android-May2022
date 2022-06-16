package com.rebliss.domain.model


import com.google.gson.annotations.SerializedName

data class ActivityTaskResponse(
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
                @SerializedName("taskFor")
                var taskFor: TaskFor? = TaskFor(),
                @SerializedName("categoryDetail")
                var categoryDetail: CategoryDetail? = CategoryDetail(),
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

            data class CategoryDetail(
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
}