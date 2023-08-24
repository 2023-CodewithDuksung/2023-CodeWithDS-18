package com.example.a2023hackathon

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.MyApplication.Companion.auth
import com.example.a2023hackathon.databinding.ItemTaskBinding

class MyTaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

class MyTaskAdapter(val context: Context, val itemList: MutableList<ItemTaskModel>?): RecyclerView.Adapter<MyTaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyTaskViewHolder(ItemTaskBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        val data = itemList!!.get(position)
        val docRef = MyApplication.db.collection("users").document(auth.uid.toString()).collection("mytasks")

        holder.binding.run {
            itemTitleView.text = data.title
            itemContentView.text = data.content
            itemDateView.text = data.date
            itemSdateView.text = data.s_date.toString()
            itemDeadlineView.text = data.d_date.toString()
            itemTimeView.text = data.d_time.toString()
            itemMajorView.text = data.major
            itemProfessorView.text = data.professor
            itemCodeView.text = data.sub_code

            enroll.setOnClickListener {
                val userId = "${MyApplication.auth.currentUser!!.uid}"

                // 강의를 이미 담았는지 여부를 확인
                val userRef = MyApplication.db.collection("users").document(userId)
                userRef.collection("mytasks")
                    .whereEqualTo("title", data.title)
                    .whereEqualTo("content", data.content)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // 해당 강의가 아직 담겨있지 않을 때만 담기 기능 실행
                            val enrollmentData = mapOf(
                                "taskId" to data.docId,
                                "title" to data.title,
                                "content" to data.content,
                                "s_date" to data.s_date,
                                "d_date" to data.d_date,
                                "d_time" to data.d_time,
                                "name" to data.name,
                                "major" to data.major,
                                "professor" to data.professor,
                                "sub_code" to data.sub_code,
                                "state" to data.state,
                            )

                            userRef.collection("mytasks")
                                .add(enrollmentData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "내과제리스트 추가 성공", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "내과제리스트 추가 실패", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // 이미 해당 강의가 담겨있는 경우
                            Toast.makeText(context, "이미 해당 과제를 담았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "과제 조회 실패", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}