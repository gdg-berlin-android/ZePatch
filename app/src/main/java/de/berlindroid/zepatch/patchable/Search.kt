package de.berlindroid.zepatch.patchable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.zepatch.R
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea

val borderColor = Color(0xFFFD6638) //  alternative Color(0xFF6750A4)
val backgroundColor = borderColor.copy(alpha = 0.2f)

@Preview
@Composable
fun PreviewSearch() {
    Search()
}

@Patch("Search")
@Composable
fun Search(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {

    SafeArea(
        shouldCapture = shouldCapture,
        onBitmap = onBitmap,
    ) {
        androidx.compose.material3.TextField(
            value = "",
            onValueChange = {},
            placeholder = {

                Text(
                    text = "                                          ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                errorContainerColor = backgroundColor,

                ),
            trailingIcon = {

                androidx.compose.material3.Icon(
                    contentDescription = "Speak",
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.mic_24dp_e3e3e3),
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .wrapContentWidth()
        )
    }
}