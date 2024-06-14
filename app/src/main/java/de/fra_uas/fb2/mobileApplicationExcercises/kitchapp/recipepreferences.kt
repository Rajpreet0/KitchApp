package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class recipepreferences : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipepreferences)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupSpinner(R.id.spinnerPortions, R.array.portions_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spinnercategory, R.array.category_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spinnertimerequired, R.array.timerequired_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spinnercomplexity, R.array.complexity_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spinnernationality, R.array.nationality_array, R.layout.spinner_items_preferences)

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
    fun nextButton(view: View) {
        val intent = Intent(this, Suggestions::class.java)
        startActivity(intent)
    }
}