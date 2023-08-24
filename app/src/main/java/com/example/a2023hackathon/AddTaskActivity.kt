package com.example.a2023hackathon

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.a2023hackathon.databinding.ActivityAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


class AddTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var binding: ActivityAddTaskBinding
    lateinit var selectedDate: String

    private lateinit var addTimeButton: Button
    private lateinit var timePicker: TimePicker

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

        val bottomSheetDialog = BottomSheetDialog(this)
        binding.addDtime.setOnClickListener {
            bottomSheetDialog.show()
        }
        setupBottomSheet()

        var toolbar = binding.toolbarBack
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false)//타이틀 없애기
    }

//    private fun showTimePickerDialog() {
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//
//        val timePickerDialog = TimePickerDialog(this,
//            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
//                val formattedTime = formatTime(selectedHour, selectedMinute)
//                // Update TextView or do other operations with the selected time
//                val selectedTimeTextView = binding.textView1
//                selectedTimeTextView.text = formattedTime
//            }, hour, minute, true)
//
//        timePickerDialog.show()
//    }
//
//    private fun formatTime(hour: Int, minute: Int): String {
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, hour)
//        calendar.set(Calendar.MINUTE, minute)
//
//        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//        return timeFormat.format(calendar.time)
//    }

    private fun setupBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_task, null)

        // 바텀 시트를 표시할 버튼에 클릭 이벤트 설정
        binding.addDtime.setOnClickListener {
            bottomSheetDialog.show()
        }

        // 각 버튼의 클릭 이벤트 처리
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val confirm = view.findViewById<Button>(R.id.confirm_button)

        timePicker.setIs24HourView(true)

        confirm.setOnClickListener {
            val hour: Int
            val minute: Int
            val am_pm: String
            if (Build.VERSION.SDK_INT >= 23) {
                hour = timePicker.hour
                minute = timePicker.minute
            } else {
                hour = timePicker.currentHour
                minute = timePicker.currentMinute
            }
            if (hour > 12) {
                am_pm = "오후"
            } else {
                am_pm = "오전"
            }
            val formattedHour = String.format("%02d", hour)
            val formattedMinute = String.format("%02d", minute)
            binding.textView1.text = "$formattedHour:$formattedMinute"
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
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
            "d_time" to binding.textView1.text.toString(),
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
