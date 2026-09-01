package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransferState
import com.example.ui.components.GlassCheckmark
import com.example.ui.components.LiquidGlassButton
import com.example.ui.theme.GlassDarkBackground
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TransferCompleteScreen(
    transferState: TransferState,
    onDoneClick: () -> Unit,
    onViewHistoryClick: () -> Unit
) {
    val completed = transferState as? TransferState.Completed
    val fileCount = completed?.fileCount ?: 0
    val totalFormatted = completed?.formattedTotal ?: "0 B"
    val isOutgoing = completed?.isOutgoing ?: true
    val summaryText = if (completed != null) {
        "$fileCount file${if (fileCount == 1) "" else "s"} ($totalFormatted) ${if (isOutgoing) "sent" else "received"} successfully"
    } else {
        "Your files were transferred successfully"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Checkmark & Success info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlassCheckmark(size = 150.dp)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Transfer Complete!",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = summaryText,
                    color = TextSecondary,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                LiquidGlassButton(
                    text = "Done",
                    onClick = onDoneClick,
                    icon = Icons.Default.Check,
                    modifier = Modifier.fillMaxWidth()
                )

                LiquidGlassButton(
                    text = "View History",
                    onClick = onViewHistoryClick,
                    icon = Icons.Default.History,
                    gradientBrush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        listOf(Color(0x331E293B), Color(0x55334155))
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
