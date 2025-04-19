package com.example.tooly

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.Dispatchers.Main


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun TextRemover(intent: Intent?) {
    val isImeVisible = WindowInsets.isImeVisible
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val developerOptionsIntent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
    val dismissAppIntent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
    var inputTxt by remember { mutableStateOf("") }
    val keyboardManger = LocalSoftwareKeyboardController.current
    val activity = context as Activity

    LaunchedEffect(Unit) {
            if (intent?.action == Intent.ACTION_SEND && intent.type?.startsWith("text/") == true) {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrEmpty()) {
                inputTxt = sharedText
            } else
                clipboardManager.getText()?.let {
                    inputTxt = it.text
                }

        } else
            clipboardManager.getText()?.let { inputTxt = it.text }

    }


    Dialog(
        onDismissRequest = { context.startActivity(dismissAppIntent) },
    ) {
        Card(
            //modifier = Modifier.size(width = 350.dp, height = 199.dp),
            shape = RoundedCornerShape(35.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Phone Extractor",
                        modifier = Modifier.weight(2f),
                        fontSize = 27.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    IconButton(onClick = {
                        context.startActivity(developerOptionsIntent)
                    }) {
                        Icon(Icons.Filled.DeveloperMode, contentDescription = "dev settings")
                    }
                }


                OutlinedTextField(
                    onValueChange = { inputTxt = it },
                    value = inputTxt,
                    label = { Text("enter text ") },
                    shape = RoundedCornerShape(25.dp),
                    maxLines = 4,
                    trailingIcon = {
                        IconButton(

                            content = {
                                if (inputTxt.isEmpty()) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentPaste,
                                        contentDescription = "Paste"
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.ClearAll,
                                        contentDescription = "Paste"
                                    )
                                }

                            },
                            onClick = {
                                if (inputTxt.isEmpty())
                                    inputTxt = clipboardManager.getText().toString()
                                else
                                    inputTxt = ""
                            },
                        )
                    }
                )
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(processTxt(inputTxt)))
                        inputTxt = ""
                        keyboardManger?.hide()
                      //  context.startActivity(dismissAppIntent)
                        finishAffinity(activity)

                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text("Extract")
                }
            }
        }
    }

}

fun processTxt(txt: String): String {
    if (txt.isBlank()) return ""

    txt.filter { !it.isWhitespace() }
    var extracted = ""
    for (i in txt.indices) {

        if (extracted.length == 11)
            break

        if (Character.getNumericValue(txt[i]) in 0..9)
            extracted += Character.getNumericValue(txt[i])
        else
            extracted = ""

    }

    return extracted
}