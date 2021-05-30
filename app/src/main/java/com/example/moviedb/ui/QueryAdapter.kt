package com.example.moviedb.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentSearchBinding
import com.example.moviedb.queryDb.QueryItem
import com.example.moviedb.queryDb.QueryRepo

class QueryAdapter(var recents: List<QueryItem>) : RecyclerView.Adapter<MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : FragmentSearchBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recents.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(recents[position])
    }

}

class MyViewHolder(val binding: FragmentSearchBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(query: QueryItem){
        binding.queryInfo = query
        binding.textView4.text = query.query_name
        //binding.imageView.setOnClickListener{
            Log.d("deneme","Silme butonu")

        //}

        binding.executePendingBindings()
    }

}