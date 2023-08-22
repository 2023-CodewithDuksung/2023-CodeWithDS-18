package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.a2023hackathon.databinding.ActivityAddLectureBinding
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import java.util.*

class AddLectureActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddLectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addLecture.setOnClickListener{
            saveStore()
            finish()
        }

        var toolbar = binding.toolbarBack
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false)//타이틀 없애기
    }

    fun saveStore() {
        val data = mapOf(
            "name" to binding.addLecturename.text.toString(),
            "term" to binding.addTerm.text.toString(),
            "professor" to binding.addProfessor.text.toString(),
            "major" to binding.addMajor.text.toString(),
            "sub_code" to binding.addSubcode.text.toString(),
        )

        MyApplication.db.collection("lectures")
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