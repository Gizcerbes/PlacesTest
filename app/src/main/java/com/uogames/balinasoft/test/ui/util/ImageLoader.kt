package com.uogames.balinasoft.test.ui.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object ImageLoader {


    fun load(
        activity: Activity,
        uri: Uri
    ): Bitmap {
        val stream = activity.contentResolver.openInputStream(uri) ?: throw FileNotFoundException()
        val bytes = stream.readBytes()

        val exif = ExifInterface(bytes.inputStream())
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        val bitmap = BitmapFactory.decodeStream(bytes.inputStream())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    fun Bitmap.changeSize(maxSize: Int): Bitmap {
        var width = width
        var height = height
        val ratio = width.toFloat() / height.toFloat()
        if (width > height) {
            width = maxSize
            height = (width / ratio).toInt()
        } else {
            height = maxSize
            width = (height * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(this, width, height, true)
    }

    fun Bitmap.compressToSize(closeSizeInBytes: Int): ByteArray {
        var minq = 0
        var maxq = 100
        var stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        if (closeSizeInBytes > stream.size()) return stream.toByteArray()
        while (maxq - minq > 1) {
            val q = (maxq + minq) / 2
            stream = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.JPEG, q, stream)
            if (stream.size() > closeSizeInBytes) maxq = q
            else if (stream.size() < closeSizeInBytes) minq = q
        }
        return stream.toByteArray()
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun ByteArray.toBase64(): String {
        return Base64.encode(this, 0, this.size)
    }


}