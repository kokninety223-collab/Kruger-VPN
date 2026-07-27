package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun DeploymentGuideScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightAppBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

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
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Deployment Guide",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Text(
                text = "Run locally or deploy for free to Render.com",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Section 1: Local Execution
        Card(
            modifier = Modifier.fillMaxWidth().testTag("card_guide_local"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1. Running Locally (Development)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                GuideStepItem(
                    stepNumber = "1",
                    title = "Install Python Dependencies",
                    command = "pip install -r requirements.txt",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "2",
                    title = "Launch FastAPI Server",
                    command = "uvicorn main:app --reload --port 8000",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "3",
                    title = "Test /get-vps Endpoint",
                    command = "curl http://localhost:8000/get-vps",
                    context = context
                )
            }
        }

        // Section 2: Render.com Free Cloud Hosting
        Card(
            modifier = Modifier.fillMaxWidth().testTag("card_guide_render"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "2. Free Deployment to Render.com",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                GuideStepItem(
                    stepNumber = "1",
                    title = "Push Code to GitHub",
                    command = "git add main.py requirements.txt\ngit commit -m 'FastAPI WireGuard controller'\ngit push origin main",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "2",
                    title = "Create New Web Service on Render",
                    description = "Go to dashboard.render.com -> Click 'New +' -> 'Web Service'. Connect your GitHub repository.",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "3",
                    title = "Configure Render Build & Start Settings",
                    description = "• Environment: Python 3\n• Build Command: pip install -r requirements.txt\n• Start Command: uvicorn main:app --host 0.0.0.0 --port \$PORT",
                    command = "uvicorn main:app --host 0.0.0.0 --port \$PORT",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "4",
                    title = "Verify Live Public Endpoint",
                    description = "Your API will be live at https://your-service.onrender.com/get-vps and interactive Swagger docs at /docs",
                    command = "curl https://your-service.onrender.com/get-vps",
                    context = context
                )
            }
        }

        // Section 3: Ubuntu 22.04 LTS Automated VPS Setup
        Card(
            modifier = Modifier.fillMaxWidth().testTag("card_guide_ubuntu"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "3. Ubuntu 22.04 WireGuard VPS Setup",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                GuideStepItem(
                    stepNumber = "1",
                    title = "Run 1-Click WireGuard Installer",
                    description = "SSH into your fresh Ubuntu 22.04 VPS as root and run this automated installation script:",
                    command = "wget https://git.io/wireguard -O wireguard-install.sh && bash wireguard-install.sh",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "2",
                    title = "Extract Client Config File (.conf)",
                    description = "View the newly generated client configuration file to grab keys and endpoints:",
                    command = "cat ~/client1.conf",
                    context = context
                )

                GuideStepItem(
                    stepNumber = "3",
                    title = "View Server Public Key & Details",
                    description = "Extract the server's public key from the main WireGuard configuration or status:",
                    command = "sudo wg show",
                    context = context
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun GuideStepItem(
    stepNumber: String,
    title: String,
    description: String? = null,
    command: String? = null,
    context: Context
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(AccentContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = DarkNavyText
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            if (!command.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CodeBackground)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = command,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        color = CodeText,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Command", command))
                            Toast.makeText(context, "Command copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Command",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
