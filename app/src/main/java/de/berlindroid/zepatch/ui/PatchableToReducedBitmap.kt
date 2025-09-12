package de.berlindroid.zepatch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.text.isDigitsOnly

@Composable
fun PatchableToReducedBitmap(
    modifier: Modifier = Modifier,
    image: ImageBitmap? = null,
    colorCount: Int = 3,
    reducedImage: ImageBitmap? = null,
    computeReducedBitmap: () -> Unit = {},
    onColorCountChanged: (Int) -> Unit = {}
) {
    // Local text state so users can clear or type partial numbers without snapping back to default
    var colorText by rememberSaveable { mutableStateOf(colorCount.toString()) }

    // Keep local text in sync if colorCount changes from outside (e.g., recomputations)
    LaunchedEffect(colorCount) {
        colorText = colorCount.toString()
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WizardSectionTitle(
            title = "Reduce Colors",
            helpText = "Choose the number of colors and generate a simplified bitmap suitable for embroidering."
        )
        image?.let {
            Image(
                bitmap = it,
                contentDescription = "patch bitmap",
                modifier = Modifier.fillMaxWidth()
            )
        }
        TextField(
            value = colorText,
            onValueChange = { new ->
                // Allow empty and numeric-only input
                if (new.isEmpty()) {
                    colorText = ""
                } else if (new.isDigitsOnly()) {
                    colorText = new
                    new.toIntOrNull()?.let { onColorCountChanged(it) }
                }
                // ignore non-digit edits
            },
            label = { Text("How many colors do you need?") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isValidColorCount(colorText)) {
                        focusManager.clearFocus()
                        computeReducedBitmap()
                    }
                }
            ),
            isError = !isValidColorCount(colorText), // Set the error state
            supportingText = {
                if (!isValidColorCount(colorText)) {
                    Text(
                        text = "Please enter a valid color count (between 1 and 7)",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Button(enabled = isValidColorCount(colorText), onClick = computeReducedBitmap) { Text("Reduce") }

        reducedImage?.let {
            Image(
                bitmap = it,
                contentDescription = "patch bitmap",
                modifier = Modifier.fillMaxWidth()
            )
        } ?: CircularProgressIndicator()
    }
}

fun isValidColorCount(colorCountText: String): Boolean =
    (colorCountText.toIntOrNull() ?: -1) in 1..7


@Preview(showBackground = true)
@Composable
fun PatchableToReducedBitmapPreview() {
    PatchableToReducedBitmap(
        image = ImageBitmap(width = 100, height = 100), // Example ImageBitmap
        colorCount = 5,
        onColorCountChanged = {}
    )
}


