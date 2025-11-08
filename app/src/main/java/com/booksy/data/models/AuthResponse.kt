package com.booksy.data.models

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("authToken")
    val authToken: String,

    @SerializedName("user_id")
    val userId: Int
)