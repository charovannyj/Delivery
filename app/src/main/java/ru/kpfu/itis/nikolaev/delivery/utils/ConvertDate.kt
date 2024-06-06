package ru.kpfu.itis.nikolaev.delivery.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ConvertDate {
    companion object{

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertFullDateToSimple(date : Calendar) : String {
            val moscowDateTime = date.toInstant()
                .atZone(ZoneId.of("Europe/Moscow"))
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            return moscowDateTime.format(formatter)
        }
        fun convertSimpleDateToFull(dateString: String): Calendar {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("ru", "RU"))
            dateFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"))
            calendar.time = date
            return calendar
        }
    }

}