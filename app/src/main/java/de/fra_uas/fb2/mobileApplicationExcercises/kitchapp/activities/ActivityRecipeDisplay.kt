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

    private lateinit var recipeNameTime: String                                                     //stores the name and time of the recipe
    private lateinit var ingredients: String                                                        //stores the ingredients
    private lateinit var instructions: String                                                       //stores the instructions
    private lateinit var portion: String                                                            //stores the portion
    private val recipeList: MutableMap<String, String> = mutableMapOf()                             //stores the recipe in format name§time -> portion§ingredients§instructions
    private var instruction: Boolean = false                                                        //shows if instructions are shown
    private var isFavorite: Boolean = false                                                         //shows if the recipe is a favorite
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
        // Get the intent data from the previous activity
        //we can get here from the home screen, recipe preferences, or recipes
        //therefore some intents might be empty if they are not set by that activity
        val heartIcon = intent.getBooleanExtra("isFavorite", false)
        val response = intent.getStringExtra("response") ?: ""
        val recipeName = intent.getStringExtra("name")?:""
        var portions = intent.getStringExtra("portion")?:""
        recipeNameTime = intent.getStringExtra("nameTime")?:""

        recipeTitle = findViewById(R.id.tvRecipeName)
        recipeText = findViewById(R.id.recipeText)
        recipeTime = findViewById(R.id.tvShowTime)
        recipePortion = findViewById(R.id.tvServingSize)
        iconSave = findViewById(R.id.iconSave)
        // Set the heart icon based on the value of isFavorite
        if (heartIcon) {
            iconSave?.setImageResource(R.drawable.heart_icon_filled)
            isFavorite=true
        } else {
            iconSave?.setImageResource(R.drawable.ic_heart_unfilled)
        }

        recipeTitle.text = recipeNameTime.split("§")[0].trim()                            //0 is the name and 1 is the time
        recipeList.putAll(getMap(this))
        instructions = recipeList[recipeNameTime]?.split("§")?.get(2)?.trim() ?: ""       //0 is the portion, 1 is the ingredients, 2 is the instructions
        ingredients = recipeList[recipeNameTime]?.split("§")?.get(1)?.trim() ?: ""
        portion = recipeList[recipeNameTime]?.split("§")?.get(0)?.trim() ?: ""
        //some activities give no portion so we set it to 1 because thats the default prompt
        if (portions == "") {
            portions = "1"
            recipePortion.text = portions
        }
        if(recipeNameTime!="") {
            recipeTime.text = recipeNameTime.split("§")[1].trim()
            if(portion!="") {
                recipePortion.text = portion
            }
            recipeText.text = ingredients

        }

        // Parse the JSON response
        try {
            val jsonResponse = JSONObject(response)
            val recipesArray = jsonResponse.getJSONArray("recipes").getJSONObject(0).getJSONArray("recipes")
            //we need to find the recipe in the array by its name
            for (i in 0 until recipesArray.length()) {
                val recipe = recipesArray.getJSONObject(i)
                val name = recipe.getString("name")
                if(name.equals(recipeName)){
                    //we found the recipe and we can set the values
                    recipeTitle.text=name
                    var time = recipe.getString("time")
                    if(!time.contains("min")){
                        time= "$time min"
                    }
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
    //get the map from the shared preferences
    private fun getMap(context: Context): MutableMap<String, String> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("savedRecipeMap", null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, MutableMap::class.java) as MutableMap<String, String>
        } else {
            mutableMapOf()
        }
    }
    //save the map to the shared preferences
    private fun saveMap(context: Context, map: MutableMap<String, String>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the map to a JSON string
        val jsonString = Gson().toJson(map)
        editor.putString("savedRecipeMap", jsonString)
        editor.apply()
    }
    //switch between instructions and ingredients
    fun switchOnClick(view: View) {
        if (!instruction) {
            recipeText.text = instructions
            instruction = true
        }else{
            recipeText.text = ingredients
            instruction=false
        }
    }
    //save the recipe to the shared preferences based on the favorite icon
    fun saveRecipe(view: View) {
        isFavorite = !isFavorite
        if (isFavorite) {
            iconSave?.setImageResource(R.drawable.heart_icon_filled)
            recipeList[recipeTitle.text.toString()+"§"+recipeTime.text.toString()] = "$portion§$ingredients§$instructions"
            saveMap(this, recipeList)
        } else {
            iconSave?.setImageResource(R.drawable.ic_heart_unfilled)
            recipeList.remove(recipeTitle.text.toString()+"§"+recipeTime.text.toString())
            saveMap(this, recipeList)
        }
    }
    //navigation bar buttons, animation disabled by flags
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