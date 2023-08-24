package com.example.a2023hackathon

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var binding: ActivityAddTaskBinding
    lateinit var selectedDate: String

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_MAJOR = "extra_major"
        const val EXTRA_PROFESSOR = "extra_professor"
        const val EXTRA_SUB_CODE = "extra_sub_code"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addTask.setOnClickListener {
            saveStore()
            finish()
        }

        binding.addDdate.setOnClickListener {
            // 현재 날짜로 초기화된 DatePickerDialog 생성
            val calendar = Calendar.getInstance() // 현재 날짜와 시간을 가지는 Calendar 객체 생성
            val datePickerDialog = DatePickerDialog(
                this, // Context
                this, // DatePickerDialog.OnDateSetListener
                calendar.get(Calendar.YEAR), // 현재 년도
                calendar.get(Calendar.MONTH), // 현재 월
                calendar.get(Calendar.DAY_OF_MONTH) // 현재 날짜
            )
            datePickerDialog.show()
        }

        var toolbar = binding.toolbarBack
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false)//타이틀 없애기
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val formattedMonth = String.format("%02d", monthOfYear + 1)
        selectedDate = "$year-$formattedMonth-$dayOfMonth"
        // 선택된 날짜를 사용
        binding.stringDate.text = selectedDate
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        return format.format(date)
    }

    fun saveStore() {
        val data = mapOf(
            "title" to binding.addTasktitle.text.toString(),
            "content" to binding.addTaskcontent.text.toString(),
            "date" to dateToString(Date()),
            "s_date" to dateToString(Date()),
            "d_date" to selectedDate,
            "name" to intent.getStringExtra(EXTRA_NAME),
            "professor" to intent.getStringExtra(EXTRA_PROFESSOR),
            "major" to intent.getStringExtra(EXTRA_MAJOR),
            "sub_code" to intent.getStringExtra(EXTRA_SUB_CODE),
            "state" to binding.state.text.toString()
        )

        MyApplication.db.collection("tasks")
            .add(data)
            .addOnSuccessListener {
                Log.d("ToyProject", "data firestore save ok")
            }
            .addOnFailureListener {
                Log.d("ToyProject", "data firestore save error")
            }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { //뒤로 가기 버튼
                onBackPressed() // 기본 뒤로가기 동작 수행
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
