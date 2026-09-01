package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransferState
import com.example.ui.components.LiquidGlassButton
import com.example.ui.components.TransferProgressRing
import com.example.ui.theme.GlassDarkBackground
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TransferScreen(
    transferState: TransferState,
    onCancelClick: () -> Unit
) {
    // Pressing system back mid-transfer should cancel cleanly instead of
    // silently leaving the P2P transfer running while the UI navigates away.
    BackHandler(enabled = transferState is TransferState.InProgress || transferState is TransferState.Connecting) {
        onCancelClick()
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when (transferState) {
                        is TransferState.Failed -> "Transfer Failed"
                        is TransferState.Completed -> "Finishing Up"
                        else -> "Transferring"
                    },
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Transfer Gauge & Stats
            when (transferState) {
                is TransferState.InProgress -> {
                    TransferProgressRing(
                        progressFraction = transferState.progressFraction,
                        percent = transferState.progressPercent,
                        transferredFormatted = transferState.formattedTransferred,
                        totalFormatted = transferState.formattedTotal,
                        currentFileName = transferState.currentFileName,
                        speedFormatted = transferState.formattedSpeed,
                        timeRemainingFormatted = transferState.formattedTimeRemaining,
                        isOutgoing = transferState.isOutgoing,
                        deviceName = transferState.deviceName
                    )
                }
                is TransferState.Connecting -> {
                    TransferProgressRing(
                        progressFraction = 0.05f,
                        percent = 5,
                        transferredFormatted = "0 B",
                        totalFormatted = "Connecting...",
                        currentFileName = "Establishing secure P2P handshake",
                        speedFormatted = "-- MB/s",
                        timeRemainingFormatted = "Connecting...",
                        isOutgoing = true,
                        deviceName = transferState.device.deviceName
                    )
                }
                is TransferState.Completed -> {
                    // Real data - this state is only shown for the brief moment
                    // before navigation moves on to TransferCompleteScreen.
                    TransferProgressRing(
                        progressFraction = 1f,
                        percent = 100,
                        transferredFormatted = transferState.formattedTotal,
                        totalFormatted = transferState.formattedTotal,
                        currentFileName = "${transferState.fileCount} file(s) transferred",
                        speedFormatted = "--",
                        timeRemainingFormatted = "Done",
                        isOutgoing = transferState.isOutgoing,
                        deviceName = transferState.deviceName
                    )
                }
                is TransferState.Failed -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = NeonPink,
                            modifier = Modifier.height(56.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = transferState.errorReason.ifBlank { "The transfer could not be completed." },
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    // Idle / Searching / ConnectionRequested / Cancelled: this screen
                    // isn't meant to be shown for these states (the nav graph should
                    // have already moved on), so render a neutral indeterminate state
                    // instead of fabricated progress numbers.
                    TransferProgressRing(
                        progressFraction = 0f,
                        percent = 0,
                        transferredFormatted = "0 B",
                        totalFormatted = "--",
                        currentFileName = "Preparing transfer...",
                        speedFormatted = "-- MB/s",
                        timeRemainingFormatted = "Please wait",
                        isOutgoing = true,
                        deviceName = ""
                    )
                }
            }

            // Cancel / Dismiss Button
            LiquidGlassButton(
                text = if (transferState is TransferState.Failed) "Dismiss" else "Cancel Transfer",
                onClick = onCancelClick,
                icon = Icons.Default.Close,
                gradientBrush = Brush.horizontalGradient(
                    listOf(
                        Color(0x33FF1744),
                        Color(0x66FF1744)
                    )
                ),
                glowColor = NeonPink,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
        }
    }
}
