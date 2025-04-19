package com.example.tooly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
        createDevOptionShortcut()
        // enableEdgeToEdge()

        if (intent?.action == Intent.ACTION_PROCESS_TEXT) {
           // currentIntent.value = intent
            Log.i("intent","ACTION_PROCESS_TEXT on create")
            val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            val processedText = processTxt(text.toString())
            val result = Intent()
            result.putExtra(Intent.EXTRA_PROCESS_TEXT, processedText)
            setResult(Activity.RESULT_OK, result)
            finish()
        }

        setContent {
            ToolyTheme {

                TextRemover(currentIntent.value)

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        //idk why if it fires to new activity not new intent even when the app is running (will check this later)
        if (intent.action==Intent.ACTION_PROCESS_TEXT)
            Log.i("intent","ACTION_PROCESS_TEXT")
        else if (intent.action == Intent.ACTION_SEND)
            Log.i("intent","ACTION_SEND")

        currentIntent.value = intent
    }

    private fun createDevOptionShortcut() {
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