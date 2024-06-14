package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity



class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupSpinner(R.id.spLanguage, R.array.languages, R.layout.spinner_items_profile)
    }
    private fun setupSpinner(spinnerId: Int, arrayResourceId: Int, layoutResourceId: Int) {
        // Create an ArrayAdapter using a string array resource and a custom layout for spinner items
        val adapter = ArrayAdapter.createFromResource(
            this,
            arrayResourceId,
            layoutResourceId
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Initialize the Spinner
        val spinner = findViewById<Spinner>(spinnerId)

        // Apply the adapter to the spinner
        spinner.adapter = adapter
    }

    fun homeButton(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, MainActivityGrocery::class.java)
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, MainActivityRecipes::class.java)
        startActivity(intent)
    }

    fun profileButton(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
