package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Patch("EventFahrplanLogo")
@Composable
fun EventFahrplanLogo(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.eventfahrplan_logo),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun PreviewEventFahrplanLogo() {
    EventFahrplanLogo()
}
