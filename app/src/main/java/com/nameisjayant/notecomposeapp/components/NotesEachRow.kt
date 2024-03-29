package com.nameisjayant.notecomposeapp.components


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nameisjayant.notecomposeapp.data.model.NoteResponse
import com.nameisjayant.notecomposeapp.ui.theme.Pink80

@Composable
fun NoteEachRow(
    modifier: Modifier = Modifier,
    note: NoteResponse,
    editNote: () -> Unit,
    deleteNote: (String) -> Unit
) {
    Log.d("main", "NoteEachRow: ${note.id}")
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .background(
                Pink80.copy(alpha = 0.6f),
                RoundedCornerShape(8.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = editNote
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.note?.title ?: "", style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600
                    )
                )
                SpacerHeight()
                Text(
                    text = note.note?.description ?: "", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    ),
                    maxLines = 5
                )
            }
            SpacerWidth()
            IconButtonComponent(
                imageVector = Icons.Default.Delete,
                tint = Color.Red.copy(alpha = 0.6f)
            ) {
                deleteNote(note.id ?: "")
            }
        }

    }
}