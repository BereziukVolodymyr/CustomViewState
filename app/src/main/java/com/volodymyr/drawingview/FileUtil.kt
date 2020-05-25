package com.volodymyr.drawingview

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.*

suspend fun storeImage(context: Context, imageData: Bitmap, filename: String): Boolean {
    var sdIconStorageDir = File(
        context.getExternalFilesDir(null)
            .toString()
    )
    // create storage directories, if they don't exist
    if (!sdIconStorageDir.exists()) {
        sdIconStorageDir.mkdirs()
    }
    try {
        val filePath: String =
            sdIconStorageDir.toString() + File.separator.toString() + filename
        val fileOutputStream = FileOutputStream(filePath)
        val bos = BufferedOutputStream(fileOutputStream)
        imageData.compress(Bitmap.CompressFormat.PNG, 100, bos)
        bos.flush()
        bos.close()
    } catch (e: FileNotFoundException) {
        Log.w("TAG", "Error saving image file: " + e.message)
        return false
    } catch (e: IOException) {
        Log.w("TAG", "Error saving image file: " + e.message)
        return false
    }
    return true
}