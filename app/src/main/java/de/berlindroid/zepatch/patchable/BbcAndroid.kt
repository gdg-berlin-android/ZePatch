package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

@Preview
@Patch("BbcAndroid")
@Composable
fun BbcAndroid(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        val blocksPainter = rememberVectorPainter(
            300.dp,
            300.dp,
            autoMirror = true,
            viewportWidth = 108f,
            viewportHeight = 108f,
        ) { viewportWidth, viewportHeight ->
            Path(
                PathParser().parsePathString(
                    "M68.17,37.19C67.94,37.32 67.68,37.42 67.39,37.49C67.09,37.56 66.78,37.6 66.45,37.6C66.02,37.6 65.63,37.53 65.28,37.41C64.93,37.28 64.64,37.1 64.4,36.86C64.16,36.62 63.97,36.33 63.85,35.99C63.72,35.64 63.65,35.25 63.65,34.82C63.65,34.4 63.72,34.02 63.85,33.68C63.99,33.34 64.18,33.05 64.43,32.8C64.68,32.56 64.98,32.38 65.33,32.25C65.68,32.12 66.07,32.05 66.5,32.05C66.81,32.05 67.09,32.08 67.36,32.15C67.63,32.21 67.88,32.3 68.11,32.42V33.47C67.9,33.34 67.67,33.23 67.42,33.16C67.18,33.09 66.92,33.05 66.65,33.05C66.28,33.05 65.96,33.12 65.7,33.26C65.43,33.4 65.23,33.6 65.09,33.86C64.95,34.12 64.88,34.44 64.88,34.82C64.88,35.2 64.95,35.52 65.08,35.78C65.22,36.05 65.42,36.25 65.67,36.39C65.93,36.53 66.24,36.6 66.61,36.6C67.17,36.6 67.69,36.45 68.17,36.16V37.19ZM61.24,39.65H70.89V30H61.24V39.65Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color.White),
            )
            Path(
                PathParser().parsePathString(
                    "M54.01,35.19H53.04V36.66H54C54.33,36.66 54.58,36.6 54.76,36.47C54.93,36.35 55.02,36.17 55.02,35.94C55.02,35.44 54.69,35.19 54.01,35.19ZM54.52,34.16C54.67,34.03 54.75,33.86 54.75,33.64C54.75,33.21 54.46,32.99 53.87,32.99H53.04V34.34H53.87C54.16,34.34 54.37,34.28 54.52,34.16ZM55.9,36.81C55.74,37.04 55.5,37.22 55.2,37.35C54.89,37.48 54.53,37.54 54.1,37.54H51.94V32.11H53.97C54.57,32.11 55.04,32.23 55.37,32.47C55.7,32.71 55.86,33.06 55.86,33.51C55.86,33.77 55.8,33.99 55.69,34.18C55.57,34.37 55.39,34.52 55.16,34.63C55.48,34.74 55.73,34.91 55.9,35.14C56.07,35.37 56.15,35.65 56.15,35.97C56.15,36.3 56.07,36.58 55.9,36.81ZM49.18,39.65H58.82V30H49.18V39.65Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color.White),
            )
            Path(
                PathParser().parsePathString(
                    "M41.95,35.19H40.98V36.66H41.93C42.26,36.66 42.52,36.6 42.69,36.47C42.87,36.35 42.96,36.17 42.96,35.94C42.96,35.44 42.62,35.19 41.95,35.19ZM42.46,34.16C42.61,34.03 42.69,33.86 42.69,33.64C42.69,33.21 42.39,32.99 41.81,32.99H40.98V34.34H41.81C42.09,34.34 42.31,34.28 42.46,34.16ZM43.84,36.81C43.68,37.04 43.44,37.22 43.14,37.35C42.83,37.48 42.47,37.54 42.04,37.54H39.88V32.11H41.91C42.51,32.11 42.98,32.23 43.31,32.47C43.64,32.71 43.8,33.06 43.8,33.51C43.8,33.77 43.74,33.99 43.62,34.18C43.51,34.37 43.33,34.52 43.1,34.63C43.42,34.74 43.67,34.91 43.84,35.14C44.01,35.37 44.09,35.65 44.09,35.97C44.09,36.3 44.01,36.58 43.84,36.81ZM37.11,39.65H46.76V30H37.11V39.65Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color.White),
            )
        }
        val soundsPainter = rememberVectorPainter(
            300.dp,
            300.dp,
            autoMirror = true,
            viewportWidth = 108f,
            viewportHeight = 108f,
        ) { viewportWidth, viewportHeight ->
            Path(
                PathParser().parsePathString(
                    "M70.69,78H54.2C54.09,78 54,77.91 54,77.8V44.43C54,44.32 54.09,44.23 54.2,44.23H70.69C70.8,44.23 70.89,44.32 70.89,44.43V77.8C70.89,77.91 70.8,78 70.69,78Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color(0xFFFA6400)),
            )
            Path(
                PathParser().parsePathString(
                    "M51.39,73.18H43.35C43.23,73.18 43.14,73.09 43.14,72.97V49.25C43.14,49.14 43.23,49.05 43.35,49.05H51.39C51.5,49.05 51.59,49.14 51.59,49.25V72.97C51.59,73.09 51.5,73.18 51.39,73.18Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color(0xFFD24712)),
            )
            Path(
                PathParser().parsePathString(
                    "M40.53,65.94H36.11C36,65.94 35.91,65.85 35.91,65.74V56.49C35.91,56.38 36,56.29 36.11,56.29H40.53C40.64,56.29 40.73,56.38 40.73,56.49V65.74C40.73,65.85 40.64,65.94 40.53,65.94Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color(0xFFA13104)),
            )
        }
        val iPlayerPainter = rememberVectorPainter(
            300.dp,
            300.dp,
            autoMirror = true,
            viewportWidth = 108f,
            viewportHeight = 108f,
        ) { viewportWidth, viewportHeight ->
            Path(
                PathParser().parsePathString(
                    "M65.67,61.89L69.09,67.81C69.14,67.91 69.11,68.03 69.01,68.09L51.6,78.14C51.51,78.19 51.39,78.16 51.33,78.07L47.91,72.15C47.86,72.05 47.89,71.93 47.99,71.87L65.4,61.82C65.49,61.76 65.61,61.8 65.67,61.89Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color(0xFFAF0D5B)),
            )
            Path(
                PathParser().parsePathString(
                    "M65.67,60.33L69.09,54.41C69.14,54.32 69.11,54.19 69.01,54.14L51.6,44.09C51.51,44.03 51.39,44.07 51.33,44.16L47.91,50.08C47.86,50.18 47.89,50.3 47.99,50.36L65.4,60.41C65.49,60.46 65.61,60.43 65.67,60.33Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color(0xFFFF4C98)),
            )
            Path(
                PathParser().parsePathString(
                    "M46.56,71.37H39.73C39.62,71.37 39.53,71.28 39.53,71.16V51.06C39.53,50.95 39.62,50.86 39.73,50.86H46.56C46.67,50.86 46.76,50.95 46.76,51.06V71.16C46.76,71.28 46.67,71.37 46.56,71.37Z",
                ).toNodes(),
                PathFillType.EvenOdd,
                fill = SolidColor(Color(0xFFDC2878)),
            )
        }
        Box(modifier = Modifier.border(4.dp, Color.White)) {
            Image(
                painter = blocksPainter,
                contentDescription = null,
                modifier = Modifier.offset { IntOffset(0, 0) }.scale(1.7f))
            Image(
                painter = iPlayerPainter,
                contentDescription = null,
                modifier = Modifier.offset { IntOffset(-60.dp.roundToPx(), 0) })
            Image(
                painter = soundsPainter,
                contentDescription = null,
                modifier = Modifier.offset { IntOffset(+60.dp.roundToPx(), 0) })
            Text(
                text = "#dcbln25", style = TextStyle(
                    color = Color.White,
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                ), modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
            )
        }
    }
}
