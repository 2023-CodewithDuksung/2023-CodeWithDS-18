package com.example.a2023hackathon

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemCommunityMeBinding
import com.example.a2023hackathon.databinding.ItemCommunityOtherBinding

private val userNumberMap = mutableMapOf<String, Int>()

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
        return if (itemList[position].user == myName) {
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
                communityMessage.gravity = Gravity.RIGHT
            }
        } else if (holder is OtherCommunityViewHolder) {
            holder.binding.run {
                communityMessage.gravity = Gravity.LEFT
                communityUser.text = "익명"
                val userName = data.user
                val userNumber = userNumberMap.getOrPut(userName.toString()) {
                    // 새로운 사용자일 경우 새로운 숫자 부여
                    userNumberMap.size + 1
                }
                communityUser.text = "익명$userNumber"
                communityMessage.text = data.text
                messageTime.text = data.time
            }
        }
    }

    fun setData(list: List<ItemCommunityModel>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}