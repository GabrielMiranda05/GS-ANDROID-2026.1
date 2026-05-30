package br.com.chamazero.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Agriculture
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.chamazero.data.model.IrrigationStatus
import br.com.chamazero.data.model.RiskLevel
import br.com.chamazero.data.model.Terrain
import br.com.chamazero.ui.theme.GrayText
import br.com.chamazero.ui.theme.GreenLight
import br.com.chamazero.ui.theme.GreenPrimary
import br.com.chamazero.ui.theme.GreenSurface
import br.com.chamazero.ui.theme.OrangeRisk
import br.com.chamazero.ui.theme.OrangeRiskLight
import br.com.chamazero.ui.theme.WhiteSurface

@Composable
fun TerrainCard(terrain: Terrain, onEditClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = terrain.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar terreno",
                    tint = GrayText,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onEditClick)
                )
                Spacer(modifier = Modifier.width(8.dp))
                RiskBadge(riskLevel = terrain.fireRiskLevel)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${terrain.city} / ${terrain.state}",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Agriculture,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${terrain.cropType} · ${terrain.hectares.toInt()} ha",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WeatherMetricChip(
                    icon = Icons.Outlined.Thermostat,
                    label = "TEMP.",
                    value = "${terrain.temperature.toInt()}°C",
                    modifier = Modifier.weight(1f)
                )
                WeatherMetricChip(
                    icon = Icons.Outlined.Opacity,
                    label = "UMIDADE",
                    value = "${terrain.humidity}%",
                    modifier = Modifier.weight(1f)
                )
                WeatherMetricChip(
                    icon = Icons.Outlined.Speed,
                    label = "PRESSÃO",
                    value = "${terrain.pressure}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocalFireDepartment,
                        contentDescription = null,
                        tint = GrayText,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Risco de incêndio",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayText
                    )
                }
                Text(
                    text = "${terrain.fireRiskPercent}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            val progressColor = when (terrain.fireRiskLevel) {
                RiskLevel.LOW -> GreenLight
                RiskLevel.MEDIUM -> OrangeRisk
                RiskLevel.HIGH -> Color(0xFFE53935)
            }

            LinearProgressIndicator(
                progress = { terrain.fireRiskPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50.dp)),
                color = progressColor,
                trackColor = GreenSurface,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val dotColor = if (terrain.irrigationStatus == IrrigationStatus.ACTIVE)
                        GreenPrimary else OrangeRisk
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val irrigationText = if (terrain.irrigationStatus == IrrigationStatus.ACTIVE) {
                        "Irrigação: Ativa"
                    } else if (terrain.lastIrrigationDate != null) {
                        "Irrigação: Última irrigação em ${terrain.lastIrrigationDate}"
                    } else {
                        "Irrigação: Inativa"
                    }
                    Text(
                        text = irrigationText,
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayText
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun RiskBadge(riskLevel: RiskLevel) {
    val (text, textColor, bgColor) = when (riskLevel) {
        RiskLevel.LOW -> Triple("Baixo risco", GreenPrimary, GreenSurface)
        RiskLevel.MEDIUM -> Triple("Médio risco", OrangeRisk, OrangeRiskLight)
        RiskLevel.HIGH -> Triple("Alto risco", Color(0xFFE53935), Color(0xFFFFEBEE))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun WeatherMetricChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(GreenSurface)
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrayText,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = GrayText,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
