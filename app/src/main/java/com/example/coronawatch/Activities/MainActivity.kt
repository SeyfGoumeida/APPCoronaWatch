package com.example.coronawatch.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coronawatch.Activities.ArticlesActivity
import com.example.coronawatch.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_btn.setOnClickListener() {

            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }


        guest_btn.setOnClickListener(){

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }


            //---------------------------------------------------------------------------------------------------------


        }


}





