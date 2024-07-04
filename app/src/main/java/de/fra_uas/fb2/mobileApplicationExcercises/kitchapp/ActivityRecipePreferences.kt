package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        portion = findViewById<Spinner>(R.id.spPortions);
        category = findViewById<Spinner>(R.id.spCategory);
        time = findViewById<Spinner>(R.id.spTimerequired);
        complexity = findViewById<Spinner>(R.id.spComplexity);
        nationality = findViewById<Spinner>(R.id.spNationality);

        setupSpinner(R.id.spPortions, R.array.portions_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spCategory, R.array.category_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spTimerequired, R.array.timerequired_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spComplexity, R.array.complexity_array, R.layout.spinner_items_preferences)
        setupSpinner(R.id.spNationality, R.array.nationality_array, R.layout.spinner_items_preferences)


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
    fun nextButton(view: View) {
        val portionTxt = portion.selectedItem as String;
        val categoryTxt = category.selectedItem as String;
        val timeTxt = time.selectedItem as String;
        val complexityTxt = complexity.selectedItem as String;
        val nationalityTxt = nationality.selectedItem as String


        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    networkHelper.suggestRecipe(portionTxt, categoryTxt, timeTxt, complexityTxt, nationalityTxt, "tomato, rice, basil, potatos, cooking creme, paprika, salt, pepper","peanuts, curry", "keto")
                }
                withContext(Dispatchers.Main) {
                    Log.d("Data for response: ", response.toString())
                    val intent = Intent(applicationContext, ActivitySuggestions::class.java).apply {
                        putExtra("response", response.toString())
                    }
                    startActivity(intent)
                }
            } catch (e: IOException) {
                Log.d("SERVER ERROR", "${e}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "User can't be registered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        startActivity(intent)
    }
}