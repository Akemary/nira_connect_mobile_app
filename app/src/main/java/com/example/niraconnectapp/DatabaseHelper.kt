package com.example.niraconnectapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "NiraConnect.db"
        private const val DATABASE_VERSION = 4  // Incremented to force database recreation with new data

        // User Table
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_FULL_NAME = "full_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_CREATED_AT = "created_at"

        // ID Registration Applications Table
        const val TABLE_ID_REGISTRATIONS = "id_registrations"
        const val COLUMN_REGISTRATION_ID = "registration_id"
        const val COLUMN_DATE_OF_BIRTH = "date_of_birth"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_MARITAL_STATUS = "marital_status"
        const val COLUMN_PLACE_OF_BIRTH = "place_of_birth"
        const val COLUMN_NATIONALITY = "nationality"
        const val COLUMN_DISTRICT_OF_BIRTH = "district_of_birth"
        const val COLUMN_FATHERS_NAME = "fathers_name"
        const val COLUMN_FATHERS_NIN = "fathers_nin"
        const val COLUMN_MOTHERS_NAME = "mothers_name"
        const val COLUMN_MOTHERS_NIN = "mothers_nin"
        const val COLUMN_GUARDIAN_NAME = "guardian_name"
        const val COLUMN_GUARDIAN_NIN = "guardian_nin"
        const val COLUMN_GUARDIAN_RELATIONSHIP = "guardian_relationship"
        const val COLUMN_EDUCATION_LEVEL = "education_level"
        const val COLUMN_OCCUPATION = "occupation"
        const val COLUMN_CURRENT_ADDRESS = "current_address"
        const val COLUMN_CURRENT_DISTRICT = "current_district"
        const val COLUMN_PERMANENT_ADDRESS = "permanent_address"
        const val COLUMN_PERMANENT_DISTRICT = "permanent_district"
        const val COLUMN_PHOTO_DATA = "photo_data"
        const val COLUMN_TRACKING_NUMBER = "tracking_number"
        const val COLUMN_STATUS = "status"
        const val COLUMN_SUBMITTED_AT = "submitted_at"

        // ID Renewal Applications Table
        const val TABLE_ID_RENEWALS = "id_renewals"
        const val COLUMN_RENEWAL_ID = "renewal_id"
        const val COLUMN_CURRENT_NIN = "current_nin"
        const val COLUMN_CURRENT_ID_NUMBER = "current_id_number"
        const val COLUMN_DATE_OF_ISSUE = "date_of_issue"
        const val COLUMN_DATE_OF_EXPIRY = "date_of_expiry"
        const val COLUMN_RENEWAL_REASON = "renewal_reason"

        // ID Replacement Applications Table
        const val TABLE_ID_REPLACEMENTS = "id_replacements"
        const val COLUMN_REPLACEMENT_ID = "replacement_id"
        const val COLUMN_REPLACEMENT_REASON = "replacement_reason"
        const val COLUMN_POLICE_REPORT_NUMBER = "police_report_number"
        const val COLUMN_INCIDENT_DATE = "incident_date"
        const val COLUMN_INCIDENT_DESCRIPTION = "incident_description"

        // Support Requests Table
        const val TABLE_SUPPORT_REQUESTS = "support_requests"
        const val COLUMN_SUPPORT_ID = "support_id"
        const val COLUMN_ISSUE_TYPE = "issue_type"
        const val COLUMN_SUBJECT = "subject"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_REFERENCE_ID = "reference_id"
        const val COLUMN_SUPPORT_STATUS = "support_status"

        // Application Status Table
        const val TABLE_APPLICATION_STATUS = "application_status"
        const val COLUMN_STATUS_ID = "status_id"
        const val COLUMN_APPLICATION_TYPE = "application_type"
        const val COLUMN_APPLICATION_ID = "application_id"
        const val COLUMN_STATUS_TITLE = "status_title"
        const val COLUMN_STATUS_DESCRIPTION = "status_description"
        const val COLUMN_PROGRESS_STEP = "progress_step"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users Table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FULL_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        // Create ID Registrations Table
        val createIdRegistrationsTable = """
            CREATE TABLE $TABLE_ID_REGISTRATIONS (
                $COLUMN_REGISTRATION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_FULL_NAME TEXT NOT NULL,
                $COLUMN_DATE_OF_BIRTH TEXT NOT NULL,
                $COLUMN_GENDER TEXT NOT NULL,
                $COLUMN_MARITAL_STATUS TEXT,
                $COLUMN_PLACE_OF_BIRTH TEXT,
                $COLUMN_NATIONALITY TEXT NOT NULL,
                $COLUMN_DISTRICT_OF_BIRTH TEXT,
                $COLUMN_FATHERS_NAME TEXT,
                $COLUMN_FATHERS_NIN TEXT,
                $COLUMN_MOTHERS_NAME TEXT,
                $COLUMN_MOTHERS_NIN TEXT,
                $COLUMN_GUARDIAN_NAME TEXT,
                $COLUMN_GUARDIAN_NIN TEXT,
                $COLUMN_GUARDIAN_RELATIONSHIP TEXT,
                $COLUMN_EDUCATION_LEVEL TEXT,
                $COLUMN_OCCUPATION TEXT,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_EMAIL TEXT,
                $COLUMN_CURRENT_ADDRESS TEXT NOT NULL,
                $COLUMN_CURRENT_DISTRICT TEXT NOT NULL,
                $COLUMN_PERMANENT_ADDRESS TEXT,
                $COLUMN_PERMANENT_DISTRICT TEXT,
                $COLUMN_PHOTO_DATA BLOB,
                $COLUMN_TRACKING_NUMBER TEXT UNIQUE,
                $COLUMN_STATUS TEXT DEFAULT 'Submitted',
                $COLUMN_SUBMITTED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Create ID Renewals Table
        val createIdRenewalsTable = """
            CREATE TABLE $TABLE_ID_RENEWALS (
                $COLUMN_RENEWAL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_FULL_NAME TEXT NOT NULL,
                $COLUMN_DATE_OF_BIRTH TEXT NOT NULL,
                $COLUMN_CURRENT_NIN TEXT NOT NULL,
                $COLUMN_CURRENT_ID_NUMBER TEXT NOT NULL,
                $COLUMN_DATE_OF_ISSUE TEXT,
                $COLUMN_DATE_OF_EXPIRY TEXT NOT NULL,
                $COLUMN_RENEWAL_REASON TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_EMAIL TEXT,
                $COLUMN_CURRENT_ADDRESS TEXT NOT NULL,
                $COLUMN_CURRENT_DISTRICT TEXT NOT NULL,
                $COLUMN_PHOTO_DATA BLOB,
                $COLUMN_TRACKING_NUMBER TEXT UNIQUE,
                $COLUMN_STATUS TEXT DEFAULT 'Submitted',
                $COLUMN_SUBMITTED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Create ID Replacements Table
        val createIdReplacementsTable = """
            CREATE TABLE $TABLE_ID_REPLACEMENTS (
                $COLUMN_REPLACEMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_FULL_NAME TEXT NOT NULL,
                $COLUMN_DATE_OF_BIRTH TEXT NOT NULL,
                $COLUMN_CURRENT_NIN TEXT NOT NULL,
                $COLUMN_CURRENT_ID_NUMBER TEXT NOT NULL,
                $COLUMN_DATE_OF_ISSUE TEXT,
                $COLUMN_DATE_OF_EXPIRY TEXT,
                $COLUMN_REPLACEMENT_REASON TEXT NOT NULL,
                $COLUMN_POLICE_REPORT_NUMBER TEXT,
                $COLUMN_INCIDENT_DATE TEXT NOT NULL,
                $COLUMN_INCIDENT_DESCRIPTION TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_EMAIL TEXT,
                $COLUMN_CURRENT_ADDRESS TEXT NOT NULL,
                $COLUMN_CURRENT_DISTRICT TEXT NOT NULL,
                $COLUMN_PHOTO_DATA BLOB,
                $COLUMN_TRACKING_NUMBER TEXT UNIQUE,
                $COLUMN_STATUS TEXT DEFAULT 'Submitted',
                $COLUMN_SUBMITTED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Create Support Requests Table
        val createSupportRequestsTable = """
            CREATE TABLE $TABLE_SUPPORT_REQUESTS (
                $COLUMN_SUPPORT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_FULL_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_ISSUE_TYPE TEXT NOT NULL,
                $COLUMN_SUBJECT TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_REFERENCE_ID TEXT UNIQUE,
                $COLUMN_SUPPORT_STATUS TEXT DEFAULT 'Open',
                $COLUMN_SUBMITTED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Create Application Status Table
        val createApplicationStatusTable = """
            CREATE TABLE $TABLE_APPLICATION_STATUS (
                $COLUMN_STATUS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_APPLICATION_TYPE TEXT NOT NULL, -- 'registration', 'renewal', 'replacement'
                $COLUMN_APPLICATION_ID INTEGER NOT NULL,
                $COLUMN_STATUS_TITLE TEXT NOT NULL,
                $COLUMN_STATUS_DESCRIPTION TEXT NOT NULL,
                $COLUMN_PROGRESS_STEP INTEGER NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        // Execute all table creation statements
        db.execSQL(createUsersTable)
        db.execSQL(createIdRegistrationsTable)
        db.execSQL(createIdRenewalsTable)
        db.execSQL(createIdReplacementsTable)
        db.execSQL(createSupportRequestsTable)
        db.execSQL(createApplicationStatusTable)

        // Pre-populate with sample data
        prePopulateData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATION_STATUS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUPPORT_REQUESTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ID_REPLACEMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ID_RENEWALS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ID_REGISTRATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // User Management Methods
    fun addUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FULL_NAME, user.fullName)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_PHONE_NUMBER, user.phoneNumber)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(username: String, password: String): User? {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)

        val cursor = db.query(
            TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
            )
        } else {
            null
        }.also { cursor.close() }
    }

    fun isUsernameTaken(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun isEmailTaken(email: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ID Registration Methods
    fun addIdRegistration(registration: IdRegistration): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, registration.userId)
            put(COLUMN_FULL_NAME, registration.fullName)
            put(COLUMN_DATE_OF_BIRTH, registration.dateOfBirth)
            put(COLUMN_GENDER, registration.gender)
            put(COLUMN_MARITAL_STATUS, registration.maritalStatus)
            put(COLUMN_PLACE_OF_BIRTH, registration.placeOfBirth)
            put(COLUMN_NATIONALITY, registration.nationality)
            put(COLUMN_DISTRICT_OF_BIRTH, registration.districtOfBirth)
            put(COLUMN_FATHERS_NAME, registration.fathersName)
            put(COLUMN_FATHERS_NIN, registration.fathersNin)
            put(COLUMN_MOTHERS_NAME, registration.mothersName)
            put(COLUMN_MOTHERS_NIN, registration.mothersNin)
            put(COLUMN_GUARDIAN_NAME, registration.guardianName)
            put(COLUMN_GUARDIAN_NIN, registration.guardianNin)
            put(COLUMN_GUARDIAN_RELATIONSHIP, registration.guardianRelationship)
            put(COLUMN_EDUCATION_LEVEL, registration.educationLevel)
            put(COLUMN_OCCUPATION, registration.occupation)
            put(COLUMN_PHONE_NUMBER, registration.phoneNumber)
            put(COLUMN_EMAIL, registration.email)
            put(COLUMN_CURRENT_ADDRESS, registration.currentAddress)
            put(COLUMN_CURRENT_DISTRICT, registration.currentDistrict)
            put(COLUMN_PERMANENT_ADDRESS, registration.permanentAddress)
            put(COLUMN_PERMANENT_DISTRICT, registration.permanentDistrict)
            put(COLUMN_PHOTO_DATA, registration.photoData)
            put(COLUMN_TRACKING_NUMBER, registration.trackingNumber)
            put(COLUMN_STATUS, registration.status)
        }
        return db.insert(TABLE_ID_REGISTRATIONS, null, values)
    }

    // ID Renewal Methods
    fun addIdRenewal(renewal: IdRenewal): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, renewal.userId)
            put(COLUMN_FULL_NAME, renewal.fullName)
            put(COLUMN_DATE_OF_BIRTH, renewal.dateOfBirth)
            put(COLUMN_CURRENT_NIN, renewal.currentNin)
            put(COLUMN_CURRENT_ID_NUMBER, renewal.currentIdNumber)
            put(COLUMN_DATE_OF_ISSUE, renewal.dateOfIssue)
            put(COLUMN_DATE_OF_EXPIRY, renewal.dateOfExpiry)
            put(COLUMN_RENEWAL_REASON, renewal.renewalReason)
            put(COLUMN_PHONE_NUMBER, renewal.phoneNumber)
            put(COLUMN_EMAIL, renewal.email)
            put(COLUMN_CURRENT_ADDRESS, renewal.currentAddress)
            put(COLUMN_CURRENT_DISTRICT, renewal.currentDistrict)
            put(COLUMN_PHOTO_DATA, renewal.photoData)
            put(COLUMN_TRACKING_NUMBER, renewal.trackingNumber)
            put(COLUMN_STATUS, renewal.status)
        }
        return db.insert(TABLE_ID_RENEWALS, null, values)
    }

    // ID Replacement Methods
    fun addIdReplacement(replacement: IdReplacement): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, replacement.userId)
            put(COLUMN_FULL_NAME, replacement.fullName)
            put(COLUMN_DATE_OF_BIRTH, replacement.dateOfBirth)
            put(COLUMN_CURRENT_NIN, replacement.currentNin)
            put(COLUMN_CURRENT_ID_NUMBER, replacement.currentIdNumber)
            put(COLUMN_DATE_OF_ISSUE, replacement.dateOfIssue)
            put(COLUMN_DATE_OF_EXPIRY, replacement.dateOfExpiry)
            put(COLUMN_REPLACEMENT_REASON, replacement.replacementReason)
            put(COLUMN_POLICE_REPORT_NUMBER, replacement.policeReportNumber)
            put(COLUMN_INCIDENT_DATE, replacement.incidentDate)
            put(COLUMN_INCIDENT_DESCRIPTION, replacement.incidentDescription)
            put(COLUMN_PHONE_NUMBER, replacement.phoneNumber)
            put(COLUMN_EMAIL, replacement.email)
            put(COLUMN_CURRENT_ADDRESS, replacement.currentAddress)
            put(COLUMN_CURRENT_DISTRICT, replacement.currentDistrict)
            put(COLUMN_PHOTO_DATA, replacement.photoData)
            put(COLUMN_TRACKING_NUMBER, replacement.trackingNumber)
            put(COLUMN_STATUS, replacement.status)
        }
        return db.insert(TABLE_ID_REPLACEMENTS, null, values)
    }

    // Support Request Methods
    fun addSupportRequest(supportRequest: SupportRequest): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, supportRequest.userId)
            put(COLUMN_FULL_NAME, supportRequest.fullName)
            put(COLUMN_EMAIL, supportRequest.email)
            put(COLUMN_PHONE_NUMBER, supportRequest.phoneNumber)
            put(COLUMN_ISSUE_TYPE, supportRequest.issueType)
            put(COLUMN_SUBJECT, supportRequest.subject)
            put(COLUMN_DESCRIPTION, supportRequest.description)
            put(COLUMN_REFERENCE_ID, supportRequest.referenceId)
            put(COLUMN_SUPPORT_STATUS, supportRequest.status)
        }
        return db.insert(TABLE_SUPPORT_REQUESTS, null, values)
    }

    // Application Status Methods
    fun getApplicationStatus(trackingNumber: String? = null, nin: String? = null): ApplicationStatus? {
        val db = readableDatabase
        var cursor: Cursor? = null

        return try {
            // Search in all application tables
            val query = """
                SELECT 'registration' as application_type, $COLUMN_REGISTRATION_ID as app_id, 
                       $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
                FROM $TABLE_ID_REGISTRATIONS 
                WHERE $COLUMN_TRACKING_NUMBER = ? OR $COLUMN_CURRENT_NIN = ?
                UNION ALL
                SELECT 'renewal' as application_type, $COLUMN_RENEWAL_ID as app_id,
                       $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
                FROM $TABLE_ID_RENEWALS 
                WHERE $COLUMN_TRACKING_NUMBER = ? OR $COLUMN_CURRENT_NIN = ?
                UNION ALL
                SELECT 'replacement' as application_type, $COLUMN_REPLACEMENT_ID as app_id,
                       $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
                FROM $TABLE_ID_REPLACEMENTS 
                WHERE $COLUMN_TRACKING_NUMBER = ? OR $COLUMN_CURRENT_NIN = ?
            """.trimIndent()

            val args = arrayOf(trackingNumber, nin, trackingNumber, nin, trackingNumber, nin)
            cursor = db.rawQuery(query, args)

            if (cursor.moveToFirst()) {
                ApplicationStatus(
                    applicationType = cursor.getString(cursor.getColumnIndexOrThrow("application_type")),
                    applicationId = cursor.getLong(cursor.getColumnIndexOrThrow("app_id")),
                    trackingNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACKING_NUMBER)),
                    status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                    submittedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMITTED_AT))
                )
            } else {
                null
            }
        } finally {
            cursor?.close()
        }
    }

    // Get user's applications
    fun getUserApplications(userId: Long): List<UserApplication> {
        val applications = mutableListOf<UserApplication>()
        val db = readableDatabase

        val query = """
            SELECT 'registration' as type, $COLUMN_REGISTRATION_ID as id, $COLUMN_FULL_NAME, 
                   $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
            FROM $TABLE_ID_REGISTRATIONS WHERE $COLUMN_USER_ID = ?
            UNION ALL
            SELECT 'renewal' as type, $COLUMN_RENEWAL_ID as id, $COLUMN_FULL_NAME,
                   $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
            FROM $TABLE_ID_RENEWALS WHERE $COLUMN_USER_ID = ?
            UNION ALL
            SELECT 'replacement' as type, $COLUMN_REPLACEMENT_ID as id, $COLUMN_FULL_NAME,
                   $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
            FROM $TABLE_ID_REPLACEMENTS WHERE $COLUMN_USER_ID = ?
            ORDER BY $COLUMN_SUBMITTED_AT DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), userId.toString(), userId.toString()))

        while (cursor.moveToNext()) {
            applications.add(
                UserApplication(
                    type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)),
                    trackingNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACKING_NUMBER)),
                    status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                    submittedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMITTED_AT))
                )
            )
        }
        cursor.close()
        return applications
    }

    // Get user's recent applications (last 20)
    fun getRecentApplications(userId: Long): List<UserApplication> {
        val applications = mutableListOf<UserApplication>()
        val db = readableDatabase

        val query = """
            SELECT 'registration' as type, $COLUMN_REGISTRATION_ID as id, $COLUMN_FULL_NAME, 
                   $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
            FROM $TABLE_ID_REGISTRATIONS WHERE $COLUMN_USER_ID = ?
            UNION ALL
            SELECT 'renewal' as type, $COLUMN_RENEWAL_ID as id, $COLUMN_FULL_NAME,
                   $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
            FROM $TABLE_ID_RENEWALS WHERE $COLUMN_USER_ID = ?
            UNION ALL
            SELECT 'replacement' as type, $COLUMN_REPLACEMENT_ID as id, $COLUMN_FULL_NAME,
                   $COLUMN_TRACKING_NUMBER, $COLUMN_STATUS, $COLUMN_SUBMITTED_AT
            FROM $TABLE_ID_REPLACEMENTS WHERE $COLUMN_USER_ID = ?
            ORDER BY $COLUMN_SUBMITTED_AT DESC
            LIMIT 20
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), userId.toString(), userId.toString()))

        while (cursor.moveToNext()) {
            applications.add(
                UserApplication(
                    type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)),
                    trackingNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACKING_NUMBER)),
                    status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                    submittedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMITTED_AT))
                )
            )
        }
        cursor.close()
        return applications
    }

    // Get application statistics for charts (whole numbers for Y-axis)
    fun getApplicationStatistics(): Map<String, Int> {
        val stats = mutableMapOf<String, Int>()
        val db = readableDatabase

        // Get total counts
        val registrationCount = getTableCount(db, TABLE_ID_REGISTRATIONS)
        val renewalCount = getTableCount(db, TABLE_ID_RENEWALS)
        val replacementCount = getTableCount(db, TABLE_ID_REPLACEMENTS)

        // Get status counts for each type - FIXED to include "Pending" as "Submitted"
        val registrationStatus = getStatusCounts(db, TABLE_ID_REGISTRATIONS)
        val renewalStatus = getStatusCounts(db, TABLE_ID_RENEWALS)
        val replacementStatus = getStatusCounts(db, TABLE_ID_REPLACEMENTS)

        // Add to stats
        stats["total_registrations"] = registrationCount
        stats["total_renewals"] = renewalCount
        stats["total_replacements"] = replacementCount
        stats["total_applications"] = registrationCount + renewalCount + replacementCount

        // IMPORTANT: Map "Submitted" to "Pending" for consistency with HomeActivity
        // HomeActivity shows "Submitted" as "Pending"
        val allStatuses = listOf("Submitted", "In Progress", "Ready", "Rejected")

        // Initialize all statuses to 0
        allStatuses.forEach { status ->
            val dbStatus = if (status == "Submitted") "Submitted" else status
            val key = "registration_${status.toLowerCase().replace(" ", "_")}"
            stats[key] = registrationStatus[dbStatus] ?: 0
        }

        allStatuses.forEach { status ->
            val dbStatus = if (status == "Submitted") "Submitted" else status
            val key = "renewal_${status.toLowerCase().replace(" ", "_")}"
            stats[key] = renewalStatus[dbStatus] ?: 0
        }

        allStatuses.forEach { status ->
            val dbStatus = if (status == "Submitted") "Submitted" else status
            val key = "replacement_${status.toLowerCase().replace(" ", "_")}"
            stats[key] = replacementStatus[dbStatus] ?: 0
        }

        // Calculate totals for each status across all application types
        allStatuses.forEach { status ->
            val dbStatus = if (status == "Submitted") "Submitted" else status
            val registrationCountForStatus = registrationStatus[dbStatus] ?: 0
            val renewalCountForStatus = renewalStatus[dbStatus] ?: 0
            val replacementCountForStatus = replacementStatus[dbStatus] ?: 0
            val totalForStatus = registrationCountForStatus + renewalCountForStatus + replacementCountForStatus

            val statusKey = "${status.toLowerCase().replace(" ", "_")}_total"
            stats[statusKey] = totalForStatus
        }

        return stats
    }

    // Get pie-chart specific data for ID Registrations
    fun getRegistrationPieChartData(): Map<String, Int> {
        val data = mutableMapOf<String, Int>()
        val db = readableDatabase

        val statusCounts = getStatusCounts(db, TABLE_ID_REGISTRATIONS)

        // Ensure all statuses are included, even if count is 0
        // Map "Submitted" to "Pending" for consistency with UI
        val allStatuses = listOf("Submitted", "In Progress", "Ready", "Rejected")
        allStatuses.forEach { status ->
            data[status] = statusCounts[status] ?: 0
        }

        return data
    }

    // Get pie-chart specific data for ID Renewals
    fun getRenewalPieChartData(): Map<String, Int> {
        val data = mutableMapOf<String, Int>()
        val db = readableDatabase

        val statusCounts = getStatusCounts(db, TABLE_ID_RENEWALS)

        // Ensure all statuses are included, even if count is 0
        val allStatuses = listOf("Submitted", "In Progress", "Ready", "Rejected")
        allStatuses.forEach { status ->
            data[status] = statusCounts[status] ?: 0
        }

        return data
    }

    // Get pie-chart specific data for ID Replacements
    fun getReplacementPieChartData(): Map<String, Int> {
        val data = mutableMapOf<String, Int>()
        val db = readableDatabase

        val statusCounts = getStatusCounts(db, TABLE_ID_REPLACEMENTS)

        // Ensure all statuses are included, even if count is 0
        val allStatuses = listOf("Submitted", "In Progress", "Ready", "Rejected")
        allStatuses.forEach { status ->
            data[status] = statusCounts[status] ?: 0
        }

        return data
    }

    private fun getTableCount(db: SQLiteDatabase, tableName: String): Int {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $tableName", null)
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }.also { cursor.close() }
    }

    private fun getStatusCounts(db: SQLiteDatabase, tableName: String): Map<String, Int> {
        val counts = mutableMapOf<String, Int>()
        val cursor = db.rawQuery(
            "SELECT $COLUMN_STATUS, COUNT(*) as count FROM $tableName GROUP BY $COLUMN_STATUS",
            null
        )

        while (cursor.moveToNext()) {
            val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
            val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
            counts[status] = count
        }
        cursor.close()

        // Ensure all statuses are in the map, even with 0 count
        val allStatuses = listOf("Submitted", "In Progress", "Ready", "Rejected")
        allStatuses.forEach { status ->
            if (!counts.containsKey(status)) {
                counts[status] = 0
            }
        }

        return counts
    }

    // Helper method to convert bitmap to byte array
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    // Helper method to convert byte array to bitmap
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // For debugging: check what data exists
    fun debugCheckData(): String {
        val db = readableDatabase
        val sb = StringBuilder()

        sb.append("=== DATABASE DEBUG INFO ===\n")
        sb.append("Database Version: $DATABASE_VERSION\n\n")

        sb.append("Table Counts:\n")
        sb.append("- Users: ${getTableCount(db, TABLE_USERS)}\n")
        sb.append("- Registrations: ${getTableCount(db, TABLE_ID_REGISTRATIONS)}\n")
        sb.append("- Renewals: ${getTableCount(db, TABLE_ID_RENEWALS)}\n")
        sb.append("- Replacements: ${getTableCount(db, TABLE_ID_REPLACEMENTS)}\n")
        sb.append("- Support Requests: ${getTableCount(db, TABLE_SUPPORT_REQUESTS)}\n")
        sb.append("- Application Status: ${getTableCount(db, TABLE_APPLICATION_STATUS)}\n\n")

        sb.append("Registration Status Breakdown:\n")
        val regStatus = getStatusCounts(db, TABLE_ID_REGISTRATIONS)
        regStatus.forEach { (status, count) ->
            sb.append("  - $status: $count\n")
        }

        sb.append("\nRenewal Status Breakdown:\n")
        val renewalStatus = getStatusCounts(db, TABLE_ID_RENEWALS)
        renewalStatus.forEach { (status, count) ->
            sb.append("  - $status: $count\n")
        }

        sb.append("\nReplacement Status Breakdown:\n")
        val replacementStatus = getStatusCounts(db, TABLE_ID_REPLACEMENTS)
        replacementStatus.forEach { (status, count) ->
            sb.append("  - $status: $count\n")
        }

        return sb.toString()
    }

    // Pre-populate database with sample data
    private fun prePopulateData(db: SQLiteDatabase) {
        // First, create a sample user
        val userValues = ContentValues().apply {
            put(COLUMN_FULL_NAME, "John Doe")
            put(COLUMN_EMAIL, "john.doe@example.com")
            put(COLUMN_USERNAME, "johndoe")
            put(COLUMN_PASSWORD, "password123")
            put(COLUMN_PHONE_NUMBER, "256700000000")
            put(COLUMN_CREATED_AT, "2024-01-01 10:00:00")
        }
        val userId = db.insert(TABLE_USERS, null, userValues)

        // Status options - "Submitted" is the pending status
        val statusOptions = listOf("Submitted", "In Progress", "Ready", "Rejected")
        val districts = listOf("Kampala", "Wakiso", "Mukono", "Jinja", "Mbale", "Gulu", "Mbarara", "Masaka", "Lira", "Hoima", "Arua", "Soroti", "Fort Portal", "Kabale", "Masindi")
        val nationalities = listOf("Ugandan", "Kenyan", "Tanzanian", "Rwandan", "Burundian", "South Sudanese", "Congolese")
        val genders = listOf("Male", "Female")
        val maritalStatuses = listOf("Single", "Married", "Divorced", "Widowed")
        val educationLevels = listOf("Primary", "Secondary", "Diploma", "Bachelor", "Master", "PhD")
        val occupations = listOf("Student", "Teacher", "Engineer", "Doctor", "Farmer", "Business", "Government", "Unemployed", "Driver", "Nurse", "Accountant", "Lawyer")
        val firstNames = listOf("John", "Mary", "David", "Sarah", "James", "Grace", "Peter", "Anna", "Michael", "Ruth", "Joseph", "Esther", "Daniel", "Joyce", "Paul")
        val lastNames = listOf("Doe", "Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Wilson", "Anderson", "Thomas", "Taylor", "Moore")

        // Generate 100 ID Registration applications with different statuses
        // 5 will be "Submitted" (Pending) status as requested
        for (i in 1..100) {
            val firstName = firstNames[i % firstNames.size]
            val lastName = lastNames[(i + 3) % lastNames.size]
            val fullName = "$firstName $lastName"

            // Assign status: first 5 are Submitted (Pending), then distribute others
            // Only 15 rejected total across all applications
            val status = when {
                i <= 5 -> "Submitted" // Exactly 5 pending applications as requested
                i <= 50 -> "In Progress" // 45 in progress
                i <= 90 -> "Ready" // 40 ready
                else -> "Rejected" // Only 10 rejected (not 15, we'll add more in other types)
            }

            val regValues = ContentValues().apply {
                put(COLUMN_USER_ID, userId)
                put(COLUMN_FULL_NAME, fullName)
                put(COLUMN_DATE_OF_BIRTH, "${1980 + i % 40}-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_GENDER, genders[i % genders.size])
                put(COLUMN_MARITAL_STATUS, maritalStatuses[i % maritalStatuses.size])
                put(COLUMN_PLACE_OF_BIRTH, "Hospital ${(i % 15) + 1}")
                put(COLUMN_NATIONALITY, nationalities[i % nationalities.size])
                put(COLUMN_DISTRICT_OF_BIRTH, districts[i % districts.size])
                put(COLUMN_FATHERS_NAME, "Father ${firstName} ${lastNames[(i + 1) % lastNames.size]}")
                put(COLUMN_FATHERS_NIN, "NIN-F${String.format("%08d", i)}")
                put(COLUMN_MOTHERS_NAME, "Mother ${firstNames[(i + 1) % firstNames.size]} ${lastNames[(i + 2) % lastNames.size]}")
                put(COLUMN_MOTHERS_NIN, "NIN-M${String.format("%08d", i)}")
                put(COLUMN_GUARDIAN_NAME, "Guardian ${firstNames[(i + 2) % firstNames.size]} ${lastNames[(i + 3) % lastNames.size]}")
                put(COLUMN_GUARDIAN_NIN, "NIN-G${String.format("%08d", i)}")
                put(COLUMN_GUARDIAN_RELATIONSHIP, if (i % 3 == 0) "Parent" else if (i % 3 == 1) "Sibling" else "Uncle/Aunt")
                put(COLUMN_EDUCATION_LEVEL, educationLevels[i % educationLevels.size])
                put(COLUMN_OCCUPATION, occupations[i % occupations.size])
                put(COLUMN_PHONE_NUMBER, "2567${String.format("%08d", 10000000 + i)}")
                put(COLUMN_EMAIL, "${firstName.toLowerCase()}.${lastName.toLowerCase()}${i}@example.com")
                put(COLUMN_CURRENT_ADDRESS, "Plot ${i}, Street ${(i % 20) + 1}, ${districts[(i + 1) % districts.size]}")
                put(COLUMN_CURRENT_DISTRICT, districts[(i + 1) % districts.size])
                put(COLUMN_PERMANENT_ADDRESS, "Village ${(i % 30) + 1}, Parish ${(i % 10) + 1}, ${districts[(i + 2) % districts.size]}")
                put(COLUMN_PERMANENT_DISTRICT, districts[(i + 2) % districts.size])
                // Photo data would be null in sample data
                put(COLUMN_TRACKING_NUMBER, "REG-${String.format("%07d", i)}")
                put(COLUMN_STATUS, status)
                // Stagger submission dates over the past year
                val month = (i % 12) + 1
                val day = (i % 28) + 1
                val hour = (i % 24)
                put(COLUMN_SUBMITTED_AT, "2024-${String.format("%02d", month)}-${String.format("%02d", day)} ${String.format("%02d", hour)}:${String.format("%02d", i % 60)}:00")
            }
            db.insert(TABLE_ID_REGISTRATIONS, null, regValues)
        }

        // Generate 60 ID Renewal applications with different statuses
        // 5 will be Submitted (Pending) as requested
        for (i in 1..60) {
            val firstName = firstNames[(i + 2) % firstNames.size]
            val lastName = lastNames[(i + 5) % lastNames.size]
            val fullName = "$firstName $lastName"

            // Assign status: first 5 are Submitted, distribute others, only 5 rejected
            val status = when {
                i <= 5 -> "Submitted" // 5 pending for renewals
                i <= 40 -> "In Progress" // 35 in progress
                i <= 55 -> "Ready" // 15 ready
                else -> "Rejected" // Only 5 rejected
            }

            val renewalValues = ContentValues().apply {
                put(COLUMN_USER_ID, userId)
                put(COLUMN_FULL_NAME, fullName)
                put(COLUMN_DATE_OF_BIRTH, "${1975 + i % 45}-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_CURRENT_NIN, "NIN-${String.format("%09d", 100000000 + i)}")
                put(COLUMN_CURRENT_ID_NUMBER, "CM${String.format("%08d", 80000000 + i)}")
                put(COLUMN_DATE_OF_ISSUE, "2020-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_DATE_OF_EXPIRY, "2024-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_RENEWAL_REASON, when (i % 4) {
                    0 -> "Expired"
                    1 -> "Damaged"
                    2 -> "Change of Personal Details"
                    else -> "Upgrade to Smart ID"
                })
                put(COLUMN_PHONE_NUMBER, "2567${String.format("%08d", 20000000 + i)}")
                put(COLUMN_EMAIL, "renewal.${firstName.toLowerCase()}${i}@example.com")
                put(COLUMN_CURRENT_ADDRESS, "House ${i}, Road ${(i % 25) + 1}, ${districts[(i + 3) % districts.size]}")
                put(COLUMN_CURRENT_DISTRICT, districts[(i + 3) % districts.size])
                put(COLUMN_TRACKING_NUMBER, "REN-${String.format("%07d", i)}")
                put(COLUMN_STATUS, status)
                // Stagger submission dates
                val month = (i % 12) + 1
                val day = ((i * 2) % 28) + 1
                val hour = ((i + 3) % 24)
                put(COLUMN_SUBMITTED_AT, "2024-${String.format("%02d", month)}-${String.format("%02d", day)} ${String.format("%02d", hour)}:${String.format("%02d", (i * 3) % 60)}:00")
            }
            db.insert(TABLE_ID_RENEWALS, null, renewalValues)
        }

        // Generate 25 ID Replacement applications with different statuses
        // 5 will be Submitted (Pending) as requested
        for (i in 1..25) {
            val firstName = firstNames[(i + 4) % firstNames.size]
            val lastName = lastNames[(i + 7) % lastNames.size]
            val fullName = "$firstName $lastName"

            // Assign status: 5 Submitted, distribute others, NO rejected for replacements
            val status = when {
                i <= 5 -> "Submitted" // 5 pending for replacements
                i <= 15 -> "In Progress" // 10 in progress
                else -> "Ready" // 10 ready, NO rejected for replacements
            }

            val replacementValues = ContentValues().apply {
                put(COLUMN_USER_ID, userId)
                put(COLUMN_FULL_NAME, fullName)
                put(COLUMN_DATE_OF_BIRTH, "${1985 + i % 35}-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_CURRENT_NIN, "NIN-R${String.format("%08d", 200000000 + i)}")
                put(COLUMN_CURRENT_ID_NUMBER, "ID-R${String.format("%07d", 9000000 + i)}")
                put(COLUMN_DATE_OF_ISSUE, "2022-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_DATE_OF_EXPIRY, "2027-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_REPLACEMENT_REASON, when (i % 5) {
                    0 -> "Lost"
                    1 -> "Stolen"
                    2 -> "Damaged"
                    3 -> "Destroyed in fire"
                    else -> "Mutilated"
                })
                put(COLUMN_POLICE_REPORT_NUMBER, if (i % 3 == 0) "PR-${String.format("%06d", 100000 + i)}" else null)
                put(COLUMN_INCIDENT_DATE, "2024-${String.format("%02d", ((i + 2) % 12) + 1)}-${String.format("%02d", (i % 28) + 1)}")
                put(COLUMN_INCIDENT_DESCRIPTION, "ID card was ${when (i % 5) {
                    0 -> "lost"
                    1 -> "stolen"
                    2 -> "damaged by water"
                    3 -> "destroyed in a house fire"
                    else -> "mutilated accidentally"
                }} in ${districts[i % districts.size]} district")
                put(COLUMN_PHONE_NUMBER, "2567${String.format("%08d", 30000000 + i)}")
                put(COLUMN_EMAIL, "replacement.${firstName.toLowerCase()}${i}@example.com")
                put(COLUMN_CURRENT_ADDRESS, "Apartment ${i}, Block ${(i % 10) + 1}, ${districts[(i + 4) % districts.size]}")
                put(COLUMN_CURRENT_DISTRICT, districts[(i + 4) % districts.size])
                put(COLUMN_TRACKING_NUMBER, "REP-${String.format("%07d", i)}")
                put(COLUMN_STATUS, status)
                // Stagger submission dates
                val month = (i % 12) + 1
                val day = ((i * 3) % 28) + 1
                val hour = ((i + 6) % 24)
                put(COLUMN_SUBMITTED_AT, "2024-${String.format("%02d", month)}-${String.format("%02d", day)} ${String.format("%02d", hour)}:${String.format("%02d", (i * 5) % 60)}:00")
            }
            db.insert(TABLE_ID_REPLACEMENTS, null, replacementValues)
        }

        // Add some support requests
        for (i in 1..8) {
            val supportValues = ContentValues().apply {
                put(COLUMN_USER_ID, userId)
                put(COLUMN_FULL_NAME, "John Doe")
                put(COLUMN_EMAIL, "john.doe@example.com")
                put(COLUMN_PHONE_NUMBER, "256700000000")
                put(COLUMN_ISSUE_TYPE, when (i % 4) {
                    0 -> "Technical"
                    1 -> "Application Status"
                    2 -> "Documentation"
                    else -> "General Inquiry"
                })
                put(COLUMN_SUBJECT, when (i % 4) {
                    0 -> "Website not loading properly"
                    1 -> "Application #REG-${String.format("%07d", i * 10)} status update"
                    2 -> "Required documents clarification"
                    else -> "General information request"
                })
                put(COLUMN_DESCRIPTION, "This is support request #$i regarding ${when (i % 4) {
                    0 -> "technical issues with the website"
                    1 -> "the status of my application"
                    2 -> "required documentation for ID registration"
                    else -> "general information about NIRA services"
                }}. Please assist as soon as possible.")
                put(COLUMN_REFERENCE_ID, "SUP-${String.format("%06d", 100000 + i)}")
                put(COLUMN_SUPPORT_STATUS, if (i % 3 == 0) "Open" else if (i % 3 == 1) "In Progress" else "Closed")
                put(COLUMN_SUBMITTED_AT, "2024-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", (i % 28) + 1)} ${String.format("%02d", (i + 9) % 24)}:${String.format("%02d", (i * 7) % 60)}:00")
            }
            db.insert(TABLE_SUPPORT_REQUESTS, null, supportValues)
        }

        // Add application status tracking entries for all applications
        for (appType in listOf("registration", "renewal", "replacement")) {
            val maxId = when (appType) {
                "registration" -> 100
                "renewal" -> 60
                else -> 25
            }

            for (i in 1..maxId) {
                val appId = i

                val statusEntries = listOf(
                    Triple("Application Submitted", "Your application has been submitted successfully and is awaiting initial review", 1),
                    Triple("Under Review", "Application is being reviewed by NIRA officials for completeness and accuracy", 2),
                    Triple("Processing", "Your ID card is being processed at the production center", 3),
                    Triple("Quality Check", "ID card is undergoing quality assurance checks", 4),
                    Triple("Ready for Collection", "Your ID card is ready for collection at your designated center", 5),
                    Triple("Collected", "ID card has been collected by applicant", 6),
                    Triple("Rejected", "Application was rejected - please check the reason and resubmit", 7)
                )

                // Only add status entries up to the current status
                val maxStep = when (appType) {
                    "registration" -> when {
                        i <= 5 -> 1  // Submitted/Pending (5 applications)
                        i <= 50 -> 3 // In Progress
                        i <= 90 -> 5 // Ready
                        else -> 7    // Rejected (only 10 applications)
                    }
                    "renewal" -> when {
                        i <= 5 -> 1  // Submitted/Pending (5 applications)
                        i <= 40 -> 3 // In Progress
                        i <= 55 -> 5 // Ready
                        else -> 7    // Rejected (only 5 applications)
                    }
                    else -> when {   // replacement
                        i <= 5 -> 1  // Submitted/Pending (5 applications)
                        i <= 15 -> 3 // In Progress
                        else -> 5    // Ready (NO rejected for replacements)
                    }
                }

                for ((index, status) in statusEntries.withIndex()) {
                    val (title, description, step) = status
                    if (step <= maxStep) {
                        val statusValues = ContentValues().apply {
                            put(COLUMN_APPLICATION_TYPE, appType)
                            put(COLUMN_APPLICATION_ID, appId)
                            put(COLUMN_STATUS_TITLE, title)
                            put(COLUMN_STATUS_DESCRIPTION, description)
                            put(COLUMN_PROGRESS_STEP, step)
                            // Stagger creation dates
                            val month = (i % 12) + 1
                            val day = (i % 28) + 1
                            val hour = (step * 2 + index) % 24
                            put(COLUMN_CREATED_AT, "2024-${String.format("%02d", month)}-${String.format("%02d", day)} ${String.format("%02d", hour)}:${String.format("%02d", (step * 10) % 60)}:00")
                        }
                        db.insert(TABLE_APPLICATION_STATUS, null, statusValues)
                    }
                }
            }
        }

        // Create additional users for testing
        val additionalUsers = listOf(
            mapOf("name" to "Jane Smith", "email" to "jane.smith@example.com", "username" to "janesmith", "phone" to "256711111111"),
            mapOf("name" to "Robert Johnson", "email" to "robert.j@example.com", "username" to "robertj", "phone" to "256722222222"),
            mapOf("name" to "Emily Davis", "email" to "emily.davis@example.com", "username" to "emilyd", "phone" to "256733333333"),
            mapOf("name" to "Michael Brown", "email" to "michael.b@example.com", "username" to "michaelb", "phone" to "256744444444"),
            mapOf("name" to "Sarah Wilson", "email" to "sarah.w@example.com", "username" to "sarahw", "phone" to "256755555555")
        )

        for (user in additionalUsers) {
            val userValues = ContentValues().apply {
                put(COLUMN_FULL_NAME, user["name"])
                put(COLUMN_EMAIL, user["email"])
                put(COLUMN_USERNAME, user["username"])
                put(COLUMN_PASSWORD, "password123")
                put(COLUMN_PHONE_NUMBER, user["phone"])
                put(COLUMN_CREATED_AT, "2024-01-${String.format("%02d", (additionalUsers.indexOf(user) + 1) * 5)} 14:00:00")
            }
            db.insert(TABLE_USERS, null, userValues)
        }
    }
}