package com.example.util

import com.example.repository.AppLanguage

object AppStrings {
    fun get(lang: AppLanguage): Strings {
        return when (lang) {
            AppLanguage.ENGLISH -> EnglishStrings
            AppLanguage.BURMESE -> BurmeseStrings
        }
    }
}

interface Strings {
    val appName: String
    val appTagline: String
    val shieldInactive: String
    val shieldConnecting: String
    val shieldActive: String
    val subtitleDisconnected: String
    val subtitleConnecting: String
    val subtitleConnected: String
    val buttonSecureConnection: String
    val buttonConnecting: String
    val buttonDisconnect: String
    val activeSince: String
    val notConnected: String
    val connectionLatency: String
    val shieldDisconnectedToast: String
    val shieldConnectedToast: String
    val fastestServer: String
    
    // Settings
    val vpnSettings: String
    val preferencesAndNodes: String
    val selectGatewayLocation: String
    val protectionAndProtocol: String
    val killSwitchTitle: String
    val killSwitchDesc: String
    val autoConnectTitle: String
    val autoConnectDesc: String
    val encryptedDnsTitle: String
    val encryptedDnsDesc: String
    val fastapiBackendUrl: String
    val apiEndpointLabel: String
    val saveEndpoint: String
    val wireguardConfigFile: String
    val copyConf: String
    val confCopiedToast: String
    val apiUpdatedToast: String
    val languageSectionTitle: String
    val selectLanguage: String
    val themeSectionTitle: String
    val modeLight: String
    val modeDark: String
    val modeSystem: String
    
    // Bottom Nav
    val tabShield: String
    val tabSettings: String
}

object EnglishStrings : Strings {
    override val appName = "Kruger VPN"
    override val appTagline = "Native VPN Protection"
    override val shieldInactive = "SHIELD INACTIVE"
    override val shieldConnecting = "CONNECTING..."
    override val shieldActive = "SHIELD ACTIVE"
    override val subtitleDisconnected = "Tap below to secure your connection"
    override val subtitleConnecting = "Establishing secure WireGuard tunnel..."
    override val subtitleConnected = "Your connection is encrypted & protected"
    override val buttonSecureConnection = "Secure Connection"
    override val buttonConnecting = "Connecting..."
    override val buttonDisconnect = "Disconnect"
    override val activeSince = "ACTIVE SINCE"
    override val notConnected = "Not connected"
    override val connectionLatency = "CONNECTION LATENCY"
    override val shieldDisconnectedToast = "VPN Disconnected"
    override val shieldConnectedToast = "WireGuard Tunnel Connected!"
    override val fastestServer = "Fastest Server"
    
    override val vpnSettings = "VPN Settings"
    override val preferencesAndNodes = "Preferences & Gateway Nodes"
    override val selectGatewayLocation = "SELECT GATEWAY LOCATION"
    override val protectionAndProtocol = "PROTECTION & PROTOCOL"
    override val killSwitchTitle = "Kill Switch"
    override val killSwitchDesc = "Block non-VPN traffic during drops"
    override val autoConnectTitle = "Auto-Connect Wi-Fi"
    override val autoConnectDesc = "Protect automatically on untrusted networks"
    override val encryptedDnsTitle = "1.1.1.1 Encrypted DNS"
    override val encryptedDnsDesc = "Prevent DNS leaks & domain tracking"
    override val fastapiBackendUrl = "FASTAPI BACKEND URL"
    override val apiEndpointLabel = "API Endpoint (/get-vps)"
    override val saveEndpoint = "Save Endpoint"
    override val wireguardConfigFile = "WIREGUARD CONFIG FILE"
    override val copyConf = "Copy .conf"
    override val confCopiedToast = "WireGuard .conf copied to clipboard!"
    override val apiUpdatedToast = "API Endpoint updated successfully!"
    override val languageSectionTitle = "LANGUAGE / ဘာသာစကား"
    override val selectLanguage = "Language"
    override val themeSectionTitle = "APPEARANCE MODE / အပြင်အဆင်"
    override val modeLight = "Light"
    override val modeDark = "Dark"
    override val modeSystem = "System"
    
    override val tabShield = "Shield"
    override val tabSettings = "Settings"
}

object BurmeseStrings : Strings {
    override val appName = "ကရူဂါ VPN"
    override val appTagline = "မူရင်း VPN အကာအကွယ်စနစ်"
    override val shieldInactive = "ဒိုင်း အကာအကွယ် ပိတ်ထားသည်"
    override val shieldConnecting = "ချိတ်ဆက်နေသည်..."
    override val shieldActive = "ဒိုင်း အကာအကွယ် ဖွင့်ထားသည်"
    override val subtitleDisconnected = "လုံခြုံသော ချိတ်ဆက်မှုပြုလုပ်ရန် အောက်တွင် နှိပ်ပါ"
    override val subtitleConnecting = "လုံခြုံသော WireGuard တာနယ် တည်ဆောက်နေသည်..."
    override val subtitleConnected = "သင့်ချိတ်ဆက်မှုကို လျှို့ဝှက်ကုဒ်လုပ်ထားပြီး ကာကွယ်ထားပါသည်"
    override val buttonSecureConnection = "လုံခြုံစွာ ချိတ်ဆက်မည်"
    override val buttonConnecting = "ချိတ်ဆက်နေသည်..."
    override val buttonDisconnect = "ချိတ်ဆက်မှု ဖြတ်မည်"
    override val activeSince = "စတင် ချိတ်ဆက်ချိန်"
    override val notConnected = "ချိတ်ဆက်မထားပါ"
    override val connectionLatency = "ချိတ်ဆက်မှု ကြာချိန် (PING)"
    override val shieldDisconnectedToast = "VPN ချိတ်ဆက်မှု ဖြတ်တောက်လိုက်ပါပြီ"
    override val shieldConnectedToast = "WireGuard တာနယ် ချိတ်ဆက်ပြီးပါပြီ!"
    override val fastestServer = "အမြန်ဆုံး ဆာဗာ"
    
    override val vpnSettings = "VPN ဆက်တင်များ"
    override val preferencesAndNodes = "ရွေးချယ်စရာများနှင့် ဂိတ်ဝေး ဆာဗာများ"
    override val selectGatewayLocation = "ဂိတ်ဝေး တည်နေရာ ရွေးချယ်ပါ"
    override val protectionAndProtocol = "အကာအကွယ်နှင့် ပရိုတိုကော"
    override val killSwitchTitle = "Kill Switch (အင်တာနက် ဖြတ်တောက်စနစ်)"
    override val killSwitchDesc = "VPN ပြုတ်သွားပါက ဒေတာ ထွက်မသွားအောင် တားဆီးမည်"
    override val autoConnectTitle = "အလိုအလျောက် ချိတ်ဆက်မှု"
    override val autoConnectDesc = "မယုံကြည်ရသော Wi-Fi တွင် အလိုအလျောက် ကာကွယ်မည်"
    override val encryptedDnsTitle = "1.1.1.1 လျှို့ဝှက် DNS"
    override val encryptedDnsDesc = "DNS ယိုစိမ့်မှုနှင့် စောင့်ကြည့်ခံရမှုကို တားဆီးမည်"
    override val fastapiBackendUrl = "FASTAPI ဘက်ခ်အန်း URL"
    override val apiEndpointLabel = "API အဆုံးသတ် (/get-vps)"
    override val saveEndpoint = "URL သိမ်းဆည်းမည်"
    override val wireguardConfigFile = "WIREGUARD ကွန်ဖစ် ဖိုင်"
    override val copyConf = ".conf ကူးယူမည်"
    override val confCopiedToast = "WireGuard .conf ကို ကူးယူပြီးပါပြီ!"
    override val apiUpdatedToast = "API Endpoint ကို အောင်မြင်စွာ ပြင်ဆင်ပြီးပါပြီ!"
    override val languageSectionTitle = "LANGUAGE / ဘာသာစကား"
    override val selectLanguage = "ဘာသာစကား"
    override val themeSectionTitle = "APPEARANCE MODE / အပြင်အဆင်"
    override val modeLight = "လင်းသော မုဒ်"
    override val modeDark = "မှောင်သော မုဒ်"
    override val modeSystem = "စနစ် မုဒ်"
    
    override val tabShield = "ဒိုင်းကာ"
    override val tabSettings = "ဆက်တင်များ"
}
