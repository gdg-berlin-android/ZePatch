package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

private val font = FontFamily(
    Font(R.font.belwe_bold_reduced)
)

@OptIn(ExperimentalMaterial3Api::class)
@Patch("Postavaru")
@Composable
fun Postavaru(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        // annotated string because we want some kerning
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    letterSpacing = (-1).sp
                )
            ) {
                append("postă")
            }

            withStyle(
                style = SpanStyle(
                    letterSpacing = (-5).sp
                )
            ) {
                append("va")
            }

            withStyle(
                style = SpanStyle(
                    letterSpacing = (-1).sp
                )
            ) {
                append("ru")
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.mountain_silhouette),
                contentDescription = null,
            )
            BasicText(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(),
                text = annotatedString, style = TextStyle(
                color = Color.White,
                fontSize = 64.sp,
                fontFamily = font,)
            )
        }
    }

}

@Preview
@Composable
fun PreviewPostavaru() {
    Postavaru()
}