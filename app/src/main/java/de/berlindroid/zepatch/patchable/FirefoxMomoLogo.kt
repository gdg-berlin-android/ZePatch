package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Patch("FirefoxMomoLogo")
@Preview
@Composable
fun FirefoxMomoLogo(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
            .border(2.dp, Color.Black, shape = CircleShape)
                .padding(start = 2.dp, end = 6.dp)
            .size(218.dp)
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(R.drawable.ic_momo),
                contentDescription = null
            )
        }
    }
}