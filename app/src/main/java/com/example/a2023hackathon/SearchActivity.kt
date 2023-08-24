package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ActivityMainBinding
import com.example.a2023hackathon.databinding.ActivitySearchBinding
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()

        binding.recyclerview.adapter = RecyclerViewAdapter()
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // 검색 옵션 변수
        var searchOption = "name"

        // 스피너 옵션에 따른 동작
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (binding.spinner.getItemAtPosition(position)) {
                    "과목명" -> {
                        searchOption = "name"
                    }
                    "전공명" -> {
                        searchOption = "major"
                    }
                    "교수명" -> {
                        searchOption = "professor"
                    }
                    "학기" -> {
                        searchOption = "term"
                    }
                }
            }
        }

        // 검색 옵션에 따라 검색
        binding.searchBtn.setOnClickListener {
            (binding.recyclerview.adapter as RecyclerViewAdapter).search(binding.searchWord.text.toString(), searchOption)
        }

        binding.btnAddlecture.setOnClickListener{
            intent = Intent(this, AddLectureActivity::class.java)
            startActivity(intent)
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Person 클래스 ArrayList 생성성
        var itemList : ArrayList<ItemLectureModel> = arrayListOf()

        init {  // telephoneBook의 문서를 불러온 뒤 Person으로 변환해 ArrayList에 담음
            firestore?.collection("lectures")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                itemList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ItemLectureModel::class.java)
                    itemList.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_lecture, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            val data = itemList!!.get(position)
            val enroll = viewHolder.findViewById<Button>(R.id.enroll)

            viewHolder.findViewById<TextView>(R.id.itemNameView).text = itemList[position].name
            viewHolder.findViewById<TextView>(R.id.itemMajorView).text = itemList[position].major
            viewHolder.findViewById<TextView>(R.id.itemProfessorView).text = itemList[position].professor
            viewHolder.findViewById<TextView>(R.id.itemTermView).text = itemList[position].term

            viewHolder.findViewById<TextView>(R.id.itemNameView).setOnClickListener{
                val bundle : Bundle = Bundle()
                bundle.putString("name", data.name)
                bundle.putString("major", data.major)
                bundle.putString("professor", data.professor)
                bundle.putString("sub_code", data.sub_code)

                Intent(this@SearchActivity, LectureDetailActivity::class.java).apply{
                    putExtras(bundle)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    this@SearchActivity.startActivity(this)
                }
            }

            enroll.setOnClickListener {
                val userId = "${MyApplication.auth.currentUser!!.uid}"

                // 강의를 이미 담았는지 여부를 확인
                val userRef = MyApplication.db.collection("users").document(userId)
                userRef.collection("mylectures")
                    .whereEqualTo("sub_code", data.sub_code)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // 해당 강의가 아직 담겨있지 않을 때만 담기 기능 실행
                            val enrollmentData = mapOf(
                                "name" to data.name,
                                "major" to data.major,
                                "professor" to data.professor,
                                "sub_code" to data.sub_code,
                                "term" to data.term
                            )

                            userRef.collection("mylectures")
                                .add(enrollmentData)
                                .addOnSuccessListener {
                                    Log.d("SearchActivity", "내강의리스트 추가 성공")
                                }
                                .addOnFailureListener { e ->
                                    Log.d("SearchActivity", "내강의리스트 추가 실패")
                                }
                        } else {
                            // 이미 해당 강의가 담겨있는 경우
                            Log.d("SearchActivity", "이미 해당 강의를 담았습니다.")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.d("SearchActivity", "강의 조회 실패")
                    }
            }

        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return itemList.size
        }

        // 파이어스토어에서 데이터를 불러와서 검색어가 있는지 판단
        fun search(searchWord : String, option : String) {
            firestore?.collection("lectures")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                itemList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString(option)!!.contains(searchWord)) {
                        var item = snapshot.toObject(ItemLectureModel::class.java)
                        itemList.add(item!!)
                    }
                    if(itemList.size.equals(0)){
                        binding.textView.visibility = View.VISIBLE
                        binding.btnAddlecture.visibility = View.VISIBLE
                    }
                    else{
                        binding.textView.visibility = View.GONE
                        binding.btnAddlecture.visibility = View.GONE
                    }
                }
                notifyDataSetChanged()
            }
        }
    }
}
