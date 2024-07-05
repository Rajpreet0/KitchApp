package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException

class ActivitySuggestions : AppCompatActivity() {

    private lateinit var container: LinearLayout

    private lateinit var recipesArray: JSONArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.suggestions)
        // Initializing the container
        container = findViewById(R.id.containerSuggestions)

        val response = intent.getStringExtra("response") ?: ""

        // Parse the JSON response
        try {
            val jsonResponse = JSONObject(response)
            recipesArray = jsonResponse.getJSONObject("reply").getJSONArray("recipes")

            for (i in 0 until recipesArray.length()) {
                val recipe = recipesArray.getJSONObject(i)
                val name = recipe.getString("name")
                //val ingredients = recipe.getJSONArray("ingredients").join(", ")
                //val instructionsArray = recipe.getJSONArray("instructions")
               // val instructions = instructionsArray?.join("\n")?.replace("\"", "") ?: "No instructions provided"

                val description = recipe.getString("description")

                addRow(name, description)
            }
        } catch (e: Exception) {
            Log.e("ActivitySuggestions", "Error parsing JSON response", e)
        }


    }

    private fun addRow(name: String, description: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.suggestions_layout_row, container, false)



        // Find the TextView and set its click listener
        val nameRecipe: TextView = rowView.findViewById(R.id.tvNameRecipe)
        nameRecipe.text = name

        val frameRecipe: FrameLayout = rowView.findViewById(R.id.frameRecipe)
        // Set click listener to display Recipe
        frameRecipe.setOnClickListener {

            // TODO: change Activity and Display correct Recipe
        }

        val descriptionRecipe: TextView = rowView.findViewById(R.id.tvDescriptionRecipe)
        descriptionRecipe.text = description

        val icon_save: ImageView = rowView.findViewById(R.id.icHeart)


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
        val intent = Intent(this, ActivityHome::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, ActivityGrocery::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, ActivityRecipes::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    fun profileButton(view: View){
        val intent = Intent(this, ActivityProfile::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }
    fun nextButton(view: View){
        val intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
            putExtra("recipe", recipesArray.toString())
        }
        startActivity(intent)
    }
}