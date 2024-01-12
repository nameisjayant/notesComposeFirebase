package com.nameisjayant.notecomposeapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.nameisjayant.notecomposeapp.ui.theme.Pink80
import com.nameisjayant.notecomposeapp.utils.onValueChange


@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: onValueChange,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPasswordVisible: Boolean = true,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    enable: Boolean = true,
    maxLine: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Pink80,
        disabledContainerColor = Color.Transparent,
    )
) {
    TextField(
        value = value, onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = trailingIcon,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = colors,
        keyboardOptions = keyboardOptions,
        enabled = enable,
        maxLines = maxLine,
        textStyle = style
    )
}