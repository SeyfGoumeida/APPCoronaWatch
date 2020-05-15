package com.example.coronawatch.Activities


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.coronawatch.R


class ArticlesActivityGuest : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles_guest)

        val navView: BottomNavigationView = findViewById(R.id.nav_view_guest)


        val navController = findNavController(R.id.nav_host_fragment_guest)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_guest,
                R.id.navigation_videos_guest
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }


    }

