package de.berlindroid.zepatch.patchable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@Patch("flex")
@Composable
fun Emoji(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Text("💪", fontSize = 180.sp)
    }
}

@Patch("ROBOT")
@Composable
fun Emoji2(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Text("🦾🤖\nHereWeGo", fontSize = 34.sp)
    }
}

@Preview
@Composable
fun PreviewFlex() {
    Emoji()
}