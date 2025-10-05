package de.berlindroid.zepatch.stiches

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.alpha
import androidx.core.graphics.get
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.embroidermodder.punching.Histogram
import com.embroidermodder.punching.colors
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.io.encoding.Base64
import kotlin.math.floor
import kotlin.math.sqrt

data class XY(
    val x: Float,
    val y: Float,
) {
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
    constructor(x: Double, y: Double) : this(x.toFloat(), y.toFloat())
    constructor() : this(0, 0)
}

fun XY.distanceTo(other: XY): Float = sqrt((other.x - this.x).squared + (other.y - this.y).squared)
fun XY.orthogonal(): XY = XY(y, -x)
fun XY.normalize(): XY = this / length

val XY.length: Float
    get() = distanceTo(XY())

val XY.isNaN: Boolean
    get() = x.isNaN() || y.isNaN() || length < 0.01f

operator fun XY.minus(other: XY): XY = XY(this.x - other.x, this.y - other.y)
operator fun XY.plus(other: XY): XY = XY(this.x + other.x, this.y + other.y)
operator fun XY.div(scalar: Float): XY = XY(x / scalar, y / scalar)
operator fun XY.times(scalar: Float): XY = XY(x * scalar, y * scalar)
operator fun Float.times(xy: XY): XY = XY(xy.x * this, xy.y * this)

infix fun XY.dot(other: XY): Float = (this.x * other.x) + (this.y * other.y)

private val Float.squared: Float
    get() = this * this

data class Thread(
    val color: Int,
    val stitches: Array<XY>,
    val absolute: Boolean,
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
     * Beautiful transformation of an incoming bitmap and histogram to a set of stitches.
     */
    fun createEmbroideryFromBitmap(
        name: String,
        bitmap: Bitmap,
        histogram: Histogram,
        mmWidth: Float,
        mmHeight: Float,
        mmDensityX: Float,
        mmDensityY: Float,
        satinBorderThickness: Float,
        satinBorderDensity: Float,
        satinBorderDilationRadius: Int,
    ): Embroidery {
        val strandsPerColor =
            bitmap.getColorStrands(
                histogram,
                mmWidth * 10,
                mmHeight * 10,
                mmDensityX * 10,
                mmDensityY * 10
            )

        val threads = strandsPerColor.map { strandWithColor ->
            val (color, strand) = strandWithColor
            Thread(
                color = color,
                stitches = strand.toStitchesWithMinimalJumps(),
                absolute = true,
            )
        }.toTypedArray()

        val border = if (satinBorderThickness > 0.1f && satinBorderDensity > 0.05f) {
            bitmap.satinBorder(
                color = Color.BLACK,
                thickness = satinBorderThickness,
                distance = satinBorderDensity,
                widthMm = mmWidth * 10,
                heightMm = mmHeight * 10,
                dilationRadius = satinBorderDilationRadius,
            )
        } else {
            listOf()
        }
        return Embroidery(
            name,
            threads + border
        )
    }

    private fun Bitmap.satinBorder(
        color: Int,
        thickness: Float,
        distance: Float,
        widthMm: Float,
        heightMm: Float,
        dilationRadius: Int,
    ): List<Thread> {
        val mat = Mat()
        val contours = mutableListOf<MatOfPoint>()

        val scaleX = (widthMm / width).toDouble()
        val scaleY = (heightMm / height).toDouble()
        // Convert Bitmap to Mat and resize.
        Utils.bitmapToMat(this, mat)
        Imgproc.resize(
            mat,
            mat,
            Size(0.0, 0.0),
            scaleX,
            scaleY,
            Imgproc.INTER_NEAREST,
        )
        // Convert to grayscale and apply binary threshold,
        // as for finding contours the image should be a white object on a black background.
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
        val thresh = Mat()
        Imgproc.threshold(mat, thresh, 0.0, 255.0, Imgproc.THRESH_BINARY)

        // MARK: morphological filters
        val kernelDimension = (dilationRadius * 2 + 1).toDouble()
        val kernelSize = Size(kernelDimension, kernelDimension)
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, kernelSize)

        // Dilation to close small gaps.
        Imgproc.dilate(thresh, thresh, kernel)
        // Erosion shrinks back to original border positions and helps remove small noise.
        Imgproc.erode(thresh, thresh, kernel)
        // MARK: End morphological filters

        val hierarchy = Mat() // Not used.
        Imgproc.findContours(
            thresh,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,  // Only outer contours
            Imgproc.CHAIN_APPROX_TC89_L1 // Reduced details in output
        )

        val outer = contours.asGeometries()
        val threads = outer.map {
            Thread(
                color = color,
                stitches = it.toZigZagStitch(
                    thickness * 10,
                    distance * 10
                ),
                absolute = true,
            )
        }

        return threads
    }
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

private val Array<Thread>.stitches: Array<XY>
    get() = flatMap { thread -> thread.stitches.toList() }.toTypedArray()

private fun Geometry.toStitch(): Array<XY> = coordinates.map { XY(it.x, it.y) }.toTypedArray()

private fun Geometry.toZigZagStitch(
    thickness: Float,
    distance: Float
): Array<XY> {
    val xys = mutableListOf<XY>()

    var humpDirection = 1f

    val points = coordinates.map { XY(it.x, it.y) }.removeDoubles()
    points
        .forEachIndexed { index, first ->
            val second = points.getOrElse(index + 1) { points.first() }
            val direction = second - first
            val normal = direction.orthogonal().normalize() * thickness
            val hump = normal / 2f

            if (normal.isNaN || direction.isNaN) {
                Log.e("NO", "Malformed segment at index $index: $first to $second.")
            } else {
                xys.add(first)
                xys.add(first + (humpDirection * hump))
                humpDirection *= -1f

                val stitchCount = direction.length / distance
                val step = direction * (1f / stitchCount)
                val offset = step / 4f
                for (count in 0..<stitchCount.floor) {
                    val current = step * count.toFloat()
                    xys.add(first + current + offset + (humpDirection * hump))
                    humpDirection *= -1f
                    xys.add(first + current + (3f * offset) + (humpDirection * hump))
                    humpDirection *= -1f
                }
            }
        }
    xys.add(points.first())

    return xys
        .removeDoubles()
        .toTypedArray()
}

private fun List<XY>.removeDoubles() = filterIndexed { index, xy ->
    if (index == lastIndex) {
        true
    } else {
        val other = get(index + 1)
        xy.distanceTo(other) > 0.01f
    }
}

private fun List<MatOfPoint>.asGeometries(): List<Geometry> {
    val geometryFactory = GeometryFactory()
    val polygons = this.mapNotNull { contour ->
        val points = contour.toArray().map { Coordinate(it.x, it.y) }.toTypedArray()

        if (points.size < 3) return@mapNotNull null

        val closed = if (points.first() != points.last()) points + points.first() else points
        val ring = geometryFactory.createLinearRing(closed)
        geometryFactory.createPolygon(ring)
    }

    return polygons.toMutableList()
}

private val Float.floor: Int
    get() = floor(this).toInt()
