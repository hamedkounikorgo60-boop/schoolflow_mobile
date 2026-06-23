package com.schoolflow.mobile.api

import com.schoolflow.mobile.models.*
import retrofit2.Response
import retrofit2.http.*

interface SchoolFlowApi {

    // Authentication
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @POST("change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ApiResponse<Unit>>

    // Student dashboard
    @GET("eleves/{id}")
    suspend fun getEleve(@Path("id") eleveId: Int): Response<ApiResponse<Eleve>>

    @GET("eleves/{id}/dashboard")
    suspend fun getDashboard(@Path("id") eleveId: Int): Response<ApiResponse<Eleve>>

    // Grades
    @GET("eleves/{id}/matieres")
    suspend fun getMatieres(@Path("id") eleveId: Int): Response<ApiResponse<List<Matiere>>>

    @GET("eleves/{id}/notes")
    suspend fun getNotes(
        @Path("id") eleveId: Int,
        @Query("trimestre") trimestre: Int? = null,
        @Query("matiere_id") matiereId: Int? = null
    ): Response<ApiResponse<List<Note>>>

    @GET("eleves/{id}/moyennes")
    suspend fun getMoyennesTrimestrielles(
        @Path("id") eleveId: Int
    ): Response<ApiResponse<List<MoyenneTrimestrielle>>>

    // Payments
    @GET("eleves/{id}/paiements")
    suspend fun getPaiements(@Path("id") eleveId: Int): Response<ApiResponse<SuiviPaiement>>

    @GET("paiements/{id}/recu")
    suspend fun getRecuPaiement(@Path("id") paiementId: Int): Response<ApiResponse<String>>

    // Absences
    @GET("eleves/{id}/absences")
    suspend fun getAbsences(@Path("id") eleveId: Int): Response<ApiResponse<List<Absence>>>

    // Notifications
    @GET("notifications")
    suspend fun getNotifications(): Response<ApiResponse<List<Notification>>>

    @PUT("notifications/{id}/lue")
    suspend fun marquerCommeLue(@Path("id") notificationId: Int): Response<ApiResponse<Unit>>
}
