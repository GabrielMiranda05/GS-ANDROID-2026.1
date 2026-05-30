package br.com.chamazero.data.remote

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class MockUser(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val initials: String
)

data class MockTerrain(
    val id: Int,
    val userId: Int,
    val name: String,
    val city: String,
    val state: String,
    val neighborhood: String,
    val street: String,
    val number: String,
    val hectares: Double,
    val cropType: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val humidity: Int,
    val pressure: Int,
    val fireRiskPercent: Int,
    val fireRiskLevel: String,
    val irrigationStatus: String,
    val lastIrrigationDate: String?
)

object MockDataStore {

    private val gson = Gson()
    private lateinit var prefs: SharedPreferences

    private val users = mutableListOf<MockUser>()
    private val terrains = mutableListOf<MockTerrain>()
    private var currentUserId: Int = -1
    private var nextUserId: Int = 10
    private var nextTerrainId: Int = 100

    private const val KEY_USERS = "mock_users"
    private const val KEY_TERRAINS = "mock_terrains"
    private const val KEY_CURRENT_USER = "mock_current_user"
    private const val KEY_NEXT_USER_ID = "mock_next_user_id"
    private const val KEY_NEXT_TERRAIN_ID = "mock_next_terrain_id"

    fun init(context: Context) {
        prefs = context.getSharedPreferences("chamazero_mock", Context.MODE_PRIVATE)
        loadFromPrefs()
        seedDefaultDataIfEmpty()
    }

    private fun loadFromPrefs() {
        val usersJson = prefs.getString(KEY_USERS, null)
        if (usersJson != null) {
            val type = object : TypeToken<List<MockUser>>() {}.type
            val loaded: List<MockUser> = gson.fromJson(usersJson, type)
            users.clear()
            users.addAll(loaded)
        }

        val terrainsJson = prefs.getString(KEY_TERRAINS, null)
        if (terrainsJson != null) {
            val type = object : TypeToken<List<MockTerrain>>() {}.type
            val loaded: List<MockTerrain> = gson.fromJson(terrainsJson, type)
            terrains.clear()
            terrains.addAll(loaded)
        }

        currentUserId = prefs.getInt(KEY_CURRENT_USER, -1)
        nextUserId = prefs.getInt(KEY_NEXT_USER_ID, 10)
        nextTerrainId = prefs.getInt(KEY_NEXT_TERRAIN_ID, 100)
    }

    private fun saveToPrefs() {
        if (!::prefs.isInitialized) return
        prefs.edit()
            .putString(KEY_USERS, gson.toJson(users))
            .putString(KEY_TERRAINS, gson.toJson(terrains))
            .putInt(KEY_CURRENT_USER, currentUserId)
            .putInt(KEY_NEXT_USER_ID, nextUserId)
            .putInt(KEY_NEXT_TERRAIN_ID, nextTerrainId)
            .apply()
    }

    private fun seedDefaultDataIfEmpty() {
        if (users.isEmpty()) {
            users.add(MockUser(1, "João Pereira", "joao.pereira@chamazero.com.br", "123456", "JP"))
            users.add(MockUser(2, "Maria Silva", "maria.silva@chamazero.com.br", "123456", "MS"))
        }

        if (terrains.isEmpty()) {
            terrains.add(MockTerrain(
                id = 1, userId = 1,
                name = "Fazenda Três Pinheiros",
                city = "Uberaba", state = "MG",
                neighborhood = "Zona Rural", street = "Estrada Municipal", number = "KM 8",
                hectares = 120.0, cropType = "Soja",
                latitude = -19.7472, longitude = -47.9381,
                temperature = 29.0, humidity = 54, pressure = 1015,
                fireRiskPercent = 48, fireRiskLevel = "MEDIUM",
                irrigationStatus = "INACTIVE", lastIrrigationDate = "22/05/2026"
            ))
            terrains.add(MockTerrain(
                id = 2, userId = 1,
                name = "Chácara Vale Verde",
                city = "Londrina", state = "PR",
                neighborhood = "Zona Rural Sul", street = "Rodovia PR-445", number = "KM 3",
                hectares = 18.0, cropType = "Milho",
                latitude = -23.3045, longitude = -51.1696,
                temperature = 24.0, humidity = 71, pressure = 1018,
                fireRiskPercent = 22, fireRiskLevel = "LOW",
                irrigationStatus = "INACTIVE", lastIrrigationDate = null
            ))
            terrains.add(MockTerrain(
                id = 3, userId = 2,
                name = "Sítio Boa Esperança",
                city = "Ribeirão Preto", state = "SP",
                neighborhood = "Zona Rural Norte", street = "Estrada Vicinal", number = "KM 12",
                hectares = 42.0, cropType = "Soja",
                latitude = -21.1775, longitude = -47.8103,
                temperature = 27.0, humidity = 60, pressure = 1013,
                fireRiskPercent = 35, fireRiskLevel = "MEDIUM",
                irrigationStatus = "INACTIVE", lastIrrigationDate = null
            ))
        }

        saveToPrefs()
    }

    fun findUser(email: String, password: String): MockUser? {
        return users.find { it.email.equals(email, ignoreCase = true) && it.password == password }
    }

    fun emailExists(email: String): Boolean {
        return users.any { it.email.equals(email, ignoreCase = true) }
    }

    fun createUser(name: String, email: String, password: String, initials: String): MockUser {
        val user = MockUser(nextUserId++, name, email, password, initials)
        users.add(user)
        saveToPrefs()
        return user
    }

    fun getUserById(id: Int): MockUser? = users.find { it.id == id }

    fun setCurrentUserId(id: Int) {
        currentUserId = id
        if (::prefs.isInitialized) {
            prefs.edit().putInt(KEY_CURRENT_USER, id).apply()
        }
    }

    fun getCurrentUserId(): Int = currentUserId

    fun updateTerrain(
        terrainId: Int,
        name: String,
        hectares: Double,
        cropType: String,
        state: String,
        city: String,
        neighborhood: String,
        street: String,
        number: String,
        latitude: Double,
        longitude: Double
    ): MockTerrain? {
        val index = terrains.indexOfFirst { it.id == terrainId }
        if (index == -1) return null
        val existing = terrains[index]
        val updated = existing.copy(
            name = name,
            hectares = hectares,
            cropType = cropType,
            state = state,
            city = city,
            neighborhood = neighborhood,
            street = street,
            number = number,
            latitude = latitude,
            longitude = longitude
        )
        terrains[index] = updated
        saveToPrefs()
        return updated
    }

    fun updateUser(userId: Int, name: String, email: String): MockUser? {
        val index = users.indexOfFirst { it.id == userId }
        if (index == -1) return null
        val existing = users[index]
        val nameParts = name.trim().split(" ")
        val initials = if (nameParts.size >= 2) {
            "${nameParts.first().first()}${nameParts.last().first()}".uppercase()
        } else {
            name.take(2).uppercase()
        }
        val updated = existing.copy(name = name, email = email, initials = initials)
        users[index] = updated
        saveToPrefs()
        return updated
    }

    fun getTerrainById(terrainId: Int): MockTerrain? = terrains.find { it.id == terrainId }

    fun getTerrains(userId: Int): List<MockTerrain> {
        return terrains.filter { it.userId == userId }
    }

    fun addTerrain(
        userId: Int,
        name: String,
        hectares: Double,
        cropType: String,
        state: String,
        city: String,
        neighborhood: String,
        street: String,
        number: String,
        latitude: Double,
        longitude: Double
    ): MockTerrain {
        val temperature = generateTemperature(cropType, latitude)
        val humidity = generateHumidity(cropType, temperature)
        val pressure = generatePressure(humidity)
        val riskPercent = calculateFireRisk(hectares, cropType, temperature, humidity)
        val riskLevel = when {
            riskPercent >= 60 -> "HIGH"
            riskPercent >= 35 -> "MEDIUM"
            else -> "LOW"
        }
        val irrigationActive = riskPercent >= 55 && (0..1).random() == 1
        val irrigationStatus = if (irrigationActive) "ACTIVE" else "INACTIVE"
        val lastIrrigationDate = if (irrigationActive || (0..2).random() == 0) {
            generateRecentDate()
        } else null

        val terrain = MockTerrain(
            id = nextTerrainId++,
            userId = userId,
            name = name,
            city = city,
            state = state,
            neighborhood = neighborhood,
            street = street,
            number = number,
            hectares = hectares,
            cropType = cropType,
            latitude = latitude,
            longitude = longitude,
            temperature = temperature,
            humidity = humidity,
            pressure = pressure,
            fireRiskPercent = riskPercent,
            fireRiskLevel = riskLevel,
            irrigationStatus = irrigationStatus,
            lastIrrigationDate = lastIrrigationDate
        )

        terrains.add(terrain)
        saveToPrefs()
        return terrain
    }

    private fun generateTemperature(cropType: String, latitude: Double): Double {
        val baseTemp = when {
            latitude < -25.0 -> (16..28).random()
            latitude < -15.0 -> (22..34).random()
            else -> (26..38).random()
        }
        val cropVariation = when (cropType) {
            "Café" -> -3
            "Trigo" -> -4
            "Cana-de-açúcar" -> 2
            else -> 0
        }
        return (baseTemp + cropVariation + listOf(-1, 0, 0, 1, 1, 2).random()).toDouble()
    }

    private fun generateHumidity(cropType: String, temperature: Double): Int {
        val baseHumidity = when {
            temperature >= 34 -> (25..50).random()
            temperature >= 28 -> (40..65).random()
            temperature >= 22 -> (55..78).random()
            else -> (65..88).random()
        }
        val cropVariation = when (cropType) {
            "Arroz" -> 12
            "Cana-de-açúcar" -> 8
            "Café" -> 5
            "Algodão" -> -8
            "Trigo" -> -5
            else -> 0
        }
        return (baseHumidity + cropVariation).coerceIn(15, 95)
    }

    private fun generatePressure(humidity: Int): Int {
        val base = when {
            humidity < 40 -> (1018..1028).random()
            humidity < 60 -> (1012..1022).random()
            else -> (1005..1018).random()
        }
        return base
    }

    private fun calculateFireRisk(
        hectares: Double,
        cropType: String,
        temperature: Double,
        humidity: Int
    ): Int {
        val tempFactor = when {
            temperature >= 36 -> 35
            temperature >= 32 -> 25
            temperature >= 28 -> 15
            temperature >= 24 -> 5
            else -> -5
        }
        val humidityFactor = when {
            humidity <= 25 -> 35
            humidity <= 40 -> 20
            humidity <= 55 -> 8
            humidity <= 70 -> -5
            else -> -18
        }
        val cropFactor = when (cropType) {
            "Cana-de-açúcar" -> 18
            "Algodão" -> 14
            "Soja" -> 8
            "Milho" -> 5
            "Café" -> 2
            "Trigo" -> 10
            "Arroz" -> -10
            "Feijão" -> 3
            else -> 5
        }
        val sizeFactor = when {
            hectares > 500 -> 12
            hectares > 200 -> 8
            hectares > 100 -> 5
            hectares > 50 -> 2
            else -> 0
        }
        val noise = (-8..8).random()
        return (tempFactor + humidityFactor + cropFactor + sizeFactor + noise + 10)
            .coerceIn(5, 95)
    }

    private fun generateRecentDate(): String {
        val day = (1..28).random()
        val month = (1..5).random()
        return "%02d/%02d/2026".format(day, month)
    }
}
