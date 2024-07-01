package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ActivitySuggestions : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var suggestions: Map<String, String> // name of recipe -> short description
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.suggestions)
        // Initializing the container
        container = findViewById(R.id.containerSuggestions)

        // Initializing ingredientList with sample data, replace with db data later on
        suggestions = mapOf(
            "Caprese Salad" to "A classic Italian salad with tomatoes, mozzarella, and basil",
            "Mexican Hot Dog" to "A delicious hot dog with a spicy kick",
            "Garlic Butter Shrimps" to "Juicy shrimp cooked in a rich garlic butter sauce, perfect for serving over pasta or rice."
        )

        for (recipe in suggestions) {
            val name = recipe.key
            val description = recipe.value
            addRow(name, description)
        }

    }

    private fun addRow(name: String, description: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.suggestions_layout_row, container, false)



        // Find the TextView and set its click listener
        val nameRecipe: TextView = rowView.findViewById(R.id.nameRecipe)
        nameRecipe.text = name

        val frameRecipe: FrameLayout = rowView.findViewById(R.id.frameRecipe)
        // Set click listener to display Recipe
        frameRecipe.setOnClickListener {

            // TODO: change Activity and Display correct Recipe
        }

        val descriptionRecipe: TextView = rowView.findViewById(R.id.descriptionRecipe)
        descriptionRecipe.text = description

        val icon_save: ImageView = rowView.findViewById(R.id.heartIcon)


    var isFavorite = false // Initial state, not favorited

// Set initial icon
   icon_save.setImageResource(R.drawable.heart_icon) // Unfilled heart icon by default

// Saving recipe
    icon_save.setOnClickListener {
        // Toggle isFavorite state
        isFavorite = !isFavorite

        // Update heart icon based on isFavorite state
        if (isFavorite) {
            icon_save.setImageResource(R.drawable.heart_icon_filled)
            // TODO: create detailed Recipe
            // TODO: add to saved recipes
        } else {
            icon_save.setImageResource(R.drawable.heart_icon)
            // TODO: remove from recipes
        }
    }

        // Add the inflated row layout to the container
        container.addView(rowView)
    }


    fun homeButton(view: View){
        val intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, ActivityGrocery::class.java)
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, ActivityRecipes::class.java)
        startActivity(intent)
    }

    fun profileButton(view: View){
        val intent = Intent(this, ActivityProfile::class.java)
        startActivity(intent)
    }
    fun nextButton(view: View){
        val intent = Intent(this, ActivityRecipeDisplay::class.java)
        startActivity(intent)
    }
}