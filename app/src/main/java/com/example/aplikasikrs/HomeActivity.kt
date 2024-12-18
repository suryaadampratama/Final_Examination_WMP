package com.example.aplikasikrs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.aplikasikrs.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reference to BottomNavigationView
        val navView: BottomNavigationView = binding.navView

        // Reference to Navigation Controller
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        // Set menu IDs as top-level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,      // ID fragment Home
                R.id.navigation_dashboard, // ID fragment Dashboard
                R.id.navigation_notifications // ID fragment Notifications
            )
        )

        // Connect BottomNavigationView with NavController
        navView.setupWithNavController(navController)

        // Optional: Handle item selection manually if needed
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.navigation_notifications)
                    true
                }
                else -> false
            }
        }
    }
}
