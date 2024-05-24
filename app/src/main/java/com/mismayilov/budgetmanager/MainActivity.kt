package com.mismayilov.budgetmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.data.local.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationManager {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var navController: NavController? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferencesManager.init(this)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = (supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment).navController


    }

    override fun bottomNavigationVisibility(isVisible: Boolean) {
        bottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun navigateToMain() {
        navController?.setGraph(R.navigation.base_navigation)
        bottomNavigationView.setupWithNavController(navController!!)
    }

    override fun navigateByBottomNavigation(navigationName: String) {
        val res = resources.getIdentifier(
            navigationName,
            "id",
            packageName
        )
        navController?.navigate(res)
    }

    override fun back() {
        onBackPressed()
    }

}