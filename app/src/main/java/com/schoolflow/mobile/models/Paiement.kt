package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class Paiement(
    val id: Int,
    @SerializedName("eleve_id") val eleveId: Int,
    val montant: Double,
    @SerializedName("date_paiement") val datePaiement: String,
    @SerializedName("mode_paiement") val modePaiement: String? = null,
    val reference: String? = null,
    @SerializedName("recu_url") val recuUrl: String? = null,
    val commentaire: String? = null
)

data class SuiviPaiement(
    @SerializedName("scolarite_totale") val scolariteTotale: Double,
    @SerializedName("total_paye") val totalPaye: Double,
    @SerializedName("reste_a_payer") val resteAPayer: Double,
    val paiements: List<Paiement>
)
