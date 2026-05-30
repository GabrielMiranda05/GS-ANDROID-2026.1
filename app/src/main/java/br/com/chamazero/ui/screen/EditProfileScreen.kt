package br.com.chamazero.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.chamazero.ui.component.AppButton
import br.com.chamazero.ui.component.AppTextField
import br.com.chamazero.ui.component.BottomNavBar
import br.com.chamazero.ui.navigation.NavRoutes
import br.com.chamazero.ui.theme.GrayText
import br.com.chamazero.ui.theme.GreenBackground
import br.com.chamazero.ui.theme.GreenGradientEnd
import br.com.chamazero.ui.theme.GreenGradientStart
import br.com.chamazero.ui.theme.WhiteSurface
import br.com.chamazero.ui.viewmodel.ProfileViewModel
import br.com.chamazero.util.Resource
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onProfileUpdated: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToTerrain: () -> Unit
) {
    val editName by viewModel.editName.collectAsState()
    val editEmail by viewModel.editEmail.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val profileState by viewModel.profileState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val currentUser = (profileState as? Resource.Success)?.data

    LaunchedEffect(updateState) {
        when (val s = updateState) {
            is Resource.Success -> {
                viewModel.resetUpdateState()
                viewModel.loadProfile()
                scope.launch { snackbarHostState.showSnackbar("Perfil atualizado com sucesso") }
                onProfileUpdated()
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(s.message) }
                viewModel.resetUpdateState()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = GreenBackground,
        topBar = { EditProfileTopBar(onNavigateBack = onNavigateBack) },
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoutes.PROFILE,
                onHomeClick = onNavigateToHome,
                onTerrainClick = onNavigateToTerrain,
                onProfileClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GreenGradientStart, GreenGradientEnd)
                        )
                    )
            ) {
                Text(
                    text = currentUser?.initials ?: "??",
                    style = MaterialTheme.typography.headlineMedium,
                    color = WhiteSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Editar informações do perfil",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                label = "Nome completo",
                value = editName,
                onValueChange = viewModel::onEditNameChange,
                placeholder = "Seu nome",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "E-mail",
                value = editEmail,
                onValueChange = viewModel::onEditEmailChange,
                placeholder = "seu@email.com",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Salvar alterações",
                onClick = viewModel::saveProfile,
                isLoading = updateState is Resource.Loading
            )
        }
    }
}

@Composable
private fun EditProfileTopBar(onNavigateBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenBackground)
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "Editar perfil",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
