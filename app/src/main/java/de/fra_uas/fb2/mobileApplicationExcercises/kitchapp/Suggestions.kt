package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Suggestions : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.suggestions)


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
    fun nextButton(view: View){
        val intent = Intent(this, RecipeDisplayActivity::class.java)
        startActivity(intent)
    }
}