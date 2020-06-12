package com.example.coronawatch.DataClases

class RegionDetails(
    val id: Int,
    val country_details:CountryDetails,
    val region_name: String,
    val code: Int,
    val latitude: Float,
    val longitude: Float,
    val riskvalide: Boolean,
    val riskregion: Boolean,
    val riskagentid: Int,
    val riskmoderatorid: Int
)