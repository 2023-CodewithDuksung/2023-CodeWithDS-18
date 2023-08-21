package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import com.google.firebase.firestore.FirebaseFirestore
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
            "title" to binding.addTasktitle.text.toString(),
            "content" to binding.addTaskcontent.text.toString(),
            "date" to dateToString(Date()),
            "s_date" to binding.addSdate.text.toString(),
            "d_date" to binding.addDdate.text.toString(),
            "professor" to binding.addProfessor.text.toString(),
            "major" to binding.addMajor.text.toString()
        )

//        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
