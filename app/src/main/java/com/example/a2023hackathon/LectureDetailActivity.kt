package com.example.a2023hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.a2023hackathon.databinding.ActivityLectureDetailBinding
import com.google.android.material.tabs.TabLayout

class LectureDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityLectureDetailBinding

    private lateinit var fragment1: DetailTaskFragment
    private lateinit var fragment2: DetailCommunityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLectureDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragment1 = DetailTaskFragment()
        fragment2 = DetailCommunityFragment()

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