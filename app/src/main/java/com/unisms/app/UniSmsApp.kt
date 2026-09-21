package com.unisms.app

import android.app.Application
import com.unisms.app.data.local.SecurePreferencesManager
import com.unisms.app.data.local.db.AppDatabase
import com.unisms.app.data.repository.SmsBowerRepository
import com.unisms.app.ui.util.HapticHelper
import com.unisms.app.ui.util.NotificationHelper

class UniSmsApp : Application() {

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

    companion object {
        lateinit var instance: UniSmsApp
            private set
    }
}
