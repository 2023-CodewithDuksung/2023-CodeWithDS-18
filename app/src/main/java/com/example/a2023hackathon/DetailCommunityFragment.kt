package com.example.a2023hackathon

import android.content.ContentValues.TAG
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.ViewGroup.LayoutParams
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

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
            saveStore() //전송
            binding.editTxt.setText("") // 텍스트창 초기화
            // 어댑터 재실행
            getStore()
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

//        MyApplication.db.collection("communities")
//            .orderBy("time", Query.Direction.ASCENDING)
//            .get()
//            .addOnSuccessListener { result ->
//                val itemList = mutableListOf<ItemCommunityModel>()
//                for(document in result){
//                    val item = document.toObject(ItemCommunityModel::class.java)
//                    Log.d("get테스트", ""+ item.sub_code + item.docId + item.text + item.user)
//                    if(subCode.equals(item.sub_code)){
//                        item.docId = document.id
//                        itemList.add(item)
//                    }
//                }
//                binding.communityRecyclerview.layoutManager = LinearLayoutManager(requireContext())
//                binding.communityRecyclerview.adapter = MyCommunityAdapter(itemList, myName)
//            }
//            .addOnFailureListener{
//                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
//            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootView = binding.root

        rootView.viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                // 리사이클러뷰의 원하는 높이 계산
                val recyclerViewParams = binding.communityRecyclerview.layoutParams as LayoutParams
                recyclerViewParams.height = screenHeight - binding.editLayout.height

                // 리사이클러뷰의 레이아웃 매개변수 업데이트
                binding.communityRecyclerview.layoutParams = recyclerViewParams

                scrollToBottom() // 필요한 경우 하단으로 스크롤
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        initGlobalLayoutListener()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun initGlobalLayoutListener() {
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            // 뷰의 레이아웃이 변경될 때마다 호출되는 코드 작성
            // 여기에서 뷰의 크기나 위치 등을 확인하거나 처리할 수 있음
        }
    }

    private fun scrollToBottom() {
        binding.communityRecyclerview.post {
            binding.communityRecyclerview.scrollToPosition(adapter.itemCount - 1)
        }
    }

}