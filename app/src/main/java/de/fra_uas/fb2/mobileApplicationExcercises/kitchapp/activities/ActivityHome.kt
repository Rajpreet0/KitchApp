package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.fragments.LoadingDialogFragment
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ActivityHome : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var tvGreeting: TextView
    private lateinit var btnSuggestions1: AppCompatButton
    private lateinit var btnSuggestions2: AppCompatButton
    private lateinit var progessCircular: ProgressBar

    private var response: String = ""
    private val portion: String = "1"

    private val networkHelper = NetworkHelper()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)
        tvGreeting = findViewById(R.id.tvGreeting);
        btnSuggestions1 = findViewById(R.id.btnSuggestions1);
        btnSuggestions2 = findViewById(R.id.btnSuggestions2)
        progessCircular = findViewById(R.id.progessCircular);

        btnSuggestions1.visibility = View.GONE
        btnSuggestions2.visibility = View.GONE

        tvGreeting.text = "Hi, ${sessionManager.getUsername()}"

        generateSuggestions()

    }

    private fun generateSuggestions() {
         CoroutineScope(Dispatchers.IO).launch {
             try {
                val response = withContext(Dispatchers.IO) {
                    networkHelper.suggestRecipe(
                        portion,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        true,
                        (0..9).random()
                    )
                }

                 withContext(Dispatchers.Main) {
                    this@ActivityHome.response = response.toString()
                     try {
                         val jsonResponse = JSONObject(response.toString())

                         if (jsonResponse.has("recipes") && jsonResponse.get("recipes") is JSONArray) {
                             val recipesArray = jsonResponse.getJSONArray("recipes")

                             val firstRecipe = recipesArray.getJSONObject(0).getJSONArray("recipes").getJSONObject(0)
                             val firstRecipeTitle = firstRecipe.getString("name")

                             btnSuggestions1.setText(firstRecipeTitle)
                             btnSuggestions1.visibility = View.VISIBLE
                             progessCircular.visibility = View.GONE

                             if (recipesArray.length() >= 2) {
                                 val secondRecipe = recipesArray.getJSONObject(1).getJSONArray("recipes").getJSONObject(0)
                                 val secondRecipeTitle = secondRecipe.getString("name")
                                 btnSuggestions2.setText(secondRecipeTitle)
                                 btnSuggestions2.visibility = View.VISIBLE
                             }

                         } else {
                             Log.d("Recipes: ", "Recipes key is not a JSONArray or does not exist")
                             progessCircular.visibility = View.GONE
                             Toast.makeText(applicationContext, "Error loading suggestions", Toast.LENGTH_SHORT).show()
                         }
                     } catch (e: Exception) {
                         Log.e("JSON Parsing Error", "Error parsing JSON response", e)
                         progessCircular.visibility = View.GONE
                         Toast.makeText(applicationContext, "Loading Suggenstions Failed", Toast.LENGTH_SHORT).show()
                     }
                 }
             } catch (e: IOException) {
                 Log.d("SERVER ERROR", "${e}")
                 withContext(Dispatchers.Main) {
                     Toast.makeText(
                         applicationContext,
                         "SERVER ERROR: Prompt Error",
                         Toast.LENGTH_SHORT
                     ).show()
                 }
             }
         }
    }

    private fun changeTwoSugesstions(name: String) {
        val intent: Intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
            putExtra("response", response)
            putExtra("name", name)
            putExtra("portion", portion)
        }
        startActivity(intent)
        finish()
    }

    fun suggestionBtnOne(view: View) {
        changeTwoSugesstions(btnSuggestions1.text.toString())
    }

    fun suggestionBtnTwo(view: View) {
        changeTwoSugesstions(btnSuggestions2.text.toString())
    }

    fun createRecipeButton(view: View){
        val intent = Intent(this, ActivityExcludeIngredients::class.java)
        startActivity(intent)
    }

    fun freezerButton(view: View){
        val intent = Intent(this, ActivityFreezer::class.java)
        startActivity(intent)
    }

    fun fridgeButton(view: View){
        val intent = Intent(this, ActivityFridge::class.java)
        startActivity(intent)
    }

    fun pantryButton(view: View){
        val intent = Intent(this, ActivityPantry::class.java)
        startActivity(intent)
    }
    fun suggestionOneButton(view: View){
        //here you should get to the screen where the recipe is shown in more detail
    }
    fun suggestionTwoButton(view: View){
        //same as in suggestion One
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