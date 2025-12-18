package com.example.niraconnectapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.data.Entry as ChartEntry
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeActivity : AppCompatActivity() {

    private lateinit var btnHamburger: ImageButton
    private lateinit var btnProfile: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var tvTotalApplications: TextView
    private lateinit var tvPendingCount: TextView
    private lateinit var tvInProgressCount: TextView
    private lateinit var tvReadyCount: TextView
    private lateinit var tvRejectedCount: TextView
    private lateinit var chartRegistration: PieChart
    private lateinit var chartRenewal: LineChart
    private lateinit var chartReplacement: BarChart
    private lateinit var tvRegistrationCount: TextView
    private lateinit var tvRenewalCount: TextView
    private lateinit var tvReplacementCount: TextView
    private lateinit var layoutRecentApplications: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    private val dbHelper by lazy { DatabaseHelper(this) }
    private lateinit var sharedPreferences: SharedPreferences

    private val REQUEST_GALLERY_PERMISSION = 100
    private val REQUEST_CAMERA_PERMISSION = 101
    private val REQUEST_CAMERA = 102
    private val REQUEST_GALLERY = 103
    private val REQUEST_SETTINGS = 104
    private var currentPhotoPath: String? = null
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("NiraPrefs", MODE_PRIVATE)

        initializeViews()
        setupClickListeners()
        loadUserData()
        setupRefresh()
        loadDashboardData()
    }

    private fun initializeViews() {
        btnHamburger = findViewById(R.id.btnHamburger)
        btnProfile = findViewById(R.id.btnProfile)
        tvTotalApplications = findViewById(R.id.tvTotalApplications)
        tvPendingCount = findViewById(R.id.tvPendingCount)
        tvInProgressCount = findViewById(R.id.tvInProgressCount)
        tvReadyCount = findViewById(R.id.tvReadyCount)
        tvRejectedCount = findViewById(R.id.tvRejectedCount)
        chartRegistration = findViewById(R.id.chartRegistration)
        chartRenewal = findViewById(R.id.chartRenewal)
        chartReplacement = findViewById(R.id.chartReplacement)
        tvRegistrationCount = findViewById(R.id.tvRegistrationCount)
        tvRenewalCount = findViewById(R.id.tvRenewalCount)
        tvReplacementCount = findViewById(R.id.tvReplacementCount)
        layoutRecentApplications = findViewById(R.id.layoutRecentApplications)
        progressBar = findViewById(R.id.progressBar)
        swipeRefresh = findViewById(R.id.swipeRefresh)
    }

    private fun loadUserData() {
        // Load saved profile image if exists
        val profileImageUri = sharedPreferences.getString("PROFILE_IMAGE", null)
        if (profileImageUri != null) {
            btnProfile.setImageURI(Uri.parse(profileImageUri))
        }
    }

    private fun setupClickListeners() {
        btnHamburger.setOnClickListener {
            showHamburgerMenu()
        }

        btnProfile.setOnClickListener {
            showProfilePopupMenu(it)
        }

        // Chart click listeners for details
        findViewById<View>(R.id.cardRegistration).setOnClickListener {
            showApplicationDetails("registration")
        }

        findViewById<View>(R.id.cardRenewal).setOnClickListener {
            showApplicationDetails("renewal")
        }

        findViewById<View>(R.id.cardReplacement).setOnClickListener {
            showApplicationDetails("replacement")
        }

        // Quick action buttons
        findViewById<Button>(R.id.btnQuickRegistration).setOnClickListener {
            startActivity(Intent(this, IdRegistrationActivity::class.java))
        }

        findViewById<Button>(R.id.btnQuickRenewal).setOnClickListener {
            startActivity(Intent(this, IdRenewalActivity::class.java))
        }

        findViewById<Button>(R.id.btnQuickReplacement).setOnClickListener {
            startActivity(Intent(this, IdReplacementActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewAll).setOnClickListener {
            showAllApplications()
        }
    }

    private fun setupRefresh() {
        swipeRefresh.setOnRefreshListener {
            loadDashboardData()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun loadDashboardData() {
        progressBar.visibility = View.VISIBLE

        val userId = sharedPreferences.getLong("USER_ID", -1L)
        if (userId == -1L) return

        // Get user applications
        val applications = dbHelper.getUserApplications(userId)

        // Calculate statistics with our new status categories
        val totalApps = applications.size
        val pendingApps = applications.filter { it.status.equals("Submitted", ignoreCase = true) }
        val inProgressApps = applications.filter {
            it.status.equals("In Progress", ignoreCase = true) ||
                    it.status.equals("Processing", ignoreCase = true)
        }
        val readyApps = applications.filter { it.status.equals("Ready", ignoreCase = true) }
        val rejectedApps = applications.filter { it.status.equals("Rejected", ignoreCase = true) }

        // Update counts
        tvTotalApplications.text = totalApps.toString()
        tvPendingCount.text = pendingApps.size.toString()
        tvPendingCount.setTextColor(getStatusColor("Submitted")) // Set purple color
        tvInProgressCount.text = inProgressApps.size.toString()
        tvReadyCount.text = readyApps.size.toString()
        tvRejectedCount.text = rejectedApps.size.toString()

        // Separate applications by type
        val registrationApps = applications.filter { it.type == "registration" }
        val renewalApps = applications.filter { it.type == "renewal" }
        val replacementApps = applications.filter { it.type == "replacement" }

        tvRegistrationCount.text = registrationApps.size.toString()
        tvRenewalCount.text = renewalApps.size.toString()
        tvReplacementCount.text = replacementApps.size.toString()

        // Setup interactive charts
        setupRegistrationPieChart(registrationApps)
        setupRenewalLineChart(renewalApps)
        setupReplacementBarChart(replacementApps)

        // Load recent applications
        loadRecentApplications(applications)

        progressBar.visibility = View.GONE
    }

    private fun setupRegistrationPieChart(applications: List<UserApplication>) {
        if (applications.isEmpty()) {
            chartRegistration.setNoDataText("No registration applications")
            chartRegistration.setNoDataTextColor(Color.GRAY)
            return
        }

        // Group by status for pie chart - Show "Pending" instead of "Submitted"
        val statusMap = mapOf(
            "Pending" to applications.count { it.status.equals("Submitted", ignoreCase = true) },
            "In Progress" to applications.count { it.status.equals("In Progress", ignoreCase = true) ||
                    it.status.equals("Processing", ignoreCase = true) },
            "Ready" to applications.count { it.status.equals("Ready", ignoreCase = true) },
            "Rejected" to applications.count { it.status.equals("Rejected", ignoreCase = true) }
        )

        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()
        val labels = ArrayList<String>()

        var total = 0
        statusMap.forEach { (status, count) ->
            if (count > 0) {
                total += count
                entries.add(PieEntry(count.toFloat(), status))
                labels.add(status)
                colors.add(getStatusColor(status))
            }
        }

        // If all applications are 0, show empty chart
        if (entries.isEmpty()) {
            chartRegistration.setNoDataText("No registration data available")
            chartRegistration.setNoDataTextColor(Color.GRAY)
            chartRegistration.invalidate()
            return
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 8f

        // Add value lines
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        val data = PieData(dataSet)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val percentage = (value / total * 100).roundToInt()
                return "$percentage%"
            }
        })
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        // Chart configuration
        chartRegistration.setDrawHoleEnabled(true)
        chartRegistration.holeRadius = 40f
        chartRegistration.transparentCircleRadius = 45f
        chartRegistration.setHoleColor(Color.TRANSPARENT)
        chartRegistration.setDrawCenterText(true)
        chartRegistration.centerText = "ID\nRegistrations"
        chartRegistration.setCenterTextSize(16f)
        chartRegistration.setCenterTextColor(Color.parseColor("#4A4A4A"))
        chartRegistration.description.isEnabled = false
        chartRegistration.legend.isEnabled = true
        chartRegistration.legend.textSize = 12f
        chartRegistration.setEntryLabelColor(Color.BLACK)
        chartRegistration.setEntryLabelTextSize(12f)
        chartRegistration.data = data
        chartRegistration.invalidate()

        // Add click listener
        chartRegistration.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: com.github.mikephil.charting.data.Entry?, h: Highlight?) {
                if (e != null && h != null) {
                    val index = h.x.toInt()
                    if (index >= 0 && index < labels.size) {
                        val status = labels[index]
                        // Convert "Pending" back to "Submitted" for database query
                        val dbStatus = if (status == "Pending") "Submitted" else status
                        showStatusDetails("registration", dbStatus)
                    }
                }
            }

            override fun onNothingSelected() {
                // Do nothing when nothing is selected
            }
        })
    }

    private fun setupRenewalLineChart(applications: List<UserApplication>) {
        if (applications.isEmpty()) {
            chartRenewal.setNoDataText("No renewal applications")
            chartRenewal.setNoDataTextColor(Color.GRAY)
            return
        }

        // Create mock monthly data for trend visualization
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        // Generate trend data - simulate application submissions over last 6 months
        val entries = ArrayList<com.github.mikephil.charting.data.Entry>()
        val xValues = ArrayList<String>()

        for (i in 5 downTo 0) {
            val monthIndex = (currentMonth - i + 12) % 12
            val month = months[monthIndex]
            xValues.add(month)

            // Simulate data: more applications in recent months
            val baseCount = if (applications.isEmpty()) 0 else applications.size / 6
            val randomFactor = (0..baseCount/2).random()
            val count = baseCount + randomFactor

            entries.add(com.github.mikephil.charting.data.Entry((5 - i).toFloat(), count.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Monthly Renewals")
        dataSet.color = Color.parseColor("#1976D2")
        dataSet.lineWidth = 3f
        dataSet.setCircleColor(Color.parseColor("#1976D2"))
        dataSet.circleRadius = 5f
        dataSet.setDrawCircleHole(true)
        dataSet.circleHoleRadius = 3f
        dataSet.valueTextSize = 10f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.fillColor = Color.parseColor("#801976D2")
        dataSet.setDrawFilled(true)

        val lineData = LineData(dataSet)
        chartRenewal.data = lineData

        // Chart configuration
        chartRenewal.description.isEnabled = false
        chartRenewal.legend.isEnabled = true
        chartRenewal.legend.textSize = 12f
        chartRenewal.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        chartRenewal.xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        chartRenewal.xAxis.granularity = 1f
        chartRenewal.xAxis.labelCount = 6
        chartRenewal.axisLeft.axisMinimum = 0f
        chartRenewal.axisRight.isEnabled = false
        chartRenewal.invalidate()
    }

    private fun setupReplacementBarChart(applications: List<UserApplication>) {
        if (applications.isEmpty()) {
            chartReplacement.setNoDataText("No replacement applications")
            chartReplacement.setNoDataTextColor(Color.GRAY)
            return
        }

        // Group by status for bar chart - Use display names instead of database status
        val displayStatusOrder = listOf("Pending", "In Progress", "Ready", "Rejected")
        val dbStatusOrder = listOf("Submitted", "In Progress", "Ready", "Rejected")

        val statusValues = ArrayList<BarEntry>()
        val xLabels = ArrayList<String>()

        dbStatusOrder.forEachIndexed { index, dbStatus ->
            val count = applications.count {
                when (dbStatus) {
                    "Submitted" -> it.status.equals("Submitted", ignoreCase = true)
                    "In Progress" -> it.status.equals("In Progress", ignoreCase = true) ||
                            it.status.equals("Processing", ignoreCase = true)
                    "Ready" -> it.status.equals("Ready", ignoreCase = true)
                    "Rejected" -> it.status.equals("Rejected", ignoreCase = true)
                    else -> false
                }
            }
            if (count > 0) {
                statusValues.add(BarEntry(index.toFloat(), count.toFloat()))
                // Use display name (Pending) instead of database status (Submitted)
                xLabels.add(displayStatusOrder[index])
            }
        }

        val dataSet = BarDataSet(statusValues, "Replacement Status")
        // Use display status colors
        dataSet.colors = displayStatusOrder.map { getStatusColor(it) }
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK

        val data = BarData(dataSet)
        data.barWidth = 0.5f

        chartReplacement.data = data
        chartReplacement.description.isEnabled = false
        chartReplacement.legend.isEnabled = true
        chartReplacement.legend.textSize = 12f
        chartReplacement.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
        chartReplacement.xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        chartReplacement.xAxis.granularity = 1f
        chartReplacement.axisLeft.axisMinimum = 0f
        chartReplacement.axisRight.isEnabled = false
        chartReplacement.invalidate()

        // Add click listener
        chartReplacement.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: com.github.mikephil.charting.data.Entry?, h: Highlight?) {
                if (e != null && h != null && e is BarEntry) {
                    val index = e.x.toInt()
                    if (index < xLabels.size) {
                        val displayStatus = xLabels[index]
                        // Convert display status back to database status for query
                        val dbStatus = if (displayStatus == "Pending") "Submitted" else displayStatus
                        showStatusDetails("replacement", dbStatus)
                    }
                }
            }

            override fun onNothingSelected() {
                // Do nothing when nothing is selected
            }
        })
    }

    private fun getStatusColor(status: String): Int {
        return when (status.toLowerCase()) {
            "submitted", "pending" -> Color.parseColor("#9C27B0") // Purple for both
            "in progress", "processing" -> Color.parseColor("#3B82F6") // Blue
            "ready" -> Color.parseColor("#10B981") // Green
            "approved" -> Color.parseColor("#4CAF50") // Green
            "rejected" -> Color.parseColor("#F44336") // Red
            "completed" -> Color.parseColor("#059669") // Dark Green
            else -> Color.parseColor("#9E9E9E") // Gray
        }
    }

    private fun loadRecentApplications(applications: List<UserApplication>) {
        layoutRecentApplications.removeAllViews()

        val recentApps = applications.sortedByDescending { it.submittedAt }.take(3)

        if (recentApps.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = "No recent applications"
                textSize = 14f
                setTextColor(Color.GRAY)
                gravity = android.view.Gravity.CENTER
                setPadding(0, 32, 0, 32)
            }
            layoutRecentApplications.addView(emptyView)
            return
        }

        recentApps.forEach { app ->
            val appView = layoutInflater.inflate(R.layout.item_recent_application, null)

            appView.findViewById<TextView>(R.id.tvAppType).text =
                when (app.type) {
                    "registration" -> "New ID Registration"
                    "renewal" -> "ID Renewal"
                    "replacement" -> "ID Replacement"
                    else -> app.type.replaceFirstChar { it.uppercase() }
                }

            // Show only tracking number for confidentiality
            appView.findViewById<TextView>(R.id.tvTrackingNumber).text = "Tracking: ${app.trackingNumber}"

            // Display "Pending" instead of "Submitted"
            val displayStatus = if (app.status.equals("Submitted", ignoreCase = true)) "Pending" else app.status
            appView.findViewById<TextView>(R.id.tvStatus).text = displayStatus
            appView.findViewById<TextView>(R.id.tvStatus).setTextColor(getStatusColor(displayStatus))

            // Format date
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = inputFormat.parse(app.submittedAt)
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                appView.findViewById<TextView>(R.id.tvDate).text = outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                appView.findViewById<TextView>(R.id.tvDate).text = app.submittedAt
            }

            appView.setOnClickListener {
                showApplicationTrackingInfo(app.trackingNumber, displayStatus)
            }

            layoutRecentApplications.addView(appView)
        }
    }

    private fun showApplicationDetails(type: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_application_details, null)

        val userId = sharedPreferences.getLong("USER_ID", -1L)
        val applications = dbHelper.getUserApplications(userId)
        val filteredApps = applications.filter { it.type == type }

        // Group by status but display "Pending" instead of "Submitted"
        val statusCount = mutableMapOf<String, Int>()
        filteredApps.forEach { app ->
            val displayStatus = if (app.status.equals("Submitted", ignoreCase = true)) "Pending" else app.status
            statusCount[displayStatus] = statusCount.getOrDefault(displayStatus, 0) + 1
        }

        dialogView.findViewById<TextView>(R.id.tvDialogTitle).text =
            when (type) {
                "registration" -> "ID Registration Statistics"
                "renewal" -> "ID Renewal Statistics"
                "replacement" -> "ID Replacement Statistics"
                else -> "Application Statistics"
            }

        dialogView.findViewById<TextView>(R.id.tvTotalCount).text = filteredApps.size.toString()

        val statusLayout = dialogView.findViewById<LinearLayout>(R.id.layoutStatusBreakdown)
        statusLayout.removeAllViews()

        statusCount.forEach { (displayStatus, count) ->
            val statusItem = layoutInflater.inflate(R.layout.item_status_breakdown, null)

            val color = getStatusColor(displayStatus)
            statusItem.findViewById<View>(R.id.viewStatusColor).setBackgroundColor(color)
            statusItem.findViewById<TextView>(R.id.tvStatusName).text = displayStatus
            statusItem.findViewById<TextView>(R.id.tvStatusCount).text = "$count applications"
            statusItem.findViewById<TextView>(R.id.tvPercentage).text =
                "${((count.toFloat() / filteredApps.size) * 100).roundToInt()}%"

            statusLayout.addView(statusItem)
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showStatusDetails(type: String, status: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_status_details, null)

        val userId = sharedPreferences.getLong("USER_ID", -1L)
        val applications = dbHelper.getUserApplications(userId)
        val filteredApps = applications.filter {
            it.type == type && it.status.equals(status, ignoreCase = true)
        }

        // Display "Pending" instead of "Submitted"
        val displayStatus = if (status.equals("Submitted", ignoreCase = true)) "Pending" else status

        dialogView.findViewById<TextView>(R.id.tvStatusTitle).text = "$displayStatus Applications"
        dialogView.findViewById<TextView>(R.id.tvCount).text = "${filteredApps.size} applications"

        val trackingList = dialogView.findViewById<LinearLayout>(R.id.layoutTrackingList)
        trackingList.removeAllViews()

        if (filteredApps.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = "No applications in this status"
                setTextColor(Color.GRAY)
                gravity = android.view.Gravity.CENTER
                setPadding(0, 16, 0, 16)
            }
            trackingList.addView(emptyView)
        } else {
            filteredApps.forEach { app ->
                val trackingItem = TextView(this).apply {
                    text = "• ${app.trackingNumber}"
                    textSize = 14f
                    setTextColor(Color.parseColor("#333333"))
                    setPadding(8, 8, 8, 8)
                }
                trackingList.addView(trackingItem)
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showApplicationTrackingInfo(trackingNumber: String, status: String) {
        AlertDialog.Builder(this)
            .setTitle("Application Details")
            .setMessage("Tracking Number: $trackingNumber\n\nStatus: $status\n\nUse this tracking number to check your application status at any NIRA office.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAllApplications() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_all_applications, null)

        val userId = sharedPreferences.getLong("USER_ID", -1L)
        val applications = dbHelper.getUserApplications(userId)

        val trackingList = dialogView.findViewById<LinearLayout>(R.id.layoutAllTrackingList)
        trackingList.removeAllViews()

        if (applications.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = "No applications found"
                setTextColor(Color.GRAY)
                gravity = android.view.Gravity.CENTER
                setPadding(0, 32, 0, 32)
            }
            trackingList.addView(emptyView)
        } else {
            applications.forEach { app ->
                val appItem = layoutInflater.inflate(R.layout.item_application_tracking, null)

                appItem.findViewById<TextView>(R.id.tvTrackingNumber).text = app.trackingNumber
                appItem.findViewById<TextView>(R.id.tvAppType).text =
                    when (app.type) {
                        "registration" -> "Registration"
                        "renewal" -> "Renewal"
                        "replacement" -> "Replacement"
                        else -> app.type
                    }

                // Display "Pending" instead of "Submitted"
                val displayStatus = if (app.status.equals("Submitted", ignoreCase = true)) "Pending" else app.status
                appItem.findViewById<TextView>(R.id.tvStatus).text = displayStatus
                appItem.findViewById<TextView>(R.id.tvStatus).setTextColor(getStatusColor(displayStatus))

                // Format date
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val date = inputFormat.parse(app.submittedAt)
                    val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    appItem.findViewById<TextView>(R.id.tvDate).text = outputFormat.format(date ?: Date())
                } catch (e: Exception) {
                    appItem.findViewById<TextView>(R.id.tvDate).text = app.submittedAt
                }

                appItem.setOnClickListener {
                    showApplicationTrackingInfo(app.trackingNumber, displayStatus)
                }

                trackingList.addView(appItem)
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("All Applications")
            .setPositiveButton("Close", null)
            .show()
    }

    private fun showHamburgerMenu() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_hamburger_menu, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Get window and set background to transparent
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setDimAmount(0.3f)

        // Apply dropdown toggle
        val applyDropdown = dialogView.findViewById<LinearLayout>(R.id.applyDropdown)
        val applyArrow = dialogView.findViewById<ImageView>(R.id.ivApplyArrow)

        dialogView.findViewById<View>(R.id.cardApply).setOnClickListener {
            if (applyDropdown.visibility == View.VISIBLE) {
                applyDropdown.visibility = View.GONE
                applyArrow.setImageResource(R.drawable.ic_arrow_down)
            } else {
                applyDropdown.visibility = View.VISIBLE
                applyArrow.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        // Menu item click listeners
        dialogView.findViewById<View>(R.id.cardHome).setOnClickListener {
            // Already on home, just dismiss
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.cardIdRegistration).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, IdRegistrationActivity::class.java))
        }

        dialogView.findViewById<View>(R.id.cardIdRenewal).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, IdRenewalActivity::class.java))
        }

        dialogView.findViewById<View>(R.id.cardIdReplacement).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, IdReplacementActivity::class.java))
        }

        dialogView.findViewById<View>(R.id.cardStatus).setOnClickListener {
            dialog.dismiss()
            // Navigate to StatusActivity
            startActivity(Intent(this, StatusActivity::class.java))
        }

        dialogView.findViewById<View>(R.id.cardSupport).setOnClickListener {
            dialog.dismiss()
            // Navigate to SupportActivity
            startActivity(Intent(this, SupportActivity::class.java))
        }

        dialogView.findViewById<View>(R.id.cardAbout).setOnClickListener {
            dialog.dismiss()
            // Navigate to AboutActivity
            startActivity(Intent(this, AboutActivity::class.java))
        }

        dialogView.findViewById<View>(R.id.cardLogout).setOnClickListener {
            dialog.dismiss()
            confirmLogout()
        }

        dialog.show()
    }

    private fun showProfilePopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_profile_popup, popupMenu.menu)

        // Get user info
        val userName = sharedPreferences.getString("FULL_NAME", "User") ?: "User"
        val userEmail = sharedPreferences.getString("EMAIL", "user@example.com") ?: "user@example.com"

        // Create and setup custom header view
        val headerView = layoutInflater.inflate(R.layout.menu_profile_header, null)
        headerView.findViewById<TextView>(R.id.tvUserName).text = userName
        headerView.findViewById<TextView>(R.id.tvUserEmail).text = userEmail

        // Set profile image if exists
        val profileImageUri = sharedPreferences.getString("PROFILE_IMAGE", null)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.ivProfileHeader)
        if (profileImageUri != null) {
            profileImageView.setImageURI(Uri.parse(profileImageUri))
        }

        // Set the custom header
        popupMenu.menu.findItem(R.id.menu_header).actionView = headerView

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_change_photo -> {
                    showImageSourceDialog()
                    true
                }
                R.id.menu_switch_account -> {
                    showSwitchAccountDialog()
                    true
                }
                R.id.menu_logout -> {
                    confirmLogout()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        AlertDialog.Builder(this)
            .setTitle("Choose Profile Picture")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCameraWithPermissionCheck()
                    1 -> openGalleryWithPermissionCheck()
                    2 -> { /* Cancel */ }
                }
            }
            .show()
    }

    private fun openCameraWithPermissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showPermissionRationaleDialog("Camera", "This app needs camera permission to take profile photos.") {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA_PERMISSION
                    )
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    private fun openGalleryWithPermissionCheck() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission)
            == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showPermissionRationaleDialog("Gallery", "This app needs storage permission to access your gallery for profile photos.") {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(permission),
                        REQUEST_GALLERY_PERMISSION
                    )
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    REQUEST_GALLERY_PERMISSION
                )
            }
        }
    }

    private fun showPermissionRationaleDialog(permissionName: String, message: String, onGrant: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("$message\n\nPlease grant $permissionName permission to continue.")
            .setPositiveButton("Grant") { _, _ -> onGrant() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPermissionSettingsDialog(permissionName: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("$permissionName permission was denied. Please enable it in app settings to upload profile photos.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, REQUEST_SETTINGS)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    showPermissionSettingsDialog("Gallery")
                }
            }
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    showPermissionSettingsDialog("Camera")
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create the directory if it doesn't exist
        storageDir?.mkdirs()

        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA)
                }
            } ?: run {
                Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    // Handle gallery image
                    data?.data?.let { uri ->
                        selectedImageUri = uri
                        setProfileImage(uri)
                    }
                }
                REQUEST_CAMERA -> {
                    // Handle camera image
                    currentPhotoPath?.let { path ->
                        val file = File(path)
                        if (file.exists()) {
                            val uri = FileProvider.getUriForFile(
                                this,
                                "${packageName}.provider",
                                file
                            )
                            selectedImageUri = uri
                            setProfileImage(uri)
                        } else {
                            Toast.makeText(this, "Photo file not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                REQUEST_SETTINGS -> {
                    // User returned from settings, check permissions again
                    Toast.makeText(this, "Please try again after granting permissions", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setProfileImage(uri: Uri) {
        try {
            // Set the image to profile icon
            btnProfile.setImageURI(uri)

            // Save the URI for future use
            val editor = sharedPreferences.edit()
            editor.putString("PROFILE_IMAGE", uri.toString())
            editor.apply()

            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSwitchAccountDialog() {
        AlertDialog.Builder(this)
            .setTitle("Switch Account")
            .setMessage("Do you want to switch to another account?")
            .setPositiveButton("Switch") { _, _ ->
                switchAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun switchAccount() {
        // Clear current user data
        val editor = sharedPreferences.edit()
        editor.remove("USER_ID")
        editor.remove("FULL_NAME")
        editor.remove("EMAIL")
        editor.remove("PROFILE_IMAGE")
        editor.apply()

        // Navigate back to login
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        // Clear user session
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Navigate to login
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        loadDashboardData()
    }
}