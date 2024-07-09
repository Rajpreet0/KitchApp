package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R

class ActivityRecipes : AppCompatActivity() {
    private var recipeText: LinearLayout? = null
    private val recipeList: MutableMap<String, String> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_recipes)
        recipeList.putAll(getMap(this))
        recipeText = findViewById(R.id.containerRecipes)
        buildRecipeList()
    }

    private fun buildRecipeList(){
        recipeText!!.removeAllViews()
        recipeList.forEach{
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
            recipeText!!.addView(nameTextView)
        }
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
}