package com.example.a2023hackathon

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home page"

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    updateIcons(item, R.drawable.ic_home_black_24dp)
                    loadFragment(HomeFragment())
                    supportActionBar?.title = "Home page"
                }
                R.id.navigation_dashboard -> {
                    updateIcons(item, R.drawable.ic_dashboard_black_24dp)
                    loadFragment(DashboardFragment())
                    supportActionBar?.title = "Todo List"
                }
                R.id.navigation_notifications -> {
                    updateIcons(item, R.drawable.ic_notifications_black_24dp)
                    loadFragment(NotificationsFragment())
                    supportActionBar?.title = "My page"
                }
            }
            true
        }
    }

    private fun updateIcons(selectedItem: MenuItem, selectedIconRes: Int){
        selectedItem.setIcon(selectedIconRes)
        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item != selectedItem) {
                when (item.itemId) {
                    R.id.navigation_home -> item.setIcon(R.drawable.ic_home_black_24dp)
                    R.id.navigation_dashboard -> item.setIcon(R.drawable.ic_dashboard_black_24dp)
                    R.id.navigation_notifications -> item.setIcon(R.drawable.ic_notifications_black_24dp)
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