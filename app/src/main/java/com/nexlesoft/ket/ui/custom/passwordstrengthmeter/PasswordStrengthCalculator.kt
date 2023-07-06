package com.nexlesoft.ket.ui.custom.passwordstrengthmeter

import java.util.regex.Pattern

class PasswordStrengthCalculator {
    private val specialCharPattern = Pattern.compile("^(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>])")
    private val caseSensitivePattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])")
    private val containNumberPattern = Pattern.compile("^(?=.*[0-9])")

    fun calculatePasswordSecurityLevel(password: String): Int {
        if (!hasValidLength(password)) {
            return 0
        }
        val hasLowercaseAndUppercase = hasContainsBothLowercaseAndUppercaseChar(password)
        val hasNumber = hasContainsNumber(password)
        val hasSpecialChar = hasContainsSpecialChar(password)
        var strengthLevel = 1
        if (hasLowercaseAndUppercase) {
            strengthLevel += 1
        }
        if (hasNumber) {
            strengthLevel += 1
        }
        if (hasSpecialChar) {
            strengthLevel += 1
        }
        return strengthLevel
    }

    fun hasValidLength(password: String): Boolean {
        return password.length in 6..18
    }

    fun hasContainsNumber(password: String): Boolean {
        return containNumberPattern.matcher(password).find()
    }

    fun hasContainsSpecialChar(password: String): Boolean {
        return specialCharPattern.matcher(password).find()
    }

    fun hasContainsBothLowercaseAndUppercaseChar(password: String): Boolean {
        return caseSensitivePattern.matcher(password).find()
    }
}