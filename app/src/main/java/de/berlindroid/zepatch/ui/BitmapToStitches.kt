package de.berlindroid.zepatch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.scale
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.colorHistogram
import de.berlindroid.zepatch.WizardViewModel.UiState.EmbroiderBitmap
import de.berlindroid.zepatch.isBusy
import de.berlindroid.zepatch.utils.randomBitmap

private const val MIN_DENSITY = 0.2f
private const val MAX_DENSITY = 3f

private const val MIN_BORDER = 0.2f
private const val MAX_BORDER = 150f

private const val MIN_SIZE = 10
private const val MAX_SIZE = 120
private const val MIN_DILATION = 1

@Composable
fun BitmapToStitches(
    modifier: Modifier = Modifier,
    state: EmbroiderBitmap,
    onUpdateEmbroidery: (
        densityX: Float,
        densityY: Float,
        size: Float,
        borderThickness: Float,
        borderDensity: Float,
        borderDilationRadius: Int,
    ) -> Unit,
    onCreateEmbroidery: () -> Unit,
) {
    val containerSize = LocalWindowInfo.current.containerSize

    val imageWidth = containerSize.width * 3 / 4f
    val imageHeight = (imageWidth * (state.reducedBitmap.height.toFloat() / state.reducedBitmap.width))

    var superSecretPirateMode by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WizardSectionTitle(
            title = "Generate Stitches",
            helpText = "Turn the reduced bitmap into an embroidery stitch file and preview the result."
        )
        Image(
            bitmap = state.reducedBitmap.asAndroidBitmap().scale(imageWidth.toInt(), imageHeight.toInt(), false)
                .asImageBitmap(),
            contentDescription = "reduced bitmap",
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = {
                        superSecretPirateMode = !superSecretPirateMode
                    },
                    onClick = {}
                )
        )

        if (superSecretPirateMode) {
            SuperSecretePirateControls(state, onUpdateEmbroidery)
        }

        Button(
            onClick = onCreateEmbroidery,
            enabled = !state.isBusy()
        ) { Text("Generate") }

        state.embroideryPreviewImage?.let {
            val aspect = it.width.toFloat() / it.height

            Image(
                bitmap = it.asAndroidBitmap().scale((imageWidth * aspect).toInt(), imageHeight.toInt(), false)
                    .asImageBitmap(),
                contentDescription = "patch bitmap",
                modifier = Modifier.fillMaxWidth()
            )
        } ?: Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (state.currentlyEmbroidering) CircularProgressIndicator()
        }
    }
}

@Composable
private fun ColumnScope.SuperSecretePirateControls(
    state: EmbroiderBitmap,
    onUpdateEmbroidery: (
        densityX: Float,
        densityY: Float,
        size: Float,
        borderThickness: Float,
        borderDensity: Float,
        borderDilationRadius: Int,
    ) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
    )
    {
        Column(
            Modifier.padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                text = "🪡🧵🏴‍☠️",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 96.sp)
            )


            FloatSelector(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.densityX}",
                unit = "mm",
                label = "densityX",
                errorText = { "Width density out of range ($it not in ($MIN_DENSITY, $MAX_DENSITY]" },
                acceptableNumberEntered = {
                    (it.toFloatOrNull() ?: Float.MIN_VALUE) in (MIN_DENSITY..MAX_DENSITY)
                },
                onNumberChanged = {
                    onUpdateEmbroidery(
                        it,
                        state.densityY,
                        state.size,
                        state.borderThickness,
                        state.borderDensity,
                        state.borderDilation,
                    )
                },
                onDone = {}
            )

            FloatSelector(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.densityY}",
                unit = "mm",
                label = "densityY",
                errorText = { "Height density out of range ($it not in ($MIN_DENSITY, $MAX_DENSITY]" },
                acceptableNumberEntered = {
                    (it.toFloatOrNull() ?: Float.MIN_VALUE) in (MIN_DENSITY..MAX_DENSITY)
                },
                onNumberChanged = {
                    onUpdateEmbroidery(
                        state.densityX,
                        it,
                        state.size,
                        state.borderThickness,
                        state.borderDensity,
                        state.borderDilation,
                    )
                },
                onDone = {}
            )

            IntSelector(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.size.toInt()}",
                unit = "mm",
                label = "size",
                errorText = { "Size $MIN_SIZE, $MAX_SIZE." },
                acceptableNumberEntered = {
                    (it.toIntOrNull() ?: 0) in (MIN_SIZE..MAX_SIZE)
                },
                onNumberChanged = {
                    onUpdateEmbroidery(
                        state.densityX,
                        state.densityY,
                        it.toFloat(),
                        state.borderThickness,
                        state.borderDensity,
                        state.borderDilation,
                    )
                },
                onDone = {}
            )

            FloatSelector(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.borderThickness}",
                unit = "mm",
                label = "satin border thickness",
                errorText = { "border size of $it not in ($MIN_BORDER, $MAX_BORDER]" },
                acceptableNumberEntered = {
                    (it.toFloatOrNull() ?: Float.MIN_VALUE) in (MIN_BORDER..MAX_BORDER)
                },
                onNumberChanged = {
                    onUpdateEmbroidery(
                        state.densityX,
                        state.densityY,
                        state.size,
                        it,
                        state.borderDensity,
                        state.borderDilation,
                    )
                },
                onDone = {}
            )

            FloatSelector(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.borderDensity}",
                unit = "mm",
                label = "satin border density",
                errorText = { "border size of $it not in ($MIN_DENSITY, $MAX_DENSITY]" },
                acceptableNumberEntered = {
                    (it.toFloatOrNull() ?: Float.MIN_VALUE) in (MIN_DENSITY..MAX_DENSITY)
                },
                onNumberChanged = {
                    onUpdateEmbroidery(
                        state.densityX,
                        state.densityY,
                        state.size,
                        state.borderThickness,
                        it,
                        state.borderDilation,
                    )
                },
                onDone = {}
            )

            IntSelector(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.borderDilation}",
                unit = "mm",
                label = "satin border gap bridging radius",
                errorText = { "Radius of $it not in $MIN_DILATION, $MAX_SIZE." },
                acceptableNumberEntered = {
                    (it.toIntOrNull() ?: MIN_DILATION) in (MIN_DILATION..MAX_SIZE)
                },
                onNumberChanged = {
                    onUpdateEmbroidery(
                        state.densityX,
                        state.densityY,
                        state.size,
                        state.borderThickness,
                        state.borderDensity,
                        it,
                    )
                },
                onDone = {}
            )
        }
    }
}

@Preview
@Composable
private fun Pirates() {
    Column {
        SuperSecretePirateControls(
            state = EmbroiderBitmap(
                name = "Somename",
                image = randomBitmap(100, 100).asImageBitmap(),
                colorCount = 4,
                reducedBitmap = randomBitmap(100, 100).asImageBitmap(),
                reducedHistogram = Histogram(emptyMap())
            ),
            onUpdateEmbroidery = { _, _, _, _, _, _ -> }
        )
    }
}

@Preview
@Composable
private fun BitmapToStitchesPreview() {
    val imageBitmap = ImageBitmap(width = 100, height = 100)
    val histogram = imageBitmap.asAndroidBitmap().colorHistogram()
    BitmapToStitches(
        state = EmbroiderBitmap(
            image = imageBitmap,
            colorCount = 44,
            reducedBitmap = imageBitmap,
            reducedHistogram = histogram,
            name = "MyPatch",
            currentlyEmbroidering = false,
        ),
        onUpdateEmbroidery = { _, _, _, _, _, _ -> },
        onCreateEmbroidery = {},
    )
}
