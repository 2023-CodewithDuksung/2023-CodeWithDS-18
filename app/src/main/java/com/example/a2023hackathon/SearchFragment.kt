package com.example.a2023hackathon

import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.a2023hackathon.databinding.FragmentSearchBinding
import com.google.firebase.firestore.Query


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    var searchKeyword = ""

    private lateinit var searchLectures: RecyclerView
    //    private lateinit var searchLecturesAdater: MyLectureAdapter
    private lateinit var searchLecturesLayoutMgr: LinearLayoutManager
    private var searchLecturesPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchLectures = binding.searchLectures
        searchLecturesLayoutMgr = LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        )
        searchLectures.layoutManager = searchLecturesLayoutMgr
//        searchLecturesAdater = MyLectureAdapter(requireContext(), itemList = null)
//        searchLectures.adapter = searchLecturesAdater

        binding.eSearchWord.setOnEditorActionListener{ v, actionId, event ->
            var handled = false
            if(actionId == EditorInfo.IME_ACTION_DONE || (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                binding.bSearch.performClick() // bSearch 버튼 클릭
                handled = true
            }
            handled
        }

        binding.bSearch.setOnClickListener {
            searchKeyword = binding.eSearchWord.text.toString()

            if(searchKeyword == "") {
                Toast.makeText(activity, "input keyword", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(activity, searchKeyword, Toast.LENGTH_SHORT).show()
                getSearchLectures("name")
                getSearchLectures("professor")
                getSearchLectures("major")
            }
//            removeData()
        }

        binding.chatListToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

//    private fun removeData() {
//        searchLectures.removeAllViews()
//
//        searchLecturesAdater.removeLectures(searchLecturesAdater.lectures)
//        searchLecturesAdater.notifyDataSetChanged()
//    }

    private fun getSearchLectures(searchField: String) {
        MyApplication.db.collection("lectures")
            .whereEqualTo(searchField, searchKeyword)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemLectureModel>()
                for(document in result){
                    val item = document.toObject(ItemLectureModel::class.java)
                    item.docId = document.id
                    itemList.add(item)
                    if (result.isEmpty()) {
                        binding.textView.visibility = View.VISIBLE
                        binding.btnAddlecture.visibility = View.VISIBLE
                        searchLectures.visibility = View.GONE // 검색 결과가 없을 때 리사이클러뷰 가시성 제거
                    } else {
                        binding.textView.visibility = View.GONE
                        binding.btnAddlecture.visibility = View.GONE
                        searchLectures.visibility = View.VISIBLE // 검색 결과가 있을 때 리사이클러뷰 가시성 표시
                        searchLectures.layoutManager = LinearLayoutManager(requireContext())
                        searchLectures.adapter = MyLectureAdapter(requireContext(), itemList)
                    }
                    Toast.makeText(requireContext(), "검색 데이터 가져오기 성공", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }

    }

//    private fun onSearchLecturesFetched(lectures: List<ItemLectureModel>) {
//        searchLecturesAdater.appendLectures(lectures)
//    }

}