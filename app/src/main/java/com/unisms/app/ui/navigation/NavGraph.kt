package com.unisms.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unisms.app.data.repository.SmsBowerRepository
import com.unisms.app.ui.screens.activation.ActiveOtpScreen
import com.unisms.app.ui.screens.activation.ActiveOtpViewModel
import com.unisms.app.ui.screens.dashboard.DashboardScreen
import com.unisms.app.ui.screens.dashboard.DashboardViewModel
import com.unisms.app.ui.screens.history.HistoryScreen
import com.unisms.app.ui.screens.history.HistoryViewModel
import com.unisms.app.ui.screens.onboarding.OnboardingScreen
import com.unisms.app.ui.screens.onboarding.OnboardingViewModel
import com.unisms.app.ui.screens.settings.SettingsScreen
import com.unisms.app.ui.screens.settings.SettingsViewModel
import com.unisms.app.ui.util.HapticHelper
import com.unisms.app.ui.util.NotificationHelper

@Composable
fun AppNavGraph(
    navController: NavHostController,
    repository: SmsBowerRepository,
    notificationHelper: NotificationHelper,
    hapticHelper: HapticHelper
) {
    val startDestination = if (repository.hasApiKey()) {
        Screen.Dashboard.route
    } else {
        Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(animationSpec = tween(280))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeOut(animationSpec = tween(240))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(animationSpec = tween(280))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeOut(animationSpec = tween(240))
        }
    ) {
        composable(Screen.Onboarding.route) {
            val viewModel = rememberViewModel { OnboardingViewModel(repository) }
            OnboardingScreen(
                viewModel = viewModel,
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            val viewModel = rememberViewModel { DashboardViewModel(repository) }
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToActiveOtp = { recordId ->
                    navController.navigate(Screen.ActiveOtp.createRoute(recordId))
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.ActiveOtp.route,
            arguments = listOf(navArgument("recordId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getLong("recordId") ?: 0L
            val viewModel = rememberViewModel(key = recordId.toString()) {
                ActiveOtpViewModel(recordId, repository, notificationHelper, hapticHelper)
            }
            ActiveOtpScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            val viewModel = rememberViewModel { HistoryViewModel(repository) }
            HistoryScreen(
                viewModel = viewModel,
                onNavigateToActiveOtp = { recordId ->
                    navController.navigate(Screen.ActiveOtp.createRoute(recordId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            val viewModel = rememberViewModel { SettingsViewModel(repository) }
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
inline fun <reified VM : androidx.lifecycle.ViewModel> rememberViewModel(
    key: String? = null,
    crossinline factory: () -> VM
): VM {
    return androidx.lifecycle.viewmodel.compose.viewModel(
        key = key,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return factory() as T
            }
        }
    )
}
