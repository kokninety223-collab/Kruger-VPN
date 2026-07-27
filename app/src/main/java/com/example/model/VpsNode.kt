package com.example.model

enum class HealthStatus {
    ONLINE,
    DEGRADED,
    OFFLINE
}

data class VpsNode(
    val id: String,
    val name: String,
    val ip: String,
    val port: Int = 51820,
    val publicKey: String,
    val endpoint: String,
    val allowedIps: String = "0.0.0.0/0, ::/0",
    val dns: String = "1.1.1.1, 8.8.8.8",
    val presharedKey: String? = null,
    val country: String = "SG",
    val location: String = "Singapore",
    val provider: String = "",
    val priority: Int = 1,
    val ruleBurmese: String = "",
    val ruleEnglish: String = "",
    val isActive: Boolean = true,
    val pingMs: Double? = 28.0,
    val status: HealthStatus = HealthStatus.ONLINE,
    val lastChecked: String = "Just now"
)

data class ActiveVpsConfigResponse(
    val status: String,
    val vpsId: String,
    val serverName: String,
    val serverLocation: String,
    val ip: String,
    val port: Int,
    val publicKey: String,
    val endpoint: String,
    val allowedIps: String,
    val dns: String,
    val pingMs: Double?,
    val wireguardConfigFile: String
)
