package com.example.coronawatch.Activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.coronawatch.R
import com.google.android.material.navigation.NavigationView


// ------------------------------------------------------------------------------------------------------------------------
class DashboardActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL)




        // Toolbar and View

        setContentView(R.layout.activity_dashboard)
        toolbar = findViewById(R.id.dashboard_toolbar)
        toolbar.title = "مراقبة كورونا"
        setSupportActionBar(toolbar)

        val fragment1 = MapFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment1)
        transaction.commit()
        toolbar.title = "الخريطة"




 // Navigation Drawer Section ---------------------------------------------------------------------------------------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view_dashboard)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(item: MenuItem ): Boolean {
        when (item.itemId) {
            R.id.nav_map -> {

                val fragment1 = MapFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment1)
                transaction.commit()
                toolbar.title = "الخريطة"

            }

            R.id.nav_articles -> {

                val intent = Intent(this, ArticlesActivityGuest::class.java)
                startActivity(intent)

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

 //------------------------------------------------------------------------------------------------------------------------------



    }
