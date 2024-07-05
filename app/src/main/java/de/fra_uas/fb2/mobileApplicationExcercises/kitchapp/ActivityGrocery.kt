package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ActivityGrocery : AppCompatActivity() {
    private var productText: LinearLayout? = null
    private var productAmount: LinearLayout? = null

    private val ingredientList: MutableMap<String, Int> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_grocery)
        productText = findViewById(R.id.leftLayout)
        productAmount = findViewById(R.id.rightLayout)
        ingredientList.putAll(getMap(this))
        buildProductList()
    }

                             //List to store the items+amount
    //this function removes the old ingredient list and puts the new one into the text fields
    private fun buildProductList(){
        productText!!.removeAllViews()
        productAmount!!.removeAllViews()
        ingredientList.forEach{
                (ingredient, amount) ->
            val ingredientTextView = TextView(this)
            val amountTextView = TextView(this)
            ingredientTextView.textSize = 20f                                                       //improves readability
            amountTextView.textSize = 20f
            ingredientTextView.text = ingredient
            amountTextView.text = amount.toString()
            productText!!.addView(ingredientTextView)
            productAmount!!.addView(amountTextView)
        }
    }

    private fun saveMap(context: Context, map: MutableMap<String, Int>) {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the map to a JSON string
        val jsonString = Gson().toJson(map)
        editor.putString("groceryMap", jsonString)
        editor.apply()
    }

    private fun getMap(context: Context): MutableMap<String, Int> {
        val sharedPreferences = context.getSharedPreferences("StorageMaps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("groceryMap", "")

        // Convert the JSON string back to a map
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, Int>>() {}.type)
        } else {
            mutableMapOf()
        }
    }

    //function for the pop up that is shown after edit/add/delete button pressed
    private fun showInputDialog(method: String) {
        val dialogView = layoutInflater.inflate(R.layout.popup_add_ingredient, null)
        val ingredientText = dialogView.findViewById<EditText>(R.id.editText_ingredient)
        val amountText = dialogView.findViewById<EditText>(R.id.editText_amount)
        //if add is pressed we get the add dialog that updates the list with the new ingredient
        if(method == "add") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add ingredient")
            builder.setView(dialogView)
            builder.setPositiveButton("Add") { dialog, _ ->
                val inputIngredient = ingredientText.text.toString()
                val inputAmountText = amountText.text.toString()
                if (inputIngredient.isEmpty() || inputAmountText.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val inputAmount = inputAmountText.toIntOrNull()
                    if (inputAmount != null&&inputAmount>0) {
                        ingredientList[inputIngredient] = inputAmount
                        saveMap(this, ingredientList)
                        buildProductList()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
            //if delete is pressed we delete the demanded ingredient
        }else if(method == "delete"){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete ingredient")
            builder.setView(dialogView)
            amountText.visibility = View.GONE
            builder.setPositiveButton("Delete") { dialog, _ ->
                val inputIngredient= ingredientText.text.toString()
                if (inputIngredient.isEmpty()) {
                    Toast.makeText(this, "Please enter an ingredient", Toast.LENGTH_SHORT).show()
                } else {
                    if (ingredientList.containsKey(inputIngredient)) {
                        ingredientList.remove(inputIngredient)
                        saveMap(this, ingredientList)
                        buildProductList()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a valid ingredient", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
            //if edit is pressed we can add or remove amounts from ingredients -> remove ingredient if amount is <=0
        }else if(method == "edit"){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Edit ingredient")
            builder.setView(dialogView)
            amountText.hint = "Amount to remove or add"
            builder.setPositiveButton("Confirm") { dialog, _ ->
                val inputIngredient= ingredientText.text.toString()
                val inputAmountText = amountText.text.toString()
                if (inputIngredient.isEmpty()||inputAmountText.isEmpty()) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val inputAmount = inputAmountText.toIntOrNull()
                    if (inputAmount != null) {
                        ingredientList[inputIngredient] = ingredientList[inputIngredient]!! + inputAmount
                        if(ingredientList[inputIngredient]!! <=0){
                            ingredientList.remove(inputIngredient)
                            Toast.makeText(this, "ingredient removed ", Toast.LENGTH_SHORT).show()
                            saveMap(this, ingredientList)
                        }
                        buildProductList()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a valid ingredient", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }


    // Buttons for switching activities
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
    fun addButton(view: View){
        showInputDialog("add")
    }
    fun editButton(view: View){
        showInputDialog("edit")
    }
    fun deleteButton(view: View){
        showInputDialog("delete")
    }
}