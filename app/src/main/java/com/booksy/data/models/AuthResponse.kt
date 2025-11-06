package com.booksy.data.models

data class AuthResponse(
    val authToken: String,
    val user: User
)