package com.example.lab3.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.R
import com.example.lab3.database.entity.courtsReviews
import com.example.lab3.databinding.ListItemBinding
import com.example.lab3.layoutInflater


class CourtAdapter (private val  courtRevList:List<courtsReviews>) :
    RecyclerView.Adapter<CourtAdapter.CourtViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtViewHolder {
        val itemView = ListItemBinding.inflate(parent.context.layoutInflater, parent, false)
        return CourtViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return courtRevList.size
    }

    override fun onBindViewHolder(holder: CourtViewHolder, position: Int) {
        val curRev = courtRevList[position]
        holder.rating.rating = curRev.rating.toFloat()
        holder.review.text = curRev.review


    }
    inner class CourtViewHolder(private val binding: ListItemBinding) :RecyclerView.ViewHolder(binding.root){
        val rating : RatingBar = itemView.findViewById(R.id.perRating)
        val review : TextView= itemView.findViewById(R.id.review)
    }

}