package com.example

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.IOException

/**
 * A highly polished, production-grade utility class utilizing the Android [WallpaperManager]
 * API to handle setting wallpapers for the System Home Screen, the System Lock Screen, or Both.
 */
object WallpaperHelper {
    private const val TAG = "WallpaperHelper"

    /**
     * Enum representation of target wallpaper destinations.
     */
    enum class Destination(val flag: Int) {
        HOME(WallpaperManager.FLAG_SYSTEM),
        LOCK(WallpaperManager.FLAG_LOCK),
        BOTH(WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
    }

    /**
     * Checks if setting wallpapers is supported on the current device.
     */
    fun isWallpaperSupported(context: Context): Boolean {
        val wm = WallpaperManager.getInstance(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            wm.isWallpaperSupported
        } else {
            true
        }
    }

    /**
     * Checks if changing the wallpaper is allowed on the device (e.g. not blocked by MDM policies).
     */
    fun isSetWallpaperAllowed(context: Context): Boolean {
        val wm = WallpaperManager.getInstance(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wm.isSetWallpaperAllowed
        } else {
            true
        }
    }

    /**
     * Safely applies a [Bitmap] as the device's launcher background.
     * Operates safely on Room/Background dispatcher threads.
     *
     * @param context App context.
     * @param bitmap The bitmap image to be set.
     * @param destination Target screen destination (HOME, LOCK, or BOTH).
     * @return true if successful, false otherwise.
     */
    suspend fun setWallpaper(
        context: Context,
        bitmap: Bitmap,
        destination: Destination
    ): Boolean = withContext(Dispatchers.IO) {
        if (!isWallpaperSupported(context)) {
            Log.e(TAG, "Setting wallpaper is not supported on this device.")
            return@withContext false
        }
        if (!isSetWallpaperAllowed(context)) {
            Log.e(TAG, "Changing wallpaper is not allowed on this device (it may be restricted by Admin policy).")
            return@withContext false
        }

        try {
            val wm = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // For N and above, we can set specific flags (FLAG_SYSTEM, FLAG_LOCK, or BOTH)
                wm.setBitmap(bitmap, null, true, destination.flag)
            } else {
                // Pre-N only supports setting the system wallpaper directly
                wm.setBitmap(bitmap)
            }
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied while setting wallpaper. Ensure SET_WALLPAPER permission is declared in Manifest.", e)
            false
        } catch (e: IOException) {
            Log.e(TAG, "Failed to write wallpaper bitmap.", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error occurred while setting wallpaper from Bitmap.", e)
            false
        }
    }

    /**
     * Safely reads a high-definition image stream from an Android [Uri] using ContentResolver,
     * and sets it as the device's launcher background.
     *
     * @param context App context.
     * @param uri Uri of the target file or media resource.
     * @param destination Target screen destination (HOME, LOCK, or BOTH).
     * @return true if successful, false otherwise.
     */
    suspend fun setWallpaperFromUri(
        context: Context,
        uri: Uri,
        destination: Destination
    ): Boolean = withContext(Dispatchers.IO) {
        if (!isWallpaperSupported(context)) {
            Log.e(TAG, "Setting wallpaper is not supported on this device.")
            return@withContext false
        }
        if (!isSetWallpaperAllowed(context)) {
            Log.e(TAG, "Changing wallpaper is not allowed on this device (it may be restricted by Admin policy).")
            return@withContext false
        }

        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e(TAG, "Failed to resolve uri content stream for input: $uri")
                return@withContext false
            }

            val wm = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wm.setStream(inputStream, null, true, destination.flag)
            } else {
                // Fallback for older devices.
                // Pre-N only supports setting system wallpaper, and handles InputStream directly via setStream.
                wm.setStream(inputStream)
            }
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied while setting wallpaper from Uri.", e)
            false
        } catch (e: IOException) {
            Log.e(TAG, "I/O issue encountered while processing Uri streaming.", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error occurred while setting wallpaper from Uri.", e)
            false
        } finally {
            try {
                inputStream?.close()
            } catch (ignored: Exception) {}
        }
    }

    /**
     * Safely sets the wallpaper directly from a generic [InputStream].
     * Closes the stream upon execution.
     *
     * @param context App context.
     * @param stream The raw input stream to set.
     * @param destination Target screen destination (HOME, LOCK, or BOTH).
     * @return true if successful, false otherwise.
     */
    suspend fun setWallpaperFromStream(
        context: Context,
        stream: InputStream,
        destination: Destination
    ): Boolean = withContext(Dispatchers.IO) {
        if (!isWallpaperSupported(context)) {
            Log.e(TAG, "Setting wallpaper is not supported on this device.")
            try { stream.close() } catch (ignored: Exception) {}
            return@withContext false
        }

        try {
            val wm = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wm.setStream(stream, null, true, destination.flag)
            } else {
                wm.setStream(stream)
            }
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied while setting wallpaper from stream.", e)
            false
        } catch (e: IOException) {
            Log.e(TAG, "Failed to process wallpaper input stream.", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while setting wallpaper from stream.", e)
            false
        } finally {
            try {
                stream.close()
            } catch (ignored: Exception) {}
        }
    }
}
