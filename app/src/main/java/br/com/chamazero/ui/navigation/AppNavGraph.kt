package br.com.chamazero.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.chamazero.data.remote.MockDataStore
import br.com.chamazero.ui.screen.AddTerrainScreen
import br.com.chamazero.ui.screen.EditProfileScreen
import br.com.chamazero.ui.screen.EditTerrainScreen
import br.com.chamazero.ui.screen.HomeScreen
import br.com.chamazero.ui.screen.LoginScreen
import br.com.chamazero.ui.screen.ProfileScreen
import br.com.chamazero.ui.screen.RegisterScreen
import br.com.chamazero.ui.viewmodel.AuthViewModel
import br.com.chamazero.ui.viewmodel.ProfileViewModel
import br.com.chamazero.ui.viewmodel.TerrainViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val terrainViewModel: TerrainViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoutes.REGISTER)
                }
            )
        }

        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                viewModel = terrainViewModel,
                onNavigateToAddTerrain = {
                    navController.navigate(NavRoutes.ADD_TERRAIN)
                },
                onNavigateToProfile = {
                    navController.navigate(NavRoutes.PROFILE)
                },
                onNavigateToEditTerrain = { terrainId ->
                    navController.navigate(NavRoutes.editTerrain(terrainId))
                }
            )
        }

        composable(NavRoutes.ADD_TERRAIN) {
            AddTerrainScreen(
                viewModel = terrainViewModel,
                onTerrainAdded = {
                    terrainViewModel.loadTerrains()
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(NavRoutes.PROFILE)
                }
            )
        }

        composable(
            route = NavRoutes.EDIT_TERRAIN,
            arguments = listOf(navArgument("terrainId") { type = NavType.IntType })
        ) { backStackEntry ->
            val terrainId = backStackEntry.arguments?.getInt("terrainId") ?: return@composable
            EditTerrainScreen(
                terrainId = terrainId,
                viewModel = terrainViewModel,
                onTerrainUpdated = {
                    terrainViewModel.loadTerrains()
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                viewModel = profileViewModel,
                onLogout = {
                    MockDataStore.setCurrentUserId(-1)
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                onNavigateToTerrain = {
                    navController.navigate(NavRoutes.ADD_TERRAIN)
                },
                onNavigateToEditProfile = {
                    navController.navigate(NavRoutes.EDIT_PROFILE)
                }
            )
        }

        composable(NavRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                viewModel = profileViewModel,
                onProfileUpdated = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                onNavigateToTerrain = {
                    navController.navigate(NavRoutes.ADD_TERRAIN)
                }
            )
        }
    }
}
