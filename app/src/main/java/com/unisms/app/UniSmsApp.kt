package com.unisms.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.unisms.app.data.local.SecurePreferencesManager
import com.unisms.app.data.local.db.AppDatabase
import com.unisms.app.data.repository.SmsBowerRepository
import com.unisms.app.ui.util.HapticHelper
import com.unisms.app.ui.util.NotificationHelper

class UniSmsApp : Application(), ImageLoaderFactory {

    lateinit var securePreferencesManager: SecurePreferencesManager
        private set

    lateinit var appDatabase: AppDatabase
        private set

    lateinit var repository: SmsBowerRepository
        private set

    lateinit var notificationHelper: NotificationHelper
        private set

    lateinit var hapticHelper: HapticHelper
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        securePreferencesManager = SecurePreferencesManager(this)
        appDatabase = AppDatabase.getDatabase(this)
        repository = SmsBowerRepository(securePreferencesManager, appDatabase.activationRecordDao())
        notificationHelper = NotificationHelper(this)
        hapticHelper = HapticHelper(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024)
                    .build()
            }
            .crossfade(true)
            .build()
    }

    companion object {
        lateinit var instance: UniSmsApp
            private set
    }
}
