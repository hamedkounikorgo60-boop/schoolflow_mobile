package com.schoolflow.mobile.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.schoolflow.mobile.models.Eleve
import com.schoolflow.mobile.models.User

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "schoolflow_session"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "user_data"
        private const val KEY_ELEVES = "eleves_data"
        private const val KEY_SELECTED_ELEVE = "selected_eleve_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveSession(token: String, user: User, eleves: List<Eleve>) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER, gson.toJson(user))
            putString(KEY_ELEVES, gson.toJson(eleves))
            putBoolean(KEY_IS_LOGGED_IN, true)
            if (eleves.isNotEmpty()) {
                putInt(KEY_SELECTED_ELEVE, eleves[0].id)
            }
            apply()
        }
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getUser(): User? {
        val json = prefs.getString(KEY_USER, null) ?: return null
        return gson.fromJson(json, User::class.java)
    }

    fun getEleves(): List<Eleve> {
        val json = prefs.getString(KEY_ELEVES, null) ?: return emptyList()
        val type = object : TypeToken<List<Eleve>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getSelectedEleveId(): Int = prefs.getInt(KEY_SELECTED_ELEVE, -1)

    fun setSelectedEleveId(id: Int) {
        prefs.edit().putInt(KEY_SELECTED_ELEVE, id).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
