package com.unisms.app.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Dashboard : Screen("dashboard")
    data object ActiveOtp : Screen("active_otp/{recordId}") {
        fun createRoute(recordId: Long): String = "active_otp/$recordId"
    }
    data object History : Screen("history")
    data object Settings : Screen("settings")
}
