package com.example.tooly

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Preview()
@Composable
fun TextRemover() {
    var inputTxt by remember { mutableStateOf("") }
    val isImeVisible = WindowInsets.isImeVisible
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Phone Extractor",
                modifier = Modifier
                    .weight(2.0f)
                    .padding(top = 75.dp),
                fontSize = 33.sp,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedTextField(
                onValueChange = { inputTxt = it },
                value = inputTxt,
                label = { Text("enter text ") },
                shape = RoundedCornerShape(25.dp),
                trailingIcon = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Filled.ContentPaste,
                                contentDescription = "Paste"
                            )
                        },
                        onClick = {
                            inputTxt = clipboardManager.getText().toString()
                        },
                    )
                }
            )
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(processTxt(inputTxt, context)))
                    inputTxt = ""
                },
                modifier = Modifier.padding(
                    bottom = if (isImeVisible) 140.dp else 90.dp,
                    top = 20.dp
                )
            ) {
                Text("Extract")
            }
        }
    }
}

fun processTxt(txt: String, context: Context): String {

    var formattedTxt = txt.filter { !it.isWhitespace() }
    formattedTxt = formattedTxt.map { it ->
        if (Character.getNumericValue(it) in 0..9) {
            Character.getNumericValue(it).toString()
        } else {
            it
        }
    }.joinToString("")
    println(formattedTxt)

    formattedTxt = formattedTxt.replace("[^0-9]".toRegex(), "")
    if (formattedTxt.length < 11) {
        Toast.makeText(context, "data isn't valid", Toast.LENGTH_SHORT).show()
    } else {
        var num = formattedTxt.indexOf("01")
        formattedTxt = formattedTxt.substring(num, num + 11)
    }
    return formattedTxt
}