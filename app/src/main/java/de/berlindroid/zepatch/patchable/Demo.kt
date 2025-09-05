package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea


@OptIn(ExperimentalMaterial3Api::class)
@Patch("AppLogo")
@Composable
fun AppLogo() {
    SafeArea {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.ai_logo),
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Patch("BerlindroidLogo")
@Composable
fun BerlindroidLogo() {
    SafeArea {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.voltron_nosign),
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Patch("GoogleLogo")
@Composable
fun GoogleLogo() {
    SafeArea {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.g),
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Patch("Android")
@Composable
fun AndroidLogo() {
    SafeArea {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.andriod),
            contentDescription = null
        )
    }
}


@Patch("AndyA")
@Composable
fun AndyA() {
    SafeArea {
        Column(
            modifier = Modifier
                .size(200.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Blue,
                            Color.Cyan
                        )
                    ),
                    shape = RoundedCornerShape(
                        topStartPercent = 50,
                        topEndPercent = 50,
                        bottomStartPercent = 50,
                        bottomEndPercent = 0
                    )
                ),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 70.sp,
                text = "Andy",
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Patch("AndyB")
@Composable
fun AndyB() {
    SafeArea {
        Column(
            modifier = Modifier
                .size(200.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 50,
                        bottomStartPercent = 50,
                        bottomEndPercent = 50
                    )
                )
                .border(
                    12.dp, Color.Black, RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 50,
                        bottomStartPercent = 50,
                        bottomEndPercent = 50
                    )
                ),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 64.sp,
                style = TextStyle.Default.copy(shadow = Shadow(blurRadius = 12f)),
                color = Color.Black,
                text = "Andy",
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Patch("flex")
@Composable
fun Emoji() {
    SafeArea {
        Text("💪", fontSize = 48.sp)
    }
}

@Patch("ROBOT")
@Composable
fun Emoji2() {
    SafeArea {
        Text("🦾🤖\nHereWeGo", fontSize = 34.sp)
    }
}

@Preview
@Composable
fun PreviewDemo() {
    AndyB()
}
