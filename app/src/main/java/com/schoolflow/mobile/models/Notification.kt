package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class Notification(
    val id: Int,
    val titre: String,
    val message: String,
    val type: String,
    val date: String,
    val lue: Boolean = false,
    @SerializedName("date_echeance") val dateEcheance: String? = null
) {
    val typeLabel: String
        get() = when (type) {
            "examen" -> "Examen"
            "reunion" -> "Réunion"
            "paiement" -> "Échéance de paiement"
            "annonce" -> "Annonce"
            else -> "Information"
        }

    val typeIcon: String
        get() = when (type) {
            "examen" -> "school"
            "reunion" -> "groups"
            "paiement" -> "payment"
            "annonce" -> "campaign"
            else -> "info"
        }
}
