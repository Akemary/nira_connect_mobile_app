package com.example.niraconnectapp

// User Model
data class User(
    val id: Long = 0,
    val fullName: String,
    val email: String,
    val username: String,
    val password: String,
    val phoneNumber: String? = null,
    val createdAt: String = ""
)

// ID Registration Model
data class IdRegistration(
    val registrationId: Long = 0,
    val userId: Long,
    val fullName: String,
    val dateOfBirth: String,
    val gender: String,
    val maritalStatus: String? = null,
    val placeOfBirth: String? = null,
    val nationality: String,
    val districtOfBirth: String? = null,
    val fathersName: String? = null,
    val fathersNin: String? = null,
    val mothersName: String? = null,
    val mothersNin: String? = null,
    val guardianName: String? = null,
    val guardianNin: String? = null,
    val guardianRelationship: String? = null,
    val educationLevel: String? = null,
    val occupation: String? = null,
    val phoneNumber: String,
    val email: String? = null,
    val currentAddress: String,
    val currentDistrict: String,
    val permanentAddress: String? = null,
    val permanentDistrict: String? = null,
    val photoData: ByteArray? = null,
    val trackingNumber: String = "",
    val status: String = "Submitted",
    val submittedAt: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdRegistration

        if (registrationId != other.registrationId) return false
        if (userId != other.userId) return false
        if (fullName != other.fullName) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (gender != other.gender) return false
        if (maritalStatus != other.maritalStatus) return false
        if (placeOfBirth != other.placeOfBirth) return false
        if (nationality != other.nationality) return false
        if (districtOfBirth != other.districtOfBirth) return false
        if (fathersName != other.fathersName) return false
        if (fathersNin != other.fathersNin) return false
        if (mothersName != other.mothersName) return false
        if (mothersNin != other.mothersNin) return false
        if (guardianName != other.guardianName) return false
        if (guardianNin != other.guardianNin) return false
        if (guardianRelationship != other.guardianRelationship) return false
        if (educationLevel != other.educationLevel) return false
        if (occupation != other.occupation) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (currentAddress != other.currentAddress) return false
        if (currentDistrict != other.currentDistrict) return false
        if (permanentAddress != other.permanentAddress) return false
        if (permanentDistrict != other.permanentDistrict) return false
        if (trackingNumber != other.trackingNumber) return false
        if (status != other.status) return false
        if (submittedAt != other.submittedAt) return false
        if (photoData != null) {
            if (other.photoData == null) return false
            if (!photoData.contentEquals(other.photoData)) return false
        } else if (other.photoData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = registrationId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + dateOfBirth.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + (maritalStatus?.hashCode() ?: 0)
        result = 31 * result + (placeOfBirth?.hashCode() ?: 0)
        result = 31 * result + nationality.hashCode()
        result = 31 * result + (districtOfBirth?.hashCode() ?: 0)
        result = 31 * result + (fathersName?.hashCode() ?: 0)
        result = 31 * result + (fathersNin?.hashCode() ?: 0)
        result = 31 * result + (mothersName?.hashCode() ?: 0)
        result = 31 * result + (mothersNin?.hashCode() ?: 0)
        result = 31 * result + (guardianName?.hashCode() ?: 0)
        result = 31 * result + (guardianNin?.hashCode() ?: 0)
        result = 31 * result + (guardianRelationship?.hashCode() ?: 0)
        result = 31 * result + (educationLevel?.hashCode() ?: 0)
        result = 31 * result + (occupation?.hashCode() ?: 0)
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + currentAddress.hashCode()
        result = 31 * result + currentDistrict.hashCode()
        result = 31 * result + (permanentAddress?.hashCode() ?: 0)
        result = 31 * result + (permanentDistrict?.hashCode() ?: 0)
        result = 31 * result + (photoData?.contentHashCode() ?: 0)
        result = 31 * result + trackingNumber.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + submittedAt.hashCode()
        return result
    }
}

// ID Renewal Model
data class IdRenewal(
    val renewalId: Long = 0,
    val userId: Long,
    val fullName: String,
    val dateOfBirth: String,
    val currentNin: String,
    val currentIdNumber: String,
    val dateOfIssue: String? = null,
    val dateOfExpiry: String,
    val renewalReason: String,
    val phoneNumber: String,
    val email: String? = null,
    val currentAddress: String,
    val currentDistrict: String,
    val photoData: ByteArray? = null,
    val trackingNumber: String = "",
    val status: String = "Submitted",
    val submittedAt: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdRenewal

        if (renewalId != other.renewalId) return false
        if (userId != other.userId) return false
        if (fullName != other.fullName) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (currentNin != other.currentNin) return false
        if (currentIdNumber != other.currentIdNumber) return false
        if (dateOfIssue != other.dateOfIssue) return false
        if (dateOfExpiry != other.dateOfExpiry) return false
        if (renewalReason != other.renewalReason) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (currentAddress != other.currentAddress) return false
        if (currentDistrict != other.currentDistrict) return false
        if (trackingNumber != other.trackingNumber) return false
        if (status != other.status) return false
        if (submittedAt != other.submittedAt) return false
        if (photoData != null) {
            if (other.photoData == null) return false
            if (!photoData.contentEquals(other.photoData)) return false
        } else if (other.photoData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = renewalId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + dateOfBirth.hashCode()
        result = 31 * result + currentNin.hashCode()
        result = 31 * result + currentIdNumber.hashCode()
        result = 31 * result + (dateOfIssue?.hashCode() ?: 0)
        result = 31 * result + dateOfExpiry.hashCode()
        result = 31 * result + renewalReason.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + currentAddress.hashCode()
        result = 31 * result + currentDistrict.hashCode()
        result = 31 * result + (photoData?.contentHashCode() ?: 0)
        result = 31 * result + trackingNumber.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + submittedAt.hashCode()
        return result
    }
}

// ID Replacement Model
data class IdReplacement(
    val replacementId: Long = 0,
    val userId: Long,
    val fullName: String,
    val dateOfBirth: String,
    val currentNin: String,
    val currentIdNumber: String,
    val dateOfIssue: String? = null,
    val dateOfExpiry: String? = null,
    val replacementReason: String,
    val policeReportNumber: String? = null,
    val incidentDate: String,
    val incidentDescription: String,
    val phoneNumber: String,
    val email: String? = null,
    val currentAddress: String,
    val currentDistrict: String,
    val photoData: ByteArray? = null,
    val trackingNumber: String = "",
    val status: String = "Submitted",
    val submittedAt: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdReplacement

        if (replacementId != other.replacementId) return false
        if (userId != other.userId) return false
        if (fullName != other.fullName) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (currentNin != other.currentNin) return false
        if (currentIdNumber != other.currentIdNumber) return false
        if (dateOfIssue != other.dateOfIssue) return false
        if (dateOfExpiry != other.dateOfExpiry) return false
        if (replacementReason != other.replacementReason) return false
        if (policeReportNumber != other.policeReportNumber) return false
        if (incidentDate != other.incidentDate) return false
        if (incidentDescription != other.incidentDescription) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (currentAddress != other.currentAddress) return false
        if (currentDistrict != other.currentDistrict) return false
        if (trackingNumber != other.trackingNumber) return false
        if (status != other.status) return false
        if (submittedAt != other.submittedAt) return false
        if (photoData != null) {
            if (other.photoData == null) return false
            if (!photoData.contentEquals(other.photoData)) return false
        } else if (other.photoData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = replacementId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + dateOfBirth.hashCode()
        result = 31 * result + currentNin.hashCode()
        result = 31 * result + currentIdNumber.hashCode()
        result = 31 * result + (dateOfIssue?.hashCode() ?: 0)
        result = 31 * result + (dateOfExpiry?.hashCode() ?: 0)
        result = 31 * result + replacementReason.hashCode()
        result = 31 * result + (policeReportNumber?.hashCode() ?: 0)
        result = 31 * result + incidentDate.hashCode()
        result = 31 * result + incidentDescription.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + currentAddress.hashCode()
        result = 31 * result + currentDistrict.hashCode()
        result = 31 * result + (photoData?.contentHashCode() ?: 0)
        result = 31 * result + trackingNumber.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + submittedAt.hashCode()
        return result
    }
}

// Support Request Model
data class SupportRequest(
    val supportId: Long = 0,
    val userId: Long,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val issueType: String,
    val subject: String,
    val description: String,
    val referenceId: String = "",
    val status: String = "Open",
    val submittedAt: String = ""
)

// Application Status Model
data class ApplicationStatus(
    val applicationType: String,
    val applicationId: Long,
    val trackingNumber: String,
    val status: String,
    val submittedAt: String
)

// User Application Model
data class UserApplication(
    val type: String,
    val id: Long,
    val fullName: String,
    val trackingNumber: String,
    val status: String,
    val submittedAt: String
)