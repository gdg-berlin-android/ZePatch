package de.berlindroid.zepatch.patchable

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.berlindroid.zepatch.annotations.Patch
import de.berlindroid.zepatch.ui.SafeArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET

data class Mon(
    val name: String,
    val url: String,
)

interface PokeApi {
    @GET("pokemon?limit=100000&offset=0")
    suspend fun mons(): Response<String>
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Patch("PokePatch")
@Preview
@Composable
fun PokePatch(
    shouldCapture: Boolean = false,
    onBitmap: (ImageBitmap) -> Unit = {},
) {
    var mons by remember { mutableStateOf(listOf<Mon>()) }
    var mon by remember { mutableStateOf<Mon?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mons) {
        scope.launch(Dispatchers.IO) {
            try {
                val body = Retrofit
                    .Builder()
                    .baseUrl(
                        "https://pokeapi.co/api/v2/"
                    ).addConverterFactory(
                        ScalarsConverterFactory
                            .create()
                    )
                    .build()
                    .create<PokeApi>()
                    .mons()
                    .body()!!

                mons = JSONObject(body).getJSONArray("results").toMons()
            } catch (th: Throwable) {
                Log.e("ERR", th.message, th)
            }
        }
    }

    Column {
        SafeArea(
            shouldCapture = shouldCapture,
            onBitmap = onBitmap,
        ) {
            if (mon != null) {
                AsyncImage(
                    modifier = Modifier.size(100.dp, 100.dp)
                        .background(Color.Red),
                    model = mon!!.url,
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                )
                Text(text = mon!!.name)
            } else {
                Text(
                    modifier = Modifier.rotate(45f),
                    text = mon?.name ?: "Comming sooon."
                )
            }
        }

        LazyRow {
            items(mons) {
                Button(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        mon = it
                    }) {
                    Text(text = it.name)
                }
            }
        }
    }
}

fun JSONArray.toMons(): List<Mon> = List(length()) { i ->
    when (val mon = getJSONObject(i)) {
        null -> null
        else -> Mon(
            name = mon.getString("name"),
//            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/132.png"
            url = "https://berlindroid.de/resources/images/android.png"
        )
    }
}.filterNotNull()