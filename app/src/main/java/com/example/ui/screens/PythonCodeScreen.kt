package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repository.VpsRepository
import com.example.ui.theme.AccentContainer
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CodeBackground
import com.example.ui.theme.CodeText
import com.example.ui.theme.DarkNavyText
import com.example.ui.theme.LightAppBackground
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteSurface

@Composable
fun PythonCodeScreen(
    repository: VpsRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0: main.py, 1: requirements.txt, 2: API JSON Test
    val mainCode = remember { repository.getPythonMainCode() }
    val reqCode = remember { repository.getRequirementsCode() }

    val activeConfig = repository.activeConfig.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightAppBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Python FastAPI Code",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Complete source file (main.py) and dependencies",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Button(
                onClick = {
                    val codeToCopy = if (selectedTab == 0) mainCode else reqCode
                    val title = if (selectedTab == 0) "main.py" else "requirements.txt"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText(title, codeToCopy))
                    Toast.makeText(context, "$title copied to clipboard!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("button_copy_python_code")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Tab selection
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = AccentContainer,
                    activeContentColor = DarkNavyText,
                    inactiveContainerColor = WhiteSurface,
                    inactiveContentColor = TextSecondary
                )
            ) {
                Text("main.py", fontWeight = FontWeight.Bold)
            }

            SegmentedButton(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = AccentContainer,
                    activeContentColor = DarkNavyText,
                    inactiveContainerColor = WhiteSurface,
                    inactiveContentColor = TextSecondary
                )
            ) {
                Text("requirements.txt", fontWeight = FontWeight.Bold)
            }

            SegmentedButton(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = AccentContainer,
                    activeContentColor = DarkNavyText,
                    inactiveContainerColor = WhiteSurface,
                    inactiveContentColor = TextSecondary
                )
            ) {
                Text("/get-vps JSON", fontWeight = FontWeight.Bold)
            }
        }

        // Display area
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = when (selectedTab) {
                        0 -> "main.py (FastAPI WireGuard Controller)"
                        1 -> "requirements.txt (Python Dependencies)"
                        else -> "Sample GET /get-vps Response Payload"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                val codeText = when (selectedTab) {
                    0 -> mainCode
                    1 -> reqCode
                    else -> """
{
  "status": "success",
  "vps_id": "${activeConfig?.vpsId ?: "vps-us-east"}",
  "server_name": "${activeConfig?.serverName ?: "US East Primary Node"}",
  "server_location": "${activeConfig?.serverLocation ?: "Ashburn, Virginia"}",
  "ip": "${activeConfig?.ip ?: "198.51.100.24"}",
  "port": ${activeConfig?.port ?: 51820},
  "public_key": "${activeConfig?.publicKey ?: "aB3x9Kz+L8qP2wN1mO5rT7vX8yZ0aC1bD2eE3fF4gH5="}",
  "endpoint": "${activeConfig?.endpoint ?: "198.51.100.24:51820"}",
  "allowed_ips": "${activeConfig?.allowedIps ?: "0.0.0.0/0, ::/0"}",
  "dns": "${activeConfig?.dns ?: "1.1.1.1, 8.8.8.8"}",
  "ping_ms": ${activeConfig?.pingMs ?: 28.4},
  "wireguard_config_file": "[Interface]\nPrivateKey = <CLIENT_KEY>\nAddress = 10.0.0.2/32\nDNS = 1.1.1.1\n\n[Peer]\nPublicKey = ${activeConfig?.publicKey}\nEndpoint = ${activeConfig?.endpoint}\nAllowedIPs = 0.0.0.0/0, ::/0\n"
}
""".trimIndent()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CodeBackground)
                        .padding(14.dp)
                ) {
                    Text(
                        text = codeText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        ),
                        color = CodeText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
