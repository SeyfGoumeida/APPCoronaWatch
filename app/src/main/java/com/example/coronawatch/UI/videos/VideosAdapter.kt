package com.example.coronawatch.ui.videos

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.preference.PreferenceManager
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coronawatch.DataClases.*
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class VideosAdapter(val context: Context, private val videos: Videos, private val itemLayout: Int) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(p0.context).inflate(itemLayout, p0, false) , context)
    }

    override fun getItemCount(): Int {

        return videos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

        //holder.articleContent.text = articles[p1].content
        //--------------HTML TO TextView-----------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.videoTitle.setText(Html.fromHtml(videos[p1].title, Html.FROM_HTML_MODE_LEGACY))

        } else {
            holder.videoTitle.setText(Html.fromHtml(videos[p1].title));
        }
        val gson = Gson()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        holder.commentRV.layoutManager = LinearLayoutManager(context)
        holder.fetchComments(videos[p1].id)

        if (! videos[p1].path.isEmpty() ) {

            holder.videoImage.setVideoURI(Uri.parse(videos[p1].path))
            holder.videoImage.setMediaController(MediaController(context))
            holder.videoImage.requestFocus()
            holder.videoImage.start()

            //Picasso.get().load(videos[p1].path).into(holder.videoImage)
            //Glide.with(context).load(videos[p1].path).into(holder.videoImage);
            Log.e("image" , videos[p1].path.isEmpty().toString())
        }


        holder.commentBtn.setOnClickListener {
            if(holder.commentRV.isVisible){
                holder.commentRV.visibility=View.GONE
            }else{
                holder.commentRV.visibility=View.VISIBLE
            }
        }

        holder.submit.setOnClickListener{

            var userString : String  = preferences.getString("currentUser","{}")
            Log.e("currentUser", userString)
            val trimmed: String = userString.trim()
            Log.e("CurrnetUser", trimmed)
            var user : User = gson.fromJson(trimmed , User::class.java)

            if ( holder.content.text.toString() != "")

            { holder.addComment(videos[p1].id ,user.token , holder.content.text.toString())
                Handler().postDelayed({
                    holder.content.setText("")
                holder.fetchComments(videos[p1].id) } , 500) }

        }


        preferences.apply {

            var user: User
            var jsonString : String

            Handler().postDelayed({

                jsonString = preferences.getString("user${videos[p1].mobileuserid}","{}")
                user  = gson.fromJson(jsonString , User::class.java)
                holder.profileEmail.text = user.email
                holder.profileName.text = user.username

            } , 1000)


        }
    }

    class ViewHolder(v: View , val context: Context ) : RecyclerView.ViewHolder(v) {

        var videoTitle: TextView = v.findViewById(R.id.article_text_vidoes)
        var profileName: TextView = v.findViewById(R.id.profil_name_vidoes)
        var profileEmail: TextView = v.findViewById(R.id.profil_occupation_vidoes)
        var videoImage: VideoView = v.findViewById(R.id.article_picture_vidoes)
        var commentRV: RecyclerView = v.findViewById(R.id.comments_recyclerView_vidoes)
        var submit : Button =  v.findViewById(R.id.submit_comment_button_vidoes)
        var content : TextInputEditText =  v.findViewById(R.id.comment_content_vidoes)
        var commentBtn : Button=v.findViewById((R.id.comment_button_vidoes))
        var commentNbr : TextView=v.findViewById((R.id.comments_number_textview_vidoes))
        val compositeDisposable = CompositeDisposable()
        val retrofit = RetrofitClient.instance
        val jsonAPI = retrofit.create(IAPI::class.java)



        fun fetchComments (id:Int) {

            compositeDisposable.add( jsonAPI.getCommentsVideos(id)
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


        fun displayComments (comments: VideoComments) {
             Log.e("comments" , comments.toString())
            commentRV.adapter = VideoCommentsAdapter(comments , context)

        }

        fun addComment (id: Int, authorization : String, content :String) {

            val authorizationT = " Token " + authorization

            compositeDisposable.add( jsonAPI.addCommentVideo(id, authorizationT , content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ comment -> Log.e("add comment" , comment.toString() )
                }
            )
        }
    }
}