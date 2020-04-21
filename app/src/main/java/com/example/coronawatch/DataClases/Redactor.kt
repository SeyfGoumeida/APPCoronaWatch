package com.example.coronawatch.DataClases

data class Redactor(
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val profile_id: Int,
    val user_type: Int,
    val username: String

) { override fun toString(): String =
    "{ \"id\": 4, \"email\": $email , \"username\": $username ,\"user_type\": $user_type," +
    "\"profile_id\": $profile_id,\"first_name\": $first_name ,\"last_name\": $last_name }"
}

