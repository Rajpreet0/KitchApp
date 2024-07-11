package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.fragments.LoadingDialogFragment
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ActivityRecipePreferences : AppCompatActivity() {

    private lateinit var portion: Spinner
    private lateinit var category: Spinner
    private lateinit var time: Spinner
    private lateinit var complexity: Spinner
    private lateinit var nationality: Spinner
    private lateinit var containerWithout: LinearLayout
    private lateinit var containerSpecials: LinearLayout
    private lateinit var withoutList: MutableList<String>
    private lateinit var specialsList: MutableList<String>


    private lateinit var loadingDialog: LoadingDialogFragment
    private  val networkHelper = NetworkHelper()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipepreferences)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingDialog = LoadingDialogFragment()

        portion = findViewById<Spinner>(R.id.spPortions)
        category = findViewById<Spinner>(R.id.spCategory)
        time = findViewById<Spinner>(R.id.spTimerequired)
        complexity = findViewById<Spinner>(R.id.spComplexity)
        nationality = findViewById<Spinner>(R.id.spNationality)
        containerWithout = findViewById(R.id.containerWithout)
        containerSpecials = findViewById(R.id.containerSpecials)


        withoutList = mutableListOf() // TODO: get from storagetype
        specialsList = mutableListOf() // TODO: get from storagetype








        setupSpinner(R.id.spPortions, R.array.portions_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spCategory, R.array.category_array, R.layout.spinner_items_preferences)
        setupSpinner(
            R.id.spTimerequired,
            R.array.timerequired_array,
            R.layout.spinner_items_preferences
        )
        setupSpinner(
            R.id.spComplexity,
            R.array.complexity_array,
            R.layout.spinner_items_preferences
        )
        setupSpinner(
            R.id.spNationality,
            R.array.nationality_array,
            R.layout.spinner_items_preferences
        )




    }

    private fun setupSpinner(spinnerId: Int, arrayResourceId: Int, layoutResourceId: Int) {
        // Create an ArrayAdapter using a string array resource and a custom layout for spinner items
        val adapter = ArrayAdapter.createFromResource(
            this,
            arrayResourceId,
            layoutResourceId
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Initialize the Spinner
        val spinner = findViewById<Spinner>(spinnerId)

        // Apply the adapter to the spinner
        spinner.adapter = adapter
    }

    fun addButton(view: View) {
        if (view.id == R.id.btnWithout) {
            // POPUP
            dialogBuilder("Add Ingredient to Exclude", containerWithout, list = withoutList)

        } else if (view.id == R.id.btnSpecials) {
            // POPUP
            dialogBuilder("Add Special Meal", containerSpecials, list = specialsList)
        }
    }

    fun dialogBuilder (title: String, container: LinearLayout, list: MutableList<String>) {
        try {
            // Inflate the custom layout for the dialog
            val dialogView = layoutInflater.inflate(R.layout.popup_profile, null)
            val ingredientText = dialogView.findViewById<EditText>(R.id.editText_ingredient)

            // Create and show the dialog for adding an ingredient
            AlertDialog.Builder(this).apply {
                setTitle(title)
                setView(dialogView)
                setPositiveButton("Add") { dialog, _ ->
                    val inputIngredient = ingredientText?.text?.toString()?.trim() ?: ""

                    if (inputIngredient.isEmpty()) {
                        // Show a message if the input is empty
                        Toast.makeText(
                            this@ActivityRecipePreferences,
                            "Please fill the field",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Add the ingredient to the view
                        addIngredientToView(inputIngredient, container, list)
                        list += inputIngredient
                        // TODO: Add ingredient to exclude to database
                    }
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                create().show()
            }
        } catch (e: Exception) {
            // Show an error message in case of an exception
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addIngredientToView(ingredient: String, container: LinearLayout, list: MutableList<String>) {
        try {
            // Inflate the custom layout for the ingredient row
            val inflater = LayoutInflater.from(this)
            val rowView = inflater.inflate(R.layout.ingredient_layout_preferences, container, false)

            // Set the ingredient name
            val tvIngredient = rowView.findViewById<TextView>(R.id.tvIngredientName)
            tvIngredient.text = ingredient

            // Handle the remove button click
            val icRemove = rowView.findViewById<ImageView>(R.id.icRemove)
            icRemove.setOnClickListener {
                container.removeView(rowView)
                // TODO: Remove ingredient from LIST
                list.remove(ingredient)
            }

            // Add the row to the container
            container.addView(rowView)

            // Show a confirmation message
            Toast.makeText(this, "Ingredient added: $ingredient", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Show an error message in case of an exception
            Toast.makeText(this, "Error adding ingredient. Please try again.", Toast.LENGTH_SHORT).show()
        }
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

    private fun getRecipeMap(context: Context): MutableMap<String, Int> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("recipeMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, Int>>() {}.type)
        } else {
            mutableMapOf()
        }
    }

    fun nextButton(view: View) {
        val portionTxt = portion.selectedItem as String;
        val categoryTxt = category.selectedItem as String;
        val timeTxt = time.selectedItem as String;
        val complexityTxt = complexity.selectedItem as String;
        val nationalityTxt = nationality.selectedItem as String


        val validationError = ValidationUtil.validateRecipePreferences(
            portionTxt, categoryTxt, timeTxt, complexityTxt, nationalityTxt
        )

        if (validationError != null) {
            Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show()
            return
        }


        val ingredientList = getRecipeMap(this)
        val ingredientString = StringBuilder()
        for ((key, value) in ingredientList) {
            val promptIngredient = key.split("~")[2]+" "+key.split("~")[1]+     //2 is amount 1 is unit 0 is name
                    " "+key.split("~")[0]
            ingredientString.append("$promptIngredient, ")
        }


        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                var without = StringBuilder()

                withoutList.forEach {
                    without.append(it + ", ")
                }

                val withoutString = without.toString()

                var specials = StringBuilder()

                specialsList.forEach {
                    specials.append(it + ", ")
                }

                val specialString = specials.toString()



                val response =  withContext(Dispatchers.IO) {
                    networkHelper.suggestRecipe(
                         portionTxt,
                         categoryTxt,
                         timeTxt,
                         complexityTxt,
                         nationalityTxt,
                         ingredientString.toString(),
                         withoutString,
                         specialString
                    )
                }

                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Log.d("Data for response: ", response.toString())
                    val intent = Intent(applicationContext, ActivitySuggestions::class.java).apply {
                        putExtra("response", response.toString())
                        putExtra("portion", portionTxt)
                        putExtra("category", categoryTxt)
                        putExtra("time", timeTxt)
                        putExtra("complexity", complexityTxt)
                        putExtra("nationality", nationalityTxt)
                        putExtra("ingredientString", ingredientString.toString())
                        putExtra("withoutIngredients", withoutString)
                        putExtra("specialIngredients", specialString)
                    }
                    startActivity(intent)
                }
            } catch (e: IOException) {
                Log.d("SERVER ERROR", "${e}")
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "SERVER ERROR: Prompt Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

// ADD:
//            Log.d("EXCLUDE", it)
//
//
//
//        }