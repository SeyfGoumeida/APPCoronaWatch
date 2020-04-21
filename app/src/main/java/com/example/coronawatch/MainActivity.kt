package com.example.coronawatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_btn.setOnClickListener() {

            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }


        guest_btn.setOnClickListener(){

            val intent = Intent(this, ArticlesActivity::class.java)
            startActivity(intent)
        }

            //---------------------------------------------------------------------------------------------------------




        }


            }





