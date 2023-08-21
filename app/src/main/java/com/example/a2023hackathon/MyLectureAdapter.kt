package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemLectureBinding
import com.example.a2023hackathon.databinding.ItemTaskBinding

class MyLectureViewHolder(val binding: ItemLectureBinding): RecyclerView.ViewHolder(binding.root)

class MyLectureAdapter(val context: Context, val itemList: MutableList<ItemLectureModel>): RecyclerView.Adapter<MyLectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLectureViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyLectureViewHolder(ItemLectureBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyLectureViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            itemTermView.text = data.term
            itemNameView.text = data.name
            itemMajorView.text = data.major
            itemProfessorView.text = data.professor

            itemNameView.setOnClickListener{
                val bundle : Bundle = Bundle()
                bundle.putString("major", data.major)
                bundle.putString("professor", data.professor)

                Intent(context, LectureDetailActivity::class.java).apply{
                    putExtras(bundle)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}
