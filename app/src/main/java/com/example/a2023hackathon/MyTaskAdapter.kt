package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemTaskBinding

class MyTaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

class MyTaskAdapter(val context: Context, val itemList: MutableList<ItemTaskModel>?): RecyclerView.Adapter<MyTaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyTaskViewHolder(ItemTaskBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        val data = itemList!!.get(position)

        holder.binding.run {
            itemTitleView.text = data.title
            itemContentView.text = data.content
            itemDateView.text = data.date
            itemSdateView.text = data.s_date.toString()
            itemDeadlineView.text = data.d_date.toString()
            itemMajorView.text = data.major
            itemProfessorView.text = data.professor
            itemCodeView.text = data.sub_code
        }
    }
}