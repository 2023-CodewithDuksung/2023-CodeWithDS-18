package com.example.a2023hackathon
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.a2023hackathon.databinding.ActivityLectureDetailBinding
import com.google.android.material.tabs.TabLayout

class LectureDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityLectureDetailBinding

    private lateinit var fragment1: DetailTaskFragment
    private lateinit var fragment2: DetailCommunityFragment
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLectureDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar = binding.toolbarBack
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false)//타이틀 없애기

        // Toolbar title 과목 명으로 설정하기
        binding.toolbarTitle.text = intent.getStringExtra("name")

        // 데이터 전달 받기
        val name = intent.getStringExtra("name")
        val major = intent.getStringExtra("major")
        val professor = intent.getStringExtra("professor")
        val subCode = intent.getStringExtra("sub_code")

//        binding.professor.text = professor
//        binding.major.text = major

        fragment1 = DetailTaskFragment()
        fragment2 = DetailCommunityFragment()

        val bundle = Bundle()
        bundle.putString("major", major)
        bundle.putString("professor", professor)
        bundle.putString("sub_code", subCode)

        fragment1.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment1).commit()

        val tabs: TabLayout = binding.tabs
        tabs.addTab(tabs.newTab().setText("과제"))
        tabs.addTab(tabs.newTab().setText("소통"))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                val selected: Fragment = when (position) {
                    0 -> fragment1
                    else -> fragment2
                }
                supportFragmentManager.beginTransaction().replace(R.id.container, selected).commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 구현 필요 없음
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 구현 필요 없음
            }
        })
    }
}