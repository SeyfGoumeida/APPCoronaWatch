package com.example.coronawatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.user_activity.*
import org.json.JSONObject

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)


        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SignUpFragment() , "انشاء حساب")
        adapter.addFragment(SignInFragment() , "تسجيل الدخول")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)


    }



    class MyViewPagerAdapter (manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val fragmentList : MutableList <Fragment> = ArrayList ()
        private val titleList : MutableList <String> = ArrayList ()


        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment (fragment: Fragment, title:String ) {

            fragmentList.add(fragment)
            titleList.add(title)

        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
} }
