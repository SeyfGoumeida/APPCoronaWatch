package com.example.coronawatch.DataClases


import android.content.Context
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch.R
import com.google.gson.Gson

class VideoYoutubeCommentsAdapter(  private val comments: ArrayList<YoutubeVideoComment>,val context: Context ):
    RecyclerView.Adapter<VideoYoutubeCommentsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view , context)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.initialize(comments[position])
    }
    class ViewHolder(item: View, val context: Context): RecyclerView.ViewHolder(item){
        var name: TextView = item.findViewById(R.id.comment_content)
        var date: TextView = item.findViewById(R.id.comment_date)
        var user: TextView = item.findViewById(R.id.user_name)
        var email: TextView = item.findViewById(R.id.user_email)
        fun initialize(item: YoutubeVideoComment){

            val gson = Gson()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            name.text = item.content
            date.text = item.date

            preferences.apply {
                var userJ: User
                var jsonString : String
                Handler().postDelayed({
                    jsonString = preferences.getString("user${item.mobileuserid}","{}")
                    userJ  = gson.fromJson(jsonString , User::class.java)
                    email.text = userJ.email
                    user.text = userJ.username
                } , 1000)
            }
        }
    }

}

