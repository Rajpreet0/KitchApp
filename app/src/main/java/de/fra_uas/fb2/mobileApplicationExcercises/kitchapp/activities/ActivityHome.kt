package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ActivityHome : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var tvGreeting: TextView

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

        tvGreeting.text = "Hi, ${sessionManager.getUsername()}"

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