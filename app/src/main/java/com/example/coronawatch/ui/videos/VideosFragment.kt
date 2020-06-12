package com.example.coronawatch.ui.videos

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.DataClases.Videos
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.example.coronawatch.ui.videos.VideosAdapter
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_videos.*

class VideosFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI: IAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)

        rv_videos.layoutManager = LinearLayoutManager(context)
        fetchVideos()

    }

    private fun fetchVideos() {

        compositeDisposable.add( jsonAPI.videos.subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread())
            .subscribe{videos ->
                videos.forEach {  video ->
                    fetchMobileUser(video.mobileuserid)
                }
                displayArticles (videos)
            }
        )
    }

    private fun fetchMobileUser (id:Int) {

        compositeDisposable.add( jsonAPI.getUserDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ user -> setUserIntoPrefrences(user) }
        )
    }

    private fun setUserIntoPrefrences ( user : User) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        val gson = Gson()
        val userjson = gson.toJson(user , User::class.java)
        editor.putString("user${user.profile_id}", userjson).apply()
    }



    private fun displayArticles (videos : Videos) {
        rv_videos.adapter = context?.let { VideosAdapter(it, videos , R.layout.rv_video ) }

    }

    }
