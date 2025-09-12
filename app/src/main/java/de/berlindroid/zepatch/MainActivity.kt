package de.berlindroid.zepatch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.embroidermodder.punching.Histogram
import de.berlindroid.zepatch.PatchablePreviewMode.BITMAP
import de.berlindroid.zepatch.PatchablePreviewMode.REDUCED_BITMAP
import de.berlindroid.zepatch.PatchablePreviewMode.STITCHES
import de.berlindroid.zepatch.ui.BitmapToStitches
import de.berlindroid.zepatch.ui.PatchableBoundingBox
import de.berlindroid.zepatch.ui.PatchableToBitmap
import de.berlindroid.zepatch.ui.PatchableToReducedBitmap
import de.berlindroid.zepatch.ui.theme.ZePatchTheme
import de.berlindroid.zepatch.utils.multiLet
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel as lifecycleViewModel

enum class PatchablePreviewMode {
    BITMAP, REDUCED_BITMAP, STITCHES
}

@ExperimentalMaterial3Api
@ExperimentalMaterial3AdaptiveApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZePatchTheme {
                val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
                val scope = rememberCoroutineScope()

                NavigableListDetailPaneScaffold(
                    navigator = scaffoldNavigator,
                    listPane = {
                        AnimatedPane {
                            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
                                rememberTopAppBarState()
                            )
                            Scaffold(
                                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                                contentWindowInsets = WindowInsets.statusBars,
                                topBar = {
                                    LargeTopAppBar(
                                        title = { Text("ZePatch") },
                                        scrollBehavior = scrollBehavior,
                                    )
                                },
                            ) { innerPadding ->
                                PatchableList(
                                    modifier = Modifier
                                        .padding(innerPadding)
                                        .padding(horizontal = 8.dp),
                                ) { name ->
                                    scope.launch {
                                        scaffoldNavigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            name,
                                        )
                                    }
                                }
                            }
                        }
                    },
                    detailPane = {
                        AnimatedPane {
                            scaffoldNavigator.currentDestination?.contentKey?.let { name ->
                                val patchable = patchables[name]
                                if (patchable == null) {
                                    Text("404: Patchable for $name not found")
                                } else {
                                    PatchableDetail(
                                        name = name as String,
                                        onBackClick = {
                                            scope.launch { scaffoldNavigator.navigateBack() }
                                        },
                                        patchable = patchable
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PatchableList(
    modifier: Modifier = Modifier,
    onItemClicked: (name: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = patchables.toList(),
            key = { it.first },
        ) { (name, patchable) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onItemClicked(name) },
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(name)
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "TODO",
                        )
                    }
                    Box(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        PatchableBoundingBox(patchable = patchable)
                    }
                }
            }
        }
        item(key = "navigationBar") {
            Box(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun PatchableDetail(
    modifier: Modifier = Modifier,
    name: String,
    onBackClick: () -> Unit,
    patchable: @Composable (Boolean, (ImageBitmap) -> Unit) -> Unit,
) {
    val context = LocalContext.current

    val viewModel: WizardViewModel = lifecycleViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Reset wizard state when a different patchable is opened
    LaunchedEffect(name) {
        viewModel.reset()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        savePesAfterSelection(context, result, uiState.embroideryData)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
                title = {
                    Text(name)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            ProgressHeader(
                imageBitmap = uiState.imageBitmap,
                reducedImageBitmap = uiState.reducedImageBitmap,
                currentMode = uiState.previewMode,
                embroideryData = uiState.embroideryData,
                onPrev = {
                    viewModel.setPreviewMode(
                        when (uiState.previewMode) {
                            BITMAP -> BITMAP
                            REDUCED_BITMAP -> BITMAP
                            STITCHES -> REDUCED_BITMAP
                        }
                    )
                },
                onNext = {
                    viewModel.setPreviewMode(
                        when (uiState.previewMode) {
                            BITMAP -> REDUCED_BITMAP
                            REDUCED_BITMAP -> STITCHES
                            STITCHES -> uiState.previewMode
                        }
                    )
                    if (uiState.previewMode == STITCHES) {
                        uiState.embroideryData?.let { data ->
                            val magic = String(data.toList().subList(0, 8).toByteArray())
                            val byteCount = data.size
                            val kbCount = byteCount / 1024
                            val mbCount = kbCount / 1024

                            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                type = "application/octet"
                                addCategory(Intent.CATEGORY_OPENABLE)
                                putExtra(Intent.EXTRA_TITLE, "$name.pes")
                            }

                            launcher.launch(intent)

                            Toast.makeText(
                                context,
                                "Found $byteCount bytes. ($kbCount KB, $mbCount MB)\nFile magic '$magic'.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )

            WizardContent(
                uiState.previewMode,
                uiState.imageBitmap,
                uiState.colorCount,
                uiState.reducedImageBitmap,
                uiState.reducedHistogram,
                name,
                onBitmapUpdated = viewModel::updateBitmap,
                onColorCountUpdated = viewModel::updateColorCount,
                computeReducedBitmap = viewModel::computeReducedBitmap,
                patchable = patchable
            )
        }
    }
}

@Composable
private fun WizardContent(
    currentMode: PatchablePreviewMode,
    imageBitmap: ImageBitmap?,
    colorCount: Int,
    reducedImageBitmap: ImageBitmap?,
    reducedHistogram: Histogram?,
    name: String,
    onBitmapUpdated: (ImageBitmap) -> Unit,
    onColorCountUpdated: (Int) -> Unit,
    computeReducedBitmap: () -> Unit,
    patchable: @Composable (Boolean, (ImageBitmap) -> Unit) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(size = 25.dp),
    ) {
        val scrollState = androidx.compose.foundation.rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            when (currentMode) {
                BITMAP -> PatchableToBitmap(
                    onBitmap = onBitmapUpdated,
                    patchable = patchable
                )

                REDUCED_BITMAP -> PatchableToReducedBitmap(
                    image = imageBitmap,
                    colorCount = colorCount,
                    onColorCountChanged = onColorCountUpdated,
                    computeReducedBitmap = computeReducedBitmap,
                    reducedImage = reducedImageBitmap,
                )

                STITCHES -> reducedImageBitmap?.multiLet(reducedHistogram) { img, histo ->
                    val vm: WizardViewModel = lifecycleViewModel()
                    val ui by vm.uiState.collectAsStateWithLifecycle()
                    BitmapToStitches(
                        reducedImageBitmap = img,
                        reducedHistogram = histo,
                        name = name,
                        onCreateEmbroidery = { n, b, h -> vm.createEmbroidery(n, b, h) },
                        previewImage = ui.embroideryPreviewImage,
                        creatingEmbroidery = ui.creatingEmbroidery,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressHeader(
    imageBitmap: ImageBitmap?,
    reducedImageBitmap: ImageBitmap?,
    embroideryData: ByteArray?,
    currentMode: PatchablePreviewMode,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onPrev, enabled = currentMode != BITMAP) { Text("Prev") }
        StepProgressIndicator(
            modifier = Modifier
                .height(48.dp)
                .weight(1f)
                .padding(horizontal = 8.dp),
            totalSteps = 3,
            currentStep = when (currentMode) {
                BITMAP -> 0
                REDUCED_BITMAP -> 1
                STITCHES -> 2
            }
        )
        Button(
            onClick = onNext, enabled = when (currentMode) {
                BITMAP -> imageBitmap != null
                REDUCED_BITMAP -> reducedImageBitmap != null
                STITCHES -> embroideryData != null
            }
        ) { if (currentMode == STITCHES) Text("Finish") else Text("Next") }
    }
}

@Composable
private fun StepProgressIndicator(
    modifier: Modifier = Modifier,
    totalSteps: Int,
    currentStep: Int,
) {
    Canvas(
        modifier = modifier
            .height(24.dp)
    ) {
        val radius = size.height / 4f
        val centerY = size.height / 2f
        val spacing = (size.width - (radius * 2f * totalSteps)) / (totalSteps - 1).coerceAtLeast(1)
        val strokeWidth = radius / 2f
        val lineColor = Color(0xFF9E9E9E)
        val fillColor = Color(0xFF9E9E9E)
        val outlineColor = Color(0xFF9E9E9E)

        // Compute centers for steps
        val centers = (0 until totalSteps).map { i ->
            val x = radius + i * (2f * radius + spacing)
            Offset(x, centerY)
        }

        // Draw connecting lines
        for (i in 0 until totalSteps - 1) {
            val start = centers[i]
            val end = centers[i + 1]
            drawLine(
                color = lineColor,
                start = Offset(start.x + radius, centerY),
                end = Offset(end.x - radius, centerY),
                strokeWidth = strokeWidth
            )
        }

        // Draw circles
        val displayCurrent = (totalSteps - 1 - currentStep).coerceIn(0, totalSteps - 1)
        centers.forEachIndexed { i, c ->
            val index = totalSteps - 1 - i
            if (index == displayCurrent) {
                // highlighted filled circle with thin outline
                drawCircle(color = fillColor, radius = radius, center = c)
                drawCircle(
                    color = outlineColor,
                    radius = radius,
                    center = c,
                    style = Stroke(width = strokeWidth / 2f)
                )
            } else {
                drawCircle(
                    color = outlineColor,
                    radius = radius,
                    center = c,
                    style = Stroke(width = strokeWidth / 1.5f)
                )
            }
        }
    }
}

private fun savePesAfterSelection(context: Context, result: ActivityResult, bytes: ByteArray?) {
    // TODO MOVE ME INTO VM

    if (bytes == null || bytes.isEmpty()) {
        Log.e("NO", "No PES created. Why are we here?")
        return
    }
    Log.i("YOLO?", "File for saving selected. $result")

    result.data?.let { data: Intent ->
        data.data?.let { uri: Uri ->
            val resolver = context.contentResolver.apply {
                takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }

            val outputStream = resolver.openOutputStream(uri)
            try {
                outputStream?.write(bytes)
            } finally {
                outputStream?.close()
            }

            Toast.makeText(
                context,
                "Done writing file! Happy embroidering 🪡",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
