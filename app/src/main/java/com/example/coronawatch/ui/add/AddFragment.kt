package com.example.coronawatch.ui.add

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.fragment.app.Fragment
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment()  {


    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI: IAPI
    private val VIDEO_CAPTURE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addVideoBtn.setOnClickListener{

            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(intent, VIDEO_CAPTURE)
        }
    }

    /** Check if this device has a camera */
    private fun hasCamera(): Boolean {
        return context!!.packageManager.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val videoUri = data!!.data
        val gson = Gson()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {

                var userString : String  = preferences.getString("currentUser","{}")
                val trimmed: String = userString.trim()
                var user : User = gson.fromJson(trimmed , User::class.java)
                val retrofit = RetrofitClient.instance
                jsonAPI = retrofit.create(IAPI::class.java)
                //Toast.makeText(context, user.token, Toast.LENGTH_LONG).show()
                //toast a normal  uri video then use the same directory with ur selected static video
                //videoUri="ur static uri"
                Toast.makeText(context, videoUri.path, Toast.LENGTH_LONG).show()


                compositeDisposable.add( jsonAPI.addVideo(user.token , "testing" ,videoUri.path)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        Toast.makeText(context, "Video saved to:\n"
                                + videoUri.path, Toast.LENGTH_LONG).show()

                    } , { error  ->  Toast.makeText(context ,error.message ,
                        Toast.LENGTH_LONG).show() }
                    ) )
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Video recording cancelled.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to record video", Toast.LENGTH_LONG).show() }
        }
    }
}
