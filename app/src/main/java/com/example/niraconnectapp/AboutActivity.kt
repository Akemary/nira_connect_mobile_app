package com.example.niraconnectapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Contact buttons
        findViewById<Button>(R.id.btnCallHeadquarters)?.setOnClickListener {
            callHeadquarters()
        }

        findViewById<Button>(R.id.btnEmailSupport)?.setOnClickListener {
            emailSupport()
        }

        findViewById<Button>(R.id.btnVisitWebsite)?.setOnClickListener {
            visitWebsite()
        }

        findViewById<Button>(R.id.btnViewPrivacyPolicy)?.setOnClickListener {
            showPrivacyPolicy()
        }

        findViewById<Button>(R.id.btnViewTerms)?.setOnClickListener {
            showTermsOfService()
        }
    }

    private fun callHeadquarters() {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0414349000")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot make phone call", Toast.LENGTH_SHORT).show()
        }
    }

    private fun emailSupport() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:support@nira.go.ug")
            intent.putExtra(Intent.EXTRA_SUBJECT, "NIRA Connect App Support")
            startActivity(Intent.createChooser(intent, "Send Email"))
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun visitWebsite() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.nira.go.ug")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open website", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPrivacyPolicy() {
        showInfoDialog("Privacy Policy",
            "NIRA is committed to protecting your privacy. We collect and use personal information solely for the purpose of national identification and registration services.\n\n" +
                    "• Your data is stored securely and confidentially\n" +
                    "• We comply with the Data Protection and Privacy Act\n" +
                    "• Information is only shared with authorized government agencies\n" +
                    "• You have the right to access and correct your personal data")
    }

    private fun showTermsOfService() {
        showInfoDialog("Terms of Service",
            "By using NIRA Connect, you agree to:\n\n" +
                    "• Provide accurate and truthful information\n" +
                    "• Use the app for legitimate identification purposes only\n" +
                    "• Comply with all applicable laws and regulations\n" +
                    "• Not misuse or attempt to hack the application\n" +
                    "• Accept that service availability may vary\n\n" +
                    "NIRA reserves the right to modify these terms at any time.")
    }

    private fun showInfoDialog(title: String, message: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}