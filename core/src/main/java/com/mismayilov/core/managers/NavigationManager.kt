package com.mismayilov.core.managers
interface NavigationManager {
    fun bottomNavigationVisibility(isVisible: Boolean)
    fun navigateToMain()
    fun navigateByNavigationName(navigationName: String, startDestination: String? =null)
    fun navigateByDirection(direction:Any)
    fun back()
}