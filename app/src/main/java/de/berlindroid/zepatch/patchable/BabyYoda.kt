package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

private var imageVector: ImageVector? = null

@Patch(name = "BabyYoda")
@Composable
fun BabyYoda(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            imageVector = BabyYodaVector,
            contentDescription = "BabyYoda"
        )
    }
}

private val BabyYodaVector: ImageVector
    get() {
        if (imageVector != null) {
            return imageVector!!
        }
        imageVector = Builder(
            name = "BabyYoda", defaultWidth = 792.0.dp, defaultHeight =
                612.0.dp, viewportWidth = 792.0f, viewportHeight = 612.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFA65400)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(385.27f, 391.9f)
                curveToRelative(-5.98f, 41.15f, -4.47f, 97.26f, -8.72f, 143.04f)
                curveToRelative(-6.21f, 3.39f, -16.7f, 2.49f, -26.17f, 2.62f)
                curveToRelative(-15.51f, -40.29f, -9.49f, -111.08f, 6.98f, -145.65f)
                curveTo(364.84f, 390.56f, 377.83f, 390.39f, 385.27f, 391.9f)
                close()
                moveTo(346.9f, 391.02f)
                curveToRelative(-10.06f, -2.82f, -22.39f, 2.01f, -31.4f, -1.74f)
                curveToRelative(-10.06f, 19.65f, -10.61f, 60.61f, -42.74f, 49.72f)
                curveToRelative(-2.02f, 13.14f, -14.09f, 33.37f, -3.49f, 45.35f)
                curveToRelative(22.73f, 4.12f, 28.26f, -21.16f, 42.74f, -25.29f)
                curveToRelative(-4.83f, 12.47f, -12.83f, 20.21f, -25.29f, 27.91f)
                curveToRelative(-15.1f, 9.32f, -40.66f, 15.08f, -33.14f, 42.74f)
                curveToRelative(21.24f, 7.86f, 53.58f, 9.18f, 81.11f, 7.85f)
                curveToRelative(3.81f, -13.89f, -1.61f, -27.8f, -2.62f, -41.86f)
                curveTo(329.4f, 458.38f, 341.92f, 422.9f, 346.9f, 391.02f)
                close()
                moveTo(467.26f, 456.44f)
                curveToRelative(-4.0f, -30.19f, 22.28f, -58.94f, 10.47f, -86.35f)
                curveToRelative(-9.19f, 17.39f, -22.74f, 47.2f, -40.12f, 61.05f)
                curveToRelative(-7.49f, 5.97f, -28.09f, 21.13f, -32.27f, 4.36f)
                curveToRelative(40.77f, -15.63f, 62.25f, -50.55f, 77.63f, -91.58f)
                curveToRelative(-35.53f, 4.01f, -47.04f, 39.59f, -87.22f, 41.87f)
                curveToRelative(-10.73f, 42.76f, -8.37f, 98.62f, -11.34f, 149.14f)
                curveToRelative(31.75f, 7.91f, 77.07f, 3.96f, 105.53f, -4.36f)
                curveToRelative(-0.37f, -38.58f, 0.88f, -78.79f, -6.1f, -110.77f)
                curveTo(474.65f, 428.36f, 480.65f, 452.1f, 467.26f, 456.44f)
                close()
                moveTo(271.89f, 398.0f)
                curveToRelative(4.04f, -9.33f, 3.52f, -23.23f, 3.49f, -36.63f)
                curveToRelative(-8.95f, -8.2f, -22.24f, -12.07f, -34.01f, -17.44f)
                curveTo(220.6f, 363.63f, 254.84f, 391.36f, 271.89f, 398.0f)
                close()
                moveTo(284.1f, 363.11f)
                curveToRelative(-5.31f, 19.4f, -1.05f, 48.37f, -6.98f, 67.16f)
                curveToRelative(21.76f, -3.89f, 33.86f, -33.1f, 27.91f, -61.93f)
                curveTo(298.96f, 365.7f, 293.26f, 362.68f, 284.1f, 363.11f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFBD884E)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(335.56f, 370.09f)
                curveToRelative(7.98f, -19.38f, -11.44f, -44.49f, -5.23f, -59.31f)
                curveToRelative(4.36f, -10.43f, 40.18f, -11.16f, 57.56f, -13.96f)
                curveToRelative(46.15f, -7.41f, 119.83f, -0.81f, 149.15f, -21.8f)
                curveToRelative(5.69f, 1.21f, 8.7f, 9.33f, 7.85f, 14.83f)
                curveToRelative(-1.32f, 8.46f, -14.56f, 11.98f, -22.68f, 18.32f)
                curveToRelative(-3.63f, 2.83f, -7.38f, 9.02f, -10.47f, 11.34f)
                curveToRelative(-9.73f, 7.31f, -25.06f, 10.74f, -38.38f, 16.57f)
                curveToRelative(-36.22f, 15.85f, -74.13f, 42.85f, -121.23f, 42.74f)
                curveToRelative(-11.08f, -0.03f, -24.06f, -1.6f, -33.14f, -7.85f)
                curveTo(324.74f, 370.9f, 331.02f, 371.38f, 335.56f, 370.09f)
                close()
                moveTo(526.57f, 381.43f)
                curveToRelative(-2.94f, -12.17f, -7.53f, -30.88f, -6.1f, -46.23f)
                curveToRelative(0.45f, -4.84f, 4.06f, -10.25f, 0.87f, -13.95f)
                curveToRelative(-13.0f, 8.34f, -24.65f, 21.31f, -21.81f, 42.74f)
                curveToRelative(-0.69f, 3.09f, -6.53f, 1.03f, -6.11f, 5.23f)
                curveToRelative(3.78f, 12.82f, 3.42f, 25.77f, 10.47f, 34.02f)
                curveToRelative(18.66f, 21.82f, 61.2f, -10.6f, 54.95f, -38.38f)
                curveTo(543.88f, 359.98f, 539.64f, 378.83f, 526.57f, 381.43f)
                close()
                moveTo(264.91f, 425.91f)
                curveToRelative(21.82f, -14.55f, -8.45f, -29.98f, -24.42f, -33.14f)
                curveToRelative(-1.67f, -15.78f, -8.24f, -26.65f, -16.57f, -35.76f)
                curveTo(211.27f, 395.11f, 242.18f, 412.43f, 264.91f, 425.91f)
                close()
                moveTo(264.91f, 298.57f)
                curveToRelative(-14.96f, -1.95f, -21.0f, -15.98f, -33.14f, -20.93f)
                curveToRelative(-11.1f, 12.74f, 2.69f, 34.98f, 10.47f, 44.48f)
                curveToRelative(17.73f, 21.69f, 54.39f, 42.4f, 87.22f, 40.12f)
                curveToRelative(-2.87f, -21.1f, -13.89f, -54.6f, 4.36f, -67.16f)
                curveToRelative(-19.6f, 0.32f, -39.02f, 0.15f, -55.82f, 1.74f)
                curveTo(273.49f, 297.26f, 269.22f, 299.13f, 264.91f, 298.57f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(461.15f, 196.53f)
                curveToRelative(1.22f, 10.2f, -15.51f, 9.12f, -18.32f, 3.49f)
                curveTo(443.6f, 193.69f, 456.39f, 193.46f, 461.15f, 196.53f)
                close()
                moveTo(271.02f, 198.27f)
                curveToRelative(2.98f, 5.93f, 16.98f, 5.4f, 18.32f, -1.75f)
                curveTo(286.44f, 191.4f, 271.95f, 191.52f, 271.02f, 198.27f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF96D473)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(251.83f, 445.97f)
                curveToRelative(-5.78f, -3.38f, -8.66f, -11.62f, -4.36f, -18.32f)
                curveToRelative(6.47f, 1.38f, 11.98f, 3.71f, 15.7f, 7.85f)
                curveTo(262.38f, 441.99f, 257.31f, 444.19f, 251.83f, 445.97f)
                close()
                moveTo(224.79f, 405.85f)
                curveToRelative(-4.8f, 8.28f, -6.53f, 31.22f, 3.49f, 33.15f)
                curveToRelative(6.24f, 1.2f, 13.29f, -5.67f, 12.21f, -13.95f)
                curveTo(239.48f, 417.27f, 226.92f, 411.63f, 224.79f, 405.85f)
                close()
                moveTo(572.79f, 318.63f)
                curveToRelative(2.81f, -3.89f, 7.97f, -3.52f, 7.85f, -8.72f)
                curveToRelative(-0.38f, -17.18f, -41.9f, -1.08f, -40.99f, 10.47f)
                curveToRelative(2.78f, 3.33f, 8.19f, 4.01f, 8.72f, 9.6f)
                curveToRelative(-2.26f, 6.07f, -10.6f, 4.03f, -16.57f, 2.62f)
                curveToRelative(-5.1f, 8.7f, -3.0f, 29.42f, 3.49f, 34.89f)
                curveToRelative(14.34f, -10.09f, 28.36f, -20.48f, 50.59f, -22.68f)
                curveToRelative(1.34f, -5.05f, 2.87f, -9.92f, 2.62f, -16.57f)
                curveTo(585.07f, 323.21f, 575.28f, 324.58f, 572.79f, 318.63f)
                close()
                moveTo(596.34f, 129.37f)
                curveToRelative(-21.14f, 0.93f, -46.24f, 4.28f, -67.16f, -0.87f)
                curveToRelative(-11.01f, -2.71f, -22.23f, -10.93f, -32.27f, -16.57f)
                curveToRelative(-32.33f, -18.18f, -72.41f, -40.98f, -125.59f, -36.63f)
                curveToRelative(-41.41f, 3.38f, -73.01f, 21.43f, -101.17f, 38.38f)
                curveToRelative(-10.03f, 6.03f, -20.6f, 14.51f, -31.4f, 15.7f)
                curveToRelative(-15.61f, 1.72f, -31.56f, -7.43f, -46.23f, -11.34f)
                curveToRelative(-33.29f, -8.86f, -63.94f, -12.84f, -97.68f, -18.32f)
                curveToRelative(-23.48f, -3.81f, -52.45f, -13.45f, -66.29f, 0.87f)
                curveToRelative(8.53f, 22.81f, 33.88f, 32.03f, 47.1f, 52.33f)
                curveToRelative(9.58f, 14.71f, 15.8f, 30.65f, 24.42f, 44.48f)
                curveToRelative(26.92f, 43.17f, 71.14f, 63.52f, 136.93f, 68.03f)
                curveToRelative(1.8f, -6.18f, -5.26f, -8.08f, -2.62f, -12.21f)
                curveToRelative(16.12f, 12.09f, 20.68f, 33.69f, 46.23f, 34.01f)
                curveToRelative(18.36f, 0.23f, 33.48f, -10.8f, 52.33f, -10.47f)
                curveToRelative(-2.82f, 5.32f, -12.13f, 4.15f, -14.83f, 9.59f)
                curveToRelative(42.62f, 2.2f, 86.91f, -1.46f, 127.34f, -4.36f)
                curveToRelative(0.68f, -4.46f, -6.18f, -1.38f, -6.1f, -5.23f)
                curveToRelative(45.96f, 2.03f, 86.15f, -2.44f, 101.17f, -32.27f)
                curveToRelative(1.14f, -2.27f, 3.02f, -7.34f, 3.49f, -10.47f)
                curveToRelative(2.2f, -14.57f, -4.88f, -26.95f, -3.49f, -37.5f)
                curveToRelative(0.63f, -4.74f, 5.8f, -12.78f, 8.72f, -16.57f)
                curveToRelative(24.74f, -32.11f, 91.22f, -31.73f, 141.29f, -31.4f)
                curveToRelative(-33.8f, 14.21f, -73.7f, 6.23f, -101.17f, 20.06f)
                curveToRelative(-13.0f, 6.55f, -28.12f, 20.19f, -34.02f, 34.89f)
                curveToRelative(-2.85f, 7.12f, -1.25f, 15.73f, -3.49f, 25.29f)
                curveToRelative(-2.82f, 12.05f, -11.47f, 19.27f, -13.08f, 34.01f)
                curveToRelative(51.99f, 1.67f, 101.94f, -9.71f, 131.7f, -34.89f)
                curveToRelative(7.91f, -6.69f, 13.73f, -16.51f, 20.93f, -24.42f)
                curveToRelative(7.46f, -8.19f, 15.12f, -15.99f, 22.68f, -23.55f)
                curveToRelative(15.57f, -15.57f, 33.53f, -31.12f, 39.25f, -52.33f)
                curveTo(699.35f, 125.78f, 646.0f, 127.19f, 596.34f, 129.37f)
                close()
                moveTo(227.41f, 237.52f)
                curveToRelative(-10.41f, -13.26f, -8.31f, -38.39f, -14.83f, -52.33f)
                curveToRelative(-21.32f, -45.62f, -91.85f, -42.02f, -129.96f, -71.52f)
                curveToRelative(14.25f, 1.22f, 39.08f, 9.78f, 62.8f, 17.44f)
                curveToRelative(22.25f, 7.2f, 45.73f, 12.18f, 58.44f, 20.06f)
                curveToRelative(10.59f, 6.57f, 26.04f, 22.42f, 27.91f, 34.89f)
                curveTo(234.34f, 203.26f, 223.52f, 218.28f, 227.41f, 237.52f)
                close()
                moveTo(431.5f, 130.24f)
                curveToRelative(1.41f, -0.25f, 1.5f, 0.82f, 2.62f, 0.87f)
                curveToRelative(-0.09f, 12.75f, -6.33f, 26.78f, -16.57f, 32.27f)
                curveTo(417.58f, 151.17f, 427.19f, 141.08f, 431.5f, 130.24f)
                close()
                moveTo(386.14f, 161.64f)
                curveToRelative(-3.73f, -9.65f, -4.56f, -22.19f, -6.1f, -34.02f)
                curveTo(388.59f, 132.37f, 391.46f, 151.02f, 386.14f, 161.64f)
                close()
                moveTo(407.95f, 227.93f)
                curveToRelative(-0.83f, 7.27f, -10.63f, 12.0f, -20.06f, 9.59f)
                curveToRelative(1.51f, -6.92f, 11.92f, -4.94f, 13.96f, -11.34f)
                curveToRelative(-2.5f, -11.84f, -32.98f, -12.39f, -32.27f, 0.0f)
                curveToRelative(0.41f, 7.17f, 10.49f, 4.68f, 12.21f, 12.21f)
                curveToRelative(-5.55f, 1.86f, -12.73f, -1.66f, -17.44f, -3.49f)
                curveTo(352.98f, 206.62f, 410.59f, 204.74f, 407.95f, 227.93f)
                close()
                moveTo(361.72f, 165.13f)
                curveToRelative(-15.98f, -9.03f, -25.38f, -24.62f, -39.25f, -35.76f)
                curveTo(342.3f, 131.06f, 356.73f, 145.69f, 361.72f, 165.13f)
                close()
                moveTo(238.74f, 197.4f)
                curveToRelative(4.97f, -33.88f, 87.84f, -52.02f, 101.17f, -10.47f)
                curveToRelative(-14.73f, -2.71f, -21.77f, -16.9f, -38.38f, -19.19f)
                curveTo(270.77f, 163.51f, 258.32f, 187.08f, 238.74f, 197.4f)
                close()
                moveTo(305.03f, 250.6f)
                curveToRelative(-18.44f, -0.19f, -41.56f, -10.1f, -49.72f, -19.19f)
                curveToRelative(-4.58f, -5.1f, -11.11f, -15.81f, -8.72f, -27.91f)
                curveToRelative(3.27f, -16.55f, 25.17f, -23.59f, 47.1f, -24.42f)
                curveToRelative(16.71f, -0.64f, 37.18f, 8.48f, 46.23f, 20.06f)
                curveTo(361.31f, 226.54f, 337.81f, 250.94f, 305.03f, 250.6f)
                close()
                moveTo(355.62f, 268.92f)
                curveToRelative(12.69f, -10.29f, 47.56f, -9.41f, 57.56f, 2.62f)
                curveTo(395.22f, 269.43f, 375.87f, 268.73f, 355.62f, 268.92f)
                close()
                moveTo(518.72f, 202.63f)
                curveToRelative(-1.16f, 7.18f, 1.57f, 11.82f, 0.0f, 17.44f)
                curveToRelative(-4.66f, 16.74f, -80.15f, 43.64f, -97.68f, 16.57f)
                curveToRelative(-10.31f, -15.93f, 4.3f, -37.86f, 13.08f, -45.35f)
                curveToRelative(9.71f, -8.29f, 27.77f, -17.84f, 48.84f, -14.83f)
                curveToRelative(12.04f, 1.72f, 15.15f, 8.49f, 27.91f, 12.21f)
                curveToRelative(-8.02f, -25.91f, -53.51f, -22.74f, -73.26f, -9.59f)
                curveToRelative(19.31f, -33.43f, 82.9f, -12.31f, 86.35f, 21.81f)
                curveTo(524.78f, 204.04f, 520.52f, 202.1f, 518.72f, 202.63f)
                close()
            }
        }
            .build()
        return imageVector!!
    }

@Preview()
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp).background(Black)) {
        BabyYoda()
    }
}
