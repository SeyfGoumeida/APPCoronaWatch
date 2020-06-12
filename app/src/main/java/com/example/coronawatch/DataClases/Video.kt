package com.example.coronawatch.DataClases

data class Video(
    val date: String,
    val id: Int,
    val mobileuserid: Int,
    val moderatorid: Int,
    val path: String,
    val title: String,
    val valide: Boolean
)