package de.berlindroid.zepatch

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.reduceColors
import de.berlindroid.zepatch.PatchablePreviewMode.COMPOSABLE
import de.berlindroid.zepatch.stiches.StitchToPES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel to manage the Wizard screen state.
 */

class WizardViewModel(application: Application) : AndroidViewModel(application) {

    data class UIState(
        val imageBitmap: ImageBitmap? = null,
        val reducedImageBitmap: ImageBitmap? = null,
        val reducedHistogram: Histogram? = null,
        val colorCount: Int = 3,
        val embroideryData: ByteArray? = null,
        val embroideryPreviewImage: ImageBitmap? = null,
        val creatingEmbroidery: Boolean = false,
        val error: String? = null,
        val previewMode: PatchablePreviewMode = COMPOSABLE,
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    /** Resets the whole wizard state back to initial defaults. */
    fun reset() {
        _uiState.update { UIState() }
    }

    fun setPreviewMode(mode: PatchablePreviewMode) {
        _uiState.update { it.copy(previewMode = mode) }
    }

    fun updateColorCount(count: Int) {
        _uiState.update { it.copy(colorCount = count) }
    }

    fun updateBitmap(bitmap: ImageBitmap) {
        _uiState.update {
            it.copy(
                imageBitmap = bitmap,
                // Reset downstream results when source bitmap changes
                reducedImageBitmap = null,
                reducedHistogram = null,
                embroideryData = null,
                embroideryPreviewImage = null,
            )
        }
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

            _uiState.update {
                it.copy(
                    reducedImageBitmap = reducedBmp.asImageBitmap(),
                    reducedHistogram = histogram,
                )
            }
        }
    }

    fun createEmbroidery(
        name: String,
        bitmap: ImageBitmap,
        histogram: Histogram,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(creatingEmbroidery = true, error = null) }

                val aspect = bitmap.width / bitmap.height.toFloat()
                val embroidery = StitchToPES.createEmbroideryFromBitmap(
                    name = name,
                    bitmap = bitmap.asAndroidBitmap(),
                    histogram = histogram,
                    mmWidth = 500f * aspect,
                    mmHeight = 500f,
                    mmDensityX = 4f,
                    mmDensityY = 2f,
                )

                val context = getApplication<Application>().applicationContext
                val pes = StitchToPES.convert(context, embroidery)
                val png = StitchToPES.convert(context, embroidery, "png")

                val previewImage = if (png == null || png.isEmpty()) {
                    null
                } else {
                    val decoded = BitmapFactory.decodeByteArray(png, 0, png.size)
                    decoded.scale(decoded.width * 2, decoded.height * 2).asImageBitmap()
                }

                _uiState.update {
                    it.copy(
                        embroideryData = pes,
                        embroideryPreviewImage = previewImage,
                        creatingEmbroidery = false,
                    )
                }
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(
                        creatingEmbroidery = false,
                        error = t.message ?: t.toString(),
                    )
                }
            }
        }
    }

}
