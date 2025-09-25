package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@Patch("Ziva") // you need this to register it
@Composable
fun Ziva(
    shouldCapture: Boolean = false, // used to activate the convert to bitmap
    onBitmap: (ImageBitmap) -> Unit = {}, // used to return the bitmap from the SafeArea
) {
    // Safe Area is the part that becomes the patch
    SafeArea(
        shouldCapture = shouldCapture, // You need to pass this through from the parent or it won't work
        onBitmap = onBitmap, // You need to pass this through from the parent or it won't work
    ) {
        Image(
            painter = painterResource(R.drawable.kura),
            modifier = Modifier
              .size(200.dp)
              .fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )
    }
    // Add interactive pieces here. This will not be part of the patch
}

@Preview
@Composable
fun ZivaPreview() {
    Ziva()
}