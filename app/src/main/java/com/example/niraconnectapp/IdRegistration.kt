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

class IdRegistrationActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etDateOfBirth: EditText
    private lateinit var spGender: Spinner
    private lateinit var spMaritalStatus: Spinner
    private lateinit var etPlaceOfBirth: EditText
    private lateinit var spNationality: Spinner
    private lateinit var spDistrictOfBirth: Spinner
    private lateinit var etFathersName: EditText
    private lateinit var etFathersNin: EditText
    private lateinit var etMothersName: EditText
    private lateinit var etMothersNin: EditText
    private lateinit var etGuardianName: EditText
    private lateinit var etGuardianNin: EditText
    private lateinit var etGuardianRelationship: EditText
    private lateinit var spEducationLevel: Spinner
    private lateinit var spOccupation: Spinner
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var etCurrentAddress: EditText
    private lateinit var spCurrentDistrict: Spinner
    private lateinit var etPermanentAddress: EditText
    private lateinit var spPermanentDistrict: Spinner
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
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_registration)

        dbHelper = DatabaseHelper(this)
        initializeViews()
        setupDatePicker()
        setupSpinners()
        setupClickListeners()
    }

    private fun initializeViews() {
        etFullName = findViewById(R.id.etFullName)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        spGender = findViewById(R.id.spGender)
        spMaritalStatus = findViewById(R.id.spMaritalStatus)
        etPlaceOfBirth = findViewById(R.id.etPlaceOfBirth)
        spNationality = findViewById(R.id.spNationality)
        spDistrictOfBirth = findViewById(R.id.spDistrictOfBirth)
        etFathersName = findViewById(R.id.etFathersName)
        etFathersNin = findViewById(R.id.etFathersNin)
        etMothersName = findViewById(R.id.etMothersName)
        etMothersNin = findViewById(R.id.etMothersNin)
        etGuardianName = findViewById(R.id.etGuardianName)
        etGuardianNin = findViewById(R.id.etGuardianNin)
        etGuardianRelationship = findViewById(R.id.etGuardianRelationship)
        spEducationLevel = findViewById(R.id.spEducationLevel)
        spOccupation = findViewById(R.id.spOccupation)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        etCurrentAddress = findViewById(R.id.etCurrentAddress)
        spCurrentDistrict = findViewById(R.id.spCurrentDistrict)
        etPermanentAddress = findViewById(R.id.etPermanentAddress)
        spPermanentDistrict = findViewById(R.id.spPermanentDistrict)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivPhoto = findViewById(R.id.ivPhoto)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto)
        btnRemovePhoto = findViewById(R.id.btnRemovePhoto)
        tvPhotoStatus = findViewById(R.id.tvPhotoStatus)
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateOfBirth()
        }

        etDateOfBirth.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateOfBirth() {
        val dateFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        etDateOfBirth.setText(sdf.format(calendar.time))
    }

    private fun setupSpinners() {
        // Gender Spinner
        val genderOptions = arrayOf("Select Gender", "Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender.adapter = genderAdapter

        // Marital Status Spinner
        val maritalOptions = arrayOf("Select Marital Status", "Single", "Married", "Divorced", "Widowed")
        val maritalAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maritalOptions)
        maritalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spMaritalStatus.adapter = maritalAdapter

        // Nationality Spinner
        val nationalityOptions = arrayOf("Select Nationality", "Ugandan", "Kenyan", "Tanzanian", "Rwandese", "Burundian", "South Sudanese", "Other")
        val nationalityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nationalityOptions)
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spNationality.adapter = nationalityAdapter

        // District Spinner (Common for all district fields)
        val districtOptions = arrayOf(
            "Select District", "Kampala", "Wakiso", "Mukono", "Jinja", "Mbale", "Gulu", "Lira", "Mbarara",
            "Fort Portal", "Masaka", "Entebbe", "Hoima", "Arua", "Soroti", "Moroto", "Kabale", "Other"
        )
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtOptions)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spDistrictOfBirth.adapter = districtAdapter
        spCurrentDistrict.adapter = districtAdapter
        spPermanentDistrict.adapter = districtAdapter

        // Education Level Spinner
        val educationOptions = arrayOf(
            "Select Education Level", "None", "Primary", "O-Level", "A-Level", "Diploma",
            "Bachelor's Degree", "Master's Degree", "PhD", "Other"
        )
        val educationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, educationOptions)
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEducationLevel.adapter = educationAdapter

        // Occupation Spinner
        val occupationOptions = arrayOf(
            "Select Occupation", "Student", "Farmer", "Teacher", "Doctor", "Engineer", "Business Person",
            "Civil Servant", "Military", "Police", "Driver", "Nurse", "Accountant", "Lawyer", "Unemployed", "Other"
        )
        val occupationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, occupationOptions)
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spOccupation.adapter = occupationAdapter
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

        if (etDateOfBirth.text.toString().trim().isEmpty()) {
            showError("Please select your date of birth")
            return false
        }

        if (spGender.selectedItemPosition == 0) {
            showError("Please select your gender")
            return false
        }

        if (spNationality.selectedItemPosition == 0) {
            showError("Please select your nationality")
            return false
        }

        // Validate that either parents or guardian information is provided
        val hasFatherInfo = etFathersName.text.toString().trim().isNotEmpty() || etFathersNin.text.toString().trim().isNotEmpty()
        val hasMotherInfo = etMothersName.text.toString().trim().isNotEmpty() || etMothersNin.text.toString().trim().isNotEmpty()
        val hasGuardianInfo = etGuardianName.text.toString().trim().isNotEmpty()

        if (!hasFatherInfo && !hasMotherInfo && !hasGuardianInfo) {
            showError("Please provide either parent information or guardian information")
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

        if (spCurrentDistrict.selectedItemPosition == 0) {
            showError("Please select your current district")
            return false
        }

        return true
    }

    private fun submitApplication() {
        if (!validateForm()) {
            return
        }

        // Show loading state
        btnSubmit.text = "Submitting..."
        btnSubmit.isEnabled = false

        // Get current user ID from SharedPreferences
        val sharedPreferences = getSharedPreferences("NiraPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getLong("USER_ID", -1L)

        if (userId == -1L) {
            showError("User not logged in")
            btnSubmit.text = "Submit Application"
            btnSubmit.isEnabled = true
            return
        }

        // Convert photo to byte array
        val photoData = photoBitmap?.let { dbHelper.bitmapToByteArray(it) }

        // Create IdRegistration object with all form data
        val registration = IdRegistration(
            userId = userId,
            fullName = etFullName.text.toString().trim(),
            dateOfBirth = etDateOfBirth.text.toString().trim(),
            gender = spGender.selectedItem.toString(),
            maritalStatus = if (spMaritalStatus.selectedItemPosition > 0) spMaritalStatus.selectedItem.toString() else null,
            placeOfBirth = etPlaceOfBirth.text.toString().trim(),
            nationality = spNationality.selectedItem.toString(),
            districtOfBirth = if (spDistrictOfBirth.selectedItemPosition > 0) spDistrictOfBirth.selectedItem.toString() else null,
            fathersName = etFathersName.text.toString().trim(),
            fathersNin = etFathersNin.text.toString().trim(),
            mothersName = etMothersName.text.toString().trim(),
            mothersNin = etMothersNin.text.toString().trim(),
            guardianName = etGuardianName.text.toString().trim(),
            guardianNin = etGuardianNin.text.toString().trim(),
            guardianRelationship = etGuardianRelationship.text.toString().trim(),
            educationLevel = if (spEducationLevel.selectedItemPosition > 0) spEducationLevel.selectedItem.toString() else null,
            occupation = if (spOccupation.selectedItemPosition > 0) spOccupation.selectedItem.toString() else null,
            phoneNumber = etPhoneNumber.text.toString().trim(),
            email = etEmail.text.toString().trim(),
            currentAddress = etCurrentAddress.text.toString().trim(),
            currentDistrict = spCurrentDistrict.selectedItem.toString(),
            permanentAddress = etPermanentAddress.text.toString().trim(),
            permanentDistrict = if (spPermanentDistrict.selectedItemPosition > 0) spPermanentDistrict.selectedItem.toString() else null,
            photoData = photoData,
            trackingNumber = "IDREG${System.currentTimeMillis()}"
        )

        // Submit to database
        val result = dbHelper.addIdRegistration(registration)

        if (result != -1L) {
            showSuccessDialog(registration.trackingNumber)
        } else {
            showError("Failed to submit application. Please try again.")
            btnSubmit.text = "Submit Application"
            btnSubmit.isEnabled = true
        }
    }

    private fun showSuccessDialog(trackingNumber: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Application Submitted Successfully!")
        builder.setMessage("Your National ID registration application has been submitted successfully.\n\nTracking Number: $trackingNumber\n\nPlease keep this number for future reference.")
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