package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.graphics.Color
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ActivityProfile : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var icSave: ImageView
    private lateinit var containerIngr: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        icSave = findViewById(R.id.iconSave)
        containerIngr = findViewById(R.id.containerIngredients)

        setupSpinner(R.id.spLanguage, R.array.languages, R.layout.spinner_items_profile)
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

    fun homeButton(view: View) {
        val intent = Intent(this, ActivityHome::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    fun groceryButton(view: View) {
        val intent = Intent(this, ActivityGrocery::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    fun recipesButton(view: View) {
        val intent = Intent(this, ActivityRecipes::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    fun profileButton(view: View) {
        val intent = Intent(this, ActivityProfile::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
    }

    // Tracks whether the profile is being edited or not
    private var isEditing = false

    fun profileEditButton(view: View) {
        isEditing = !isEditing
        etName.isEnabled = isEditing
        etName.isFocusable = isEditing
        etName.isFocusableInTouchMode
        etEmail.isFocusableInTouchMode
        etEmail.isEnabled = isEditing
        etEmail.isFocusable = isEditing

        // TODO: Enable editing of profile details

        icSave.visibility = if (isEditing) View.VISIBLE else View.GONE
        updateRemoveButtonsVisibility()
    }


    fun profileSaveButton(view: View) {
        if (isEditing) {
            isEditing = false
            etName.isEnabled = false
            etName.isFocusable = false
            etEmail.isEnabled = false
            etEmail.isFocusable = false
            icSave.visibility = View.GONE
            updateRemoveButtonsVisibility()

            // TODO: Save profile changes to database

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        }
    }

    fun addExclButton(view: View) {
        try {
            val dialogView = layoutInflater.inflate(R.layout.popup_profile, null)
            val ingredientText = dialogView.findViewById<EditText>(R.id.editText_ingredient)

            AlertDialog.Builder(this).apply {
                setTitle("Add ingredient to exclude")
                setView(dialogView)
                setPositiveButton("Add") { dialog, _ ->
                    val inputIngredient = ingredientText?.text?.toString()?.trim() ?: ""

                    if (inputIngredient.isEmpty()) {
                        Toast.makeText(
                            this@ActivityProfile,
                            "Please fill the field",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
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
            Log.e("AddExclButton", "Error in addExclButton: ${e.message}")
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateRemoveButtonsVisibility() {
        Log.d("UpdateVisibility", "updateRemoveButtonsVisibility called. isEditing: $isEditing")
        for (i in 0 until containerIngr.childCount) {
            val childView = containerIngr.getChildAt(i)
            val icRemove = childView.findViewById<ImageView>(R.id.icRemove)
            icRemove.visibility = if (isEditing) View.VISIBLE else View.GONE
            Log.d("UpdateVisibility", "Ingredient $i visibility: ${icRemove.visibility}")
        }
    }


    private fun addIngredientToView(ingredient: String) {
        try {
            val inflater = LayoutInflater.from(this)
            val rowView = inflater.inflate(R.layout.profile_ingredient_layout, containerIngr, false)

            val tvIngredient = rowView.findViewById<TextView>(R.id.tvIngredientName)
            tvIngredient.text = ingredient

            val icRemove = rowView.findViewById<ImageView>(R.id.icRemove)

            icRemove.setOnClickListener {
                containerIngr.removeView(rowView)
                // TODO: Remove ingredient from database
            }

            containerIngr.addView(rowView)

            Toast.makeText(this, "Ingredient added: $ingredient", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("AddIngredient", "Error adding ingredient: ${e.message}")
            Toast.makeText(this, "Error adding ingredient. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }
}