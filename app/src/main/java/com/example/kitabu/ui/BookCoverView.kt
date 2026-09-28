package com.example.kitabu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun BookCoverView(
    coverKey: String,
    title: String,
    author: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val resId = remember(coverKey, title) {
        var id = 0
        if (coverKey.isNotBlank()) {
            id = context.resources.getIdentifier(coverKey.lowercase(), "drawable", context.packageName)
        }
        if (id == 0) {
            val sanitized = title.lowercase()
                .replace(" ", "_")
                .replace("'", "")
                .replace("-", "_")
            id = context.resources.getIdentifier(sanitized, "drawable", context.packageName)
        }
        id
    }

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(RoundedCornerShape(10.dp))
        )
    } else {
        val bgColors = listOf(
            listOf(Color(0xFF475569), Color(0xFF1E293B)),
            listOf(Color(0xFF059669), Color(0xFF064E3B)),
            listOf(Color(0xFF7C3AED), Color(0xFF4C1D95)),
            listOf(Color(0xFFDB2777), Color(0xFF831843))
        )
        val selectedBrush = Brush.verticalGradient(
            colors = bgColors[kotlin.math.abs(title.hashCode()) % bgColors.size]
        )

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(selectedBrush),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
