package br.com.chamazero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.chamazero.data.model.AddTerrainRequest
import br.com.chamazero.data.model.Terrain
import br.com.chamazero.data.remote.MockDataStore
import br.com.chamazero.data.repository.TerrainRepository
import br.com.chamazero.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TerrainViewModel : ViewModel() {

    private val repository = TerrainRepository()

    private val _terrainsState = MutableStateFlow<Resource<List<Terrain>>>(Resource.Loading)
    val terrainsState: StateFlow<Resource<List<Terrain>>> = _terrainsState

    private val _addTerrainState = MutableStateFlow<Resource<Terrain>?>(null)
    val addTerrainState: StateFlow<Resource<Terrain>?> = _addTerrainState

    private val _editTerrainState = MutableStateFlow<Resource<Terrain>?>(null)
    val editTerrainState: StateFlow<Resource<Terrain>?> = _editTerrainState

    private val _editingTerrainId = MutableStateFlow<Int?>(null)

    private val _terrainName = MutableStateFlow("")
    val terrainName: StateFlow<String> = _terrainName

    private val _hectares = MutableStateFlow("")
    val hectares: StateFlow<String> = _hectares

    private val _cropType = MutableStateFlow("Soja")
    val cropType: StateFlow<String> = _cropType

    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _neighborhood = MutableStateFlow("")
    val neighborhood: StateFlow<String> = _neighborhood

    private val _street = MutableStateFlow("")
    val street: StateFlow<String> = _street

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number

    private val _latitude = MutableStateFlow("")
    val latitude: StateFlow<String> = _latitude

    private val _longitude = MutableStateFlow("")
    val longitude: StateFlow<String> = _longitude

    val cropTypes = listOf("Soja", "Milho", "Cana-de-açúcar", "Café", "Algodão", "Trigo", "Arroz", "Feijão")

    fun onTerrainNameChange(value: String) {
        if (value.length <= 60) _terrainName.value = value
    }

    fun onHectaresChange(value: String) {
        if (value.length <= 8 && value.matches(Regex("^\\d{0,6}(\\.\\d{0,2})?$"))) {
            _hectares.value = value
        }
    }

    fun onCropTypeChange(value: String) { _cropType.value = value }

    fun onStateChange(value: String) {
        val filtered = value.filter { it.isLetter() || it == ' ' }
        if (filtered.length <= 2) _state.value = filtered.uppercase()
    }

    fun onCityChange(value: String) {
        val filtered = value.filter { it.isLetter() || it == ' ' || it == '-' }
        if (filtered.length <= 50) _city.value = filtered
    }

    fun onNeighborhoodChange(value: String) {
        val filtered = value.filter { it.isLetter() || it == ' ' || it == '-' }
        if (filtered.length <= 60) _neighborhood.value = filtered
    }

    fun onStreetChange(value: String) {
        if (value.length <= 80) _street.value = value
    }

    fun onNumberChange(value: String) {
        val filtered = value.filter { it.isLetterOrDigit() || it == '-' || it == '/' }
        if (filtered.length <= 10) _number.value = filtered.uppercase()
    }

    fun onLatitudeChange(value: String) {
        if (value.length <= 10 && value.matches(Regex("^-?\\d{0,2}(\\.\\d{0,6})?$|^-$"))) {
            _latitude.value = value
        }
    }

    fun onLongitudeChange(value: String) {
        if (value.length <= 11 && value.matches(Regex("^-?\\d{0,3}(\\.\\d{0,6})?$|^-$"))) {
            _longitude.value = value
        }
    }

    fun loadTerrains() {
        viewModelScope.launch {
            _terrainsState.value = Resource.Loading
            _terrainsState.value = repository.getTerrains()
        }
    }

    fun loadTerrainForEdit(terrainId: Int) {
        val terrain = MockDataStore.getTerrainById(terrainId) ?: return
        _editingTerrainId.value = terrainId
        _terrainName.value = terrain.name
        _hectares.value = terrain.hectares.toString()
        _cropType.value = terrain.cropType
        _state.value = terrain.state
        _city.value = terrain.city
        _neighborhood.value = terrain.neighborhood
        _street.value = terrain.street
        _number.value = terrain.number
        _latitude.value = terrain.latitude.toString()
        _longitude.value = terrain.longitude.toString()
    }

    fun addTerrain() {
        viewModelScope.launch {
            _addTerrainState.value = Resource.Loading
            val request = buildRequest()
            _addTerrainState.value = repository.addTerrain(request)
        }
    }

    fun updateTerrain() {
        val id = _editingTerrainId.value ?: return
        viewModelScope.launch {
            _editTerrainState.value = Resource.Loading
            val request = buildRequest()
            _editTerrainState.value = repository.updateTerrain(id, request)
        }
    }

    private fun buildRequest() = AddTerrainRequest(
        name = _terrainName.value,
        hectares = _hectares.value.toDoubleOrNull() ?: 0.0,
        cropType = _cropType.value,
        state = _state.value,
        city = _city.value,
        neighborhood = _neighborhood.value,
        street = _street.value,
        number = _number.value,
        latitude = _latitude.value.toDoubleOrNull() ?: 0.0,
        longitude = _longitude.value.toDoubleOrNull() ?: 0.0
    )

    fun resetAddTerrainState() { _addTerrainState.value = null }
    fun resetEditTerrainState() { _editTerrainState.value = null }

    fun clearForm() {
        _editingTerrainId.value = null
        _terrainName.value = ""
        _hectares.value = ""
        _cropType.value = "Soja"
        _state.value = ""
        _city.value = ""
        _neighborhood.value = ""
        _street.value = ""
        _number.value = ""
        _latitude.value = ""
        _longitude.value = ""
    }
}
