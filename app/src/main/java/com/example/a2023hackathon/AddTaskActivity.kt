package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding

    companion object {
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

        var toolbar = binding.toolbarBack
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false)//타이틀 없애기
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        return format.format(date)
    }

    fun saveStore() {
        val data = mapOf(
            "title" to binding.addTasktitle.text.toString(),
            "content" to binding.addTaskcontent.text.toString(),
            "date" to dateToString(Date()),
            "s_date" to binding.addSdate.text.toString(),
            "d_date" to binding.addDdate.text.toString(),
            "professor" to intent.getStringExtra(EXTRA_PROFESSOR),
            "major" to intent.getStringExtra(EXTRA_MAJOR),
            "sub_code" to intent.getStringExtra(EXTRA_SUB_CODE),
            "state" to binding.state.text.toString()
        )

//        "professor" to binding.addProfessor.text.toString(),
//        "major" to binding.addMajor.text.toString(),
//        "sub_code" to binding.subCode.text.toString(),

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