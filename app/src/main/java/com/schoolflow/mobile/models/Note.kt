package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class Note(
    val id: Int,
    @SerializedName("eleve_id") val eleveId: Int,
    @SerializedName("matiere_id") val matiereId: Int,
    val valeur: Double,
    val trimestre: Int,
    val type: String? = null,
    val date: String? = null,
    val commentaire: String? = null,
    val matiere: Matiere? = null
) {
    val trimestreLabel: String
        get() = when (trimestre) {
            1 -> "1er Trimestre"
            2 -> "2ème Trimestre"
            3 -> "3ème Trimestre"
            else -> "Trimestre $trimestre"
        }
}
