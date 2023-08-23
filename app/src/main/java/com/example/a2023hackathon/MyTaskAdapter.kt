package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemTaskBinding

class MyTaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

class MyTaskAdapter(val context: List<ItemTaskModel>, val itemList: MutableList<ItemTaskModel>): RecyclerView.Adapter<MyTaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyTaskViewHolder(ItemTaskBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        val data = itemList[position]

        holder.binding.run {
            itemTitleView.text = data.title
            itemContentView.text = data.content
            itemDateView.text = data.date
            itemMajorView.text = data.major
            itemProfessorView.text = data.professor
            itemDeadlineView.text = data.d_date

            itemTitleView.setOnClickListener{
                val bundle : Bundle = Bundle()
                bundle.putString("title", data.title)
                bundle.putString("content", data.content)
                bundle.putString("date", data.date)
                bundle.putString("major", data.major)
                bundle.putString("professor", data.professor)
                bundle.putString("s_date", data.s_date)
                bundle.putString("d_date", data.d_date)
            }
        }
    }
}
