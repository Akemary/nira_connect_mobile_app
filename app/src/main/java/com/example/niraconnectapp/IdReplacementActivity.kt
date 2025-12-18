package com.example.niraconnectapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class IdReplacementActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etDateOfBirth: EditText
    private lateinit var etCurrentNin: EditText
    private lateinit var etCurrentIdNumber: EditText
    private lateinit var etDateOfIssue: EditText
    private lateinit var etDateOfExpiry: EditText
    private lateinit var spReplacementReason: Spinner
    private lateinit var etPoliceReportNumber: EditText
    private lateinit var etIncidentDate: EditText
    private lateinit var etIncidentDescription: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var etCurrentAddress: EditText
    private lateinit var spCurrentDistrict: Spinner
    private lateinit var btnSubmit: Button
    private lateinit var ivPhoto: ImageView
    private lateinit var btnTakePhoto: Button
    private lateinit var btnChoosePhoto: Button
    private lateinit var btnRemovePhoto: Button
    private lateinit var tvPhotoStatus: TextView

    private val calendar = Calendar.getInstance()
    private var photoBitmap: Bitmap? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_replacement)

        initializeViews()
        setupDatePickers()
        setupSpinners()
        setupClickListeners()
    }

    private fun initializeViews() {
        etFullName = findViewById(R.id.etFullName)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        etCurrentNin = findViewById(R.id.etCurrentNin)
        etCurrentIdNumber = findViewById(R.id.etCurrentIdNumber)
        etDateOfIssue = findViewById(R.id.etDateOfIssue)
        etDateOfExpiry = findViewById(R.id.etDateOfExpiry)
        spReplacementReason = findViewById(R.id.spReplacementReason)
        etPoliceReportNumber = findViewById(R.id.etPoliceReportNumber)
        etIncidentDate = findViewById(R.id.etIncidentDate)
        etIncidentDescription = findViewById(R.id.etIncidentDescription)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        etCurrentAddress = findViewById(R.id.etCurrentAddress)
        spCurrentDistrict = findViewById(R.id.spCurrentDistrict)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivPhoto = findViewById(R.id.ivPhoto)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto)
        btnRemovePhoto = findViewById(R.id.btnRemovePhoto)
        tvPhotoStatus = findViewById(R.id.tvPhotoStatus)
    }

    private fun setupDatePickers() {
        val dateOfBirthSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField(etDateOfBirth)
        }

        val issueDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField(etDateOfIssue)
        }

        val expiryDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField(etDateOfExpiry)
        }

        val incidentDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField(etIncidentDate)
        }

        etDateOfBirth.setOnClickListener {
            DatePickerDialog(this, dateOfBirthSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etDateOfIssue.setOnClickListener {
            DatePickerDialog(this, issueDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etDateOfExpiry.setOnClickListener {
            DatePickerDialog(this, expiryDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etIncidentDate.setOnClickListener {
            DatePickerDialog(this, incidentDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateField(dateField: EditText) {
        val dateFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        dateField.setText(sdf.format(calendar.time))
    }

    private fun setupSpinners() {
        // Replacement Reason Spinner
        val replacementReasonOptions = arrayOf(
            "Select Replacement Reason",
            "Lost",
            "Stolen",
            "Damaged",
            "Change of Personal Details",
            "Other"
        )
        val replacementReasonAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, replacementReasonOptions)
        replacementReasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spReplacementReason.adapter = replacementReasonAdapter

        // District Spinner
        val districtOptions = arrayOf(
            "Select District", "Kampala", "Wakiso", "Mukono", "Jinja", "Mbale", "Gulu", "Lira", "Mbarara",
            "Fort Portal", "Masaka", "Entebbe", "Hoima", "Arua", "Soroti", "Moroto", "Kabale", "Other"
        )
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtOptions)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCurrentDistrict.adapter = districtAdapter

        // Dynamic police report field visibility
        spReplacementReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val reason = replacementReasonOptions[position]
                val requiresPoliceReport = reason == "Lost" || reason == "Stolen"

                etPoliceReportNumber.visibility = if (requiresPoliceReport) android.view.View.VISIBLE else android.view.View.GONE
                findViewById<TextView>(R.id.tvPoliceReportLabel).visibility = if (requiresPoliceReport) android.view.View.VISIBLE else android.view.View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupClickListeners() {
        btnSubmit.setOnClickListener {
            if (validateForm()) {
                submitApplication()
            }
        }

        btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        btnChoosePhoto.setOnClickListener {
            choosePhotoFromGallery()
        }

        btnRemovePhoto.setOnClickListener {
            removePhoto()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICK)
    }

    private fun removePhoto() {
        photoBitmap = null
        ivPhoto.setImageResource(R.drawable.ic_photo_placeholder)
        btnRemovePhoto.visibility = android.view.View.GONE
        tvPhotoStatus.text = "No photo uploaded"
        tvPhotoStatus.setTextColor(getColor(android.R.color.darker_gray))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    photoBitmap = imageBitmap
                    ivPhoto.setImageBitmap(imageBitmap)
                    btnRemovePhoto.visibility = android.view.View.VISIBLE
                    tvPhotoStatus.text = "Photo uploaded successfully"
                    tvPhotoStatus.setTextColor(getColor(android.R.color.holo_green_dark))
                    Toast.makeText(this, "Photo captured successfully", Toast.LENGTH_SHORT).show()
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImage: Uri? = data?.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                        photoBitmap = bitmap
                        ivPhoto.setImageBitmap(bitmap)
                        btnRemovePhoto.visibility = android.view.View.VISIBLE
                        tvPhotoStatus.text = "Photo uploaded successfully"
                        tvPhotoStatus.setTextColor(getColor(android.R.color.holo_green_dark))
                        Toast.makeText(this, "Photo selected successfully", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        if (etFullName.text.toString().trim().isEmpty()) {
            showError("Please enter your full name")
            return false
        }

        if (etCurrentNin.text.toString().trim().isEmpty()) {
            showError("Please enter your current NIN")
            return false
        }

        if (etCurrentIdNumber.text.toString().trim().isEmpty()) {
            showError("Please enter your current ID number")
            return false
        }

        if (spReplacementReason.selectedItemPosition == 0) {
            showError("Please select replacement reason")
            return false
        }

        val reason = spReplacementReason.selectedItem.toString()
        if ((reason == "Lost" || reason == "Stolen") && etPoliceReportNumber.text.toString().trim().isEmpty()) {
            showError("Police report number is required for lost/stolen IDs")
            return false
        }

        if (etIncidentDate.text.toString().trim().isEmpty()) {
            showError("Please select incident date")
            return false
        }

        if (etIncidentDescription.text.toString().trim().isEmpty()) {
            showError("Please describe the incident")
            return false
        }

        if (photoBitmap == null) {
            showError("Please upload a recent passport photo")
            return false
        }

        if (etPhoneNumber.text.toString().trim().isEmpty()) {
            showError("Please enter your phone number")
            return false
        }

        if (etCurrentAddress.text.toString().trim().isEmpty()) {
            showError("Please enter your current address")
            return false
        }

        return true
    }

    private fun submitApplication() {
        btnSubmit.text = "Submitting..."
        btnSubmit.isEnabled = false

        // Convert photo to byte array for database storage
        val photoData = photoBitmap?.let { bitmapToByteArray(it) }

        // Simulate API call
        android.os.Handler().postDelayed({
            showSuccessDialog()
            btnSubmit.text = "Submit Application"
            btnSubmit.isEnabled = true
        }, 2000)
    }

    private fun showSuccessDialog() {
        val trackingNumber = "IDR${System.currentTimeMillis()}"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Application Submitted Successfully!")
        builder.setMessage("Your ID replacement application has been submitted.\n\nTracking Number: $trackingNumber\n\nPlease keep this number for future reference.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Helper method to convert bitmap to byte array for database storage
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }
}