package de.berlindroid.zepatch.ui

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.scale
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.colorHistogram
import de.berlindroid.zepatch.stiches.StitchToPES
import de.berlindroid.zepatch.stiches.StitchToPES.createEmbroideryFromBitmap
import de.berlindroid.zepatch.utils.multiLet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BitmapToStitches(
    modifier: Modifier = Modifier,
    reducedImageBitmap: ImageBitmap,
    reducedHistogram: Histogram,
    name: String,
    onEmbroidery: (pes: ByteArray, preview: ImageBitmap) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var displayImage by remember { mutableStateOf<ImageBitmap?>(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        Image(
            bitmap = reducedImageBitmap,
            contentDescription = "patch bitmap",
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                val aspect = reducedImageBitmap.width / reducedImageBitmap.height.toFloat()
                val embroidery = createEmbroideryFromBitmap(
                    name,
                    bitmap = reducedImageBitmap.asAndroidBitmap(),
                    histogram = reducedHistogram,
                    mmWidth = 500f * aspect,
                    mmHeight = 500f,
                    mmDensityX = 4f,
                    mmDensityY = 2f,
                )

                val pes = StitchToPES.convert(context, embroidery)
                if (pes == null || pes.isEmpty()) {
                    Log.e("EMBNO", "No pes found.")
                }

                val png = StitchToPES.convert(context, embroidery, "png")
                displayImage = if (png == null || png.isEmpty()) {
                    Log.e("PNGNO", "No png returned.")
                    null
                } else {
                    val decoded = BitmapFactory.decodeByteArray(png, 0, png.size)

                    decoded
                        .scale(decoded.width * 2, decoded.height * 2)
                        .asImageBitmap()
                }

                pes?.multiLet(displayImage) { pes, image ->
                    onEmbroidery(
                        pes, image
                    )
                }
            }
        }) { Text("Do it") }  // Compose the content through the composable utility; it will invoke the callback when ready.

        displayImage?.let {
            Image(
                bitmap = it,
                contentDescription = "patch bitmap",
                modifier = Modifier.fillMaxWidth()
            )
        } ?: CircularProgressIndicator()
    }
}

@Preview
@Composable
fun BitmapToStitchesPreview() {
    val imageBitmap =
        ImageBitmap(width = 100, height = 100) // Replace with a real ImageBitmap if needed
    val histogram = imageBitmap.asAndroidBitmap().colorHistogram()
    BitmapToStitches(
        reducedImageBitmap = imageBitmap,
        reducedHistogram = histogram,
        name = "MyPatch",
        onEmbroidery = { _, _ -> }
    )
}

