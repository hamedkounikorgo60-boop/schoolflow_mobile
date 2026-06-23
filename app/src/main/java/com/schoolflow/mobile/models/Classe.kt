package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class Classe(
    val id: Int,
    val nom: String,
    val niveau: String? = null,
    @SerializedName("annee_scolaire") val anneeScolaire: String? = null,
    val scolarite: Double? = null,
    @SerializedName("effectif") val effectif: Int? = null
)
