package br.com.chamazero.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import br.com.chamazero.ui.navigation.NavRoutes
import br.com.chamazero.ui.theme.GreenPrimary
import br.com.chamazero.ui.theme.GrayText
import br.com.chamazero.ui.theme.WhiteSurface

@Composable
fun BottomNavBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onTerrainClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = WhiteSurface,
        windowInsets = WindowInsets.navigationBars
    ) {
        NavigationBarItem(
            selected = currentRoute == NavRoutes.HOME,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Dashboard,
                    contentDescription = "Início"
                )
            },
            label = { Text("Início") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenPrimary,
                selectedTextColor = GreenPrimary,
                unselectedIconColor = GrayText,
                unselectedTextColor = GrayText,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.ADD_TERRAIN,
            onClick = onTerrainClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Map,
                    contentDescription = "Terreno"
                )
            },
            label = { Text("Terreno") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenPrimary,
                selectedTextColor = GreenPrimary,
                unselectedIconColor = GrayText,
                unselectedTextColor = GrayText,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.PROFILE,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Perfil"
                )
            },
            label = { Text("Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GreenPrimary,
                selectedTextColor = GreenPrimary,
                unselectedIconColor = GrayText,
                unselectedTextColor = GrayText,
                indicatorColor = Color.Transparent
            )
        )
    }
}
