package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.repository.AppLanguage
import com.example.repository.VpsRepository
import com.example.ui.theme.VpnWarningYellow
import com.example.ui.theme.vpnColors
import com.example.util.AppStrings
import kotlinx.coroutines.delay

enum class ClientVpnState { DISCONNECTED, CONNECTING, CONNECTED }

@Composable
fun MobileVpnClientScreen(
    repository: VpsRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeConfig by repository.activeConfig.collectAsState()
    val currentLanguage by repository.currentLanguage.collectAsState()
    val strings = AppStrings.get(currentLanguage)
    val colors = MaterialTheme.vpnColors

    var vpnState by remember { mutableStateOf(ClientVpnState.DISCONNECTED) }
    var durationSeconds by remember { mutableIntStateOf(0) }

    // Connected timer effect
    LaunchedEffect(vpnState) {
        if (vpnState == ClientVpnState.CONNECTED) {
            durationSeconds = 0
            while (vpnState == ClientVpnState.CONNECTED) {
                delay(1000)
                durationSeconds++
            }
        }
    }

    // Handle toggle action
    val toggleVpn: () -> Unit = {
        when (vpnState) {
            ClientVpnState.DISCONNECTED -> {
                vpnState = ClientVpnState.CONNECTING
            }
            ClientVpnState.CONNECTING -> {
                vpnState = ClientVpnState.DISCONNECTED
            }
            ClientVpnState.CONNECTED -> {
                vpnState = ClientVpnState.DISCONNECTED
                Toast.makeText(context, strings.shieldDisconnectedToast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Simulate connecting transition
    LaunchedEffect(vpnState) {
        if (vpnState == ClientVpnState.CONNECTING) {
            delay(1200)
            vpnState = ClientVpnState.CONNECTED
            Toast.makeText(context, strings.shieldConnectedToast, Toast.LENGTH_SHORT).show()
        }
    }

    val shieldTitle = when (vpnState) {
        ClientVpnState.DISCONNECTED -> strings.shieldInactive
        ClientVpnState.CONNECTING -> strings.shieldConnecting
        ClientVpnState.CONNECTED -> strings.shieldActive
    }

    val shieldSubtitle = when (vpnState) {
        ClientVpnState.DISCONNECTED -> strings.subtitleDisconnected
        ClientVpnState.CONNECTING -> strings.subtitleConnecting
        ClientVpnState.CONNECTED -> strings.subtitleConnected
    }

    val buttonText = when (vpnState) {
        ClientVpnState.DISCONNECTED -> strings.buttonSecureConnection
        ClientVpnState.CONNECTING -> strings.buttonConnecting
        ClientVpnState.CONNECTED -> strings.buttonDisconnect
    }

    val hours = durationSeconds / 3600
    val minutes = (durationSeconds % 3600) / 60
    val secs = durationSeconds % 60
    val activeSinceText = if (vpnState == ClientVpnState.CONNECTED) {
        String.format("%02d:%02d:%02d", hours, minutes, secs)
    } else {
        strings.notConnected
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // App Branding Header with Language Toggle Pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_kruger_logo),
                        contentDescription = "Kruger Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = strings.appName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.textPrimary,
                        letterSpacing = (-0.5).sp
                    )
                }
                Text(
                    text = strings.appTagline,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Language Switcher Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.circleContainer)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
                    .clickable {
                        val nextLang = if (currentLanguage == AppLanguage.BURMESE) AppLanguage.ENGLISH else AppLanguage.BURMESE
                        repository.setLanguage(nextLang)
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("button_language_toggle")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentLanguage.flag,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentLanguage.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.greenAction
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Center Shield Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_main_shield"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.cardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circular Status Icon Container
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            if (vpnState == ClientVpnState.CONNECTED) colors.greenAction.copy(alpha = 0.2f)
                            else colors.circleContainer
                        )
                        .border(
                            width = 2.dp,
                            color = if (vpnState == ClientVpnState.CONNECTED) colors.greenAction else colors.cardBorder,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (vpnState) {
                        ClientVpnState.DISCONNECTED -> {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Shield Inactive",
                                tint = colors.textSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        ClientVpnState.CONNECTING -> {
                            CircularProgressIndicator(
                                color = VpnWarningYellow,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        ClientVpnState.CONNECTED -> {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Shield Active",
                                tint = colors.greenAction,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Headline
                Text(
                    text = shieldTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Text(
                    text = shieldSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSubtitle
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Secure Connection Action Button (Green Pill)
                Button(
                    onClick = toggleVpn,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (vpnState == ClientVpnState.CONNECTED) Color(0xFFEF4444) else colors.greenAction,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(52.dp)
                        .testTag("button_secure_connection")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (vpnState == ClientVpnState.CONNECTED) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = buttonText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stat Card 1: ACTIVE SINCE
        Card(
            modifier = Modifier.fillMaxWidth().testTag("card_active_since"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.cardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.circleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Active Since",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = strings.activeSince,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.textSecondary,
                        letterSpacing = 1.1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = activeSinceText,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (vpnState == ClientVpnState.CONNECTED) colors.greenAction else colors.textPrimary,
                        fontFamily = if (vpnState == ClientVpnState.CONNECTED) FontFamily.Monospace else FontFamily.Default
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Stat Card 2: CONNECTION LATENCY
        Card(
            modifier = Modifier.fillMaxWidth().testTag("card_connection_latency"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.cardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.circleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Latency",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = strings.connectionLatency,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.textSecondary,
                        letterSpacing = 1.1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${activeConfig?.pingMs?.toInt() ?: 28} ms • ${activeConfig?.serverName ?: strings.fastestServer}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
