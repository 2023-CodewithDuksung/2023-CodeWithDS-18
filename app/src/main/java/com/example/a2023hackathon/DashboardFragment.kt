package com.example.a2023hackathon

import TodoAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.Query

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    private val name = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentFourBinding.inflate(inflater, container, false)
//
//        //         myCheckPermission(requireActivity() as AppCompatActivity)
//
//        binding.mainFab.setOnClickListener {
//            if(MyApplication.checkAuth()){
//                val intent = Intent(requireContext(), AddActivity::class.java)
//                startActivity(intent)
//            }
//            else {
//                Toast.makeText(requireContext(), "인증을 진행해 주세요", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        return binding.root
//    }

    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth()){
            MyApplication.db.collection("tasks")
                .orderBy("d_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemTaskModel>()
                    for(document in result){
                        val item = document.toObject(ItemTaskModel::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.todoRecyclerView.adapter = TodoTaskAdapter(requireContext(),itemList)
                    Log.d("2023Hackathon", "${itemList}")
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
}