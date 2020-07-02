package com.example.coronawatch.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.coronawatch.Activities.MainActivity
import com.example.coronawatch.Activities.mMap
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.example.coronawatch.ui.add.LocationPrefrence
import com.google.android.gms.location.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add.*
import java.io.File

class AddFragment : Fragment() {


    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI: IAPI
    private val VIDEO_CAPTURE = 101

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var preferences: LocationPrefrence

    var adanService: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_add, container, false)
        val layoutsignaler = view.findViewById<View>(R.id.signalerLayout)
        preferences = LocationPrefrence(context!!)

        val latitude = preferences.getLat()
        val longitude = preferences.getLon()

        val lang = view.findViewById<TextView>(R.id.langitudeTextView)
        lang.text = longitude.toString()
        val lat = view.findViewById<TextView>(R.id.LatitudeTextView)

        lat.text = latitude.toString()
        val addPictureButton = view.findViewById<Button>(R.id.addPictureButton)

        addPictureButton.setOnClickListener{
            pickImageFromGallery()
        }
        val addVideoBtn = view.findViewById<Button>(R.id.addVideoBtn)

        addVideoBtn.setOnClickListener{
            layoutsignaler.visibility=View.VISIBLE
            //            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            //            startActivityForResult(intent, VIDEO_CAPTURE)
        }
        val moreinfo = view.findViewById<EditText>(R.id.otherInfoEditText).text.toString()
        val symp = view.findViewById<EditText>(R.id.symptomsEditText).text.toString()
        val add = view.findViewById<EditText>(R.id.addressEditText2).text.toString()


        val submitBtn = view.findViewById<Button>(R.id.submitBtn)
        submitBtn.setOnClickListener{
            val gson = Gson()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            var userString : String  = preferences.getString("currentUser","{}")
            Log.e("currentUser", userString)
            val trimmed: String = userString.trim()
            Log.e("currentUser", trimmed)
            var user : User = gson.fromJson(trimmed , User::class.java)

            signaler(view,"Token "+user.token,latitude.toDouble(),longitude.toDouble(),R.raw.reporttest,moreinfo,add,symp)
        }

        return view
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(context, data?.data.toString(), Toast.LENGTH_LONG).show()
        imageView3.setImageURI(data?.data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView3.setImageURI(data?.data)
            Toast.makeText(context, data?.data.toString(), Toast.LENGTH_LONG).show()

        }
    }



    /** Check if this device has a camera */
    private fun hasCamera(): Boolean {
        return context!!.packageManager.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY)
    }
    fun signaler(view: View,token:String,lat:Double,lng:Double,attach:Int,moreinfo:String,add:String,symp:String){
        val retrofit = RetrofitClient.instance
        jsonAPI= retrofit.create(IAPI::class.java)
        compositeDisposable.add( jsonAPI.addreport(token,attach,symp,add,lat,lng,moreinfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    Toast.makeText(context, "Report Done !!!", Toast.LENGTH_LONG).show()
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val videoUri = data!!.data
//        val gson = Gson()
//        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//        if (requestCode == VIDEO_CAPTURE) {
//            if (resultCode == Activity.RESULT_OK) {
//
//                var userString : String  = preferences.getString("currentUser","{}")
//                val trimmed: String = userString.trim()
//                var user : User = gson.fromJson(trimmed , User::class.java)
//                val retrofit = RetrofitClient.instance
//                jsonAPI = retrofit.create(IAPI::class.java)
//                //Toast.makeText(context, user.token, Toast.LENGTH_LONG).show()
//                //toast a normal  uri video then use the same directory with ur selected static video
//                //videoUri="ur static uri"
//                Toast.makeText(context, videoUri.path, Toast.LENGTH_LONG).show()
//
//
//                compositeDisposable.add( jsonAPI.addVideo(user.token , "testing" ,videoUri.path)
//                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
//
//                        Toast.makeText(context, "Video saved to:\n"
//                                + videoUri.path, Toast.LENGTH_LONG).show()
//
//                    } , { error  ->  Toast.makeText(context ,error.message ,
//                        Toast.LENGTH_LONG).show() }
//                    ) )
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(context, "Video recording cancelled.", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(context, "Failed to record video", Toast.LENGTH_LONG).show() }
//        }
//    }


}
