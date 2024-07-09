package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities.ActivityGrocery
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities.ActivityHome
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities.ActivityRecipes


// This activity handles the user profile, allowing the user to edit their name and email,
// navigate to other sections of the app, and manage a list of ingredients to exclude.
class ActivityProfile : AppCompatActivity() {

    // Declaring views that will be used in the activity
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var icSave: ImageView
    private lateinit var containerIngr: LinearLayout

    // Tracks whether the profile is being edited or not
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views by finding them by their ID
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        icSave = findViewById(R.id.iconSave)
        containerIngr = findViewById(R.id.containerIngredients)

        // Set initial state of EditTexts to be disabled
        setEditTextsEnabled(false)

        // Setup the language selection spinner
        setupSpinner(R.id.spLanguage, R.array.languages, R.layout.spinner_items_profile)
    }

    // Function to setup the spinner with provided parameters
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

    // Function to handle the edit button click
    fun profileEditButton(view: View) {
        // Toggle the editing state
        isEditing = !isEditing

        // Enable or disable EditTexts based on the editing state
        setEditTextsEnabled(isEditing)

        // Show or hide the save icon based on the editing state
        icSave.visibility = if (isEditing) View.VISIBLE else View.GONE

        // Update visibility of remove buttons for ingredients
        updateRemoveButtonsVisibility()
    }

    // Function to handle the save button click
    fun profileSaveButton(view: View) {
        // If currently editing, save the changes
        if (isEditing) {
            isEditing = false

            // Disable the EditTexts
            setEditTextsEnabled(false)

            // Hide the save icon
            icSave.visibility = View.GONE

            // Update visibility of remove buttons for ingredients
            updateRemoveButtonsVisibility()

            // TODO: Save profile changes to database

            // Show a confirmation message
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to enable or disable the EditTexts
    private fun setEditTextsEnabled(enabled: Boolean) {
        etName.isEnabled = enabled
        etName.isFocusable = enabled
        etName.isFocusableInTouchMode = enabled
        etEmail.isEnabled = enabled
        etEmail.isFocusable = enabled
        etEmail.isFocusableInTouchMode = enabled
    }

    // Function to handle the add ingredient button click
    fun addExclButton(view: View) {
        try {
            // Inflate the custom layout for the dialog
            val dialogView = layoutInflater.inflate(R.layout.popup_profile, null)
            val ingredientText = dialogView.findViewById<EditText>(R.id.editText_ingredient)

            // Create and show the dialog for adding an ingredient
            AlertDialog.Builder(this).apply {
                setTitle("Add ingredient to exclude")
                setView(dialogView)
                setPositiveButton("Add") { dialog, _ ->
                    val inputIngredient = ingredientText?.text?.toString()?.trim() ?: ""

                    if (inputIngredient.isEmpty()) {
                        // Show a message if the input is empty
                        Toast.makeText(
                            this@ActivityProfile,
                            "Please fill the field",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Add the ingredient to the view
                        addIngredientToView(inputIngredient)
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

    // Function to update the visibility of remove buttons for ingredients
    private fun updateRemoveButtonsVisibility() {
        for (i in 0 until containerIngr.childCount) {
            val childView = containerIngr.getChildAt(i)
            val icRemove = childView.findViewById<ImageView>(R.id.icRemove)
            icRemove.visibility = if (isEditing) View.VISIBLE else View.GONE
        }
    }

    // Function to add an ingredient to the view
    private fun addIngredientToView(ingredient: String) {
        try {
            // Inflate the custom layout for the ingredient row
            val inflater = LayoutInflater.from(this)
            val rowView = inflater.inflate(R.layout.profile_ingredient_layout, containerIngr, false)

            // Set the ingredient name
            val tvIngredient = rowView.findViewById<TextView>(R.id.tvIngredientName)
            tvIngredient.text = ingredient

            // Handle the remove button click
            val icRemove = rowView.findViewById<ImageView>(R.id.icRemove)
            icRemove.setOnClickListener {
                containerIngr.removeView(rowView)
                // TODO: Remove ingredient from database
            }

            // Add the row to the container
            containerIngr.addView(rowView)

            // Show a confirmation message
            Toast.makeText(this, "Ingredient added: $ingredient", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Show an error message in case of an exception
            Toast.makeText(this, "Error adding ingredient. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }


    fun logoutButton(view: View) {
        // TODO: LOGOUT USER
    }

    fun deleteAccountButton(view: View) {
        //TODO: DELETE ACCOUNT
    }
}