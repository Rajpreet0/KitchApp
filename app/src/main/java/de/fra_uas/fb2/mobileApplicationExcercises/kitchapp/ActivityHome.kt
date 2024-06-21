package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ActivityHome : AppCompatActivity() {

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



        val query = "Hi ChatGPT, whats up!"


        // Use a CoroutineScope to run the network request on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the sendRequest function to get the server response
                val response = networkHelper.sendOpenAIRequest(query)
                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    Log.d("Data from ChatGPT: ", response)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Log.d("Data from ChatGPT: ", "FAILED")
                }
            }
        }
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
        val intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, ActivityGrocery::class.java)
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, ActivityRecipes::class.java)
        startActivity(intent)
    }

    fun profileButton(view: View){
        val intent = Intent(this, ActivityProfile::class.java)
        startActivity(intent)
    }

}