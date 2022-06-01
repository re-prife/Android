package com.mirim.refrigerator.extension.usecase

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class Settings(private val context: Context) {

    companion object {
        const val THEME_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        const val THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
        const val THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES

        private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
        private var INSTANCE: Settings? = null

        fun getInstance(context: Context): Settings {
            return INSTANCE ?: Settings(context.applicationContext).apply { INSTANCE = this }
        }
    }

    private enum class Key {
        THEME,
        INVERSE_BARCODE_COLORS,
        OPEN_LINKS_AUTOMATICALLY,
        COPY_TO_CLIPBOARD,
        SIMPLE_AUTO_FOCUS,
        FLASHLIGHT,
        VIBRATE,
        CONTINUOUS_SCANNING,
        CONFIRM_SCANS_MANUALLY,
        IS_BACK_CAMERA,
        SAVE_SCANNED_BARCODES_TO_HISTORY,
        SAVE_CREATED_BARCODES_TO_HISTORY,
        DO_NOT_SAVE_DUPLICATES,
        SEARCH_ENGINE,
        ERROR_REPORTS,
    }

    private fun <T> unsafeLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

    private val sharedPreferences by unsafeLazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private fun get(key: Key, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key.name, default)
    }

    private fun set(key: Key, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key.name, value)
            .apply()
    }

    var isBackCamera: Boolean
        get() = get(Key.IS_BACK_CAMERA, true)
        set(value) = set(Key.IS_BACK_CAMERA, value)
}