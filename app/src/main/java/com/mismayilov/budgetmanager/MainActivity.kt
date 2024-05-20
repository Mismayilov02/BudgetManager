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

class MainActivity : AppCompatActivity(), NavigationManager {
    private lateinit var  bottomNavigationView:BottomNavigationView
    private var navController: NavController? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = (supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment).navController
    }


    override fun navigateToMain() {
        navController?.setGraph(R.navigation.base_navigation)
        bottomNavigationView.visibility = View.VISIBLE


        bottomNavigationView.setupWithNavController(navController!!)
    }

    override fun navigateToAuth() {
        TODO("Not yet implemented")
    }
}