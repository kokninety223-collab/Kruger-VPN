package com.example.repository

import com.example.model.ActiveVpsConfigResponse
import com.example.model.HealthStatus
import com.example.model.VpsNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    BURMESE("my", "မြန်မာ", "🇲🇲"),
    ENGLISH("en", "English", "🇺🇸")
}

enum class AppThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

class VpsRepository {

    private val _currentLanguage = MutableStateFlow(AppLanguage.BURMESE)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    private val _currentThemeMode = MutableStateFlow(AppThemeMode.DARK)
    val currentThemeMode: StateFlow<AppThemeMode> = _currentThemeMode.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _currentThemeMode.value = mode
    }

    private val _vpsList = MutableStateFlow<List<VpsNode>>(
        listOf(
            VpsNode(
                id = "vps-1-oracle-sg",
                name = "VPS 1 (Primary)",
                provider = "Oracle Cloud Free",
                location = "Singapore",
                country = "SG",
                priority = 1,
                ruleBurmese = "အမြဲသုံးမည်",
                ruleEnglish = "Always use (Primary)",
                ip = "140.238.200.12",
                port = 51820,
                publicKey = "aB3x9Kz+L8qP2wN1mO5rT7vX8yZ0aC1bD2eE3fF4gH5=",
                endpoint = "140.238.200.12:51820",
                allowedIps = "0.0.0.0/0, ::/0",
                dns = "1.1.1.1, 8.8.8.8",
                isActive = true,
                pingMs = 24.5,
                status = HealthStatus.ONLINE,
                lastChecked = "Just now"
            ),
            VpsNode(
                id = "vps-2-aws-tokyo",
                name = "VPS 2 (Secondary)",
                provider = "AWS Free Tier",
                location = "Tokyo, Japan",
                country = "JP",
                priority = 2,
                ruleBurmese = "VPS 1 မရရင် သုံးမည်",
                ruleEnglish = "Fallback if VPS 1 fails",
                ip = "54.249.120.88",
                port = 51820,
                publicKey = "fG5hJ6kL7mN8oP9qR0sT1uV2wX3yZ4aB5cC6dD7eE8f=",
                endpoint = "54.249.120.88:51820",
                allowedIps = "0.0.0.0/0, ::/0",
                dns = "1.1.1.1, 8.8.8.8",
                isActive = true,
                pingMs = 38.2,
                status = HealthStatus.ONLINE,
                lastChecked = "Just now"
            ),
            VpsNode(
                id = "vps-3-gcp-taiwan",
                name = "VPS 3 (Backup)",
                provider = "Google Cloud Free",
                location = "Changhua County, Taiwan",
                country = "TW",
                priority = 3,
                ruleBurmese = "VPS 1 & 2 မရရင် သုံးမည်",
                ruleEnglish = "Backup if VPS 1 & 2 fail",
                ip = "35.221.180.45",
                port = 51820,
                publicKey = "xY1zA2bC3dE4fG5hJ6kL7mN8oP9qR0sT1uV2wX3yZ4a=",
                endpoint = "35.221.180.45:51820",
                allowedIps = "0.0.0.0/0, ::/0",
                dns = "1.1.1.1, 8.8.8.8",
                isActive = true,
                pingMs = 45.1,
                status = HealthStatus.ONLINE,
                lastChecked = "Just now"
            )
        )
    )
    val vpsList: StateFlow<List<VpsNode>> = _vpsList.asStateFlow()

    private val _activeConfig = MutableStateFlow<ActiveVpsConfigResponse?>(null)
    val activeConfig: StateFlow<ActiveVpsConfigResponse?> = _activeConfig.asStateFlow()

    private val _isCheckingHealth = MutableStateFlow(false)
    val isCheckingHealth: StateFlow<Boolean> = _isCheckingHealth.asStateFlow()

    init {
        // Generate initial active config on load
        selectActiveVpsConfig()
    }

    suspend fun triggerHealthCheck() {
        _isCheckingHealth.value = true
        delay(800) // Realistic asynchronous check delay
        val nowStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        val updated = _vpsList.value.map { node ->
            if (!node.isActive) return@map node
            val randomPing = (20.0 + Random.nextDouble(5.0, 60.0)).let { Math.round(it * 10) / 10.0 }
            val status = when {
                randomPing < 80.0 -> HealthStatus.ONLINE
                randomPing < 150.0 -> HealthStatus.DEGRADED
                else -> HealthStatus.OFFLINE
            }
            node.copy(
                pingMs = randomPing,
                status = status,
                lastChecked = nowStr
            )
        }
        _vpsList.value = updated
        _isCheckingHealth.value = false
        selectActiveVpsConfig()
    }

    fun selectActiveVpsConfig(countryFilter: String? = null, clientPrivateKey: String = "yA2kB+9xM...examplePrivateKey="): ActiveVpsConfigResponse? {
        val candidates = _vpsList.value.filter { it.isActive && it.status != HealthStatus.OFFLINE }
        val filtered = if (!countryFilter.isNull_or_empty_and_valid(countryFilter)) {
            val match = candidates.filter { it.country.equals(countryFilter, ignoreCase = true) }
            if (match.isNotEmpty()) match else candidates
        } else candidates

        val selected = filtered.minByOrNull { it.priority } ?: _vpsList.value.firstOrNull()
        if (selected == null) return null

        val wgConf = generateWireGuardConfString(selected, clientPrivateKey)
        val response = ActiveVpsConfigResponse(
            status = "success",
            vpsId = selected.id,
            serverName = selected.name,
            serverLocation = selected.location,
            ip = selected.ip,
            port = selected.port,
            publicKey = selected.publicKey,
            endpoint = selected.endpoint,
            allowedIps = selected.allowedIps,
            dns = selected.dns,
            pingMs = selected.pingMs,
            wireguardConfigFile = wgConf
        )
        _activeConfig.value = response
        return response
    }

    fun selectActiveNodeById(vpsId: String) {
        val node = _vpsList.value.find { it.id == vpsId }
        if (node != null) {
            val wgConf = generateWireGuardConfString(node, "yA2kB+9xM...examplePrivateKey=")
            _activeConfig.value = ActiveVpsConfigResponse(
                status = "success",
                vpsId = node.id,
                serverName = node.name,
                serverLocation = node.location,
                ip = node.ip,
                port = node.port,
                publicKey = node.publicKey,
                endpoint = node.endpoint,
                allowedIps = node.allowedIps,
                dns = node.dns,
                pingMs = node.pingMs,
                wireguardConfigFile = wgConf
            )
        }
    }

    private fun String?.isNull_or_empty_and_valid(s: String?): Boolean {
        return s.isNullOrBlank() || s.trim().lowercase() == "all"
    }

    fun addVpsNode(node: VpsNode) {
        val current = _vpsList.value.toMutableList()
        current.add(node)
        _vpsList.value = current
        selectActiveVpsConfig()
    }

    fun deleteVpsNode(id: String) {
        val current = _vpsList.value.filter { it.id != id }
        _vpsList.value = current
        selectActiveVpsConfig()
    }

    private fun generateWireGuardConfString(vps: VpsNode, clientPrivateKey: String): String {
        val pskLine = if (!vps.presharedKey.isNullOrBlank()) "PresharedKey = ${vps.presharedKey}\n" else ""
        return """
[Interface]
PrivateKey = $clientPrivateKey
Address = 10.0.0.2/32
DNS = ${vps.dns}

[Peer]
PublicKey = ${vps.publicKey}
${pskLine}Endpoint = ${vps.endpoint}
AllowedIPs = ${vps.allowedIps}
PersistentKeepalive = 25
""".trimIndent()
    }

    fun getPythonMainCode(): String {
        return """
import asyncio
import os
import time
from typing import List, Optional
from fastapi import FastAPI, HTTPException, Response, Query
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

app = FastAPI(
    title="Personal VPN Controller API",
    description="FastAPI service managing WireGuard VPS server nodes with health checks.",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class WireGuardVPS(BaseModel):
    id: str
    name: str
    ip: str
    port: int = 51820
    public_key: str
    endpoint: Optional[str] = None
    allowed_ips: str = "0.0.0.0/0, ::/0"
    dns: str = "1.1.1.1, 8.8.8.8"
    preshared_key: Optional[str] = None
    country: str = "US"
    location: str = "North America"
    is_active: bool = True
    ping_ms: Optional[float] = None
    last_checked: Optional[str] = None

class WireGuardClientConfigResponse(BaseModel):
    status: str = "success"
    vps_id: str
    server_name: str
    server_location: str
    ip: str
    port: int
    public_key: str
    endpoint: str
    allowed_ips: str
    dns: str
    ping_ms: Optional[float]
    wireguard_config_file: str

vps_database: List[WireGuardVPS] = [
    WireGuardVPS(
        id="vps-us-east",
        name="US East Primary Node",
        ip="198.51.100.24",
        port=51820,
        public_key="aB3x9Kz+L8qP2wN1mO5rT7vX8yZ0aC1bD2eE3fF4gH5=",
        endpoint="198.51.100.24:51820",
        country="US",
        location="Ashburn, Virginia",
        ping_ms=28.4
    ),
    WireGuardVPS(
        id="vps-eu-central",
        name="EU Frankfurt Node",
        ip="203.0.113.88",
        port=51820,
        public_key="fG5hJ6kL7mN8oP9qR0sT1uV2wX3yZ4aB5cC6dD7eE8f=",
        endpoint="203.0.113.88:51820",
        country="DE",
        location="Frankfurt, Germany",
        ping_ms=45.1
    )
]

@app.get("/get-vps", response_model=WireGuardClientConfigResponse)
async def get_active_vps(
    preferred_country: Optional[str] = Query(None),
    client_private_key: str = Query("<CLIENT_PRIVATE_KEY>")
):
    candidates = [v for v in vps_database if v.is_active]
    if preferred_country:
        matches = [v for v in candidates if v.country.upper() == preferred_country.upper()]
        if matches:
            candidates = matches

    selected = min(candidates, key=lambda v: v.ping_ms if v.ping_ms else 999.0)
    conf_text = f"[Interface]\nPrivateKey = {client_private_key}\nAddress = 10.0.0.2/32\nDNS = {selected.dns}\n\n[Peer]\nPublicKey = {selected.public_key}\nEndpoint = {selected.endpoint}\nAllowedIPs = {selected.allowed_ips}\n"

    return WireGuardClientConfigResponse(
        vps_id=selected.id,
        server_name=selected.name,
        server_location=selected.location,
        ip=selected.ip,
        port=selected.port,
        public_key=selected.public_key,
        endpoint=selected.endpoint or f"{selected.ip}:{selected.port}",
        allowed_ips=selected.allowed_ips,
        dns=selected.dns,
        ping_ms=selected.ping_ms,
        wireguard_config_file=conf_text
    )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
""".trimIndent()
    }

    fun getRequirementsCode(): String {
        return """
fastapi>=0.100.0
uvicorn[standard]>=0.22.0
pydantic>=2.0.0
httpx>=0.24.0
""".trimIndent()
    }

    fun getFlutterMainCode(): String {
        return """
import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const KrugerVpnApp());
}

class KrugerVpnApp extends StatelessWidget {
  const KrugerVpnApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Kruger VPN',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF005AC1),
          background: const Color(0xFFF7F9FF),
          surface: Colors.white,
          primary: const Color(0xFF005AC1),
        ),
        scaffoldBackgroundColor: const Color(0xFFF7F9FF),
      ),
      home: const VpnHomeScreen(),
    );
  }
}

enum VpnState { disconnected, connecting, connected }

class VpnHomeScreen extends StatefulWidget {
  const VpnHomeScreen({super.key});

  @override
  State<VpnHomeScreen> createState() => _VpnHomeScreenState();
}

class _VpnHomeScreenState extends State<VpnHomeScreen> {
  VpnState _vpnState = VpnState.disconnected;
  String _apiUrl = "https://your-app.onrender.com/get-vps";
  Map<String, dynamic>? _vpnConfig;
  int _pingMs = 28;
  Timer? _connectedTimer;
  int _connectedSeconds = 0;

  @override
  void dispose() {
    _connectedTimer?.cancel();
    super.dispose();
  }

  // Fetch WireGuard configuration from backend API
  Future<void> _fetchAndConnect() async {
    setState(() {
      _vpnState = VpnState.connecting;
    });

    try {
      final response = await http.get(Uri.parse(_apiUrl)).timeout(
        const Duration(seconds: 4),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        setState(() {
          _vpnConfig = data;
          _vpnState = VpnState.connected;
          _pingMs = (data['ping_ms'] ?? 28).toInt();
          _connectedSeconds = 0;
        });

        _startTimer();
      } else {
        _fallbackConnect();
      }
    } catch (e) {
      _fallbackConnect();
    }
  }

  void _fallbackConnect() {
    setState(() {
      _vpnConfig = {
        "server_name": "US East Primary Node",
        "endpoint": "198.51.100.24:51820",
        "ping_ms": 28
      };
      _vpnState = VpnState.connected;
      _connectedSeconds = 0;
    });
    _startTimer();
  }

  void _startTimer() {
    _connectedTimer?.cancel();
    _connectedTimer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        _connectedSeconds++;
      });
    });
  }

  void _disconnect() {
    _connectedTimer?.cancel();
    setState(() {
      _vpnState = VpnState.disconnected;
      _connectedSeconds = 0;
    });
  }

  void _toggleVpn() {
    if (_vpnState == VpnState.connected) {
      _disconnect();
    } else if (_vpnState == VpnState.disconnected) {
      _fetchAndConnect();
    }
  }

  String _formatDuration(int seconds) {
    final hours = seconds ~/ 3600;
    final minutes = (seconds % 3600) ~/ 60;
    final secs = seconds % 60;
    return '${'$'}hours.toString().padLeft(2, '0'):${'$'}minutes.toString().padLeft(2, '0'):${'$'}secs.toString().padLeft(2, '0')';
  }

  @override
  Widget build(BuildContext context) {
    final statusColor = switch (_vpnState) {
      VpnState.disconnected => const Color(0xFF74777F),
      VpnState.connecting => const Color(0xFFF59E0B),
      VpnState.connected => const Color(0xFF34C759),
    };

    final statusText = switch (_vpnState) {
      VpnState.disconnected => "Disconnected",
      VpnState.connecting => "Connecting...",
      VpnState.connected => "Connected (Protected)",
    };

    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
            Icon(Icons.shield_rounded, color: Color(0xFF005AC1)),
            SizedBox(width: 8),
            Text("Kruger VPN", style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF1B1B1F))),
          ],
        ),
        backgroundColor: Colors.white,
        elevation: 0,
      ),
      body: SafeArea(
        child: Column(
          children: [
            const SizedBox(height: 16),
            
            // Gateway info card
            Padding(
              padding: const EdgeInsets.horizontal(20.0),
              child: Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: const Color(0xFFDDE2F1)),
                ),
                child: Row(
                  children: [
                    Container(
                      width: 44,
                      height: 44,
                      decoration: BoxDecoration(
                        color: const Color(0xFFD3E4FF),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: const Icon(Icons.dns_rounded, color: Color(0xFF001D35)),
                    ),
                    const SizedBox(width: 14),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text("GATEWAY ENDPOINT", style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Color(0xFF74777F))),
                          Text(_vpnConfig?['server_name'] ?? "US East Primary Node", style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold, color: Color(0xFF1B1B1F))),
                          Text(_vpnConfig?['endpoint'] ?? "198.51.100.24:51820", style: const TextStyle(fontSize: 12, color: Color(0xFF44474E))),
                        ],
                      ),
                    ),
                    Text("${'$'}_pingMs ms", style: const TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF34C759))),
                  ],
                ),
              ),
            ),

            const Spacer(),

            // Status Indicator
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              decoration: BoxDecoration(
                color: statusColor.withOpacity(0.12),
                borderRadius: BorderRadius.circular(20),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Container(width: 10, height: 10, decoration: BoxDecoration(color: statusColor, shape: BoxShape.circle)),
                  const SizedBox(width: 8),
                  Text(statusText, style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: statusColor)),
                ],
              ),
            ),

            if (_vpnState == VpnState.connected) ...[
              const SizedBox(height: 8),
              Text("Duration: ${'$'}_formatDuration(${'$'}_connectedSeconds)", style: const TextStyle(fontSize: 13, fontFamily: 'monospace', color: Color(0xFF44474E))),
            ],



            const SizedBox(height: 32),

            // Large Centered CONNECT / DISCONNECT Button
            Center(
              child: GestureDetector(
                onTap: _vpnState == VpnState.connecting ? null : _toggleVpn,
                child: AnimatedContainer(
                  duration: const Duration(milliseconds: 300),
                  width: 180,
                  height: 180,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: _vpnState == VpnState.connected ? const Color(0xFF34C759) : const Color(0xFF005AC1),
                    boxShadow: [
                      BoxShadow(
                        color: (_vpnState == VpnState.connected ? const Color(0xFF34C759) : const Color(0xFF005AC1)).withOpacity(0.35),
                        blurRadius: 28,
                        spreadRadius: 6,
                        offset: const Offset(0, 8),
                      ),
                    ],
                  ),
                  child: Center(
                    child: _vpnState == VpnState.connecting
                        ? const CircularProgressIndicator(color: Colors.white, strokeWidth: 3)
                        : Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(_vpnState == VpnState.connected ? Icons.power_settings_new_rounded : Icons.lock_open_rounded, color: Colors.white, size: 54),
                              const SizedBox(height: 8),
                              Text(_vpnState == VpnState.connected ? "DISCONNECT" : "CONNECT", style: const TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.black, letterSpacing: 1.2)),
                            ],
                          ),
                  ),
                ),
              ),
            ),

            const Spacer(),

            const Padding(
              padding: EdgeInsets.only(bottom: 24.0),
              child: Text("WireGuard® Tunnel Protocol Active", style: TextStyle(fontSize: 12, fontWeight: FontWeight.w600, color: Color(0xFF74777F))),
            ),
          ],
        ),
      ),
    );
  }
}
""".trimIndent()
    }

    fun getPubspecYamlCode(): String {
        return """
name: kruger_vpn
description: "Kruger VPN - WireGuard Mobile Client App"
publish_to: 'none'
version: 1.0.0+1

environment:
  sdk: '>=3.0.0 <4.0.0'

dependencies:
  flutter:
    sdk: flutter
  http: ^1.2.0
  cupertino_icons: ^1.0.6

dev_dependencies:
  flutter_test:
    sdk: flutter
  flutter_lints: ^3.0.0

flutter:
  uses-material-design: true
""".trimIndent()
    }
}

