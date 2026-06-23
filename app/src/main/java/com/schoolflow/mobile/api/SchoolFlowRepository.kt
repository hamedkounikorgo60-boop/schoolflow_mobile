package com.schoolflow.mobile.api

import com.schoolflow.mobile.models.*
import com.schoolflow.mobile.utils.Resource
import retrofit2.Response

class SchoolFlowRepository {

    private val api = RetrofitClient.getApi()

    private suspend fun <T> safeApiCall(call: suspend () -> Response<ApiResponse<T>>): Resource<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Resource.Success(body.data!!)
                } else {
                    Resource.Error(body?.message ?: "Erreur inconnue")
                }
            } else {
                when (response.code()) {
                    401 -> Resource.Error("Session expirée. Veuillez vous reconnecter.")
                    403 -> Resource.Error("Accès refusé.")
                    404 -> Resource.Error("Ressource non trouvée.")
                    422 -> Resource.Error("Données invalides.")
                    500 -> Resource.Error("Erreur serveur. Veuillez réessayer plus tard.")
                    else -> Resource.Error("Erreur: ${response.code()}")
                }
            }
        } catch (e: java.net.ConnectException) {
            Resource.Error("Impossible de se connecter au serveur. Vérifiez votre connexion internet.")
        } catch (e: java.net.SocketTimeoutException) {
            Resource.Error("Le serveur met trop de temps à répondre.")
        } catch (e: Exception) {
            Resource.Error("Erreur: ${e.localizedMessage ?: "Erreur inconnue"}")
        }
    }

    suspend fun login(email: String, password: String): Resource<LoginResponse> {
        return safeApiCall { api.login(LoginRequest(email, password)) }
    }

    suspend fun logout(): Resource<Unit> {
        return safeApiCall { api.logout() }
    }

    suspend fun forgotPassword(email: String): Resource<Unit> {
        return safeApiCall { api.forgotPassword(ForgotPasswordRequest(email)) }
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmation: String
    ): Resource<Unit> {
        return safeApiCall {
            api.changePassword(ChangePasswordRequest(currentPassword, newPassword, confirmation))
        }
    }

    suspend fun getDashboard(eleveId: Int): Resource<Eleve> {
        return safeApiCall { api.getDashboard(eleveId) }
    }

    suspend fun getMatieres(eleveId: Int): Resource<List<Matiere>> {
        return safeApiCall { api.getMatieres(eleveId) }
    }

    suspend fun getNotes(eleveId: Int, trimestre: Int? = null, matiereId: Int? = null): Resource<List<Note>> {
        return safeApiCall { api.getNotes(eleveId, trimestre, matiereId) }
    }

    suspend fun getMoyennesTrimestrielles(eleveId: Int): Resource<List<MoyenneTrimestrielle>> {
        return safeApiCall { api.getMoyennesTrimestrielles(eleveId) }
    }

    suspend fun getPaiements(eleveId: Int): Resource<SuiviPaiement> {
        return safeApiCall { api.getPaiements(eleveId) }
    }

    suspend fun getRecuPaiement(paiementId: Int): Resource<String> {
        return safeApiCall { api.getRecuPaiement(paiementId) }
    }

    suspend fun getAbsences(eleveId: Int): Resource<List<Absence>> {
        return safeApiCall { api.getAbsences(eleveId) }
    }

    suspend fun getNotifications(): Resource<List<Notification>> {
        return safeApiCall { api.getNotifications() }
    }

    suspend fun marquerCommeLue(notificationId: Int): Resource<Unit> {
        return safeApiCall { api.marquerCommeLue(notificationId) }
    }
}
