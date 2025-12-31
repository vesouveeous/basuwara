package com.dicoding.basuwara.ui.components.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedEmailTextField(
    text: String,
    onChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isValidEmail by remember(text) {
        mutableStateOf( android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches())
    }
    OutlinedTextField(
        value = text,
        onValueChange = { onChange(it) },
        label = {
            Text("Name or Email Address",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            ) },
        placeholder = { Text(text = "Name or Email Address") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f).testTag("email"),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )

    )
    if (text.isNotEmpty() && !isValidEmail) {
        Text(
            text = "Invalid Email",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}