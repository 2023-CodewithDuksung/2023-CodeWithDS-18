package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.MyApplication.Companion.auth
import com.example.a2023hackathon.databinding.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoTaskViewHolder(val binding: ItemTodoTaskBinding): RecyclerView.ViewHolder(binding.root)
class DoneTaskViewHolder(val binding: ItemDoneTaskBinding): RecyclerView.ViewHolder(binding.root)

class TodoTaskAdapter(val context: Context, val itemList: MutableList<ItemTaskModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        if (viewType == 0) {
            return DoneTaskViewHolder(ItemDoneTaskBinding.inflate(layoutInflater))
        } else {
            return TodoTaskViewHolder(ItemTodoTaskBinding.inflate(layoutInflater))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].state == "0") {
            0  //완료된 할일
        } else {
            1 // 진행중인 할일
        }
    }


    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        val data = itemList.get(position)

        if (holder is TodoTaskViewHolder) {
            holder.binding.run {
                todoTaskTitle.text = data.title
                todoMajorTitle.text = data.name

                val dDay = calculateDateDifference(data.d_date, toDayStr)
                val dDayText = if (dDay > 0) {
                    "D-$dDay" // D-day가 0 이상일 경우 "D-n" 형식으로 표시
                } else if (dDay == 0L) {
                    "D-day"
                } else {
                    "${data.d_date}" // 마감일이 지났을 경우 "마감" 표시
                }

                todoTaskDDay.text = dDayText

                if (dDay < 0) {
                    todoTaskDDay.setTextColor(Color.RED) // 빨간색
                } else {
                    // D-day 표시에 대한 색상 설정 (예: 검은색)
                    todoTaskDDay.setTextColor(Color.BLACK)
                }

                // 텍스트뷰의 텍스트 설정
                //todoTaskDDay.text = dDayText

                val docId = data.docId

                todoTaskCheckbox.setOnClickListener {
//                val position = holder.adapterPosition
                    Log.d("2023Hackathon", "docId: ${data.docId}") // 확인용 로그 추가
                    Log.d("2023Hackathon", "docId: ${docId}") // 확인용 로그 추가

                    if (data.docId != null) {
                        if (docId != null) {
                            data.state = "0"

                            MyApplication.db.collection("users")
                                .document(auth.uid.toString())
                                .collection("mytasks")
                                .document(docId.toString())
                                .update("state", data.state)
                                .addOnSuccessListener {
                                    Log.d("2023Hackathon", "Firestore 데이터 업데이트 성공")
                                    refreshDataFromFirestore()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("2023Hackathon", "Firestore 데이터 업데이트 실패", e)
                                }
                        } else {
                            Log.d("2023Hackathon", "docId is null") // 확인용 로그 추가
                        }
                    }
                    moveItemToLast()
                }
            }
        } else if (holder is DoneTaskViewHolder){
            holder.binding.run {
                todoTaskTitle.text = data.title
                doneMajorTitle.text= data.major

                val dDay = calculateDateDifference(data.d_date, toDayStr)
                val dDayText = if (dDay > 0) {
                    "D-$dDay" // D-day가 0 이상일 경우 "D-n" 형식으로 표시
                } else if (dDay == 0L) {
                    "D-day"
                } else {
                    "${data.d_date}" // 마감일이 지났을 경우 "마감" 표시
                }

                val docId = data.docId

                doneTaskCheckbox.setOnClickListener {
//                val position = holder.adapterPosition
                    Log.d("2023Hackathon", "docId: ${data.docId}") // 확인용 로그 추가
                    Log.d("2023Hackathon", "docId: ${docId}") // 확인용 로그 추가

                    if (data.docId != null) {
                        if (docId != null) {
                            data.state = "1"

                            MyApplication.db.collection("users")
                                .document(auth.uid.toString())
                                .collection("mytasks")
                                .document(docId.toString())
                                .update("state", data.state)
                                .addOnSuccessListener {
                                    Log.d("2023Hackathon", "Firestore 데이터 업데이트 성공")
                                    refreshDataFromFirestore()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("2023Hackathon", "Firestore 데이터 업데이트 실패", e)
                                }
                        } else {
                            Log.d("2023Hackathon", "docId is null") // 확인용 로그 추가
                        }
                    }
                    moveItemToLast()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    //
    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        return format.format(date)
    }

    val toDayStr = dateToString(Date())

    //d-day계산: string형 날짜 데이터를 date형을 바꾸고 d-day계산 후 long형으로 변환
    fun calculateDateDifference(dDateStr: String?, toDayStr: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

        try {
            val dDate = dateFormat.parse(dDateStr)
            val toDay = dateFormat.parse(toDayStr)

            // 날짜 차이를 계산하고 밀리초로 반환
            val differenceInMillis = dDate.time - toDay.time

            // 밀리초를 일로 변환하여 반환
            return differenceInMillis / (24 * 60 * 60 * 1000)

        } catch (e: Exception) {
            // 날짜 파싱 오류 처리
            e.printStackTrace()
        }

        // 오류 발생 시 기본값으로 -1 반환
        return -1
    }

    fun moveItemToLast() {
        val doneItem = itemList.find {it.state == "0"}
        doneItem?.let {
            itemList.remove(it)
            itemList.add(it)
            notifyDataSetChanged()
        }
    }

    fun refreshDataFromFirestore() {
        // 파이어베이스에서 데이터를 다시 불러온 후에
        // itemList를 업데이트하고 RecyclerView를 갱신합니다.
        MyApplication.db.collection("users")
            .document(auth.uid.toString())
            .collection("mytasks")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val newItemList = mutableListOf<ItemTaskModel>()
                for (document in querySnapshot.documents) {
                    val item = document.toObject(ItemTaskModel::class.java)
                    item?.let {
                        newItemList.add(item)
                    }
                }
                moveItemToLast()
            }
            .addOnFailureListener { e ->
                Log.w("2023Hackathon", "Firestore 데이터 불러오기 실패", e)
            }
    }

    fun updateData(newItemList: List<ItemTaskModel>) {
        itemList.clear()
        itemList.addAll(newItemList)
        notifyDataSetChanged() // 데이터 변경을 RecyclerView에 알립니다.
    }
}