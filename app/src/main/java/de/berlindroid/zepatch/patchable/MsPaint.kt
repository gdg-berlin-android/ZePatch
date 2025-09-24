package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea
import android.graphics.Bitmap
import androidx.compose.runtime.mutableIntStateOf
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import android.graphics.Paint.Cap as AndroidCap
import android.graphics.Paint.Style as AndroidStyle
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.core.graphics.createBitmap

private enum class OutlineShape { Circle, Diamond, Square, Heart }
private data class StrokePath(val color: Color, val points: SnapshotStateList<Offset>)
private enum class Tool { Brush, Fill }

// Simple BFS flood fill on an ARGB_8888 bitmap
private fun floodFill(bitmap: Bitmap, sx: Int, sy: Int, newColor: Int) {
    if (sx < 0 || sy < 0 || sx >= bitmap.width || sy >= bitmap.height) return
    val target = bitmap[sx, sy]
    if (target == newColor) return
    val w = bitmap.width
    val h = bitmap.height
    val deque = ArrayDeque<Int>()
    fun push(x: Int, y: Int) {
        if (x in 0 until w && y in 0 until h) {
            if (bitmap[x, y] == target) {
                deque.addLast((y shl 16) or x)
                bitmap[x, y] = newColor
            }
        }
    }
    push(sx, sy)
    while (deque.isNotEmpty()) {
        val p = deque.removeFirst()
        val x = p and 0xFFFF
        val y = (p ushr 16) and 0xFFFF
        push(x + 1, y)
        push(x - 1, y)
        push(x, y + 1)
        push(x, y - 1)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Patch("MsPaint")
@Composable
fun MsPaint(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {

    // Palette colors as requested
    val palette = listOf(
        Color.White,
        Color.Black,
        Color.Red,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.Green,
        Color(0xFFFFA500), // orange
        Color(0xFF800080), // purple
        Color(0xFF0000FF), // blue
        Color(0xFFFFC0CB), // pink
        Color(0xFFA52A2A), // brown
    )

    val selectedColor = remember { mutableStateOf(Color.Red) }
    val outlineShape = remember { mutableStateOf(OutlineShape.Diamond) }

    // list of strokes (each stroke is a polyline of points)
    val strokes = remember { mutableStateListOf<StrokePath>() }

    val currentTool = remember { mutableStateOf(Tool.Brush) }

    // Display bitmap that holds filled regions. Size is set on first layout.
    val displayBitmapState = remember { mutableStateOf<Bitmap?>(null) }
    // Increment to trigger redraws after mutating the bitmap in-place
    val bitmapVersion = remember { mutableIntStateOf(0) }

    // Android paint used when rasterizing strokes to a work bitmap
    val androidPaint = remember {
        AndroidPaint().apply {
            isAntiAlias = true
            style = AndroidStyle.STROKE
            strokeCap = AndroidCap.ROUND
            strokeWidth = 12f
        }
    }

    // Clearing drawing when outline shape changes
    LaunchedEffect(outlineShape.value) {
        strokes.clear()
        displayBitmapState.value?.eraseColor(Color.Transparent.toArgb())
        bitmapVersion.intValue++
    }

    Column(modifier = Modifier.padding(8.dp)) {
        // Top controls: palette (outside the SafeArea)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            palette.forEach { color ->
                val selected = selectedColor.value == color
                Spacer(modifier = Modifier.width(2.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .size(if (selected) 28.dp else 24.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(1.dp, Color.DarkGray, CircleShape)
                            .padding(2.dp)
                            .clickable { selectedColor.value = color }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tool selection
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { currentTool.value = Tool.Brush },
                enabled = currentTool.value != Tool.Brush,
            ) { Text("Brush") }
            Button(
                onClick = { currentTool.value = Tool.Fill },
                enabled =  currentTool.value != Tool.Fill,
            ) { Text("Fill") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // SafeArea contains the drawing canvas only
        SafeArea(
            shouldCapture = shouldCapture,
            onBitmap = onBitmap,
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.Transparent)
                    .onSizeChanged { sz ->
                        val w = sz.width.coerceAtLeast(1)
                        val h = sz.height.coerceAtLeast(1)
                        val existing = displayBitmapState.value
                        if (existing == null || existing.width != w || existing.height != h) {
                            displayBitmapState.value = createBitmap(w, h)
                            // No need to redraw strokes here; they are rendered vectorially. Fills will use a work bitmap when needed.
                            bitmapVersion.intValue++
                        }
                    }
                    .pointerInput(selectedColor.value, outlineShape.value, currentTool.value) {
                        if (currentTool.value == Tool.Brush) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    strokes.add(StrokePath(selectedColor.value, mutableStateListOf(offset)))
                                },
                                onDrag = { _, dragAmount ->
                                    val last = strokes.lastOrNull() ?: return@detectDragGestures
                                    val lastPoint = last.points.last()
                                    last.points.add(lastPoint + dragAmount)
                                }
                            )
                        }
                    }
                    .pointerInput(selectedColor.value, outlineShape.value, currentTool.value, bitmapVersion.intValue) {
                        if (currentTool.value == Tool.Fill) {
                            detectTapGestures(
                                onTap = { offset ->
                                    val bmp = displayBitmapState.value ?: return@detectTapGestures
                                    val x = offset.x.toInt().coerceIn(0, bmp.width - 1)
                                    val y = offset.y.toInt().coerceIn(0, bmp.height - 1)

                                    // Prepare work bitmap: copy current fills, then rasterize strokes as barriers
                                    val work = bmp.copy(Bitmap.Config.ARGB_8888, true)
                                    val barrierColor = 0x01000000
                                    val barrierPaint = AndroidPaint().apply {
                                        isAntiAlias = true
                                        style = AndroidStyle.STROKE
                                        strokeCap = AndroidCap.ROUND
                                        strokeWidth = androidPaint.strokeWidth
                                        color = barrierColor
                                    }
                                    val aCanvas = AndroidCanvas(work)
                                    // Draw all stroke segments as thin barriers
                                    strokes.forEach { stroke ->
                                        for (i in 1 until stroke.points.size) {
                                            val s = stroke.points[i - 1]
                                            val e = stroke.points[i]
                                            aCanvas.drawLine(s.x, s.y, e.x, e.y, barrierPaint)
                                        }
                                    }

                                    // Run flood fill from tap
                                    val newColor = selectedColor.value.toArgb() or (0xFF shl 24)
                                    floodFill(work, x, y, newColor)

                                    // Remove barrier pixels from result (make them transparent)
                                    val w = work.width
                                    val h = work.height
                                    val pixels = IntArray(w * h)
                                    work.getPixels(pixels, 0, w, 0, 0, w, h)
                                    for (i in pixels.indices) {
                                        if (pixels[i] == barrierColor) pixels[i] = 0x00000000
                                    }
                                    work.setPixels(pixels, 0, w, 0, 0, w, h)

                                    // Replace display bitmap with new reference so Compose observes state change
                                    displayBitmapState.value = work
                                    bitmapVersion.intValue++
                                }
                            )
                        }
                    }
                    .height(240.dp)
            ) {
                // Compute outline path within canvas
                val padding = 16f
                val rect = Rect(padding, padding, size.width - padding, size.height - padding)
                val outline = Path().apply {
                    when (outlineShape.value) {
                        OutlineShape.Circle -> addOval(rect)
                        OutlineShape.Square -> addRect(rect)
                        OutlineShape.Diamond -> {
                            moveTo(rect.center.x, rect.top)
                            lineTo(rect.right, rect.center.y)
                            lineTo(rect.center.x, rect.bottom)
                            lineTo(rect.left, rect.center.y)
                            close()
                        }
                        OutlineShape.Heart -> {
                            val w = rect.width
                            val h = rect.height
                            val x = rect.left
                            val y = rect.top
                            // A symmetric heart built with two cubic Bezier curves inside the rect
                            moveTo(x + 0.5f * w, y + 0.28f * h)
                            cubicTo(
                                x + 0.15f * w, y + 0.00f * h,
                                x + 0.00f * w, y + 0.38f * h,
                                x + 0.50f * w, y + 0.88f * h,
                            )
                            cubicTo(
                                x + 1.00f * w, y + 0.38f * h,
                                x + 0.85f * w, y + 0.00f * h,
                                x + 0.50f * w, y + 0.28f * h,
                            )
                            close()
                        }
                    }
                }

                // Fill inside white
                drawPath(outline, color = Color.White, style = Fill)
                // Draw outline stroke black
                drawPath(outline, color = Color.Black, style = Stroke(width = 6f))

                // Clip to inside for drawing
                clipPath(outline) {
                    // reference version to trigger redraw after bitmap changes
                    // Draw filled regions bitmap first
                    displayBitmapState.value?.let { bmp ->
                        drawImage(bmp.asImageBitmap())
                    }

                    // Draw strokes on top for crisp edges and live preview
                    val strokeWidth = 12f
                    strokes.forEach { stroke ->
                        // draw as connected segments
                        for (i in 1 until stroke.points.size) {
                            drawLine(
                                color = stroke.color,
                                start = stroke.points[i - 1],
                                end = stroke.points[i],
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bottom controls: outline shape selection (outside SafeArea)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { outlineShape.value = OutlineShape.Circle },
                ) {
                    Text("Circle")
                }
                Button(
                    onClick = { outlineShape.value = OutlineShape.Diamond },
                ) {
                    Text("Diamond")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { outlineShape.value = OutlineShape.Square }) {
                    Text("Square")
                }
                Button(onClick = { outlineShape.value = OutlineShape.Heart }) {
                    Text("Heart")
                }
            }
        }
    }
}