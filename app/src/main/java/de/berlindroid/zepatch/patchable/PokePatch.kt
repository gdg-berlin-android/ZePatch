package de.berlindroid.zepatch.patchable

import android.util.Log
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
    @GET("pokemon?limit=1400&offset=0")
    suspend fun mons(): Response<String>
}

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
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val api = Retrofit
                    .Builder()
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                    .create<PokeApi>()

                val response = api.mons()
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    mons = JSONObject(body).getJSONArray("results").toMons()
                    Log.d("PokePatch", "Successfully loaded ${mons.size} Pokemon")
                } else {
                    error = "API response error: ${response.code()}"
                    Log.e("PokePatch", "API response error: ${response.code()}")
                }
            } catch (th: Throwable) {
                Log.e("PokePatch", "Error loading Pokemon data: ${th.message}", th)
                error = "Failed to load Pokemon: ${th.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column {
        SafeArea(
            shouldCapture = shouldCapture,
            onBitmap = onBitmap,
        ) {
            when {
                isLoading -> {
                    Text(
                        modifier = Modifier.rotate(45f),
                        text = "Loading Pokemon..."
                    )
                }

                error != null -> {
                    Text(
                        modifier = Modifier.rotate(45f),
                        text = error!!,
                        color = Color.Red
                    )
                }

                mon != null -> {
                    AsyncImage(
                        modifier = Modifier
                            .size(150.dp, 150.dp)
                            .background(Color.White),
                        model = mon!!.url,
                        contentScale = ContentScale.Fit,
                        contentDescription = mon!!.name,
                        onError = { error ->
                            Log.e(
                                "PokePatch",
                                "Failed to load image for ${mon!!.name}: ${mon!!.url}, error: $error"
                            )
                        }
                    )
                    Text(
                        text = mon!!.name.replaceFirstChar { it.uppercaseChar() },
                        color = Color.Black
                    )
                }

                else -> {
                    Text(
                        modifier = Modifier.rotate(45f),
                        text = "Click a Pokemon to see it!"
                    )
                }
            }
        }

        if (mons.isNotEmpty()) {
            LazyRow {
                items(mons) { pokemon ->
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            mon = pokemon
                            Log.d(
                                "PokePatch",
                                "Selected Pokemon: ${pokemon.name}, URL: ${pokemon.url}"
                            )
                        }
                    ) {
                        Text(text = pokemon.name.replaceFirstChar { it.uppercaseChar() })
                    }
                }
            }
        }
    }
}

fun JSONArray.toMons(): List<Mon> = List(length()) { i ->
    try {
        val monData = getJSONObject(i)
        val name = monData.getString("name")
        val pokemonUrl = monData.getString("url")

        // Extract Pokemon ID from URL more safely
        // URL format: https://pokeapi.co/api/v2/pokemon/1/
        val urlParts = pokemonUrl.trimEnd('/').split("/")
        val pokemonId = urlParts.lastOrNull() ?: return@List null

        // Validate that pokemonId is actually a number
        if (pokemonId.toIntOrNull() == null) {
            Log.w("PokePatch", "Invalid Pokemon ID: $pokemonId from URL: $pokemonUrl")
            return@List null
        }

        // Use the correct PokeAPI sprites URL format
        val spriteUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"

        Mon(
            name = name,
            url = spriteUrl
        )
    } catch (e: Exception) {
        Log.e("PokePatch", "Error parsing Pokemon data at index $i: ${e.message}")
        null
    }
}.filterNotNull()