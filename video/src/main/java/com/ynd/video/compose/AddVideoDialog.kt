package com.ynd.video.compose

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ynd.shared.R

@Composable
fun AddVideoDialog(
    recordedUri: Uri,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1e3c72),
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = { Text(text = stringResource(R.string.add_description)) },
        text = {
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_description),
                        color = Color.White.copy(alpha = 0.6f)
                    )
                              },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(description) },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
            ) { Text(text = stringResource(R.string.save_video)) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White.copy(alpha = 0.7f))
            ) { Text(text = stringResource(R.string.cancel_video)) }
        }
    )
}
