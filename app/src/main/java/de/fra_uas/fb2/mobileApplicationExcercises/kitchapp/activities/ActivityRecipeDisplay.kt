package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import org.json.JSONObject

class ActivityRecipeDisplay : AppCompatActivity() {
    private lateinit var recipeTitle: TextView
    private lateinit var recipeText: TextView

    private lateinit var ingredients: String
    private lateinit var instructions: String

    private var instruction: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val response = intent.getStringExtra("response") ?: ""
        val recipeName = intent.getStringExtra("name")?:""

        recipeTitle = findViewById(R.id.tvRecipeName)
        recipeText = findViewById(R.id.recipeText)


        // Parse the JSON response
        try {
            val jsonResponse = JSONObject(response)
            val recipesArray = jsonResponse.getJSONArray("recipes").getJSONObject(0).getJSONArray("recipes")

            for (i in 0 until recipesArray.length()) {
                val recipe = recipesArray.getJSONObject(i)
                val name = recipe.getString("name")
                if(name.equals(recipeName)){
                    recipeTitle.text=name
                    ingredients=recipe.getJSONArray("ingredients").join("\n").replace("\"", "")
                    instructions = recipe.getString("instructions").replace("\"", "")
                    recipeText.text=ingredients
                    break
                }
                //val ingredients = recipe.getJSONArray("ingredients").join(", ")
                //val instructionsArray = recipe.getJSONArray("instructions")
                // val instructions = instructionsArray?.join("\n")?.replace("\"", "") ?: "No instructions provided"

                //val description = recipe.getString("description")

            }
        } catch (e: Exception) {
            Log.e("ActivitySuggestions", "Error parsing JSON response", e)
        }

    }

    fun switchOnClick(view: View) {
        if (!instruction) {
            recipeText.text = instructions
            instruction = true
        }else{
            recipeText.text = ingredients
            instruction=false
        }
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
}