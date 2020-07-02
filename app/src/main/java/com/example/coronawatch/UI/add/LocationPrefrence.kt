package com.example.coronawatch.ui.add

import android.content.Context

class LocationPrefrence (context : Context) {

    private val NAME = "Location SharedPreference"
    private val Latitude = "latitude"
    private val Longitude= "longitude"

    val preference = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun getLat(): String {
        return preference.getString(Latitude, "36.9142")
    }

    fun getLon(): String {
        return preference.getString(Longitude, "7.7427")
    }
    fun setLocation(latitude:String , longitude:String){
        val editor = preference.edit()
        editor.putString(Latitude,latitude)
        editor.putString(Longitude,longitude)
        editor.apply()
    }

}