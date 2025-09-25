package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@Patch("Android Developer Tips YouTube Channel QR Code")
@Composable
fun AndroidDeveloperTipsQrCode(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(R.drawable.adt_qr_code),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Green)
        )
    }
}

@Preview
@Composable
fun AndroidDeveloperTipsQrCodePreview() {
    AndroidDeveloperTipsQrCode()
}

@Patch("Android Developer Tips YouTube Channel QR Code Vector")
@Composable
fun AndroidDeveloperTipsQrCodeVector(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(300.dp),
                painter = painterResource(R.drawable.adt_qr_code_vector),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Green)
            )

            Text(
                text = "Android Developer Tips",
                color = Color.Green,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Preview
@Composable
fun AndroidDeveloperTipsQrCodeVectorPreview() {
    AndroidDeveloperTipsQrCodeVector()
}

@Patch("Android Developer Tips YouTube Channel Logo")
@Composable
fun AndroidDeveloperTipsLogo(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Box(
            Modifier
                .border(2.dp, Color.Black)
                .size(300.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                painter = painterResource(R.drawable.adt_logo),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Green)
            )
        }
    }
}

@Preview
@Composable
fun AndroidDeveloperTipsLogoPreview() {
    AndroidDeveloperTipsLogo()
}

@Patch("Android Developer Tips YouTube Channel Badge")
@Composable
fun AndroidDeveloperTipsBadge(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(R.drawable.adt_badge),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Green)
        )
    }
}

@Preview
@Composable
fun AndroidDeveloperTipsBadgePreview() {
    AndroidDeveloperTipsBadge()
}
