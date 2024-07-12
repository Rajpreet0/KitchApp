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
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
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
    // BEGIN: Raj
    private lateinit var sessionManager: SessionManager
    private lateinit var tvGreeting: TextView
    private lateinit var btnSuggestions1: AppCompatButton
    private lateinit var progessCircular: ProgressBar

    private var error: Boolean = false
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
        progessCircular = findViewById(R.id.progessCircular);

        btnSuggestions1.visibility = View.GONE

        tvGreeting.text = "Hi, ${sessionManager.getUsername()}"

        generateSuggestions()

    }

    // Function to generate random recipe Suggestion
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

                             btnSuggestions1.text = firstRecipeTitle
                             btnSuggestions1.visibility = View.VISIBLE
                             progessCircular.visibility = View.GONE


                         } else {
                             Log.d("Recipes: ", "Recipes key is not a JSONArray or does not exist")
                             withContext(Dispatchers.Main) {
                                 progessCircular.visibility = View.GONE
                                 btnSuggestions1.visibility = View.VISIBLE
                                 btnSuggestions1.text = "Oops something went wrong"
                             }
                             error = true
                         }
                     } catch (e: Exception) {
                         Log.e("JSON Parsing Error", "Error parsing JSON response", e)
                         withContext(Dispatchers.Main) {
                             progessCircular.visibility = View.GONE
                             btnSuggestions1.visibility = View.VISIBLE
                             btnSuggestions1.text = "Oops something went wrong"
                         }
                         error = true
                     }
                 }
             } catch (e: IOException) {
                 Log.d("SERVER ERROR", "${e}")
                 withContext(Dispatchers.Main) {
                     progessCircular.visibility = View.GONE
                     btnSuggestions1.visibility = View.VISIBLE
                     btnSuggestions1.text = "Oops something went wrong"
                 }
                 error = true

             }
         }
    }


    fun suggestionBtnOne(view: View) {
        if(error) {
            generateSuggestions()
            progessCircular.visibility = View.VISIBLE
            btnSuggestions1.visibility = View.GONE
            error = true
        } else {
            val intent: Intent = Intent(this, ActivityRecipeDisplay::class.java).apply {
                putExtra("response", response)
                putExtra("name", btnSuggestions1.text.toString())
                putExtra("portion", portion)
            }
            startActivity(intent)
            finish()
        }
    }
    // END: Raj

    // BEGIN: Ron
    fun createRecipeButton(view: View){                                                             //go to create recipe screen
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
    // END: Ron

    // BEGIN:  Raj
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
    // END: Raj

}