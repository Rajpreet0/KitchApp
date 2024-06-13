package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivityFreezer : AppCompatActivity() {
    private var productText: LinearLayout? = null
    private var productAmount: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_freezer)
        productText = findViewById(R.id.leftLayout)
        productAmount = findViewById(R.id.rightLayout)
    }
    private val products = mutableListOf<String>()                                                  //product list
    private val amounts = mutableListOf<String>()                                                   //amount list

    private fun showInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_add_ingredient, null)
        val ingredientText = dialogView.findViewById<EditText>(R.id.editText_ingredient)
        val amountText = dialogView.findViewById<EditText>(R.id.editText_amount)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add ingredient")
        builder.setView(dialogView)
        builder.setPositiveButton("Add") { dialog, _ ->
            val inputIngredient = ingredientText.text.toString()
            val inputAmount = amountText.text.toString()
            products.add(inputIngredient)
            amounts.add(inputAmount)
            val ingredientTextView = TextView(this)
            val amountTextView = TextView(this)
            ingredientTextView.text = inputIngredient
            amountTextView.text = inputAmount
            productText!!.addView(ingredientTextView)
            productAmount!!.addView(amountTextView)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }


    // Buttons for switching activities
    fun homeButton(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun groceryButton(view: View){
        val intent = Intent(this, MainActivityGrocery::class.java)
        startActivity(intent)
    }
    fun recipesButton(view: View){
        val intent = Intent(this, MainActivityRecipes::class.java)
        startActivity(intent)
    }
    fun profileButton(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
    fun addButton(view: View){
        showInputDialog()
    }
    fun editButton(view: View){
        //should enable the text field to add/delete ingredients
    }
}