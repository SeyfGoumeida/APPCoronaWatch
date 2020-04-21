package com.example.coronawatch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.password
import kotlinx.android.synthetic.main.sign_up_fragment.username
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import org.json.JSONObject

class SignInFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.sign_in_fragment, container, false)

        view.loginbtn.setOnClickListener { view ->


            loginbtn.setOnClickListener() {


                val url = "https://coronawatch.herokuapp.com/api/users/login/"

                val params = HashMap<String,Any> ()
                params["username"] = username.text.toString()
               // params["email"] = email.text.toString()
                params["password"] = password.text.toString()


                val jasonObject = JSONObject(params)

                val request = JsonObjectRequest (
                    Request.Method.POST, url , jasonObject , Response.Listener {
                            response ->
                        try {
                            test2.text = "Response: $response"
                        }catch (e:Exception){
                            test2.text = "Exception: $e"
                        }

                    }, Response.ErrorListener{
                        // Error in request
                        test2.text = "Volley error: $it"
                    })


                // Volley request policy, only one time request to avoid duplicate transaction
                request.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                // Add the volley post request to the request queue
                context?.let { it -> VolleySingleton.getInstance(it).addToRequestQueue(request) }
            }
        }

        // Return the fragment view/layout
        return view

    }

}
