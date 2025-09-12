package de.berlindroid.zepatch.stiches

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.alpha
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.get
import androidx.core.graphics.scale
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.colors
import com.embroidermodder.punching.reduceColors
import de.berlindroid.zepatch.converter.R
import kotlin.collections.toMutableMap
import kotlin.io.encoding.Base64
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class XY(
    val x: Float,
    val y: Float,
) {
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
    constructor() : this(0, 0)
}

fun XY.distanceTo(other: XY): Float = sqrt((other.x - this.x).squared + (other.y - this.y).squared)

val XY.length: Float
    get() = distanceTo(XY())

private val Float.squared: Float
    get() = this * this

data class Thread(
    val color: Int,
    val stitches: Array<XY>,
    val absolute: Boolean = false,
)

data class Embroidery(
    val name: String,
    val threads: Array<Thread>
)

object StitchToPES {
    fun convert(context: Context, embroidery: Embroidery, fileFormat: String = "pes"): ByteArray? {
        Log.i("PYTHON", "Creating PES for '${embroidery.name}'.")

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }

        try {
            val py = Python.getInstance()

            val converter = py.getModule("converter")
            val result = converter.callAttr("convert", embroidery, fileFormat)

            val pesBytes = result.toJava(ByteArray::class.java)
            Log.i("PYTHON", "Found these bytes: ${Base64.encode(pesBytes)}.")

            return pesBytes
        } catch (th: Throwable) {
            Log.e("PYTHON", "No.", th)
            Toast.makeText(context, "Python error: ${th.message}.", Toast.LENGTH_LONG).show()
            return null
        }
    }

    /**
     * Crappy first iteration, do not look to closely.
     */
    fun createEmbroideryFromBitmap(
        name: String,
        bitmap: Bitmap,
        histogram: Histogram,
        mmWidth: Float,
        mmHeight: Float,
        mmDensityX: Float,
        mmDensityY: Float
    ): Embroidery {
        val strandsPerColor =
            bitmap.getColorStrands(
                histogram,
                mmWidth,
                mmHeight,
                mmDensityX,
                mmDensityY
            )

        return Embroidery(
            name,
            strandsPerColor.map { strandWithColor ->
                val (color, strand) = strandWithColor
                Thread(
                    color = color,
                    stitches = strand
                        .toStitchesWithMinimalJumps()
                        .map {
                            XY(
                                (it.x * 10).toInt() / 10f,
                                (it.y * 10).toInt() / 10f,
                            )
                        }.toTypedArray(),
                    absolute = true,
                )
            }.toTypedArray()
        )
    }

    fun createDummyEmbroidery(distance: Float = 25f, step: Float = 1.5f) = Embroidery(
        name = "RedCircleInGreenFilledRectangle",
        threads = arrayOf(
            Thread(
                color = 0xff0000,
                absolute = false,
                stitches = (0..distance.toInt()).map { y ->
                    val dir = if (y % 2 == 0) 1 else -1
                    (0..distance.toInt()).map { x ->
                        XY(dir * step, 0f)
                    } + XY(0f, step)
                }.flatten().toTypedArray()
            ),
            Thread(
                color = 0x00ff00,
                absolute = true,
                stitches = (0..360).map { angle ->
                    val alpha = angle.radians()
                    XY(
                        x = cos(alpha) * distance * step / 2 + distance / 2 * step,
                        y = sin(alpha) * distance * step / 2 + distance / 2 * step,
                    )
                }.toTypedArray()
            )
        )
    )

    private fun Int.radians(): Float = ((this / 180.0) * PI).toFloat()

    private fun getSampleBitmap(context: Context) =
        ResourcesCompat.getDrawable(context.resources, R.drawable.test, null)
            ?.toBitmap(config = Bitmap.Config.ARGB_4444)
            ?.scale(32, 32, false)
            ?.reduceColors(3)
            ?.first
}

private fun List<List<XY>>.toStitchesWithMinimalJumps(): Array<XY> {
    if (isEmpty()) return arrayOf()

    val pixelStrands = mutableListOf(*toTypedArray())

    // TODO: Think about backtracking or similar optimizations
    val stitches = mutableListOf<XY>()
    stitches.addAll(pixelStrands.removeAt(0))

    // TODO: NEED TO REVERSE FIRST HERE?
    while (pixelStrands.isNotEmpty()) {
        val (nextStrand, reverseNeeded) = pixelStrands.closest(stitches)
        val removed = pixelStrands.remove(nextStrand)
        assert(removed)

        if (reverseNeeded) {
            stitches.addAll(nextStrand.asReversed())
        } else {
            stitches.addAll(nextStrand)
        }
    }

    return stitches.toTypedArray()
}

private data class ClosestStrand(
    val strand: List<XY>,
    val needsReversal: Boolean
)

private fun List<List<XY>>.closest(strand: List<XY>): ClosestStrand {
    val mini = minByOrNull {
        listOf(
            strand.last().distanceTo(it.first()),
            strand.last().distanceTo(it.last())
        ).minOrNull() ?: Float.MAX_VALUE
    }!!

    val reversal =
        strand.last().distanceTo(mini.last()) < strand.last().distanceTo(mini.first())

    return ClosestStrand(mini, reversal)
}

private fun Bitmap.getColorStrands(
    histogram: Histogram,
    widthMm: Float,
    heightMm: Float,
    densityX: Float,
    densityY: Float,
    maxStitchDistance: Float = XY(densityX, densityY).length * 2f
): Map<Int, List<List<XY>>> {
    val resultMap: MutableMap<Int, MutableList<MutableList<XY>>> =
        histogram.colors.associate { color ->
            color to mutableListOf<MutableList<XY>>(mutableListOf())
        }.toMutableMap<Int, MutableList<MutableList<XY>>>()

    val scaledHeight = (heightMm / densityY).toInt()
    val scaledWidth = (widthMm / densityX).toInt()

    for (y in 0..<scaledHeight) {
        val pixelY = ((y / scaledHeight.toFloat()) * height).toInt()

        for (x in 0..<scaledWidth) {
            val pixelX = ((x / scaledWidth.toFloat()) * width).toInt()
            val currentPixelMm = XY(x * densityX, y * densityY)

            val currentColor = get(pixelX, pixelY)

            if (currentColor.alpha != 255) {
                // ignore non opaque pixel colors
                continue
            }

            if (currentColor !in resultMap) {
                // color outside of histogram found (rounding? filtering?)
                continue
            }

            val lastColoredPixelXY = resultMap[currentColor]!!.last().lastOrNull()
            if (lastColoredPixelXY != null) {
                if (lastColoredPixelXY.distanceTo(currentPixelMm) > maxStitchDistance) {
                    // current pixel is too far away from last pixel
                    // create a new strand
                    resultMap[currentColor]!!.add(mutableListOf())
                }
            }

            val strandsByCurrentColor = resultMap[currentColor]!!
            val lastStrand = strandsByCurrentColor.last()
            lastStrand.add(currentPixelMm)
        }
    }

    return resultMap
}
