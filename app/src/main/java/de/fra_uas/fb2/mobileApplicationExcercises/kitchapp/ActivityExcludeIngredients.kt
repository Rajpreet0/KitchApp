package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityExcludeIngredients : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var ingredientList: Map<String, Int>

    private val icons = arrayOf(
        R.drawable.freezer_icon,
        R.drawable.fridge_svgrepo_com,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_excludeingredients)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initializing the container
        container = findViewById(R.id.containerIngredients)

        // Initializing ingredientList with sample data, replace with db data later on
        ingredientList = mapOf(
            "Apple" to 1,        // Stored in the fridge
            "Banana" to 2,       // Stored in the pantry
            "Frozen Peas" to 0,  // Stored in the freezer
            "Carrot" to 1,       // Stored in the fridge
            "Bread" to 2         // Stored in the pantry
        )

        for (ingredient in ingredientList) {
            val storageType = ingredient.value
            val name = ingredient.key
            addRow(storageType, name)
        }
    }

    private fun addRow(storageType: Int, nameIngredient: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.exclude_layout_row, container, false)

        // Find the ImageView
        val iconView: ImageView = rowView.findViewById(R.id.icon)

        // Assign the correct icon based on storage type
        if (storageType in icons.indices) {
            val storageIcon = icons[storageType] // 0: Freezer, 1: Fridge
            iconView.setImageResource(storageIcon)
        } else {
            // Hide the icon if storageType is not 0 or 1
            iconView.visibility = View.GONE
        }

        // Find the TextView and set its click listener
        val textView: TextView = rowView.findViewById(R.id.tv_ingredientName)
        textView.text = nameIngredient
        // Set click listener to toggle text color
        textView.setOnClickListener {
            // Check current text color
            val currentColor = (textView.currentTextColor and Color.WHITE)
            if (currentColor == Color.WHITE) {
                // Change text color to red
                textView.setTextColor(Color.RED)
            } else {
                // Change text color back to white
                textView.setTextColor(Color.WHITE)
            }
        }

        // Add the inflated row layout to the container
        container.addView(rowView)
    }

    fun homeButton(view: View) {
        val intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
    }

    fun groceryButton(view: View) {
        val intent = Intent(this, ActivityGrocery::class.java)
        startActivity(intent)
    }

    fun recipesButton(view: View) {
        val intent = Intent(this, ActivityRecipes::class.java)
        startActivity(intent)
    }

    fun profileButton(view: View) {
        val intent = Intent(this, ActivityProfile::class.java)
        startActivity(intent)
    }

    fun nextButton(view: View) {
        val intent = Intent(this, ActivityRecipePreferences::class.java)
        startActivity(intent)
    }
}
