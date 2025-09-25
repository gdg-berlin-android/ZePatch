package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea


@Patch("Qumber")
@Composable
fun AndLogo(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Box(
            modifier = Modifier
                .border(
                    BorderStroke(
                        1.dp,
                        Color.Black
                    ), RoundedCornerShape(100)
                )
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                painter = painterResource(R.drawable.android_seeklogo),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun AndLogoPreview() {
    AndLogo()
}