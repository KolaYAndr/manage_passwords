package com.kolayandr.passwordmanager.ui.thenee

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.kolayandr.passwordmanager.navigation.Screen
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Auth
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                onAuthSuccess = { navHostController.navigate(Screen.PasswordList) }
            )
        }
        composable<Screen.PasswordList> {
            PasswordListScreen(
                onPasswordClick = { passwordId ->
                    navHostController.navigate(Screen.PasswordDetail(passwordId))
                },
                onLogout = { navHostController.navigate(Screen.Auth) }
            )
        }
        composable<Screen.PasswordDetail>(
            typeMap = mapOf(typeOf<Screen.PasswordDetail>() to NavType.StringType),
        ) { backstack ->
            val id = backstack.toRoute<Screen.PasswordDetail>().passwordId
            PasswordDetailScreen(
                passwordId = id,
                onSave = {},
                onBack = { navHostController.navigate(Screen.PasswordList) }
            )
        }
    }
}
