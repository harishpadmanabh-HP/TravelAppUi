package com.harish.travelappui.models


import com.google.gson.annotations.SerializedName

data class CitiesResponse(
    @SerializedName("cities")
    val cities: List<City>
) {
    data class City(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image_path")
        val imagePath: String,
        @SerializedName("sub_title")
        val subTitle: String,
        @SerializedName("title")
        val title: String
    )
}