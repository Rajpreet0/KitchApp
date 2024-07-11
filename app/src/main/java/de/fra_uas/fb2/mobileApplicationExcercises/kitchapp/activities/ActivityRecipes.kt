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
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
//BEGIN_Ron
class ActivityRecipes : AppCompatActivity() {
    private var recipeContainer: LinearLayout? = null
    private val recipeList: MutableMap<String, String> = mutableMapOf()
    private val markedForDeletion = mutableSetOf<String>()
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
                addRow(it.key.split("-")[0], it.key.split("-")[1], it.key)
        }
    }
    //END_Ron
    //BEGIN_Daria
    private fun addRow(name: String, time: String, key: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.savedrecipe_layout_row, recipeContainer, false)

        // Find the TextView and set its text
        val nameRecipe: TextView = rowView.findViewById(R.id.tvNameRecipe)
        nameRecipe.text = name
        nameRecipe.textSize = 20f


        val timeRecipe: TextView = rowView.findViewById(R.id.tvTime)
        timeRecipe.text = time


        // Handle the favorite icon logic
        val icon_save: ImageView = rowView.findViewById(R.id.icHeart)
        var isFavorite = true // Initial state, not favorited
        icon_save.setImageResource(R.drawable.heart_icon_filled) // Unfilled heart icon by default

        icon_save.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                icon_save.setImageResource(R.drawable.heart_icon_filled)
                markedForDeletion.remove(key)
            } else {
                icon_save.setImageResource(R.drawable.ic_heart_unfilled_white)
                // TODO: remove from recipes after intent / activity change
                markedForDeletion.add(key)
            }
        }

        val frameRecipe: FrameLayout = rowView.findViewById(R.id.frameRecipe)
        frameRecipe.setOnClickListener {
            val intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
                putExtra("nameTime", "$name-$time")
                putExtra("isFavorite", true)
            }
            startActivity(intent)
        }

        // Add the inflated row layout to the container
        recipeContainer?.addView(rowView)
    }
    //END_Daria
    //BEGIN_Ron
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
        val jsonString = sharedPreferences.getString("savedRecipeMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, String>>() {}.type)
        } else {
            mutableMapOf()
        }
    }

    fun delete(view: View){
        for (key in markedForDeletion) {
            recipeList.remove(key)
        }
        saveMap(this, recipeList)
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
}
//END_Ron