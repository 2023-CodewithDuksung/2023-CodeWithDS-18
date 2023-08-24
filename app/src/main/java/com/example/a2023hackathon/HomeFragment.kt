package com.example.a2023hackathon

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.MyApplication.Companion.auth
import com.example.a2023hackathon.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var selectedDate: String = "0"
    private lateinit var presentSemester: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance() // 일단 현재 날짝 가져옴
            calendar.set(year, month, dayOfMonth) // 사용자가 선택한 날짜로 Calendar 객체를 업데이트

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            selectedDate = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            updateLectureListForSelectedDate()
        }

        binding.btnAddlecture.setOnClickListener {
            val intent = Intent(requireContext(), AddLectureActivity::class.java)
            startActivity(intent)
        }

        binding.menuSearch.setOnClickListener {
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val mylecturefragment: Fragment = SearchFragment()
            transaction.replace(R.id.main_layout, mylecturefragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        if(MyApplication.checkAuth()){
            binding.greeting.text = "${MyApplication.email}님 환영합니다!"
        } else {
            binding.greeting.text = "로그인 혹은 회원가입을 진행해주세요."
        }

        val itemList = mutableListOf<ItemLectureModel>()
        val adapter = MyLectureAdapter(requireContext(), itemList)
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter = adapter

        return binding.root
    }


    private fun updateLectureListForSelectedDate() {
        if(MyApplication.checkAuth()){
            MyApplication.db.collection("users").document(auth.uid.toString()).collection("mytasks")
                .whereEqualTo("d_date", selectedDate)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemTaskModel>()
                    for(document in result){
                        val item = document.toObject(ItemTaskModel::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    // Create and show the bottom sheet with lecture list
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    val view = LayoutInflater.from(requireContext()).inflate(
                        R.layout.bottom_sheet_lecture_list,
                        null
                    )
                    val recyclerView = view.findViewById<RecyclerView>(R.id.bottomSheetRecyclerView)
                    val date = view.findViewById<TextView>(R.id.dateString)
                    val guide = view.findViewById<TextView>(R.id.textView)

                    date.setText(selectedDate)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = MyTaskAdapter(requireContext(), itemList) // 사용자 선택한 날짜에 맞는 아이템 리스트로 설정

                    if(result.size() == 0) guide.visibility = View.VISIBLE

                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.show()

                }
                .addOnFailureListener{
                    Toast.makeText(context, "선택한 날짜: $selectedDate", Toast.LENGTH_SHORT).show()
//                    onError()
                }
        }
    }

    override fun onStart() {
        super.onStart()

        presentSemester = "2023년 2학기"

        binding.psemester.text = "${presentSemester} 강좌 전체보기"

        MyApplication.db.collection("users").document(auth.uid.toString()).collection("mylectures")
            .whereEqualTo("term", "${presentSemester}")
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
                Toast.makeText(context, "내강의리스트 가져오기 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }

//        MyApplication.db.collection("lectures")
//            .orderBy("term", Query.Direction.DESCENDING)
//            .get()
//            .addOnSuccessListener { result ->
//                val itemList = mutableListOf<ItemLectureModel>()
//                for(document in result){
//                    val item = document.toObject(ItemLectureModel::class.java)
//                    item.docId = document.id
//                    itemList.add(item)
//                }
//                binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                binding.feedRecyclerView.adapter = MyLectureAdapter(requireContext(), itemList)
////                Toast.makeText(context, "내강의리스트 가져오기 성공", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener{
//                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
//            }
    }

}

