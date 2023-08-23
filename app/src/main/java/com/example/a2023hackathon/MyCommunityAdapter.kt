package com.example.a2023hackathon

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemCommunityMeBinding
import com.example.a2023hackathon.databinding.ItemCommunityOtherBinding

class MyCommunityViewHolder(val binding: ItemCommunityMeBinding) : RecyclerView.ViewHolder(binding.root)
class OtherCommunityViewHolder(val binding: ItemCommunityOtherBinding) : RecyclerView.ViewHolder(binding.root)

class MyCommunityAdapter(private val itemList: MutableList<ItemCommunityModel>, private val myName: String? = null):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == 0) {
            return MyCommunityViewHolder(ItemCommunityMeBinding.inflate(layoutInflater))
        } else {
            return OtherCommunityViewHolder(ItemCommunityOtherBinding.inflate(layoutInflater))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].name == myName) {
            0 // 로그인한 사용자의 이름과 일치하는 경우 (내 채팅)
        } else {
            1 // 로그인한 사용자의 이름과 다른 경우 (다른 사람 채팅)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        val data = itemList.get(position)

        if (holder is MyCommunityViewHolder) {
            holder.binding.run {
                communityMessage.text = data.text
                messageTime.text = data.time
            }
        } else if (holder is OtherCommunityViewHolder) {
            holder.binding.run {
                communityUser.text = data.name
                communityMessage.text = data.text
                messageTime.text = data.time
            }
        }
    }
}