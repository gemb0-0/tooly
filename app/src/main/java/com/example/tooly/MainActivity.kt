package com.example.tooly

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.tooly.ui.theme.ToolyTheme

class MainActivity : ComponentActivity() {
    private val currentIntent = mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CreateDevOptionShortcut()
        // enableEdgeToEdge()

        setContent {
            ToolyTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                TextRemover(currentIntent.value)
                // DialogSizedApp(onDismiss = { onBackPressedDispatcher.onBackPressed() })
                //   }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent.value=intent
    }

    private fun CreateDevOptionShortcut() {
        val shortcut = ShortcutInfoCompat.Builder(this, "gg")
            .setShortLabel("Dev Options")
            .setLongLabel("Open Developer Settings")
            .setIcon(IconCompat.createWithResource(this, R.drawable.rounded_developer_mode_24))
            .setIntent(
                Intent(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
            )
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)


    }
}