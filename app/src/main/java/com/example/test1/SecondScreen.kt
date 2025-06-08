package com.example.test1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.databinding.ActivitySecondScreenBinding

class SecondScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySecondScreenBinding

    // Modern way to handle getting a result back from another activity.
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedName = result.data?.getStringExtra("EXTRA_SELECTED_USER_NAME")
            binding.selectedUserNameTextView.text = selectedName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add a back button to the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Second Screen"

        val name = intent.getStringExtra("EXTRA_USER_NAME")
        binding.nameTextView.text = name

        binding.chooseUserButton.setOnClickListener {
            val intent = Intent(this, ThirdScreen::class.java)
            resultLauncher.launch(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}