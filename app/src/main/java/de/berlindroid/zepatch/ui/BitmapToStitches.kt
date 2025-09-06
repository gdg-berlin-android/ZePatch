package de.berlindroid.zepatch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.colorHistogram

@Composable
fun BitmapToStitches(
    modifier: Modifier = Modifier,
    reducedImageBitmap: ImageBitmap,
    reducedHistogram: Histogram,
    name: String,
    onCreateEmbroidery: (name: String, bitmap: ImageBitmap, histogram: Histogram) -> Unit,
    previewImage: ImageBitmap?,
    creatingEmbroidery: Boolean,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Image(
            bitmap = reducedImageBitmap,
            contentDescription = "patch bitmap",
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            onCreateEmbroidery(name, reducedImageBitmap, reducedHistogram)
        }) { Text("Do it") }

        val preview = previewImage
        if (preview != null) {
            Image(
                bitmap = preview,
                contentDescription = "patch bitmap",
                modifier = Modifier.fillMaxWidth()
            )
        } else if (creatingEmbroidery) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun BitmapToStitchesPreview() {
    val imageBitmap = ImageBitmap(width = 100, height = 100)
    val histogram = imageBitmap.asAndroidBitmap().colorHistogram()
    BitmapToStitches(
        reducedImageBitmap = imageBitmap,
        reducedHistogram = histogram,
        name = "MyPatch",
        onCreateEmbroidery = { _, _, _ -> },
        previewImage = null,
        creatingEmbroidery = false,
    )
}

