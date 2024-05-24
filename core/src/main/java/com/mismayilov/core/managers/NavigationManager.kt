package com.mismayilov.core.managers
interface NavigationManager {
    fun bottomNavigationVisibility(isVisible: Boolean)
    fun navigateToMain()
    fun navigateByBottomNavigation(navigationName: String)
    fun back()
}