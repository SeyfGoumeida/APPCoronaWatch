package com.example.coronawatch.DataClases

import java.util.*

class Region (
        val id: Int,
        val region : RegionDetails,
        val nb_death : Int,
        val nb_recovered : Int,
        val date: Date,
        val valide: Boolean,
        val nb_notyetsick: Int,
        val nb_suspected: Int,
        val nb_confirme: Int,
        val regionid: Int,
        val agentid: Int
)