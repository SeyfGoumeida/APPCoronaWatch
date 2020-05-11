package com.example.coronawatch.ui.home

import android.os.Bundle

import android.preference.PreferenceManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coronawatch.DataClases.*

import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI:IAPI

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

         val retrofit = RetrofitClient.instance
         jsonAPI = retrofit.create(IAPI::class.java)

        rv_home.layoutManager = LinearLayoutManager(context)
        fetchArticles()
    }


// Functions ---------------------------------------------------------------------------------------------------------------------

    private fun fetchArticles() {

        compositeDisposable.add( jsonAPI.articles.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe{articles ->
                articles.forEach {  article ->
                    fetchRedactor(article.redactor)
                }
                displayArticles (articles)
               }
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
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString("redactor${redactor.profile_id}", "$redactor").apply()
    }

    private fun displayArticles (articles : Articles ) {
        rv_home.adapter = ArticlesAdapter( context ,articles , R.layout.rv_home_article )

    }

}
