package ru.kpfu.itis.nikolaev.delivery.utils

import android.content.ContentProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

class Image {
    companion object{
        private var contextRef: WeakReference<Context>? = null

        fun setContext(context: Context) {
            contextRef = WeakReference(context)
        }

        fun createBitmapFromVector(art: Int): Bitmap? {
            val context = contextRef?.get() ?: return null
            val drawable = ContextCompat.getDrawable(context, art) ?: return null
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
}