package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import org.json.JSONObject
//BEGIN_Ron
class ActivityRecipeDisplay : AppCompatActivity() {
    private lateinit var recipeTitle: TextView
    private lateinit var recipeText: TextView
    private lateinit var recipeTime: TextView
    private lateinit var recipePortion: TextView

    private lateinit var recipeNameTime: String
    private lateinit var ingredients: String
    private lateinit var instructions: String
    private lateinit var portion: String
    private val recipeList: MutableMap<String, String> = mutableMapOf()
    private var instruction: Boolean = false
    private var isFavorite: Boolean = false
    private var iconSave: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val heartIcon = intent.getBooleanExtra("isFavorite", false)
        val response = intent.getStringExtra("response") ?: ""
        val recipeName = intent.getStringExtra("name")?:""
        val portions = intent.getStringExtra("portion")?:""
        recipeNameTime = intent.getStringExtra("nameTime")?:""

        recipeTitle = findViewById(R.id.tvRecipeName)
        recipeText = findViewById(R.id.recipeText)
        recipeTime = findViewById(R.id.tvShowTime)
        recipePortion = findViewById(R.id.tvServingSize)
        iconSave = findViewById(R.id.iconSave)
        if (heartIcon) {
            iconSave?.setImageResource(R.drawable.heart_icon_filled)
            isFavorite=true
        } else {
            iconSave?.setImageResource(R.drawable.ic_heart_unfilled)
        }

        recipeTitle.text = recipeNameTime.split(" - ")[0].trim()                            //0 is the name and 1 is the time
        recipeList.putAll(getMap(this))
        instructions = recipeList[recipeNameTime]?.split(" § ")?.get(2)?.trim() ?: ""       //0 is the portion, 1 is the ingredients, 2 is the instructions
        ingredients = recipeList[recipeNameTime]?.split(" § ")?.get(1)?.trim() ?: ""
        portion = recipeList[recipeNameTime]?.split(" § ")?.get(0)?.trim() ?: ""
        if(recipeNameTime!="") {
            recipeTime.text = recipeNameTime.split(" - ")[1].trim()
            recipePortion.text = portion
            recipeText.text = ingredients

        }

        // Parse the JSON response
        try {
            val jsonResponse = JSONObject(response)
            val recipesArray = jsonResponse.getJSONArray("recipes").getJSONObject(0).getJSONArray("recipes")

            for (i in 0 until recipesArray.length()) {
                val recipe = recipesArray.getJSONObject(i)
                val name = recipe.getString("name")
                if(name.equals(recipeName)){
                    recipeTitle.text=name
                    val time = recipe.getString("time")
                    recipeTime.text=time
                    ingredients=recipe.getJSONArray("ingredients").join("\n").replace("\"", "")
                    instructions = recipe.getString("instructions").replace("\"", "")
                    recipeText.text=ingredients
                    recipePortion.text=portions
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("ActivitySuggestions", "Error parsing JSON response", e)
        }

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

    private fun saveMap(context: Context, map: MutableMap<String, String>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the map to a JSON string
        val jsonString = Gson().toJson(map)
        editor.putString("savedRecipeMap", jsonString)
        editor.apply()
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
    fun saveRecipe(view: View) {
        isFavorite = !isFavorite
        if (isFavorite) {
            iconSave?.setImageResource(R.drawable.heart_icon_filled)
            recipeList[recipeTitle.text.toString()+" - "+recipeTime.text.toString()] = "$portion § $ingredients § $instructions"
            saveMap(this, recipeList)
        } else {
            iconSave?.setImageResource(R.drawable.ic_heart_unfilled)
            recipeList.remove(recipeTitle.text.toString()+" - "+recipeTime.text.toString())
            saveMap(this, recipeList)
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
//END_Ron