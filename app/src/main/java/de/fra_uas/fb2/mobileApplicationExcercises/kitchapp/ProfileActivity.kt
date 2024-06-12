package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        val spinner: Spinner = findViewById(R.id.spLanguage)
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
    }
}
