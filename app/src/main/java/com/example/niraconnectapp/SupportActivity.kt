package com.example.niraconnectapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class SupportActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var spIssueType: Spinner
    private lateinit var etSubject: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        initializeViews()
        setupSpinners()
        setupClickListeners()
    }

    private fun initializeViews() {
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        spIssueType = findViewById(R.id.spIssueType)
        etSubject = findViewById(R.id.etSubject)
        etDescription = findViewById(R.id.etDescription)
        btnSubmit = findViewById(R.id.btnSubmit)
    }

    private fun setupSpinners() {
        // Issue Type Spinner
        val issueTypeOptions = arrayOf(
            "Select Issue Type",
            "Application Status Inquiry",
            "Technical Issues",
            "Document Verification",
            "Payment Issues",
            "Lost/Stolen ID",
            "Change of Personal Details",
            "General Inquiry",
            "Complaint",
            "Other"
        )
        val issueTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, issueTypeOptions)
        issueTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spIssueType.adapter = issueTypeAdapter
    }

    private fun setupClickListeners() {
        btnSubmit.setOnClickListener {
            if (validateForm()) {
                submitSupportRequest()
            }
        }

        // Setup click listeners for quick contact options
        findViewById<LinearLayout>(R.id.cardCallSupport).setOnClickListener {
            callSupport()
        }

        findViewById<LinearLayout>(R.id.cardEmailSupport).setOnClickListener {
            emailSupport()
        }

        findViewById<LinearLayout>(R.id.cardLiveChat).setOnClickListener {
            startLiveChat()
        }

        findViewById<LinearLayout>(R.id.cardVisitOffice).setOnClickListener {
            findNearestOffice()
        }
    }

    private fun validateForm(): Boolean {
        if (etFullName.text.toString().trim().isEmpty()) {
            showError("Please enter your full name")
            return false
        }

        if (etEmail.text.toString().trim().isEmpty()) {
            showError("Please enter your email address")
            return false
        }

        if (etPhoneNumber.text.toString().trim().isEmpty()) {
            showError("Please enter your phone number")
            return false
        }

        if (spIssueType.selectedItemPosition == 0) {
            showError("Please select issue type")
            return false
        }

        if (etSubject.text.toString().trim().isEmpty()) {
            showError("Please enter subject")
            return false
        }

        if (etDescription.text.toString().trim().isEmpty()) {
            showError("Please describe your issue")
            return false
        }

        return true
    }

    private fun submitSupportRequest() {
        // Show loading state
        btnSubmit.text = "Submitting..."
        btnSubmit.isEnabled = false

        // Simulate API call
        android.os.Handler().postDelayed({
            showSuccessDialog()
            btnSubmit.text = "Submit Request"
            btnSubmit.isEnabled = true
        }, 1500)
    }

    private fun callSupport() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:0414349000")
        startActivity(intent)
    }

    private fun emailSupport() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:support@nira.go.ug")
        intent.putExtra(Intent.EXTRA_SUBJECT, "NIRA Support Request")
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    private fun startLiveChat() {
        Toast.makeText(this, "Live Chat feature coming soon!", Toast.LENGTH_SHORT).show()
    }

    private fun findNearestOffice() {
        Toast.makeText(this, "Opening NIRA offices map...", Toast.LENGTH_SHORT).show()
        // In real app, this would open maps with NIRA office locations
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Support Request Submitted")
        builder.setMessage("Your support request has been submitted successfully. Our team will contact you within 24 hours. Reference ID: NIRA-${System.currentTimeMillis()}")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            clearForm()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun clearForm() {
        etFullName.text.clear()
        etEmail.text.clear()
        etPhoneNumber.text.clear()
        spIssueType.setSelection(0)
        etSubject.text.clear()
        etDescription.text.clear()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}