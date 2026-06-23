package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class Eleve(
    val id: Int,
    val matricule: String,
    val nom: String,
    val prenom: String,
    val photo: String? = null,
    @SerializedName("date_naissance") val dateNaissance: String? = null,
    @SerializedName("lieu_naissance") val lieuNaissance: String? = null,
    val sexe: String? = null,
    val redoublant: Boolean = false,
    @SerializedName("classe_id") val classeId: Int,
    val classe: Classe? = null,
    @SerializedName("moyenne_generale") val moyenneGenerale: Double? = null,
    val rang: Int? = null,
    @SerializedName("total_eleves") val totalEleves: Int? = null,
    @SerializedName("dernieres_notes") val dernieresNotes: List<Note>? = null
) {
    val nomComplet: String get() = "$prenom $nom"
    val photoUrl: String? get() = photo
}
