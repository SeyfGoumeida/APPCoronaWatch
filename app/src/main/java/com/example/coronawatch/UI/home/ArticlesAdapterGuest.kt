package com.example.coronawatch.ui.home

import android.content.Context
import android.os.Build
import android.os.Handler
import android.preference.PreferenceManager
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coronawatch.DataClases.*
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ArticlesAdapterGuest(val context : Context, private val articles: Articles, private val itemLayout : Int) :
    RecyclerView.Adapter<ArticlesAdapterGuest.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(p0.context).inflate(itemLayout, p0, false) , context)
    }

    override fun getItemCount(): Int {

        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

        //holder.articleContent.text = articles[p1].content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.articleContent.setText(Html.fromHtml(articles[p1].content, Html.FROM_HTML_MODE_LEGACY))

        } else {
            holder.articleContent.setText(Html.fromHtml(articles[p1].content));
        }
        val gson = Gson()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        holder.commentRV.layoutManager = LinearLayoutManager(context)
        holder.fetchComments(articles[p1].id)
        if (! articles[p1].attachments.isEmpty() ) {

            Glide.with(context)
                .load(articles[p1].attachments[0].path)
                .into(holder.articleImage)

            //Picasso.get().load().into()
            Log.e("image${articles[p1].attachments.isEmpty()}" , articles[p1].attachments[0].path )
        }

        holder.commentBtn.setOnClickListener {
            if(holder.commentRV.isVisible){
                holder.commentRV.visibility=View.GONE
            }else{
                holder.commentRV.visibility=View.VISIBLE
            }
        }


        preferences.apply {

            var redactor: Redactor
            var jsonString : String

            Handler().postDelayed({

                jsonString = preferences.getString("redactor${articles[p1].redactor}","{}")
                redactor  = gson.fromJson(jsonString , Redactor::class.java)
                holder.profileEmail.text = redactor.email
                holder.profileName.text = redactor.username

            } , 1000)


        }
    }

    class ViewHolder(v: View , val context: Context ) : RecyclerView.ViewHolder(v) {

        var articleContent: TextView = v.findViewById(R.id.article_text)
        var profileName: TextView = v.findViewById(R.id.profil_name)
        var profileEmail: TextView = v.findViewById(R.id.profil_occupation)
        var articleImage: ImageView = v.findViewById(R.id.article_picture)
        var commentRV: RecyclerView = v.findViewById(R.id.comments_recyclerView)
        var commentBtn :Button=v.findViewById((R.id.comment_button))
        var commentNbr : TextView=v.findViewById((R.id.comments_number_textview))
        val compositeDisposable = CompositeDisposable()
        val retrofit = RetrofitClient.instance
        val jsonAPI = retrofit.create(IAPI::class.java)


        fun fetchComments (id:Int) {

            compositeDisposable.add( jsonAPI.getComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ comments ->

                   comments.forEach {  comment ->

                       Log.e("mobile user" , comment.mobileuserid.toString())
                        fetchUser(comment.mobileuserid)
                    }

                    displayComments(comments)
                    commentNbr.text=comments.size.toString()
                }
            )
        }

        private fun fetchUser(id:Int) {

            compositeDisposable.add( jsonAPI.getUserDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ user -> setUserIntoPrefrences (user) }
            )
        }

        private fun setUserIntoPrefrences ( user : User) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            val gson = Gson()
            val userjson = gson.toJson(user, User::class.java)
            editor.putString("user${user.profile_id}", userjson).apply()
        }


        fun displayComments (comments: Comments) {
             Log.e("comments" , comments.toString())
            commentRV.adapter = CommentsAdapter(comments , context)
        }



        }

}