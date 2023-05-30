package com.example.internship.person.utility

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class EmailValidatorTest(private val input: String, private val expectedOutput: Boolean) {

    @Test
    fun `email validation should return expected result`() {
        val validator = EmailValidator()

        val sut = validator.isValid(input)

        Assert.assertEquals(expectedOutput, sut)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: email({0})={1}")
        fun data() = listOf(
            arrayOf("test@example.com", true),
            arrayOf("test+123@example.com", false),
            arrayOf("test.example@example.com", true),
            arrayOf("test@example", false),
            arrayOf("test@", false),
            arrayOf("@example.com", false),
            arrayOf("", false)
        )
    }
}