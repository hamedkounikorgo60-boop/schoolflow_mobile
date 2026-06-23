package com.schoolflow.mobile.models

data class Matiere(
    val id: Int,
    val nom: String,
    val coefficient: Int,
    val enseignant: String? = null,
    val moyenne: Double? = null,
    val notes: List<Note>? = null
)
