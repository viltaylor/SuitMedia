package com.example.test1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.R
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPalindrome: EditText
    private lateinit var btnCheck: Button
    private lateinit var btnNext: Button

    private var isPalindromeChecked = false
    private var isPalindromeResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etPalindrome = findViewById(R.id.etSentence)
        btnCheck = findViewById(R.id.btnCheck)
        btnNext = findViewById(R.id.btnNext)

        //check button
        btnCheck.setOnClickListener {
            val input = etPalindrome.text.toString()
            if (input.isBlank()) {
                showDialog("Please enter a sentence.")
                isPalindromeChecked = false
            } else {
                isPalindromeResult = checkPalindrome(input)
                isPalindromeChecked = true
                val message = if (isPalindromeResult) "isPalindrome" else "not palindrome"
                showDialog(message)
            }
        }

        //next button
        btnNext.setOnClickListener {
            if (!isPalindromeChecked) {
                showDialog("Please check the sentence first.")
                return@setOnClickListener
            }

            if (!isPalindromeResult) {
                showDialog("not palindrome")
                return@setOnClickListener
            }

            val name = etName.text.toString()
            if (name.isBlank()) {
                showDialog("Please enter your name before proceeding.")
            } else {
                val intent = Intent(this, SecondScreen::class.java)
                intent.putExtra("username", name)
                startActivity(intent)
            }
        }
    }

    private fun checkPalindrome(text: String): Boolean {
        val cleaned = text
            .lowercase(Locale.ROOT)
            .filter { it.isLetterOrDigit() }
        return cleaned == cleaned.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
