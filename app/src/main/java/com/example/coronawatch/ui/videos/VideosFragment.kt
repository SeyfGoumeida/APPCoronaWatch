package com.example.coronawatch.UI.videos

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coronawatch.DataClases.User
import com.example.coronawatch.DataClases.Videos
import com.example.coronawatch.DataClases.YoutubeVideos
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.example.coronawatch.ui.videos.YoutubeVideoAdapter
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
        var videoTypes : ArrayList<String> = ArrayList()
        videoTypes.add("Videos")
        videoTypes.add("Youtube")

        val selectTypeVideo = view!!.findViewById(R.id.videoTypeSpinner) as Spinner
        selectTypeVideo.adapter= ArrayAdapter<String>(context!!,android.R.layout.simple_list_item_1,videoTypes)

        rv_videos.layoutManager = LinearLayoutManager(context)
        selectTypeVideo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "اختر نوع الفيديو", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0)
                {
                    fetchVideos()
                } else{
                    fetchYoutubeVideos()
                }
            }
        }
    }

    private fun fetchVideos() {
        compositeDisposable.add( jsonAPI.videos.subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread())
            .subscribe{videos ->
                videos.forEach {  video ->
                    fetchMobileUser(video.mobileuserid)
                }
                displayVideos(videos)
            }
        )
    }
    private fun fetchYoutubeVideos() {
        compositeDisposable.add( jsonAPI.getyoutubevideos("youtube").subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread())
            .subscribe({videos ->
                displayYoutubeVideos(videos)
            },{erreur ->
                Toast.makeText(context, erreur.message, Toast.LENGTH_LONG).show()

            }
            )

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

    private fun displayVideos (videos : Videos) {
        rv_videos.adapter = context?.let {
            VideosAdapter(it, videos, R.layout.rv_video)
        }
    }
    private fun displayYoutubeVideos (videos : YoutubeVideos) {

        rv_videos.adapter = context?.let {
            YoutubeVideoAdapter(it, videos, R.layout.rv_youtube_video)

        }
    }
    }
