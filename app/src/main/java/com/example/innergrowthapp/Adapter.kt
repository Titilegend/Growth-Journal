package com.example.innergrowthapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.innergrowthapp.databinding.JournalItemsBinding


class Adapter(private val dataList:List<JournalEntries>):RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(val adapterBinding:JournalItemsBinding)
        :RecyclerView.ViewHolder(adapterBinding.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = JournalItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        holder.adapterBinding.dateTextView.text = dataList[position].date
        holder.adapterBinding.titleTextView.text = dataList[position].title
        holder.adapterBinding.descriptionTextView.text = dataList[position].description


    }

    override fun getItemCount(): Int {
        return  dataList.size
    }
    fun getJournalId(position:Int):String{
        return dataList[position].journalId
    }


}