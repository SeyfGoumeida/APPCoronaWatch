package com.example.coronawatch.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch.APIHandler
import com.example.coronawatch.DataClases.Articles
import com.example.coronawatch.DataClases.Redactor
import com.example.coronawatch.MainActivity
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_loader.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //lateinit var apiHandler : APIHandler
    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI:IAPI



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //apiHandler = context?.let { APIHandler(it) }!!
         val gson = Gson()
         var preferences = PreferenceManager.getDefaultSharedPreferences(context)
         val editor = preferences.edit()
         var retrofit = RetrofitClient.instance
         jsonAPI = retrofit.create(IAPI::class.java)

        rv_home.layoutManager = LinearLayoutManager(context)
        fetchArticles()
       // rv_home.adapter = ArticlesAdapter(apiHandler.getArticles(), R.layout.rv_home_article)


    }

    private fun fetchArticles() {

        compositeDisposable.add( jsonAPI.articles
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{articles ->

                articles.forEach {  article ->
                    fetchRedactor(article.redactor)
                }
                displayArticles (articles) }

        )
    }

    private fun fetchRedactor (id:Int) {

        compositeDisposable.add( jsonAPI.getRedactorDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ redactor ->  setRedactorIntoPrefrences(redactor) }

        )


    }


    private fun setRedactorIntoPrefrences ( redactor : Redactor ) {
        val gson = Gson()
        var preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString("redactor${redactor.profile_id}", "$redactor").apply()
    }


    private fun displayArticles (articles : Articles ) {

        rv_home.adapter = ArticlesAdapter( articles , R.layout.rv_home_article )

    }

    override fun onResume() {

        super.onResume()
    }

    inner class ArticlesAdapter(private val articles: Articles , private val itemLayout : Int) :
        RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            Log.e("inside create " , articles[p1].content )
            return ViewHolder(LayoutInflater.from(p0.context).inflate(itemLayout, p0, false))
        }

        override fun getItemCount(): Int {
            Log.e("inside adapter " , articles.size.toString())
            return articles.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

            Log.e("inside binder " , articles[p1].content )
            Log.e("inside binder", "------------------------------------------------------------------------------------------")
            holder.articleContent.text = articles[p1].content
            val gson = Gson()
            var preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()

                preferences.apply {



                    var redactor: Redactor
                    lateinit var jsonString : String
                    Handler().postDelayed({

                            jsonString = preferences.getString("redactor${articles[p1].redactor}","{}")
                            Log.e("--------------", jsonString)
                            redactor  = gson.fromJson(jsonString , Redactor::class.java)
                            Log.e("--------------", redactor.email)
                            holder.profileEmail.text = redactor.email
                            holder.profileName.text = redactor.username

                    } , 0)


                    if (! articles[p1].attachments.isEmpty() )
                    Picasso.get().load(articles[p1].attachments[0].path).into(holder.articleImage);


                }
        }


        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            var articleContent: TextView = v.findViewById(R.id.article_text)
            var profileName: TextView = v.findViewById(R.id.profil_name)
            var profileEmail: TextView = v.findViewById(R.id.profil_occupation)
            var articleImage: ImageView = v.findViewById(R.id.article_picture)


        }
    }



}
