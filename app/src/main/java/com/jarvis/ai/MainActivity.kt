package com.jarvis.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.jarvis.ai.ui.JarvisRoot
import com.jarvis.ai.ui.theme.JarvisTheme
import com.jarvis.ai.util.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Permissions handle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Saari permissions maangein
        if (!PermissionHelper.hasAllPermissions(this)) {
            requestPermissionLauncher.launch(PermissionHelper.getRequiredPermissions())
        }

        setContent {
            JarvisTheme {
                JarvisRoot()
            }
        }
    }
}
