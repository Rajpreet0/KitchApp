package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {

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
        val email = "test2@example.com"
        val password = "password123"

        // Use a CoroutineScope to run the network request on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the sendRequest function to get the server response
                val response = networkHelper.sendOpenAIRequest(query)
                 val responseLogin = networkHelper.login(email, password)
                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    Log.d("Data from ChatGPT: ", response)
                    Log.d("Data from Login: ", responseLogin.toString())
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Log.d("Data from ChatGPT: ", "FAILED")
                    Log.d("Data from Login: ", "FAILED")
                }
            }
        }
    }
    fun createRecipeButton(view: View){
        val intent = Intent(this, MainActivityRecipes::class.java)
        startActivity(intent)
    }

    fun freezerButton(view: View){
        val intent = Intent(this, MainActivityFreezer::class.java)
        startActivity(intent)
    }

    fun fridgeButton(view: View){
        val intent = Intent(this, MainActivityFridge::class.java)
        startActivity(intent)
    }

    fun pantryButton(view: View){
        val intent = Intent(this, MainActivityPantry::class.java)
        startActivity(intent)
    }
    fun suggestionOneButton(view: View){
        //here you should get to the screen where the recipe is shown in more detail
    }
    fun suggestionTwoButton(view: View){
        //same as in suggestion One
    }

    fun homeButton(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, MainActivityGrocery::class.java)
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, MainActivityRecipes::class.java)
        startActivity(intent)
    }

    fun profileButton(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

}