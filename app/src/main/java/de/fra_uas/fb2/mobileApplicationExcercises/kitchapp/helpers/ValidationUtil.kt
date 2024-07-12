package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers

import android.util.Patterns

// BEGIN: Raj
object ValidationUtil {

    // Patterns Class used for validating Email formality
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Password must be above 6 characters and should contain any kind of Digit
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 && password.any { it.isDigit() }
    }

    // Register Validation
    fun validateRegisterInput(username: String, email: String, password: String, confirmPassword: String): String? {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return "Please fill in all fields"
        }
        if (!isValidEmail(email)) {
            return "Please enter a valid email"
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long and contain a number"
        }
        if (password != confirmPassword) {
            return "Passwords do not match"
        }
        return null
    }

    // Login Validation
    fun validateLoginInputs(email: String, password: String): String? {
        if(email.isBlank() || password.isBlank()) {
            return "Please fill in both email and password"
        }

        if (!isValidEmail(email)) {
            return "Please enter a valid email"
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long and contain a number"
        }

        return null
    }

    // Update Password Validation
    fun validateUpdatePassword(password: String, confirmPassword: String): String? {
        if (password.isBlank() || confirmPassword.isBlank()) {
            return "Please fill in both password and confirm password"
        }

        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long and contain a number"
        }

        return null
    }

    // Recipe Preferences Validation
    fun validateRecipePreferences(portion: String?, category: String?, time: String?, complexity: String?, nationality: String?): String? {
        if (portion.isNullOrEmpty()) {
            return "Please select a portion size"
        }
        if (category.isNullOrEmpty()) {
            return "Please select a category"
        }
        if (time.isNullOrEmpty()) {
            return "Please select a time requirement"
        }
        if (complexity.isNullOrEmpty()) {
            return "Please select a complexity level"
        }
        if (nationality.isNullOrEmpty()) {
            return "Please select a nationality"
        }
        return null
    }
}
// END: Raj