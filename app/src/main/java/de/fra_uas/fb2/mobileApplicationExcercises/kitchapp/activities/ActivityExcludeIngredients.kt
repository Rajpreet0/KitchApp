package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R

class ActivityExcludeIngredients : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private val ingredientList: MutableMap<String, Int> = mutableMapOf()

    private val icons = arrayOf(
        R.drawable.ic_freezer,
        R.drawable.ic_fridge,
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

        val pantryMap = getPantryMap(this)
        val freezerMap = getFreezerMap(this)
        val fridgeMap = getFridgeMap(this)

        //the pantry items get icon 2
        for((key, value) in pantryMap) {
            ingredientList["$key~$value"] = 2
        }
        //the freezer items get icon 0
        for((key, value) in freezerMap) {
            ingredientList["$key~$value"] = 0
        }
        //the fridge items get icon 1
        for((key, value) in fridgeMap) {
            ingredientList["$key~$value"] = 1
        }

        for (ingredient in ingredientList) {
            val storageType = ingredient.value
            val name = ingredient.key.split("~")[0]
            addRow(storageType, name)
        }
        saveMap(this, ingredientList)


        // checkbox for excluding all ingrediens
        val excludeAll: CheckBox = findViewById(R.id.button3)

        // Set an OnCheckedChangeListener
        excludeAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Checkbox is checked
                ingredientList.clear()
                saveMap(this, ingredientList)

                container.children.forEach { view ->
                    val textView = view.findViewById<TextView>(R.id.tvIngredientName)
                    textView.setTextColor(Color.RED)
                }

            } else {
                // Checkbox is unchecked
                //the pantry items get icon 2
                for((key, value) in pantryMap) {
                    ingredientList["$key~$value"] = 2
                }
                //the freezer items get icon 0
                for((key, value) in freezerMap) {
                    ingredientList["$key~$value"] = 0
                }
                //the fridge items get icon 1
                for((key, value) in fridgeMap) {
                    ingredientList["$key~$value"] = 1
                }

                saveMap(this, ingredientList)

                container.children.forEach { view ->
                    val textView = view.findViewById<TextView>(R.id.tvIngredientName)
                    textView.setTextColor(Color.WHITE)
                }
            }
        }

    }


    private fun saveMap(context: Context, map: MutableMap<String, Int>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the map to a JSON string
        val jsonString = Gson().toJson(map)
        editor.putString("recipeMap", jsonString)
        editor.apply()
    }


    private fun getPantryMap(context: Context): MutableMap<String, Int> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("pantryMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, Int>>() {}.type)
        } else {
            mutableMapOf()
        }
    }
    private fun getFreezerMap(context: Context): MutableMap<String, Int> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("freezerMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, Int>>() {}.type)
        } else {
            mutableMapOf()
        }
    }
    private fun getFridgeMap(context: Context): MutableMap<String, Int> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("fridgeMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, Int>>() {}.type)
        } else {
            mutableMapOf()
        }
    }

    private fun addRow(storageType: Int, nameIngredient: String) {
        // Inflate the row layout
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.exclude_layout_row, container, false)

        // Find the ImageView
        val iconView: ImageView = rowView.findViewById(R.id.icStorageType)

        // Assign the correct icon based on storage type
        if (storageType in icons.indices) {
            val storageIcon = icons[storageType] // 0: Freezer, 1: Fridge
            iconView.setImageResource(storageIcon)
        } else {
            // Hide the icon if storageType is not 0 or 1
            iconView.visibility = View.GONE
        }

        // Find the TextView and set its click listener
        val textView: TextView = rowView.findViewById(R.id.tvIngredientName)
        textView.text = nameIngredient
        // Set click listener to toggle text color
        textView.setOnClickListener {
            // Check current text color
            val currentColor = (textView.currentTextColor and Color.WHITE)
            if (currentColor == Color.WHITE) {
                // Change text color to red
                textView.setTextColor(Color.RED)
                // Remove the ingredient from the map
                if (ingredientList.containsKey(textView.text)) {
                    ingredientList.remove(textView.text)
                    saveMap(this, ingredientList)
                }
            } else {
                    // Change text color back to white
                    textView.setTextColor(Color.WHITE)
                    val ingredient = textView.text.toString()
                    ingredientList[ingredient] = 1
                    saveMap(this, ingredientList)

            }
        }

        // Add the inflated row layout to the container
        container.addView(rowView)
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

    fun nextButton(view: View) {
        val intent = Intent(this, ActivityRecipePreferences::class.java)
        startActivity(intent)
    }
}
