package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    }

    fun saveStore() {
        val data = mapOf(
            "name" to binding.addLecturename.text.toString(),
            "term" to binding.addTerm.text.toString(),
            "professor" to binding.addProfessor.text.toString(),
            "major" to binding.addMajor.text.toString()
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
}