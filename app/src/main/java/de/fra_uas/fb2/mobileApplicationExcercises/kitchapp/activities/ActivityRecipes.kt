package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R

class ActivityRecipes : AppCompatActivity() {
    private var recipeContainer: LinearLayout? = null
    private val recipeList: MutableMap<String, String> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_recipes)
        recipeList.putAll(getMap(this))


        recipeContainer = findViewById(R.id.containerRecipes)
        buildRecipeList()
    }

    private fun buildRecipeList(){
        recipeContainer!!.removeAllViews()
        recipeList.forEach{
                addRow(it.key, "10") // TODO: get time from database/recipe
        }
    }

    /*
    (name, description) ->
            val nameTextView = TextView(this)
            nameTextView.textSize = 20f                                                       //improves readability
            nameTextView.text = name
            nameTextView.setOnClickListener {
                val intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
                    putExtra("nameTime", name)
                }
                startActivity(intent)
            }
            recipeContainer!!.addView(nameTextView)
     */

    private fun addRow(name: String, time: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.savedrecipe_layout_row, recipeContainer, false)

        // Find the TextView and set its text
        val nameRecipe: TextView = rowView.findViewById(R.id.tvNameRecipe)
        nameRecipe.text = name
        nameRecipe.textSize = 20f


        val timeRecipe: TextView = rowView.findViewById(R.id.tvTime)
        timeRecipe.text = "$time min"


        // Handle the favorite icon logic
        val icon_save: ImageView = rowView.findViewById(R.id.icHeart)
        var isFavorite = true // Initial state, not favorited
        icon_save.setImageResource(R.drawable.heart_icon_filled) // Unfilled heart icon by default

        icon_save.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                icon_save.setImageResource(R.drawable.heart_icon_filled)
                saveRecipe(name)
            } else {
                icon_save.setImageResource(R.drawable.ic_heart_unfilled_white)
                // TODO: remove from recipes after intent / activity change

            }
        }

        val frameRecipe: FrameLayout = rowView.findViewById(R.id.frameRecipe)
        frameRecipe.setOnClickListener {
            val intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
                putExtra("nameTime", name)
            }
            startActivity(intent)
        }

        // Add the inflated row layout to the container
        recipeContainer?.addView(rowView)
    }




    private fun getMap(context: Context): MutableMap<String, String> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("savedRecipeMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, String>>() {}.type)
        } else {
            mutableMapOf()
        }
    }

    fun delete(view: View){
        val sharedPreferences = getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("savedRecipeMap")
        editor.apply()
        recipeList.clear()
        buildRecipeList()
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

    fun saveRecipe(name: String) {
        // TODO: Save the recipe to shared preferences if recipe not existent
        /*
        val sharedPreferences = getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val savedRecipeMap = getMap(this)
        savedRecipeMap[name] = "10"
        editor.putString("savedRecipeMap", Gson().toJson(savedRecipeMap))
        editor.apply()

         */
    }
}