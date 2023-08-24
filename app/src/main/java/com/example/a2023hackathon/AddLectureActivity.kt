package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.a2023hackathon.databinding.ActivityAddLectureBinding
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import java.util.*

class AddLectureActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddLectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addMajor.setOnClickListener {
            showMajorSelectionDialog()
        }

        // 학기 선택 버튼 클릭 이벤트 처리
        binding.addTerm.setOnClickListener {
            showTermSelectionDialog()
        }

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
            "term" to binding.termString.text.toString(),
            "professor" to binding.addProfessor.text.toString(),
            "major" to binding.majorString.text.toString(),
            "sub_code" to binding.addSubcode.text.toString(),
            "mytasks" to null,
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

    private fun showMajorSelectionDialog() {
        val options = arrayOf("국어국문학전공", "일어일문학전공", "중어중문학전공", "영어영문학전공", "불어불문학전공", "독어독문학전공", "스페인어전공", "사학전공", "철학전공",
            "미술사학전공", "문화인류학전공", "경영학전공", "회계학전공", "국제통상학전공", "법학전공", "문헌정보학전공", "사회학전공", "심리학전공", "아동가족학전공", "사회복지학전공", "정치외교학전공", "의상디자인전공", "유아교육과",
            "디지털소프트웨어공학부", "컴퓨터공학전공", "IT미디어공학전공", "사이버보안전공", "소프트웨어전공", "바이오공학전공", "수학전공", "정보통계학전공", "화학전공", "식품영양학전공", "생활체육학전공", "약학과", "동양화전공", "서양화전공",
            "실내디자인전공", "시각디자인전공", "텍스타일디자인전공", "한국학전공", "한국어교육전공")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("학과 선택")
        builder.setItems(options) { dialog, which ->
            val selectedMajor = options[which]
            binding.majorString.text = selectedMajor
        }
        builder.create().show()
    }

    private fun showTermSelectionDialog() {
        val options = arrayOf("2023년도 1학기", "2023년도 2학기", "2024년도 1학기", "2024년도 2학기")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("학기 선택")
        builder.setItems(options) { dialog, which ->
            val selectedTerm = options[which]
            binding.termString.text = selectedTerm
        }
        builder.create().show()
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