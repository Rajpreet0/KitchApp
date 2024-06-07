package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity_Groceery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_groceery)

    }
    fun homeButton(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, MainActivity_Groceery::class.java)
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, MainActivity_recipes::class.java)
        startActivity(intent)
    }

    fun profileButton(view: View){
        //here you should go to the profile Screen if its added
    }
}