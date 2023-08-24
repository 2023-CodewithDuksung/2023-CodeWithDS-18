package com.example.a2023hackathon

import android.content.ContentValues.TAG
import android.graphics.Rect
import android.view.ViewGroup.LayoutParams
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.databinding.FragmentDetailCommunityBinding
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class DetailCommunityFragment : Fragment() {
    lateinit var binding: FragmentDetailCommunityBinding
    private var myName = MyApplication.email
    lateinit var itemList: MutableList<ItemCommunityModel>
    private lateinit var adapter: MyCommunityAdapter
    lateinit var subCode: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailCommunityBinding.inflate(inflater, container, false)

        subCode = arguments?.getString("sub_code").toString()
        Log.d("get테스트", ""+ subCode)

        // 어댑터를 설정하고 리사이클러뷰에 연결
        itemList = mutableListOf<ItemCommunityModel>()
        adapter = MyCommunityAdapter(itemList, myName)
        binding.communityRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.communityRecyclerview.adapter = adapter

        // 메세지 전송 버튼 클릭 시
        binding.editBtn.setOnClickListener {
            val message = binding.editTxt.text.toString().trim() // 전송을 할 때 텍스트를 받아오기-선언 위치 중요
            if(message.isEmpty()){
                Toast.makeText(requireContext(), "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                saveStore() //전송
                binding.editTxt.setText("") // 텍스트창 초기화
                // 어댑터 재실행
                getStore()
            }
        }

        // 메세지 입력 후 엔터 클릭 시에도 전송
        binding.editTxt.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                saveStore()
                binding.editTxt.setText("") // 텍스트창 초기화
                // 어댑터 재실행
                getStore()
                return@setOnEditorActionListener true
            }
            false
        }

        // 키보드가 활성화되면 리사이클러뷰의 크기 조정
        binding.communityRecyclerview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.communityRecyclerview.scrollToPosition(adapter.itemCount - 1)
        }

        MyApplication.db.collection("communities")
            .orderBy("time", Query.Direction.ASCENDING)
            //.whereEqualTo("sub_code", subCode)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                itemList = mutableListOf<ItemCommunityModel>()
                for (document in snapshot!!) {
                    val item = document.toObject(ItemCommunityModel::class.java)
                    if(subCode.equals(item.sub_code)){
                        item.docId = document.id
                        itemList.add(item)
                    }
                }

                adapter.setData(itemList) // 어댑터 데이터 갱신
                // 가장 아래의 아이템으로 스크롤
                binding.communityRecyclerview.scrollToPosition(adapter.itemCount - 1)
            }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        MyApplication.db.collection("communities")
            .orderBy("time", Query.Direction.ASCENDING)
            //.whereEqualTo("sub_code", subCode)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                itemList = mutableListOf<ItemCommunityModel>()
                for (document in snapshot!!) {
                    val item = document.toObject(ItemCommunityModel::class.java)
                    if(subCode.equals(item.sub_code)){
                        item.docId = document.id
                        itemList.add(item)
                    }
                }

                adapter.setData(itemList) // 어댑터 데이터 갱신
                // 가장 아래의 아이템으로 스크롤
                binding.communityRecyclerview.scrollToPosition(adapter.itemCount - 1)
            }
    }


    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        return format.format(date)
    }

    fun saveStore() {
        val data = mapOf(
            "text" to binding.editTxt.text.toString(),
            "time" to dateToString(Date()),
            "user" to myName,
            "sub_code" to subCode
        )

        MyApplication.db.collection("communities")
            .add(data)
            .addOnSuccessListener {
                Log.d("Chatting", "data firestore save ok")
            }
            .addOnFailureListener {
                Log.d("Chatting", "data firestore save error")
            }
    }

    fun getStore() {
        MyApplication.db.collection("communities")
            .orderBy("time", Query.Direction.ASCENDING)
            .whereEqualTo("sub_code", subCode)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                val itemList = mutableListOf<ItemCommunityModel>()
                for (document in snapshot!!) {
                    val item = document.toObject(ItemCommunityModel::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }

                adapter.setData(itemList) // 어댑터 데이터 갱신
            }
    }

}