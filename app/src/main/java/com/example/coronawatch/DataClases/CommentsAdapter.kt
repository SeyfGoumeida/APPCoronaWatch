package com.example.coronawatch.DataClases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch.R


    class CommentsAdapter(private val comments: ArrayList<CommentsItem>):
        RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return comments.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.initialize(comments[position])
        }
        class ViewHolder(item: View): RecyclerView.ViewHolder(item){
            var name: TextView = item.findViewById(R.id.comment_content)

            fun initialize(item:CommentsItem){
                name.text=item.content
            }
        }

    }
