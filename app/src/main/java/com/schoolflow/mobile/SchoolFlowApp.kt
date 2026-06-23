package com.schoolflow.mobile

import android.app.Application
import com.schoolflow.mobile.api.RetrofitClient
import com.schoolflow.mobile.utils.SessionManager

class SchoolFlowApp : Application() {

    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        sessionManager = SessionManager(this)
        RetrofitClient.init(sessionManager)
    }

    companion object {
        lateinit var instance: SchoolFlowApp
            private set
    }
}
