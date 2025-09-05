package de.berlindroid.zepatch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Common SafeArea wrapper to provide consistent padding around patchable content.
 *
 * Extended to optionally capture the rendered content into an ImageBitmap when
 * shouldCapture becomes true, returning it via onBitmap.
 */
@Composable
fun SafeArea(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier
            .padding(32.dp)
            .drawWithContent {
                // Record content drawing into the graphics layer
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawContent()
            }
            .background(Color.Transparent),
    ) {
        LaunchedEffect(shouldCapture) {
            if (shouldCapture) {
                coroutineScope.launch(Dispatchers.IO) {
                    val bitmap = graphicsLayer.toImageBitmap()
                    onBitmap(bitmap)
                }
            }
        }
        content()
    }
}
