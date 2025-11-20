package com.shalom.ethiopiancalendar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shalom.calendar.di.androidPlatformModule
import com.shalom.calendar.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize Koin if not already started
        initKoin()

        setContent {
            // Provide Koin context to composables
            KoinContext {
                App()
            }
        }
    }

    private fun initKoin() {
        if (GlobalContext.getOrNull() == null) {
            Log.d("MainActivity", "Starting Koin from MainActivity")
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(applicationContext)
                modules(appModules + androidPlatformModule(applicationContext))
            }
        } else {
            Log.d("MainActivity", "Koin already started")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}