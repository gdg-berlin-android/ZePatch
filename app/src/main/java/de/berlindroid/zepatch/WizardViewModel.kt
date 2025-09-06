package de.berlindroid.zepatch

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.reduceColors
import de.berlindroid.zepatch.PatchablePreviewMode.COMPOSABLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to manage the Wizard screen state.
 */
class WizardViewModel : ViewModel() {

    data class UIState(
        val imageBitmap: ImageBitmap? = null,
        val reducedImageBitmap: ImageBitmap? = null,
        val reducedHistogram: Histogram? = null,
        val colorCount: Int = 3,
        val embroideryData: ByteArray? = null,
        val embroideryPreviewImage: ImageBitmap? = null,
        val previewMode: PatchablePreviewMode = COMPOSABLE,
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    /** Resets the whole wizard state back to initial defaults. */
    fun reset() {
        _uiState.value = UIState()
    }

    fun setPreviewMode(mode: PatchablePreviewMode) {
        _uiState.value = _uiState.value.copy(previewMode = mode)
    }

    fun updateColorCount(count: Int) {
        _uiState.value = _uiState.value.copy(colorCount = count)
    }

    fun updateBitmap(bitmap: ImageBitmap) {
        _uiState.value = _uiState.value.copy(
            imageBitmap = bitmap,
            // Reset downstream results when source bitmap changes
            reducedImageBitmap = null,
            reducedHistogram = null,
            embroideryData = null,
            embroideryPreviewImage = null,
        )
    }

    fun computeReducedBitmap() {
        val state = _uiState.value
        val image = state.imageBitmap ?: return
        val colorCount = state.colorCount
        viewModelScope.launch(Dispatchers.IO) {
            val aspect = image.width / image.height.toFloat()
            val (reducedBmp, histogram) = image.asAndroidBitmap()
                .copy(Bitmap.Config.ARGB_8888, false)
                .scale((512 * aspect).toInt(), 512, false)
                .reduceColors(colorCount)

            _uiState.value = _uiState.value.copy(
                reducedImageBitmap = reducedBmp.asImageBitmap(),
                reducedHistogram = histogram,
            )
        }
    }

    fun updateEmbroidery(data: ByteArray, preview: ImageBitmap) {
        _uiState.value = _uiState.value.copy(
            embroideryData = data,
            embroideryPreviewImage = preview,
        )
    }

    fun setReducedResult(image: ImageBitmap, histogram: Histogram) {
        _uiState.value = _uiState.value.copy(
            reducedImageBitmap = image,
            reducedHistogram = histogram,
        )
    }
}
