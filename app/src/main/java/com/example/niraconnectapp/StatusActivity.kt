package com.example.niraconnectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat

class StatusActivity : AppCompatActivity() {

    private lateinit var etTrackingNumber: EditText
    private lateinit var etNin: EditText
    private lateinit var btnCheckStatus: Button
    private lateinit var statusLayout: LinearLayout
    private lateinit var tvStatusTitle: TextView
    private lateinit var tvStatusDescription: TextView
    private lateinit var tvLastUpdate: TextView
    private lateinit var progressStep1: LinearLayout
    private lateinit var progressStep2: LinearLayout
    private lateinit var progressStep3: LinearLayout
    private lateinit var progressStep4: LinearLayout
    private lateinit var progressStep5: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        initializeViews()
        setupClickListeners()

        // Initially hide status layout
        statusLayout.visibility = android.view.View.GONE
    }

    private fun initializeViews() {
        etTrackingNumber = findViewById(R.id.etTrackingNumber)
        etNin = findViewById(R.id.etNin)
        btnCheckStatus = findViewById(R.id.btnCheckStatus)
        statusLayout = findViewById(R.id.statusLayout)
        tvStatusTitle = findViewById(R.id.tvStatusTitle)
        tvStatusDescription = findViewById(R.id.tvStatusDescription)
        tvLastUpdate = findViewById(R.id.tvLastUpdate)
        progressStep1 = findViewById(R.id.progressStep1)
        progressStep2 = findViewById(R.id.progressStep2)
        progressStep3 = findViewById(R.id.progressStep3)
        progressStep4 = findViewById(R.id.progressStep4)
        progressStep5 = findViewById(R.id.progressStep5)
    }

    private fun setupClickListeners() {
        btnCheckStatus.setOnClickListener {
            if (validateForm()) {
                checkApplicationStatus()
            }
        }

        // Contact Support button inside status results
        val btnContactSupport = findViewById<Button>(R.id.btnContactSupport)
        btnContactSupport.setOnClickListener {
            navigateToSupport()
        }

        // Contact Support button in help card
        val btnHelpContactSupport = findViewById<Button>(R.id.btnHelpContactSupport)
        btnHelpContactSupport.setOnClickListener {
            navigateToSupport()
        }
    }

    private fun navigateToSupport() {
        val intent = android.content.Intent(this, SupportActivity::class.java)
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        if (etTrackingNumber.text.toString().trim().isEmpty() && etNin.text.toString().trim().isEmpty()) {
            showError("Please enter either Tracking Number or NIN")
            return false
        }
        return true
    }

    private fun checkApplicationStatus() {
        // Show loading state
        btnCheckStatus.text = "Checking Status..."
        btnCheckStatus.isEnabled = false

        // Simulate API call
        android.os.Handler().postDelayed({
            val trackingNumber = etTrackingNumber.text.toString().trim()
            val nin = etNin.text.toString().trim()

            // For demo, generate random status (1-5)
            val randomStatus = (1..5).random()
            displayStatus(randomStatus)

            btnCheckStatus.text = "Check Status"
            btnCheckStatus.isEnabled = true
        }, 1500)
    }

    private fun displayStatus(status: Int) {
        statusLayout.visibility = android.view.View.VISIBLE

        // Reset all progress steps
        resetProgressSteps()

        when (status) {
            1 -> {
                tvStatusTitle.text = "Application Received"
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.status_pending))
                tvStatusDescription.text = "Your application has been received and is awaiting initial review. Please allow 2-3 business days for processing."
                updateProgressStep(progressStep1, true)
            }
            2 -> {
                tvStatusTitle.text = "Under Review"
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.status_in_progress))
                tvStatusDescription.text = "Your application is currently being reviewed by our verification team. This usually takes 1-2 business days."
                updateProgressStep(progressStep1, true)
                updateProgressStep(progressStep2, true)
            }
            3 -> {
                tvStatusTitle.text = "Processing"
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.status_in_progress))
                tvStatusDescription.text = "Your application is being processed. Your National ID card is being prepared for production."
                updateProgressStep(progressStep1, true)
                updateProgressStep(progressStep2, true)
                updateProgressStep(progressStep3, true)
            }
            4 -> {
                tvStatusTitle.text = "Ready for Collection"
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.status_ready))
                tvStatusDescription.text = "Your National ID is ready for collection at your designated NIRA office. Please bring your receipt and original documents."
                updateProgressStep(progressStep1, true)
                updateProgressStep(progressStep2, true)
                updateProgressStep(progressStep3, true)
                updateProgressStep(progressStep4, true)
            }
            5 -> {
                tvStatusTitle.text = "Completed"
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.status_completed))
                tvStatusDescription.text = "Your National ID has been collected successfully. Thank you for using NIRA services."
                updateProgressStep(progressStep1, true)
                updateProgressStep(progressStep2, true)
                updateProgressStep(progressStep3, true)
                updateProgressStep(progressStep4, true)
                updateProgressStep(progressStep5, true)
            }
        }

        val currentTime = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.US).format(java.util.Date())
        tvLastUpdate.text = "Last updated: $currentTime"
    }

    private fun resetProgressSteps() {
        updateProgressStep(progressStep1, false)
        updateProgressStep(progressStep2, false)
        updateProgressStep(progressStep3, false)
        updateProgressStep(progressStep4, false)
        updateProgressStep(progressStep5, false)
    }

    private fun updateProgressStep(stepLayout: LinearLayout, isCompleted: Boolean) {
        val icon = stepLayout.getChildAt(0) as ImageView
        val text = stepLayout.getChildAt(1) as TextView

        if (isCompleted) {
            icon.setColorFilter(ContextCompat.getColor(this, R.color.status_completed))
            text.setTextColor(ContextCompat.getColor(this, R.color.status_completed))
        } else {
            icon.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary))
            text.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}