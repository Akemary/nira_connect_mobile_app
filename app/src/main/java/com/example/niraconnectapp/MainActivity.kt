package com.example.niraconnectapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // REMOVED the automatic login check - Always show login page
        // This ensures users always see the login screen when opening the app

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("NiraPrefs", MODE_PRIVATE)
        initializeViews()
        setupClickListeners()
        setupUI()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupUI() {
        // Set up input field backgrounds and styles to match new forms
        etUsername.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
        etPassword.background = ContextCompat.getDrawable(this, R.drawable.edit_text_background)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            if (validateForm()) {
                loginUser()
            }
        }

        tvRegister.setOnClickListener {
            navigateToRegistration()
        }

        tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun validateForm(): Boolean {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()

        if (username.isEmpty()) {
            showError("Please enter your username")
            return false
        }

        if (password.isEmpty()) {
            showError("Please enter your password")
            return false
        }

        if (password.length < 6) {
            showError("Password must be at least 6 characters")
            return false
        }

        return true
    }

    private fun loginUser() {
        showLoading(true)

        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()

        // Simulate network delay
        android.os.Handler().postDelayed({
            val user = dbHelper.checkUser(username, password)

            if (user != null) {
                // Save user session
                saveUserSession(user.id, user.username, user.fullName, user.email)
                showSuccess("Login successful!")
                navigateToHome()
            } else {
                showError("Invalid username or password")
                showLoading(false)
            }
        }, 1500)
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

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showForgotPasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Reset Password")
            .setView(dialogView)
            .setPositiveButton("Send Reset Link") { dialog, _ ->
                val email = etEmail.text.toString().trim()
                if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    resetPassword(email)
                } else {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun resetPassword(email: String) {
        // In a real app, this would call your backend API
        Toast.makeText(this, "Password reset link sent to $email", Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = android.view.View.VISIBLE
            btnLogin.text = "Signing In..."
            btnLogin.isEnabled = false
            btnLogin.alpha = 0.7f
        } else {
            progressBar.visibility = android.view.View.GONE
            btnLogin.text = "Login"
            btnLogin.isEnabled = true
            btnLogin.alpha = 1f
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}