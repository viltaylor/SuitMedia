package com.example.test1.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("data") val data: List<User>
)