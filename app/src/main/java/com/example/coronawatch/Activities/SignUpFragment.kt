package com.example.coronawatch.Activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.password
import kotlinx.android.synthetic.main.sign_up_fragment.username
import java.util.*



class SignUpFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    var callbackManager : CallbackManager? = null
    lateinit var jsonAPI:IAPI
    var   mGoogleSignInClient : GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1038582062348-8ad8vf7s4eo1cjk0skb21iojegonth0o.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso);


        callbackManager = CallbackManager.Factory.create()


        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)

        signupbtn.setOnClickListener() {

            compositeDisposable.add( jsonAPI.signUser(username.text.toString(),
                email.text.toString(),
                password.text.toString(), password2.text.toString() ,
                "First" , "Last"
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user -> setUserIntoPrefrences(user)
                    val intent = Intent(context, ArticlesActivity::class.java)
                    startActivity(intent)
                     } , { error  ->  Toast.makeText(context , error.message ,
                    Toast.LENGTH_LONG).show() } )
            )

        }


       google_signup.setOnClickListener {
            googleSignup()
        }

        facebook_signup.setOnClickListener {
            //First step
            facebookSignup()
        }
    }

    fun facebookSignup(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile","email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //Second step
                    handleFacebookAccessToken(result?.accessToken)
                }

                override fun onCancel() {
                }
                override fun onError(error: FacebookException?) {
                }

            })
    }
    fun handleFacebookAccessToken(token : AccessToken?){

        Toast.makeText(context , "${token!!.token}" , Toast.LENGTH_LONG).show()
        Log.e("FB" , "${token!!.token}")

        compositeDisposable.add( jsonAPI.facebbokLogin(token!!.token)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe( { user -> setUserIntoPrefrences(user)
                val intent = Intent(context, ArticlesActivity::class.java)
                startActivity(intent)
            } , { error  ->  Toast.makeText(context ,error.message ,
                Toast.LENGTH_LONG).show() }
            ) )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)

        if(requestCode == RC_SIGN_IN){

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Toast.makeText(context , task.isSuccessful.toString() , Toast.LENGTH_LONG).show()
            BackEndAuth(task)

        }
    }


    fun BackEndAuth (completedTask: Task<GoogleSignInAccount>){

        try {

            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account!!.idToken.toString()


            Toast.makeText(context , account!!.email, Toast.LENGTH_LONG).show()
            Log.e("Token" , idToken)
            println(idToken)
            Toast.makeText(context , account!!.idToken, Toast.LENGTH_LONG).show()


            compositeDisposable.add( jsonAPI.googleLogin(idToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    setUserIntoPrefrences(user)
                    val intent = Intent(context, ArticlesActivity::class.java)
                    startActivity(intent)
                } , { error  ->  Toast.makeText(context ,error.message ,
                    Toast.LENGTH_LONG).show() }
                ) )

        } catch (e: ApiException) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Toast.makeText(context , "$e" ,
                Toast.LENGTH_LONG).show()
        }


    }

    private fun googleSignup() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun setUserIntoPrefrences ( user : User) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        val gson = Gson()
        val userjson = gson.toJson(user , User::class.java)
        editor.putString("user", userjson).apply()
    }

}
