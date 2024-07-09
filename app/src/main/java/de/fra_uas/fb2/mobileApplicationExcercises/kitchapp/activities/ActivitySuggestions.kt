package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
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
import android.widget.Toast
import org.json.JSONObject
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.fragments.LoadingDialogFragment
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import org.w3c.dom.Text

class ActivitySuggestions : AppCompatActivity() {
    private val networkHelper = NetworkHelper()
    private lateinit var loadingDialog: LoadingDialogFragment

    private lateinit var container: LinearLayout

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
    private val recipeList: MutableMap<String, String> = mutableMapOf()

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
        recipeList.putAll(getMap(this))
        loadingDialog= LoadingDialogFragment()



        // Parse the JSON response
        try {
            val jsonResponse = JSONObject(response)
            val recipesArray = jsonResponse.getJSONArray("recipes")

            Log.d("RECIPE ARRAY", recipesArray.toString())
            for (i in 0 until recipesArray.length()) {
                val recipesObject = recipesArray.getJSONObject(i).getJSONArray("recipes")
                for (j in 0 until recipesObject.length()) {
                    val recipe = recipesObject.getJSONObject(j)
                    val name = recipe.getString("name")
                    val description = recipe.getString("description")
                    val time = recipe.getString("time")
                    //val ingredients = recipe.getJSONArray("ingredients").join(", ")
                    //val instructionsArray = recipe.getJSONArray("instructions")
                    // val instructions = instructionsArray?.join("\n")?.replace("\"", "") ?: "No instructions provided"
                    addRow(name, description, time)
                }
            }
        } catch (e: Exception) {
            Log.e("ActivitySuggestions", "Error parsing JSON response", e)
        }


    }
    fun generateButton(view: View) {
        container.removeAllViews()
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
                    response = newResponse.toString()
                    parseAndDisplayRecipes(response)
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
    }


    private fun parseAndDisplayRecipes(response: String) {
        try {
            val jsonResponse = JSONObject(response)
            val recipesArray = jsonResponse.getJSONArray("recipes")

            for (i in 0 until recipesArray.length()) {
                val recipesObject = recipesArray.getJSONObject(i).getJSONArray("recipes")

                for (j in 0 until recipesObject.length()) {
                    val recipe = recipesObject.getJSONObject(j)
                    val name = recipe.getString("name")
                    val description = recipe.getString("description")
                    val time = recipe.getString("time")
                    addRow(name, description, time)
                }
            }
        } catch (e: Exception) {
            Log.e("ActivitySuggestions", "Error parsing JSON response", e)
        }
    }

    private fun saveMap(context: Context, map: MutableMap<String, String>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the map to a JSON string
        val jsonString = Gson().toJson(map)
        editor.putString("savedRecipeMap", jsonString)
        editor.apply()
    }

    private fun getMap(context: Context): MutableMap<String, String> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("savedRecipeMap", null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, MutableMap::class.java) as MutableMap<String, String>
        } else {
            mutableMapOf()
        }
    }



    private fun addRow(name: String, description: String, time: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.suggestions_layout_row, container, false)

        val frameRecipe: FrameLayout = rowView.findViewById(R.id.frameRecipe)

        // Find the TextView and set its text
        val nameRecipe: TextView = rowView.findViewById(R.id.tvNameRecipe)
        nameRecipe.text = name

        val descriptionRecipe: TextView = rowView.findViewById(R.id.tvDescriptionRecipe)
        descriptionRecipe.text = description

        val timeRecipe: TextView = rowView.findViewById(R.id.tvTime)
        timeRecipe.text = "$time min"

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
                recipeList[name] = description
                saveMap(this, recipeList)
                // TODO: add to saved recipes
            } else {
                icon_save.setImageResource(R.drawable.ic_heart_unfilled)
                // TODO: remove from recipes
                recipeList.remove(name)
                saveMap(this, recipeList)
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
        val choosenDrawable: Drawable? = ContextCompat.getDrawable(this,
            R.drawable.choosen_recipe_layout
        )
        val defaultDrawable: Drawable? = ContextCompat.getDrawable(this,
            R.drawable.bg_roundedcorners
        )

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
