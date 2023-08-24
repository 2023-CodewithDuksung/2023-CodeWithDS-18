package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemLectureBinding


class MyLectureViewHolder(val binding: ItemLectureBinding): RecyclerView.ViewHolder(binding.root)

class MyLectureAdapter(val context: Context, val itemList: MutableList<ItemLectureModel>): RecyclerView.Adapter<MyLectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLectureViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyLectureViewHolder(ItemLectureBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyLectureViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            itemTermView.text = data.term
            itemNameView.text = data.name
            itemMajorView.text = data.major
            itemProfessorView.text = data.professor

            itemNameView.setOnClickListener{
                val bundle : Bundle = Bundle()
                bundle.putString("name", data.name)
                bundle.putString("major", data.major)
                bundle.putString("professor", data.professor)
                bundle.putString("sub_code", data.sub_code)

                Intent(context, LectureDetailActivity::class.java).apply{
                    putExtras(bundle)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    context.startActivity(this)
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
                                    Toast.makeText(context, "내강의리스트 추가 성공", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "내강의리스트 추가 실패", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // 이미 해당 강의가 담겨있는 경우
                            Toast.makeText(context, "이미 해당 강의를 담았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "강의 조회 실패", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
