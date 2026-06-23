package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class Absence(
    val id: Int,
    @SerializedName("eleve_id") val eleveId: Int,
    val date: String,
    val motif: String? = null,
    val justifiee: Boolean = false,
    @SerializedName("nombre_heures") val nombreHeures: Int? = null,
    val commentaire: String? = null
)
