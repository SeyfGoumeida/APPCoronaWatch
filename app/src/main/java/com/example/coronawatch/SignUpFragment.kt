package com.example.coronawatch

import android.content.Context
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
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import org.json.JSONObject

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.sign_up_fragment, container, false)

        view.signupbtn.setOnClickListener { view ->

            signupbtn.setOnClickListener() {

                val url = "https://coronawatch.herokuapp.com/api/users/emailsign/"

                val params = HashMap<String,Any> ()
                params["username"] = username.text.toString()
                params["email"] = email.text.toString()
                params["password"] = password.text.toString()
                params["password2"] = password2.text.toString()
                params["first_name"] = "First"
                params["last_name"] = "Name"

                val jasonObject = JSONObject(params)

                val request = JsonObjectRequest (
                    Request.Method.POST, url , jasonObject , Response.Listener {
                            response ->
                        try {
                            test.text = "Response: $response"
                        }catch (e:Exception){
                            test.text = "Exception: $e"
                        }

                    }, Response.ErrorListener{
                        // Error in request
                        test.text = "Volley error: $it"
                    })


                // Volley request policy, only one time request to avoid duplicate transaction
                request.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                // Add the volley post request to the request queue
                getContext()?.let { it -> VolleySingleton.getInstance(it).addToRequestQueue(request) }
            }
        }

        // Return the fragment view/layout
        return view


    }

}
