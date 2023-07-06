package com.nexlesoft.ket.ui.custom.passwordstrengthmeter

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PasswordStrengthCalculatorTest {

    @Test
    fun has_ContainNumber_Test() {
        val calculator = PasswordStrengthCalculator()
        val password1 = "abcdehihfe"
        assert(!calculator.hasContainsNumber(password1))
        val password2 = "abcdefght$@#"
        assert(!calculator.hasContainsNumber(password2))
        val password3 = "abc123456789"
        assert(calculator.hasContainsNumber(password3))
    }

    @Test
    fun has_ContainsSpecialCharacter_Test() {
        val calculator = PasswordStrengthCalculator()
        val password1 = "123456avc"
        assert(!calculator.hasContainsSpecialChar(password1))
        val password2 = "123456Aa@"
        assert(calculator.hasContainsSpecialChar(password2))
    }

    @Test
    fun has_ContainsBothLowercaseAndUppercase_Test() {
        val calculator = PasswordStrengthCalculator()
        val password1 = "123456az"
        assert(!calculator.hasContainsBothLowercaseAndUppercaseChar(password1))
        val password2 = "123456AZ"
        assert(!calculator.hasContainsBothLowercaseAndUppercaseChar(password2))
        val password3 = "123456Aabc"
        assert(calculator.hasContainsBothLowercaseAndUppercaseChar(password3))
    }

    @Test
    fun calculatePasswordSecurityLevel_Test() {
        val calculator = PasswordStrengthCalculator()
        val password1 = "abcde"
        assert(calculator.calculatePasswordSecurityLevel(password1) == 0)
        val password2 = "abcdefgh"
        assert(calculator.calculatePasswordSecurityLevel(password2) == 1)
        val passwordList1 = listOf("abcdeAa", "abcde123", "abcde@#$")
        for (password in passwordList1) {
            val strengthLevel = calculator.calculatePasswordSecurityLevel(password)
            assert(strengthLevel == 2)
        }
        val passwordList2 = listOf("abcdeAB1", "Abcd@#$", "12345@$#")
        for (password in passwordList2) {
            val strengthLevel = calculator.calculatePasswordSecurityLevel(password)
            assert(strengthLevel == 3)
        }
        val password3 = "abcdeAB1@#$"
        assert(calculator.calculatePasswordSecurityLevel(password3) == 4)
    }
}
