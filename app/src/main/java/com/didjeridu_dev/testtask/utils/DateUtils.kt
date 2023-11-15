package com.didjeridu_dev.testtask.utils

import java.text.SimpleDateFormat
import java.util.Locale

class DateUtils {
    companion object {
        /**
        Форматирование даты
         */
        fun formatDate(inputDate: String): String {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormatter = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
            return try {
                val date = inputFormatter.parse(inputDate)
                date?.let { outputFormatter.format(it) } ?: inputDate
            } catch (e: Exception) {
                e.printStackTrace()
                inputDate
            }
        }
    }
}