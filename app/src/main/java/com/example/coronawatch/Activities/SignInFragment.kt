package com.example.coronawatch.Activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.coronawatch.Activities.ArticlesActivity
import com.example.coronawatch.DataClases.Redactor
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.example.coronawatch.Volley.VolleySingleton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*
import kotlinx.android.synthetic.main.sign_up_fragment.password
import kotlinx.android.synthetic.main.sign_up_fragment.username
import org.json.JSONObject

class SignInFragment : Fragment() {


    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI:IAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)

        loginbtn.setOnClickListener {

            compositeDisposable.add( jsonAPI.getUser(username.text.toString(), password.text.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe{ user -> setUserIntoPrefrences(user)
                test2.text = user.toString()

                }
            )

            val intent = Intent(context, ArticlesActivity::class.java)
            startActivity(intent)
        }

        }

    private fun setUserIntoPrefrences ( user : User) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString("user${user.profile_id}", "$user").apply()
    }

    }



