package com.example.coronawatch.ui.home

import android.content.Context
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch.DataClases.Articles
import com.example.coronawatch.DataClases.Redactor
import com.example.coronawatch.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class ArticlesAdapter(private val context : Context? , private val articles: Articles, private val itemLayout : Int) :
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
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)


        preferences.apply {

            var redactor: Redactor
            var jsonString : String

            Handler().postDelayed({

                jsonString = preferences.getString("redactor${articles[p1].redactor}","{}")
                Log.e("--------------", jsonString)
                redactor  = gson.fromJson(jsonString , Redactor::class.java)
                Log.e("--------------", redactor.email)
                holder.profileEmail.text = redactor.email
                holder.profileName.text = redactor.username

            } , 1000)

            if (! articles[p1].attachments.isEmpty() )
                Picasso.get().load(articles[p1].attachments[0].path).into(holder.articleImage)

        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var articleContent: TextView = v.findViewById(R.id.article_text)
        var profileName: TextView = v.findViewById(R.id.profil_name)
        var profileEmail: TextView = v.findViewById(R.id.profil_occupation)
        var articleImage: ImageView = v.findViewById(R.id.article_picture)

    }
}