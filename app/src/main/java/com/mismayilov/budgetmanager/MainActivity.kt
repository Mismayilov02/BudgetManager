package com.mismayilov.budgetmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.mismayilov.auth.fragment.PinFragment.Companion.isLockedScreen
import com.mismayilov.auth.fragment.PinFragment.Companion.twoFactorAuth
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.core.managers.TwoFactorAuthManager
import com.mismayilov.data.local.SharedPreferencesManager
import com.mismayilov.uikit.util.getResourceIdByName
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationManager,TwoFactorAuthManager {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var navController: NavController? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferencesManager.init(this)
        twoFactorAuth = SharedPreferencesManager.getValue("twoFactorAuth", false)
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

    override fun navigateByNavigationName(navigationName: String, startDestination: String?) {
        val res = getResourceIdByName(this, navigationName, "id")
        if (startDestination != null) {
            val startDestinationRes = getResourceIdByName(this, startDestination, "id")
            navController?.navigate(
                res,
                null,
                NavOptions.Builder().setPopUpTo(startDestinationRes, true).build()
            )
        } else navController?.navigate(res)
    }

    override fun navigateByDirection(direction: Any) {
        navController?.navigate(direction as NavDirections)
    }

    override fun navigateByBundle(navigationName: String, bundle: Bundle) {
        val res = getResourceIdByName(this, navigationName, "id")
        navController?.navigate(res, bundle)
    }

    override fun onPause() {
        super.onPause()
        if (twoFactorAuth) {
            isLockedScreen = true
            navigateByNavigationName("pinFragment")
        }
    }

    override fun back() {
        navController?.popBackStack()
    }

    override fun onBackPressed() {
        if (navController?.currentDestination?.id == R.id.home_navigation || isLockedScreen) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun isTwoFactorAuthEnabled(): Boolean {
        return twoFactorAuth
    }

    override fun setTwoFactorAuthEnabled(isEnabled: Boolean) {
        twoFactorAuth = isEnabled
        SharedPreferencesManager.setValue("twoFactorAuth", isEnabled)
    }

}