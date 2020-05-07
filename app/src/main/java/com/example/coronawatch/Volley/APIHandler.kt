package com.example.coronawatch.Volley

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.coronawatch.DataClases.Articles
import com.example.coronawatch.DataClases.Comment
import com.example.coronawatch.DataClases.Redactor
import com.example.coronawatch.R
import com.google.gson.Gson
import org.json.JSONObject

class APIHandler ( val context: Context) : AppCompatActivity() {

    val gson = Gson()
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = preferences.edit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

// -------------------------------------------------------------------------------------------------------------------------------


    fun initilize ( ){

        Log.e("API HANDLER" ,"ON creattttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttte" )
        val url = "https://coronawatch.herokuapp.com/api/article/"

        val request = JsonArrayRequest (

            Request.Method.GET, url , Response.Listener { response ->

                try  { editor.putString("articles", "$response").apply() }
                catch (e:Exception){ Log.e("inside try : test","Exception: $e" ) }

            } , Response.ErrorListener{ Log.e("testing query", "$it" ) })

        request.retryPolicy = DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1f)

        VolleySingleton.getInstance(context).addToRequestQueue(request)

    }


// -------------------------------------------------------------------------------------------------------------------------------


    fun getArticles(): Articles {

        Log.e("API HANDLER" ,"Inside getartciles...............................................................")
        var articles : Articles
        preferences.apply {

            val jsonString = preferences.getString("articles","{}")
            articles  = gson.fromJson(jsonString , Articles::class.java)

        }
        Log.e("inside try : test", articles.size.toString())
            return articles

    }


// -------------------------------------------------------------------------------------------------------------------------------

    fun getRedactorDetails(ID:Int): Redactor? {

        val url = "https://coronawatch.herokuapp.com/api/users/redactor/$ID/"
        var redactor : Redactor? = null

        val request = JsonObjectRequest (
            Request.Method.GET, url , Response.Listener {
                    response ->
                try {
                    var item = response
                    editor.putString("jsonArticle$", "$item").apply()
                    val jsonRedactor = "$response"
                    redactor = gson.fromJson(jsonRedactor, Redactor::class.java)
                    println("> From JSON String:\n$redactor")

                }catch (e:Exception){
                    print ("Exception: $e")
                }

            }, Response.ErrorListener{
                // Error in request
                print( "Volley error: $it")
            })

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
        return redactor

    }


// -------------------------------------------------------------------------------------------------------------------------------


    fun getComments(ID:Int): MutableList<Comment> {

        val url = "https://coronawatch.herokuapp.com/api/article/$ID/comments/"
        var comments = mutableListOf<Comment>()

        val request = JsonObjectRequest (
            Request.Method.GET, url , Response.Listener {
                    response ->
                try {

                    (0 until response.length()).forEach { i ->
                        var item = response.getJSONObject(i.toString())
                        val jsonComment = "$item"
                        val comment : Comment = gson.fromJson(jsonComment, Comment::class.java)
                        println("> From JSON String:\n$comment")
                        comments.add(comment)
                }

                }catch (e:Exception){
                    print("Exception: $e")
                }

            }, Response.ErrorListener{
                // Error in request
                print("Volley error: $it")
            })

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
        return comments
    }


// -------------------------------------------------------------------------------------------------------------------------------


    fun commentArticle( ID: Int , Token:String , content:String ) {

        val url = "https://coronawatch.herokuapp.com/api/article/$ID/newComment/"

        val params = HashMap<String,Any> ()

        params["Authorization"] = Token
        params["content"] = content

        val jasonObject = JSONObject(params)

        val request = JsonObjectRequest (
            Request.Method.POST, url , jasonObject , Response.Listener {
                    response ->
                try {
                   print("Response: $response")
                }catch (e:Exception){
                    print("Exception: $e")
                }

            }, Response.ErrorListener{
                // Error in request
             print( "Volley error: $it")
            })

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)

    }


}









