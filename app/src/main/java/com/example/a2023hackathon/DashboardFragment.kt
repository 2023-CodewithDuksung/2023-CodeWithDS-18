//package com.example.a2023hackathon
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.a2023hackathon.databinding.FragmentDashboardBinding
//import com.example.a2023hackathon.databinding.FragmentNotificationsBinding
//
//class DashboardFragment : Fragment() {
//
//    lateinit var binding: FragmentDashboardBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentDashboardBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }
////}
//
package com.example.a2023hackathon

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

    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth()){
            MyApplication.db.collection("users").document(MyApplication.auth.uid.toString()).collection("mytasks")
                .orderBy("d_date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemTaskModel>()
                    for(document in result){
                        val item = document.toObject(ItemTaskModel::class.java)
                        item.docId = document.id
                        Log.d("todo", "todo 받아오는 중")
                        binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.todoRecyclerView.adapter = TodoTaskAdapter(requireContext(),itemList)
//
//                        if(item.state == "1" ){
//                            binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                            binding.todoRecyclerView.adapter = TodoTaskAdapter(requireContext(),itemList)
//
//                        }
//                        else{
//                            binding.doneRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                            binding.doneRecyclerView.adapter = DoneTaskAdapter(requireContext(),itemList)
//                            Log.d("2023Hackathon", "doneTaskAdapter 구현 ")
//                            Log.d("2023Hackathon", "${item.state}")
//                        }

                        itemList.add(item)

                        TodoTaskAdapter(requireContext(),itemList).moveItemToLast()

                        TodoTaskAdapter(requireContext(),itemList).notifyDataSetChanged()
                    }

                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
}


//
//package com.example.a2023hackathon
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.a2023hackathon.MyApplication.Companion.auth
//import com.example.a2023hackathon.databinding.FragmentDashboardBinding
//import com.example.a2023hackathon.databinding.FragmentDetailTaskBinding
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ListenerRegistration
//import com.google.firebase.firestore.Query
//import kotlin.math.log
//
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
//class DashboardFragment : Fragment() {
//
//    private var param1: String? = null
//    private var param2: String? = null
//    lateinit var major: String
//    lateinit var professor: String
//    lateinit var subCode: String
//    lateinit var state: String
//    private lateinit var itemList: MutableList<ItemTaskModel>
//    private lateinit var todoTaskAdapter: TodoTaskAdapter
////    private lateinit var doneTaskAdapter: DoneTaskAdapter
//
//
//    lateinit var binding: FragmentDashboardBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//            state = it.getString("state") ?: "1"
//        }
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentDashboardBinding.inflate(inflater, container, false)
//
//        itemList = mutableListOf() // itemList 초기화
//
//        // arguments에서 state 값을 가져와 초기화
//        state = arguments?.getString("state") ?: "1"
//
//        // 어댑터 초기화
//        todoTaskAdapter = TodoTaskAdapter(requireContext(), itemList)
////        doneTaskAdapter = DoneTaskAdapter(requireContext(), itemList)
//
//        // RecyclerView에 어댑터 설정
//        if (state == "1") {
//            binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//            binding.todoRecyclerView.adapter = todoTaskAdapter
//        } else {
//            binding.doneRecyclerView.layoutManager = LinearLayoutManager(requireContext())
////            binding.doneRecyclerView.adapter = doneTaskAdapter
//        }
//        return binding.root
//    }
//
//    // Firestore 리스너
//    private var firestoreListener: ListenerRegistration? = null
//
//    // Firestore 참조
//    private val db = FirebaseFirestore.getInstance()
//    private val collectionReference = db.collection("users").document(auth.uid.toString()).collection("mytasks")
//
//    override fun onStart() {
//        super.onStart()
//        // 주석 처리한 코드를 주석 해제하고 isChecked 변수를 사용하여 어댑터를 선택적으로 설정
//        if (MyApplication.checkAuth()) {
//            MyApplication.db.collection("users").document(auth.uid.toString()).collection("mytasks")
//                .orderBy("d_date", Query.Direction.ASCENDING)
//                .get()
//                .addOnSuccessListener { result ->
//                    val itemList = mutableListOf<ItemTaskModel>()
//                    for (document in result) {
//                        val item = document.toObject(ItemTaskModel::class.java)
//                        item.docId = document.id
//                        itemList.add(item)
//                    }
//
//                    // state 값에 따라 어댑터 선택
////                    val adapter = if (state == "1") {
////                        todoTaskAdapter // Todo 어댑터
////                    } else {
////                        todoTaskAdapter
////                        Log.d("2023Hackathon", "doneTaskAdapter")
//////                        doneTaskAdapter // Done 어댑터
////                    }
//
//                    // RecyclerView에 어댑터 설정
//                    if (state == "1") {
//                        binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                        binding.todoRecyclerView.adapter = todoTaskAdapter // 선택된 어댑터 설정
//                        Log.d("2023Hackathon", "if work success")
//                    } else {
//                        binding.doneRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                        binding.doneRecyclerView.adapter = todoTaskAdapter // 선택된 어댑터 설정
//                        Log.d("2023Hackathon", "doneTaskAdapter")
//                    }
//
//                    Log.d("2023Hackathon", "${itemList}")
//                }
//                .addOnFailureListener {
//                    Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    // Firestore 리스너 설정
//    // Firestore 리스너 설정
//    private fun setupFirestoreListener() {
//        firestoreListener = collectionReference.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                // 오류 처리
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && !snapshot.isEmpty) {
//                // Firestore에서 데이터가 변경될 때 호출되는 부분
//                // 변경된 데이터를 처리하고 RecyclerView를 업데이트
//                val updatedItems = snapshot.toObjects(ItemTaskModel::class.java)
//
//                // "done" 상태와 "In progress" 상태를 분류하여 RecyclerView 어댑터 업데이트
//                val doneItems = updatedItems.filter { it.state == "0" }
//                val inProgressItems = updatedItems.filter { it.state == "1" }
//
//                // RecyclerView 어댑터의 데이터 목록
//                val todoItemList: MutableList<ItemTaskModel> = mutableListOf()
//                val doneItemList: MutableList<ItemTaskModel> = mutableListOf()
//
//                // 데이터 분류
//                todoItemList.clear()
//                todoItemList.addAll(inProgressItems)
//
//                doneItemList.clear()
//                doneItemList.addAll(doneItems)
//
//                // RecyclerView 어댑터 업데이트
//                if (state == "1") {
//                    binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                    binding.todoRecyclerView.adapter = todoTaskAdapter
//                    todoTaskAdapter.updateData(todoItemList)
//                } else {
//                    binding.doneRecyclerView.layoutManager = LinearLayoutManager(requireContext())
////                    binding.doneRecyclerView.adapter = doneTaskAdapter
////                    doneTaskAdapter.updateData(doneItemList)
//                }
//            }
//        }
//    }
//
//
//    // Firestore 리스너 해제
//    private fun removeFirestoreListener() {
//        firestoreListener?.remove()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        setupFirestoreListener()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        removeFirestoreListener()
//    }
//}