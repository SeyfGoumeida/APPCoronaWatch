package com.example.coronawatch.DataClases

data class User(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val profile_id: Int,
    val token: String,
    val user_type: Int,
    val username: String
)