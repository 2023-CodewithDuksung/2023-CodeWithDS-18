package com.example.a2023hackathon

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a2023hackathon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var binding: ActivityMainBinding

    companion object{
        const val DOC_ID = "extra_doc_id"
        const val SUB_CODE = "extra_sub_code"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home page"

        if(MyApplication.checkAuth()){
            bottomNavigationView = findViewById(R.id.bottomNavigationView)
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        updateIcons(item, R.drawable.calendar_1)
                        loadFragment(HomeFragment())
                        supportActionBar?.title = "Home page"
                    }
                    R.id.navigation_dashboard -> {
                        updateIcons(item, R.drawable.checklist_1)
                        loadFragment(DashboardFragment())
                        supportActionBar?.title = "Todo List"
                    }
                    R.id.navigation_notifications -> {
                        updateIcons(item, R.drawable.user_1)
                        loadFragment(NotificationsFragment())
                        supportActionBar?.title = "My page"
                    }
                }
                true
            }
            loadFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }else{
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateIcons(selectedItem: MenuItem, selectedIconRes: Int){
        selectedItem.setIcon(selectedIconRes)
        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item != selectedItem) {
                when (item.itemId) {
                    R.id.navigation_home -> item.setIcon(R.drawable.calendar)
                    R.id.navigation_dashboard -> item.setIcon(R.drawable.checklist)
                    R.id.navigation_notifications -> item.setIcon(R.drawable.user)
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_layout, fragment)
        transaction.addToBackStack(null) // Optional: Add the fragment to the back stack
        transaction.commit()
    }
}