package com.nameisjayant.notecomposeapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.ui.theme.Neutral40
import com.nameisjayant.notecomposeapp.ui.theme.Neutral80
import com.nameisjayant.notecomposeapp.utils.onValueChange

@Composable
fun SearchFieldComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: onValueChange
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Neutral40,
            focusedIndicatorColor = Neutral40
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search_notes),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Neutral80,
                    fontWeight = FontWeight.W400
                )
            )
        },
        trailingIcon = {
            if (value.isNotEmpty())
                IconButtonComponent(
                    imageVector = Icons.Default.Close,
                    tint = Color.Gray
                ) {
                    onValueChange("")
                }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )

}