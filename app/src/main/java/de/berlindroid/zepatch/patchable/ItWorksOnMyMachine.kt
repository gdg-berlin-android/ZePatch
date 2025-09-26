package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@Patch("ItWorksBw")
@Composable
fun ItWorksBw(
    shouldCapture: Boolean = false, // used to activate the convert to bitmap
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    // Safe Area is the part that becomes the patch
    SafeArea(
        shouldCapture = shouldCapture, // You need to pass this through from the parent or it won't work
        onBitmap = onBitmap, // You need to pass this through from the parent or it won't work
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "¯\\_(ツ)_/¯",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp),
                color = Color.Black
            )
            Text(
                "IT WORKS",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                "on my machine",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = Italic,
                color = Color.Black
            )
        }
    }
}

@Patch("ItWorksWb")
@Composable
fun ItWorksWb(
    shouldCapture: Boolean = false, // used to activate the convert to bitmap
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture, // You need to pass this through from the parent or it won't work
        onBitmap = onBitmap, // You need to pass this through from the parent or it won't work
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "¯\\_(ツ)_/¯",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp),
                color = Color.White
            )
            Text(
                "IT WORKS",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                "on my machine",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = Italic,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun ItWorksOnMyMachineBwPreview() {
    ItWorksBw()
}

@Preview
@Composable
fun ItWorksOnMyMachineWbPreview() {
    ItWorksWb()
}