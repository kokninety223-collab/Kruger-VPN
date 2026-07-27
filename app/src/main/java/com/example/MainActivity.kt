package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repository.VpsRepository
import com.example.ui.screens.MobileVpnClientScreen
import com.example.ui.screens.VpnSettingsScreen
import com.example.ui.theme.VpnControllerTheme
import com.example.ui.theme.vpnColors
import com.example.util.AppStrings

class MainActivity : ComponentActivity() {
    private val vpsRepository = VpsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by vpsRepository.currentThemeMode.collectAsState()
            VpnControllerTheme(themeMode = themeMode) {
                MainAppScreen(repository = vpsRepository)
            }
        }
    }
}

@Composable
fun MainAppScreen(repository: VpsRepository) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Shield, 1: Settings
    val currentLanguage by repository.currentLanguage.collectAsState()
    val strings = AppStrings.get(currentLanguage)
    val vpnColors = MaterialTheme.vpnColors

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = vpnColors.bottomNavBg,
                contentColor = vpnColors.textSecondary,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(imageVector = Icons.Default.Security, contentDescription = strings.tabShield) },
                    label = {
                        Text(
                            strings.tabShield,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = vpnColors.greenAction,
                        selectedTextColor = vpnColors.textPrimary,
                        indicatorColor = vpnColors.pillSelected,
                        unselectedIconColor = vpnColors.textSecondary,
                        unselectedTextColor = vpnColors.textSecondary
                    ),
                    modifier = Modifier.testTag("tab_shield")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = strings.tabSettings) },
                    label = {
                        Text(
                            strings.tabSettings,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = vpnColors.greenAction,
                        selectedTextColor = vpnColors.textPrimary,
                        indicatorColor = vpnColors.pillSelected,
                        unselectedIconColor = vpnColors.textSecondary,
                        unselectedTextColor = vpnColors.textSecondary
                    ),
                    modifier = Modifier.testTag("tab_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> MobileVpnClientScreen(repository = repository)
                1 -> VpnSettingsScreen(repository = repository)
            }
        }
    }
}
