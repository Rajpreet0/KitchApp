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
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R

// BEGIN: Daria
class ActivityExcludeIngredients : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private val ingredientList: MutableMap<String, Int> = mutableMapOf() // Ron

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
        // END: Daria
        // BEGIN: Ron
        val pantryMap = getPantryMap(this)                                                   //fetching all the storage maps from the shared preferences
        val freezerMap = getFreezerMap(this)
        val fridgeMap = getFridgeMap(this)

        //the pantry items get icon 2
        for((key, value) in pantryMap) {
            ingredientList["$key§$value"] = 2
        }
        //the freezer items get icon 0
        for((key, value) in freezerMap) {
            ingredientList["$key§$value"] = 0
        }
        //the fridge items get icon 1
        for((key, value) in fridgeMap) {
            ingredientList["$key§$value"] = 1
        }
        //if the list is empty, show a message
        if(ingredientList.isEmpty()){
            addRow(2, "Please put some")
            addRow(2, "ingredients in")
            addRow(2, "your storages.")
            addRow(2, "Otherwise generation")
            addRow(2, "will be randomly!")
        }else {
            for (ingredient in ingredientList) {
                val storageType = ingredient.value // Daria
                val name = ingredient.key.split("§")[0]
                addRow(storageType, name) // Daria
            }
            saveMap(this, ingredientList)
        }
        // END: RON

        // BEGIN: Daria
        // checkbox for excluding all ingredients
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
        // END: Daria
        // BEGIN: Ron
            } else {
                // Checkbox is unchecked
                //the pantry items get icon 2
                for((key, value) in pantryMap) {
                    ingredientList["$key§$value"] = 2
                }
                //the freezer items get icon 0
                for((key, value) in freezerMap) {
                    ingredientList["$key§$value"] = 0
                }
                //the fridge items get icon 1
                for((key, value) in fridgeMap) {
                    ingredientList["$key§$value"] = 1
                }
                // END: Ron

                // BEGIN: Daria
                saveMap(this, ingredientList)

                container.children.forEach { view ->
                    val textView = view.findViewById<TextView>(R.id.tvIngredientName)
                    textView.setTextColor(Color.WHITE)
                }
            }
        }

    }
    // END: Daria

    // BEGIN: Ron
    private fun saveMap(context: Context, map: MutableMap<String, Int>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the map to a JSON string
        val jsonString = Gson().toJson(map)
        editor.putString("recipeMap", jsonString)
        editor.apply()
    }
    //functions to get the maps from the shared preferences
    private fun getPantryMap(context: Context): MutableMap<String, Int> {
        //we open the shared preferences and we get the JSONstring of the map
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
    // END: Ron

    // BEGIN: Daria
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
            } else {
                    // Change text color back to white
                    textView.setTextColor(Color.WHITE)
            }
        }

        // Add the inflated row layout to the container
        container.addView(rowView)
    }
    // END: Daira

    // BEGIN: Ron, Raj (flags)
    fun homeButton(view: View){
        val intent = Intent(this, ActivityHome::class.java).apply {          //go to home page
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, ActivityGrocery::class.java).apply {       //go to grocery page
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }
    fun recipesButton(view: View){                                                                  //go to recipes page
        val intent = Intent(this, ActivityRecipes::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    fun profileButton(view: View){                                                                  //go to profile page
        val intent = Intent(this, ActivityProfile::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    // END: Ron, Raj

    // BEGIN: Ron
    //function to check if an ingredient is excluded and remove it from the map
    //called if the user clicks the next button
    private fun checkExcluded() {
        container.children.forEach { view ->
            val textView = view.findViewById<TextView>(R.id.tvIngredientName)
            val name =textView.text.toString()                                                      //get the name of the ingredient
            if (textView.currentTextColor == Color.RED) {                                           //check if the ingredient is excluded(RED)
                var safeKey = ""                                                                    //variable to store the key to remove the ingredient from the map
                for ((key, value) in ingredientList) {                                              //loop through the map
                    if (key.split("§")[0] == name) {                                      //check if the ingredient is excluded
                        safeKey = key                                                               //store the key to remove the ingredient from the map
                    }
                }
                if (ingredientList.containsKey(safeKey)) {                                          //remove the ingredient from the map
                    ingredientList.remove(safeKey)
                    saveMap(this, ingredientList)
                }
            }
        }
    }
    fun nextButton(view: View) {                                                                    //go to recipe preferences page
        val intent = Intent(this, ActivityRecipePreferences::class.java)
        checkExcluded()
        startActivity(intent)
    }

    // END: Ron
}
