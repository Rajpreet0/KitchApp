package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity_Freezer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_freezer)

    }

    fun editFreezer(view: View) {
        //val intent = Intent(this, EditFreezer::class.java)
        //startActivity(intent)
    }
}