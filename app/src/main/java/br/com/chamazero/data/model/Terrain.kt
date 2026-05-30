package br.com.chamazero.data.model

data class Terrain(
    val id: Int,
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
    val fireRiskLevel: RiskLevel,
    val irrigationStatus: IrrigationStatus,
    val lastIrrigationDate: String?
)

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

enum class IrrigationStatus {
    ACTIVE, INACTIVE
}

data class AddTerrainRequest(
    val name: String,
    val hectares: Double,
    val cropType: String,
    val state: String,
    val city: String,
    val neighborhood: String,
    val street: String,
    val number: String,
    val latitude: Double,
    val longitude: Double
)
