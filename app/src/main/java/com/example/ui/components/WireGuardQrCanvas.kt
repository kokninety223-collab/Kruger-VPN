package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun WireGuardQrCanvas(
    configData: String,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 160.dp
) {
    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.size(sizeDp - 24.dp)) {
            val gridCount = 21
            val cellSize = this.size.width / gridCount

            // Deterministic hash based on config data
            val hash = configData.hashCode()

            // Draw Finder Patterns (Top-Left, Top-Right, Bottom-Left)
            drawFinderPattern(0, 0, cellSize)
            drawFinderPattern(gridCount - 7, 0, cellSize)
            drawFinderPattern(0, gridCount - 7, cellSize)

            // Draw Data Matrix Modules
            for (r in 0 until gridCount) {
                for (c in 0 until gridCount) {
                    // Skip finder pattern zones
                    val inTopLeft = r < 7 && c < 7
                    val inTopRight = r < 7 && c >= gridCount - 7
                    val inBottomLeft = r >= gridCount - 7 && c < 7
                    if (inTopLeft || inTopRight || inBottomLeft) continue

                    val cellHash = abs((hash * (r + 1) * 31 + (c + 1) * 17 + r * c) % 100)
                    if (cellHash > 45) {
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFinderPattern(startCol: Int, startRow: Int, cellSize: Float) {
    val x = startCol * cellSize
    val y = startRow * cellSize
    val outerSize = 7 * cellSize

    // Outer Black Box
    drawRect(
        color = Color.Black,
        topLeft = Offset(x, y),
        size = Size(outerSize, outerSize)
    )
    // Inner White Box
    drawRect(
        color = Color.White,
        topLeft = Offset(x + cellSize, y + cellSize),
        size = Size(outerSize - 2 * cellSize, outerSize - 2 * cellSize)
    )
    // Center Black Box
    drawRect(
        color = Color.Black,
        topLeft = Offset(x + 2 * cellSize, y + 2 * cellSize),
        size = Size(outerSize - 4 * cellSize, outerSize - 4 * cellSize)
    )
}
