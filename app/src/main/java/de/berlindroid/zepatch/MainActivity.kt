@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package de.berlindroid.zepatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import de.berlindroid.zepatch.ui.PatchableDetail
import de.berlindroid.zepatch.ui.PatchableList
import de.berlindroid.zepatch.ui.theme.ZePatchTheme
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import android.util.Log

@ExperimentalMaterial3Api
@ExperimentalMaterial3AdaptiveApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "Unable to load OpenCV!")
        }

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
                                    Text("404: Patchable for $name not found.")
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
