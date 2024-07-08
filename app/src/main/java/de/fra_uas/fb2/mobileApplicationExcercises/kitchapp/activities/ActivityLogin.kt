package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.fragments.LoadingDialogFragment
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper

class ActivityLogin : AppCompatActivity() {

    private  val networkHelper = NetworkHelper()
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loadingDialog: LoadingDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)

        loadingDialog = LoadingDialogFragment()
    }

    fun signUpButton(view: View){
        val intent = Intent(this, ActivityCreateAcount::class.java)
        startActivity(intent)
    }

    /*
    *  Two Users are in the DB here is the Login Data
    *
    *   email: test2@example.com
    *   password: password123
    *
    * */

    fun loginButton(view: View){
        val intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
        /*CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                val response = networkHelper.login(email.text.toString(), password.text.toString())
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Log.d("Data from Login: ", response.toString())
                    startActivity(intent)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Log.d("SERVER ERROR", "Login Failed - ${e}")
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }*/
    }
    fun forgotPasswordButton(view: View){
        //to be designed
    }
}


/*** EXAMPLE JSON RESPONSE ***/
/*

{
  "recipes": [
    {
      "recipes": [
        {
          "name": "Paprika Salsa Noodles",
          "description": "Noodles with a paprika salsa twist",
          "time": "30",
          "ingredients": [
            "paprika",
            "salsa",
            "pepper",
            "noodles"
          ],
          "instructions": "1. Cook noodles. 2. Sauté paprika with salsa and pepper. 3. Mix noodles with the paprika salsa sauce."
        },
        {
          "name": "Spicy Veggie Noodles",
          "description": "Noodles with rich spicy flavors",
          "time": "30",
          "ingredients": [
            "paprika",
            "salsa",
            "pepper",
            "noodles"
          ],
          "instructions": "1. Cook noodles. 2. Make a sauce with salsa, paprika, and pepper. 3. Toss noodles in the sauce."
        },
        {
          "name": "Peppery Salsa Pasta",
          "description": "Pasta with a vibrant salsa mix",
          "time": "30",
          "ingredients": [
            "paprika",
            "salsa",
            "pepper",
            "noodles"
          ],
          "instructions": "1. Cook noodles. 2. Stir-fry paprika with salsa and pepper. 3. Combine noodles with the stir-fried salsa."
        }
      ]
    }
  ]
}

 */