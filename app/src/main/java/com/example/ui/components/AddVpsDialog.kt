package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.HealthStatus
import com.example.model.VpsNode
import com.example.ui.theme.CardBorder
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteSurface
import java.util.UUID

@Composable
fun AddVpsDialog(
    onDismiss: () -> Unit,
    onAddVps: (VpsNode) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var portStr by remember { mutableStateOf("51820") }
    var publicKey by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("US") }
    var location by remember { mutableStateOf("") }
    var dns by remember { mutableStateOf("1.1.1.1, 8.8.8.8") }
    var allowedIps by remember { mutableStateOf("0.0.0.0/0, ::/0") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("add_vps_dialog_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Add WireGuard VPS Node",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Configure a custom WireGuard VPS server for Python health checks and client assignment.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Server Name (e.g. US West Dallas)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth().testTag("input_vps_name")
                )

                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("VPS Server IP / Domain") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth().testTag("input_vps_ip")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = portStr,
                        onValueChange = { portStr = it },
                        label = { Text("UDP Port") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                        modifier = Modifier.weight(1f).testTag("input_vps_port")
                    )

                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country Code") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                        modifier = Modifier.weight(1f).testTag("input_vps_country")
                    )
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location (e.g. Seattle, WA)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth().testTag("input_vps_location")
                )

                OutlinedTextField(
                    value = publicKey,
                    onValueChange = { publicKey = it },
                    label = { Text("WireGuard Public Key") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth().testTag("input_vps_public_key")
                )

                OutlinedTextField(
                    value = dns,
                    onValueChange = { dns = it },
                    label = { Text("DNS Servers") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth().testTag("input_vps_dns")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss, modifier = Modifier.testTag("button_cancel_add")) {
                        Text("Cancel", color = TextSecondary)
                    }

                    Button(
                        onClick = {
                            if (ip.isNotBlank() && publicKey.isNotBlank()) {
                                val portInt = portStr.toIntOrNull() ?: 51820
                                val vps = VpsNode(
                                    id = "vps-custom-${UUID.randomUUID().toString().take(6)}",
                                    name = if (name.isNotBlank()) name else "Custom VPS ($ip)",
                                    ip = ip.trim(),
                                    port = portInt,
                                    publicKey = publicKey.trim(),
                                    endpoint = "${ip.trim()}:$portInt",
                                    allowedIps = allowedIps,
                                    dns = dns,
                                    country = if (country.isNotBlank()) country.uppercase() else "US",
                                    location = if (location.isNotBlank()) location else "Custom Location",
                                    isActive = true,
                                    pingMs = 32.0,
                                    status = HealthStatus.ONLINE,
                                    lastChecked = "Just now"
                                )
                                onAddVps(vps)
                                onDismiss()
                            }
                        },
                        enabled = ip.isNotBlank() && publicKey.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("button_save_vps")
                    ) {
                        Text("Save Server", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
