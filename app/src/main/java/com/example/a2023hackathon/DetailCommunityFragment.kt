package com.example.a2023hackathon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.databinding.FragmentDetailCommunityBinding

class DetailCommunityFragment : Fragment() {
    lateinit var binding: FragmentDetailCommunityBinding
    private var myName = MyApplication.email

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailCommunityBinding.inflate(inflater, container, false)

        // 어댑터를 설정하고 리사이클러뷰에 연결
        val itemList = mutableListOf<ItemCommunityModel>()
        val adapter = MyCommunityAdapter(itemList, myName)
        binding.communityRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.communityRecyclerview.adapter = adapter

        // 메세지 전송 버튼 클릭 시
        binding.editBtn.setOnClickListener {

        }

        return binding.root
    }

}