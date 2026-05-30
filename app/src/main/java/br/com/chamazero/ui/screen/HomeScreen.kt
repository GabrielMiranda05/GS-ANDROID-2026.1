package br.com.windfyr.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.windfyr.ui.component.BottomNavBar
import br.com.windfyr.ui.component.TerrainCard
import br.com.windfyr.ui.navigation.NavRoutes
import br.com.windfyr.ui.theme.GrayBorder
import br.com.windfyr.ui.theme.GrayText
import br.com.windfyr.ui.theme.GreenBackground
import br.com.windfyr.ui.theme.GreenGradientEnd
import br.com.windfyr.ui.theme.GreenGradientStart
import br.com.windfyr.ui.theme.GreenPrimary
import br.com.windfyr.ui.theme.GreenSurface
import br.com.windfyr.ui.theme.WhiteSurface
import br.com.windfyr.ui.viewmodel.TerrainViewModel
import br.com.windfyr.util.Resource

@Composable
fun HomeScreen(
    viewModel: TerrainViewModel,
    onNavigateToAddTerrain: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEditTerrain: (Int) -> Unit
) {
    val terrainsState by viewModel.terrainsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTerrains()
    }

    Scaffold(
        containerColor = GreenBackground,
        topBar = { HomeTopBar() },
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoutes.HOME,
                onHomeClick = {},
                onTerrainClick = onNavigateToAddTerrain,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        when (val state = terrainsState) {
            is Resource.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            is Resource.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    items(state.data) { terrain ->
                        TerrainCard(
                            terrain = terrain,
                            onEditClick = { onNavigateToEditTerrain(terrain.id) }
                        )
                    }
                    item {
                        AddTerrainButton(onClick = onNavigateToAddTerrain)
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
            is Resource.Error -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = state.message,
                        color = GrayText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GreenGradientStart, GreenGradientEnd)
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = WhiteSurface,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "Windfyr",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AddTerrainButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = GrayBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .background(GreenSurface)
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "+ Cadastrar novo terreno",
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText
        )
    }
}
