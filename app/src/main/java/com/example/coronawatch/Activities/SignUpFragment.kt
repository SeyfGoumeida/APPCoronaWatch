package com.example.coronawatch.Activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.coronawatch.ArticlesActivity
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.example.coronawatch.Volley.VolleySingleton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.password
import kotlinx.android.synthetic.main.sign_up_fragment.username
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import org.json.JSONObject

class SignUpFragment : Fragment() {


    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI: IAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)

        signupbtn.setOnClickListener() {

            compositeDisposable.add( jsonAPI.signUser(username.text.toString(),
                email.text.toString(),
                password.text.toString(), password2.text.toString() ,
                "First" , "Last"
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe{ user -> setUserIntoPrefrences(user)
                    test.text = user.toString()

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
