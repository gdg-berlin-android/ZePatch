package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea
import dev.nstv.composablesheep.library.ComposableSheep
import dev.nstv.composablesheep.library.model.Sheep
import dev.nstv.composablesheep.library.util.SheepColor

@Preview
@Patch("Composable Sheep")
@Composable
fun ComposableSheepPatchable(
    shouldCapture: Boolean = false, // used to activate the convert to bitmap
    onBitmap: (ImageBitmap) -> Unit = {}, // used to return the bitmap from the SafeArea
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        ComposableSheep(
            sheep = Sheep(fluffColor = SheepColor.Green),
            modifier = Modifier.size(300.dp),
        )
    }
}