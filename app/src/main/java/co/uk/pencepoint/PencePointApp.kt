package co.uk.pencepoint

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Application class for the PencePoint app.
 *
 * Configures global components and development-time strictness rules.
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation for dependency injection.
 */
@HiltAndroidApp
class PencePointApp : Application() {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
        super.onCreate()
    }

    /**
     * Configures [StrictMode] for debug builds to catch disk/network leaks
     * and other potential performance regressions during development.
     */
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}
