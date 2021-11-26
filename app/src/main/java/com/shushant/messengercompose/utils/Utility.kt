package com.shushant.messengercompose.utils

import android.os.Build
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.shushant.messengercompose.BuildConfig
import com.shushant.messengercompose.utils.Constants.application
import timber.log.Timber

object Utility {
    /**
     * Show a log message in the console
     */
    fun showMessage(message: String) {
        Timber.e("""$application$message""")
    }

    /**
     * Get current time stamp
     */
    fun currentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Get application version
     */
    fun applicationVersion(): String {
        return "${BuildConfig.VERSION_NAME}:${BuildConfig.VERSION_CODE}"
    }

    /**
     * Get device id
     */
    fun getDeviceId(): String {
        return Build.ID
    }

    /**
     * Get device model
     */
    fun deviceModel(): String {
        return "${Build.MODEL} ${Build.BRAND} ${Build.DEVICE}"
    }

    /**
     * Get phone OS
     */
    fun systemOS(): String {
        return "${Build.ID} ${Build.VERSION.SDK_INT} ${Build.VERSION.CODENAME}"
    }

    /**
     * Convert timestamp to a readable format in time ago
     */
    fun Long.getTimeAgo(): String {
        return TimeAgo.using(time = this)
    }
}
