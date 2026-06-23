package com.schoolflow.mobile.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Double.formatMoney(): String {
    val format = NumberFormat.getNumberInstance(Locale.FRANCE)
    return "${format.format(this)} FCFA"
}

fun Double.formatNote(): String {
    return if (this == this.toLong().toDouble()) {
        "${this.toLong()}/20"
    } else {
        "${"%.2f".format(this)}/20"
    }
}

fun Double.formatMoyenne(): String {
    return "%.2f".format(this)
}

fun String.formatDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}

fun String.formatDateTime(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy à HH:mm", Locale.FRANCE)
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}
