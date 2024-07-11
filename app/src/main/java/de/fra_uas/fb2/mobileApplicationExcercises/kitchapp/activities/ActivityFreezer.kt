package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.ActivityProfile
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R

// This activity handles the grocery list functionality of the KitchApp.
// It allows users to:
// - View a list of ingredients
// - Add new ingredients
// - Edit existing ingredients
// - Delete selected ingredients
// The activity uses a LinearLayout to display ingredients as rows,
// and persists data using SharedPreferences and Gson for serialization.




class ActivityFreezer : AppCompatActivity() {

    // UI elements
    private var containerIngredients: LinearLayout? = null
    private var icSave: ImageView? = null

    // Data structures
    private val ingredientList: MutableMap<String, String> = mutableMapOf()                         //has name and unit as key "name~unit" and amount as value
    private val markedForDeletion = mutableSetOf<String>()

    // State variables
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_freezer)

        // Initialize UI elements
        containerIngredients = findViewById(R.id.containerIngredients)
        icSave = findViewById(R.id.icSave)

        // Initially hide the save button
        icSave?.visibility = View.GONE

        // Load saved ingredients
        ingredientList.putAll(getMap(this))
        // Build the product list UI
        buildProductList()

        // Setup click listener for save button
        icSave?.setOnClickListener {
            saveChanges()
        }
    }

    // Save changes made during editing mode
    private fun saveChanges() {
        setEditTextsEnabled(false)
        icSave?.visibility = View.GONE
        isEditing = false
        ingredientList.clear()
        containerIngredients?.let { container ->
            for (i in 0 until container.childCount) {
                val rowView = container.getChildAt(i)
                val ingredient = rowView.findViewById<EditText>(R.id.tvIngredientName).text.toString()
                val amount = rowView.findViewById<EditText>(R.id.etAmount).text.toString()
                val unit = rowView.findViewById<Spinner>(R.id.spUnit).getSelectedItem().toString()
                ingredientList["$ingredient~$unit"]=amount
                saveMap(this, ingredientList)
            }
        }
    }

    // Hide the soft keyboard
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Enable or disable EditText fields based on editing mode
    private fun setEditTextsEnabled(enabled: Boolean) {
        containerIngredients?.let { container ->
            for (i in 0 until container.childCount) {
                val rowView = container.getChildAt(i)
                rowView.findViewById<EditText>(R.id.tvIngredientName).isEnabled = enabled
                rowView.findViewById<EditText>(R.id.etAmount).isEnabled = enabled
                rowView.findViewById<Spinner>(R.id.spUnit).isEnabled = enabled
            }
        }
    }

    // Build the list of ingredients in the UI
    private fun buildProductList() {
        containerIngredients?.removeAllViews()
        ingredientList.forEach { (ingredient, amount) ->
            addRow(ingredient, amount)
        }
    }

    private fun checkUnit(unit: String): Int {
        if(unit=="pc"){
            return 0
        }else if(unit=="gr"){
            return 1
        }else if(unit=="ml") {
            return 2
        }else return 3
    }

    // Add a row to the ingredient list UI
    private fun addRow(nameAmountIngredient: String, amount: String) {
        val inflater = LayoutInflater.from(this)
        val rowView: View = inflater.inflate(R.layout.ingredient_layout_row, containerIngredients, false)

        // Initialize row elements
        val tvName: EditText = rowView.findViewById(R.id.tvIngredientName)
        val etAmount: EditText = rowView.findViewById(R.id.etAmount)
        val spUnit: Spinner = rowView.findViewById(R.id.spUnit)
        val cbDelete: CheckBox = rowView.findViewById(R.id.cbDelete)

        // Set values
        tvName.setText(nameAmountIngredient.split("~")[0].trim())
        val selection = checkUnit(nameAmountIngredient.split("~")[1].trim())
        etAmount.setText(amount)

        // Setup spinner
        setupSpinner(spUnit, R.array.units, R.layout.spinner_items_unit, selection)

        // Set up listeners
        cbDelete.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) markedForDeletion.add(nameAmountIngredient) else markedForDeletion.remove(nameAmountIngredient)
        }

        tvName.setOnFocusChangeListener { view, hasFocus ->
            tvName.isCursorVisible = hasFocus && isEditing
            if (!hasFocus) hideKeyboard(view)
        }

        etAmount.setOnFocusChangeListener { view, hasFocus ->
            etAmount.isCursorVisible = hasFocus && isEditing
            if (!hasFocus) hideKeyboard(view)
        }

        spUnit.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) hideKeyboard(view)
        }

        // Disable EditText fields initially
        tvName.isEnabled = false
        etAmount.isEnabled = false
        spUnit.isEnabled = false
        cbDelete.isChecked = false

        // Add row to container
        containerIngredients?.addView(rowView)
    }

    // Setup spinner with custom adapter
    private fun setupSpinner(spinner: Spinner, arrayResourceId: Int, layoutResourceId: Int, selection: Int) {
        val adapter = ArrayAdapter.createFromResource(this, arrayResourceId, layoutResourceId)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(selection)
    }

    // Save ingredient list to SharedPreferences
    private fun saveMap(context: Context, map: MutableMap<String, String>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = Gson().toJson(map)
        sharedPreferences.edit().putString("freezerMap", jsonString).apply()
    }

    // Retrieve ingredient list from SharedPreferences
    private fun getMap(context: Context): MutableMap<String, String> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("freezerMap", "")
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, String>>() {}.type)
        } else {
            mutableMapOf()
        }
    }

    // Handle add button click
    fun addButton(view: View) {
        showInputDialog("add")
    }

    // Handle edit button click
    fun editButton(view: View) {
        isEditing = !isEditing
        setEditTextsEnabled(isEditing)
        icSave?.visibility = if (isEditing) View.VISIBLE else View.GONE
    }

    // Handle delete button click
    fun deleteButton(view: View) {
        if (markedForDeletion.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Delete selected ingredients?")
                .setMessage("Are you sure you want to delete the selected ingredients?")
                .setPositiveButton("Delete") { _, _ ->
                    // Remove ingredients from list and UI+
                    markedForDeletion.forEach { ingredient ->
                        ingredientList.remove(ingredient)
                    }
                    saveMap(this, ingredientList)
                    buildProductList()
                    markedForDeletion.clear()
                    Toast.makeText(this, "Ingredients deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "No ingredients selected for deletion", Toast.LENGTH_SHORT).show()
        }
    }


    // Show input dialog for adding new ingredients
    private fun showInputDialog(method: String) {
        val dialogView = layoutInflater.inflate(R.layout.ingredient_dialog_add, null)
        val nameIngredient = dialogView.findViewById<EditText>(R.id.tvIngredientName)
        val amountNumber = dialogView.findViewById<EditText>(R.id.etAmount)
        val spUnit = dialogView.findViewById<Spinner>(R.id.spUnit)

        setupSpinner(spUnit, R.array.units, R.layout.spinner_items_profile, 0)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add ingredient")
            .setPositiveButton("Add") { _, _ ->
                val inputIngredient = nameIngredient.text.toString().trim()
                val inputAmountText = amountNumber.text.toString().trim()
                val selectedUnit = spUnit.selectedItem.toString().trim()
                if (inputIngredient.isNotEmpty() && inputAmountText.isNotEmpty()) {
                    val inputAmount = inputAmountText.toIntOrNull()
                    if (inputAmount != null && inputAmount > 0) {
                        ingredientList["$inputIngredient~$selectedUnit"] = inputAmountText
                        saveMap(this, ingredientList)
                        buildProductList()
                    } else {
                        Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Function to handle the home button click
    fun homeButton(view: View) {
        // Start ActivityHome without animation
        val intent = Intent(this, ActivityHome::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    // Function to handle the grocery button click
    fun groceryButton(view: View) {
        // Start ActivityGrocery without animation
        val intent = Intent(this, ActivityGrocery::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    // Function to handle the recipes button click
    fun recipesButton(view: View) {
        // Start ActivityRecipes without animation
        val intent = Intent(this, ActivityRecipes::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    // Function to handle the profile button click
    fun profileButton(view: View) {
        // Start ActivityProfile without animation
        val intent = Intent(this, ActivityProfile::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }
}