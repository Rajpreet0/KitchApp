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
import org.w3c.dom.Text

class MainActivityFridge : AppCompatActivity() {
    private var productText: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_fridge)
        productText = findViewById(R.id.ingredientsLayout)

    }
    private val products = mutableListOf<String>()                                                  //products list to generate the text views

    private fun showInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_input, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText_input)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Input Dialog")
        builder.setView(dialogView)
        builder.setPositiveButton("OK") { dialog, _ ->
            val inputText = editText.text.toString()
            products.add(inputText)
            val textView = TextView(this)
            textView.text = inputText
            productText!!.addView(textView)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

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
        //here you should go to the profile Screen if its added
    }
    fun editButton(view: View){
        //edit functions
    }
    fun addButton(view: View){
        //add functions
        showInputDialog()
    }
}