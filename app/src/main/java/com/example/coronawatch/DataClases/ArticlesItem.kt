package com.example.coronawatch.DataClases

data class ArticlesItem(
    val attachments: List<Attachment>,
    val content: String,
    val date: String,
    val id: Int,
    val moderatorid: Int,
    val redactor: Int,
    val title: String,
    val valide: Boolean
)