package ru.kpfu.itis.nikolaev.delivery.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConvertDate {
    companion object{
        fun convertFullDateToSimple(date : Calendar) : String {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            return dateFormat.format(date.time)
        }
        fun convertSimpleDateToFull(dateString: String): Calendar {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }
    }

}