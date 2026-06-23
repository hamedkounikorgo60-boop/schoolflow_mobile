package com.schoolflow.mobile.ui.payments

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.schoolflow.mobile.SchoolFlowApp
import com.schoolflow.mobile.databinding.ActivityPaymentReceiptBinding
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class PaymentReceiptActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECU_URL = "recu_url"
    }

    private lateinit var binding: ActivityPaymentReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val recuUrl = intent.getStringExtra(EXTRA_RECU_URL)
        if (recuUrl != null) {
            loadReceipt(recuUrl)
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (recuUrl != null) {
                downloadReceipt(recuUrl)
            }
            true
        }
    }

    private fun loadReceipt(url: String) {
        binding.progressBar.visible()
        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    binding.progressBar.gone()
                }
            }
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            val token = SchoolFlowApp.instance.sessionManager.getToken()
            val headers = mapOf("Authorization" to "Bearer $token")
            loadUrl(url, headers)
        }
    }

    private fun downloadReceipt(url: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle("Reçu de paiement")
                setDescription("Téléchargement du reçu de paiement")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "recu_paiement_${System.currentTimeMillis()}.pdf"
                )
                val token = SchoolFlowApp.instance.sessionManager.getToken()
                addRequestHeader("Authorization", "Bearer $token")
            }

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            Toast.makeText(this, "Téléchargement démarré", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors du téléchargement", Toast.LENGTH_SHORT).show()
        }
    }
}
