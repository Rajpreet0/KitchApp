package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class ActivityLogin : AppCompatActivity() {

    private  val networkHelper = NetworkHelper()
    private lateinit var email: EditText
    private lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.editTextTextEmailAddress3)
        password = findViewById(R.id.editTextTextPassword)
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
        /*
>>>>>>> main:app/src/main/java/de/fra_uas/fb2/mobileApplicationExcercises/kitchapp/Login.kt
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkHelper.login(email.text.toString(), password.text.toString())
                withContext(Dispatchers.Main) {
                    Log.d("Data from Login: ", response.toString())
                    startActivity(intent)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Log.d("SERVER ERROR", "Login Failed - ${e}")
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
         */
    }
    fun forgotPasswordButton(view: View){
        //to be designed
    }
}