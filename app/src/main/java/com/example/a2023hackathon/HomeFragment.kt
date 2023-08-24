package com.example.a2023hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.databinding.FragmentHomeBinding
import com.example.a2023hackathon.databinding.FragmentNotificationsBinding
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        MyApplication.db.collection("lectures")
            .orderBy("term", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemLectureModel>()
                for(document in result){
                    val item = document.toObject(ItemLectureModel::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.feedRecyclerView.adapter = MyLectureAdapter(requireContext(), itemList)
//                Toast.makeText(context, "내강의리스트 가져오기 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }
}