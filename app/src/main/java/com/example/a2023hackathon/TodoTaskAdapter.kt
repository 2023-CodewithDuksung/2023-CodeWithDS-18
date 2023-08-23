package com.example.a2023hackathon

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a2023hackathon.databinding.ItemTaskBinding
import com.example.a2023hackathon.databinding.ItemTodoTaskBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoTaskViewHolder(val binding: ItemTodoTaskBinding): RecyclerView.ViewHolder(binding.root)


class TodoTaskAdapter(val context: Context, val itemList: MutableList<ItemTaskModel>): RecyclerView.Adapter<TodoTaskViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodoTaskViewHolder(ItemTodoTaskBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: TodoTaskViewHolder, position: Int) {
        val data = itemList[position]

        holder.binding.run {
            todoTaskTitle.text = data.title
            todoMajorTitle.text = data.major

            val dDay = calculateDateDifference(data.d_date, toDayStr)
            val dDayText = if (dDay > 0) {
                "D-$dDay" // D-day가 0 이상일 경우 "D-n" 형식으로 표시
            } else if(dDay == 0L){
                "D-day"
            }else{
                "${data.d_date}" // 마감일이 지났을 경우 "마감" 표시
            }

            // 텍스트뷰의 텍스트 설정
            todoTaskDDay.text = dDayText

            // 마감일이 지났을 때 빨간색으로 텍스트 색상 변경
            if (dDay < 0) {
                todoTaskDDay.setTextColor(Color.RED) // 빨간색
            } else {
                // D-day 표시에 대한 색상 설정 (예: 검은색)
                todoTaskDDay.setTextColor(Color.BLACK)
            }

            todoTaskTitle.setOnClickListener{
                val bundle : Bundle = Bundle()
                bundle.putString("title", data.title)
                bundle.putString("content", data.content)
                bundle.putString("date", data.date)
                bundle.putString("major", data.major)
                bundle.putString("professor", data.professor)
                bundle.putString("s_date", data.s_date)
                bundle.putString("d_date", data.d_date)

                Intent(context, LectureDetailActivity::class.java).apply{
                    putExtras(bundle)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
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
//    inner class ViewHolder(private val binding: ItemTodoTaskBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: ItemTaskModel) {
//            binding.apply {
//                todo_task_title.text = item.type
//                TodoTaskTitle.text = item.title
//                // 다른 필요한 바인딩 작업 수행
//            }
//        }
//    }
}