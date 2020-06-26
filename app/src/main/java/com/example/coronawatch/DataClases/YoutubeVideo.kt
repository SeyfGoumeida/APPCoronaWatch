package com.example.coronawatch.DataClases

data class YoutubeVideo(
    val id: Int,
    val source_type: String ,
    val url: String,
    val date: String,
    val valide: Boolean,
    val moderatorid: Int
)