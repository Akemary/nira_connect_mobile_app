package com.example.niraconnectapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("NiraPrefs", MODE_PRIVATE)
        initializeViews()
        setupClickListeners()
        setupUI()
    }

    private fun initializeViews() {
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupUI() {
        // Set up input field backgrounds to match new forms
        etFullName.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
        etEmail.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
        etUsername.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
        etPassword.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
        etConfirmPassword.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            if (validateForm()) {
                registerUser()
            }
        }

        tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun validateForm(): Boolean {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (fullName.isEmpty()) {
            showError("Please enter your full name")
            return false
        }

        if (fullName.split(" ").size < 2) {
            showError("Please enter your full name (first and last name)")
            return false
        }

        if (email.isEmpty()) {
            showError("Please enter your email address")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter a valid email address")
            return false
        }

        if (dbHelper.isEmailTaken(email)) {
            showError("Email is already registered")
            return false
        }

        if (username.isEmpty()) {
            showError("Please enter a username")
            return false
        }

        if (username.length < 4) {
            showError("Username must be at least 4 characters long")
            return false
        }

        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            showError("Username can only contain letters, numbers, and underscores")
            return false
        }

        if (dbHelper.isUsernameTaken(username)) {
            showError("Username is already taken")
            return false
        }

        if (password.isEmpty()) {
            showError("Please enter a password")
            return false
        }

        if (password.length < 6) {
            showError("Password must be at least 6 characters long")
            return false
        }

        if (!password.matches(Regex(".*[A-Z].*"))) {
            showError("Password must contain at least one uppercase letter")
            return false
        }

        if (!password.matches(Regex(".*[0-9].*"))) {
            showError("Password must contain at least one number")
            return false
        }

        if (confirmPassword.isEmpty()) {
            showError("Please confirm your password")
            return false
        }

        if (password != confirmPassword) {
            showError("Passwords do not match")
            return false
        }

        return true
    }

    private fun registerUser() {
        showLoading(true)

        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()
        val createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val user = User(
            fullName = fullName,
            email = email,
            username = username,
            password = password,
            createdAt = createdAt
        )

        // Simulate network delay
        android.os.Handler().postDelayed({
            val userId = dbHelper.addUser(user)

            if (userId != -1L) {
                // Save user session
                saveUserSession(userId, username, fullName, email)
                showSuccessDialog()
            } else {
                showError("Registration failed. Please try again.")
                showLoading(false)
            }
        }, 2000)
    }

    private fun saveUserSession(userId: Long, username: String, fullName: String, email: String) {
        val editor = sharedPreferences.edit()
        editor.putLong("USER_ID", userId)
        editor.putString("USERNAME", username)
        editor.putString("FULL_NAME", fullName)
        editor.putString("EMAIL", email)
        editor.putBoolean("IS_LOGGED_IN", true)
        editor.apply()
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registration Successful!")
        builder.setMessage("Welcome to NIRA Connect! Your account has been created successfully. You can now apply for National ID services.")
        builder.setPositiveButton("Get Started") { dialog, _ ->
            dialog.dismiss()
            navigateToHome()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = android.view.View.VISIBLE
            btnRegister.text = "Creating Account..."
            btnRegister.isEnabled = false
            btnRegister.alpha = 0.7f
        } else {
            progressBar.visibility = android.view.View.GONE
            btnRegister.text = "Create Account"
            btnRegister.isEnabled = true
            btnRegister.alpha = 1f
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}