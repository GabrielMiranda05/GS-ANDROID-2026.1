package br.com.chamazero.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.chamazero.ui.component.AppButton
import br.com.chamazero.ui.component.AppTextField
import br.com.chamazero.ui.component.BottomNavBar
import br.com.chamazero.ui.navigation.NavRoutes
import br.com.chamazero.ui.theme.GrayBorder
import br.com.chamazero.ui.theme.GrayText
import br.com.chamazero.ui.theme.GreenBackground
import br.com.chamazero.ui.theme.GreenPrimary
import br.com.chamazero.ui.theme.GreenSurface
import br.com.chamazero.ui.theme.WhiteSurface
import br.com.chamazero.ui.viewmodel.TerrainViewModel
import br.com.chamazero.util.Resource
import kotlinx.coroutines.launch

@Composable
fun EditTerrainScreen(
    terrainId: Int,
    viewModel: TerrainViewModel,
    onTerrainUpdated: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val terrainName by viewModel.terrainName.collectAsState()
    val hectares by viewModel.hectares.collectAsState()
    val cropType by viewModel.cropType.collectAsState()
    val state by viewModel.state.collectAsState()
    val city by viewModel.city.collectAsState()
    val neighborhood by viewModel.neighborhood.collectAsState()
    val street by viewModel.street.collectAsState()
    val number by viewModel.number.collectAsState()
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()
    val editTerrainState by viewModel.editTerrainState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var dropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(terrainId) {
        viewModel.loadTerrainForEdit(terrainId)
    }

    LaunchedEffect(editTerrainState) {
        when (val s = editTerrainState) {
            is Resource.Success -> {
                viewModel.resetEditTerrainState()
                viewModel.clearForm()
                onTerrainUpdated()
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(s.message) }
                viewModel.resetEditTerrainState()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = GreenBackground,
        topBar = { EditTerrainTopBar(onNavigateBack = onNavigateBack) },
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoutes.ADD_TERRAIN,
                onHomeClick = onNavigateToHome,
                onTerrainClick = {},
                onProfileClick = {}
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Atualize as informações do terreno.",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                label = "Nome do terreno",
                value = terrainName,
                onValueChange = viewModel::onTerrainNameChange,
                placeholder = "Sítio Boa Esperança"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    label = "Hectares",
                    value = hectares,
                    onValueChange = viewModel::onHectaresChange,
                    placeholder = "42",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tipo de plantação",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box {
                        OutlinedTextField(
                            value = cropType,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                IconButton(onClick = { dropdownExpanded = true }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = GrayBorder,
                                focusedContainerColor = WhiteSurface,
                                unfocusedContainerColor = GreenSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            viewModel.cropTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        viewModel.onCropTypeChange(type)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    label = "Estado",
                    value = state,
                    onValueChange = viewModel::onStateChange,
                    placeholder = "SP",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Characters
                    ),
                    modifier = Modifier.weight(1f)
                )
                AppTextField(
                    label = "Cidade",
                    value = city,
                    onValueChange = viewModel::onCityChange,
                    placeholder = "Ribeirão Preto",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                    ),
                    modifier = Modifier.weight(2f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Bairro",
                value = neighborhood,
                onValueChange = viewModel::onNeighborhoodChange,
                placeholder = "Zona Rural Norte",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    label = "Rua",
                    value = street,
                    onValueChange = viewModel::onStreetChange,
                    placeholder = "Estrada Vicinal",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                    ),
                    modifier = Modifier.weight(2f)
                )
                AppTextField(
                    label = "Número",
                    value = number,
                    onValueChange = viewModel::onNumberChange,
                    placeholder = "KM 12",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    label = "Latitude",
                    value = latitude,
                    onValueChange = viewModel::onLatitudeChange,
                    placeholder = "-21.1775",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                AppTextField(
                    label = "Longitude",
                    value = longitude,
                    onValueChange = viewModel::onLongitudeChange,
                    placeholder = "-47.8103",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Salvar alterações",
                onClick = viewModel::updateTerrain,
                isLoading = editTerrainState is Resource.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EditTerrainTopBar(onNavigateBack: () -> Unit) {
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
            text = "Editar terreno",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
