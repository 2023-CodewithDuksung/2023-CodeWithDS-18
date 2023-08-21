package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addTask.setOnClickListener {
            saveStore()
            finish()
        }
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        return format.format(date)
    }

    fun saveStore() {
        val data = mapOf(
            "title" to binding.addTasktitle,
            "content" to binding.addTaskcontent,
            "date" to dateToString(Date()),
            "s_date" to binding.addSdate,
            "d_date" to binding.addDdate,
            "professor" to binding.addProfessor,
            "major" to binding.addMajor
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
}
