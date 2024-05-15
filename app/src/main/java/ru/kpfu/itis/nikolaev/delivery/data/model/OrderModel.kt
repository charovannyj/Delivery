package ru.kpfu.itis.nikolaev.delivery.data.model

import java.io.Serial
import java.util.Calendar
import java.util.Date

data class OrderModel(
    //val id: Serial,
    val addressFrom: String,
    val addressTo: String,
    val price: Int,
    val dimensions: String,
    val uidSender: String,
    val uidRecipient: String,
    val date: Calendar
)