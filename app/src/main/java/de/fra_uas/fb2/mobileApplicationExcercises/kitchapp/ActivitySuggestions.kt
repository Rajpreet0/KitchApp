package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ActivitySuggestions : AppCompatActivity() {
    private val networkHelper = NetworkHelper()
    private lateinit var loadingDialog: LoadingDialogFragment

    private lateinit var container: LinearLayout

    private lateinit var recipesArray: JSONArray

    private lateinit var choosenRecipe: String
    private lateinit var response:String
    private lateinit var portionTxt: String
    private lateinit var categoryTxt: String
    private lateinit var timeTxt: String
    private lateinit var complexityTxt: String
    private lateinit var nationalityTxt: String
    private lateinit var ingredientString: String
    private lateinit var withoutIngredients: String
    private lateinit var specialIngredients: String
    private lateinit var newResponse: JsonObject
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.suggestions)
        // Initializing the container
        container = findViewById(R.id.containerSuggestions)

        response = intent.getStringExtra("response") ?: ""
        portionTxt = intent.getStringExtra("portion") ?: ""
        categoryTxt = intent.getStringExtra("category") ?: ""
        timeTxt = intent.getStringExtra("time") ?: ""
        complexityTxt = intent.getStringExtra("complexity") ?: ""
        nationalityTxt = intent.getStringExtra("nationality") ?: ""
        ingredientString = intent.getStringExtra("ingredientString") ?: ""
        withoutIngredients = intent.getStringExtra("withoutIngredients") ?: ""
        specialIngredients = intent.getStringExtra("specialIngredients") ?: ""

        loadingDialog= LoadingDialogFragment()



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
    fun generateButton(view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                newResponse =  withContext(Dispatchers.IO) {
                    networkHelper.suggestRecipe(
                        portionTxt,
                        categoryTxt,
                        timeTxt,
                        complexityTxt,
                        nationalityTxt,
                        ingredientString.toString(),
                        withoutIngredients,
                        specialIngredients
                    )
                }

                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                }
            } catch (e: IOException) {
                Log.d("SERVER ERROR", "${e}")
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "SERVER ERROR: Prompt Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        container.removeAllViews()
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

        val frameRecipe: FrameLayout = rowView.findViewById(R.id.frameRecipe)

        // Find the TextView and set its text
        val nameRecipe: TextView = rowView.findViewById(R.id.tvNameRecipe)
        nameRecipe.text = name

        val descriptionRecipe: TextView = rowView.findViewById(R.id.tvDescriptionRecipe)
        descriptionRecipe.text = description

        // Set click listener on the frameRecipe to handle background change
        frameRecipe.setOnClickListener {
            onCLick(frameRecipe, name)
        }

        // Handle the favorite icon logic
        val icon_save: ImageView = rowView.findViewById(R.id.icHeart)
        var isFavorite = false // Initial state, not favorited
        icon_save.setImageResource(R.drawable.ic_heart_unfilled) // Unfilled heart icon by default

        icon_save.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                icon_save.setImageResource(R.drawable.heart_icon_filled)
                // TODO: create detailed Recipe
                // TODO: add to saved recipes
            } else {
                icon_save.setImageResource(R.drawable.ic_heart_unfilled)
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

    fun nextButton(view: View) {
        if(choosenRecipe.isNotEmpty()) {
            val intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
                putExtra("response", response)
                putExtra("name", choosenRecipe)
            }
            startActivity(intent)
        }else{
            Toast.makeText(this, "Please select a Recipe", Toast.LENGTH_SHORT).show()
        }
    }


    private fun onCLick(view: View, name: String) {
        val choosenDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.choosen_recipe_layout)
        val defaultDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.bg_roundedcorners)

        val currentDrawable = view.background

        if (currentDrawable?.constantState == defaultDrawable?.constantState) {
            view.background = choosenDrawable
            choosenRecipe = name
        } else {
            view.background = defaultDrawable
            choosenRecipe = ""
        }
    }

}
