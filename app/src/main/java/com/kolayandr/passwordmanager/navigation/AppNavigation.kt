package com.kolayandr.passwordmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kolayandr.passwordmanager.ui.detail.PasswordDetailScreen
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
        }

        composable<Screen.PasswordList> {
        }

        composable<Screen.PasswordDetail>(
            typeMap = mapOf(typeOf<Screen.PasswordDetail>() to NavType.StringType),
        ) { backstack ->
            val id = backstack.toRoute<Screen.PasswordDetail>().passwordId
            PasswordDetailScreen(
                passwordId = id,
                onSave = {
                    // Возвращаемся на предыдущий экран (список)
                    navHostController.popBackStack()
                },
                onBack = {
                    // Возвращаемся на предыдущий экран (список)
                    navHostController.popBackStack()
                }
            )
        }
    }
}