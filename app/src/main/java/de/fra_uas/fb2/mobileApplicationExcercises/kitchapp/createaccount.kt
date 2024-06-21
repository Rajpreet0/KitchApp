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

class createaccount : AppCompatActivity() {

    private  val networkHelper = NetworkHelper()
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_createaccount)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        username = findViewById(R.id.editTextText4);
        email = findViewById(R.id.editTextTextEmailAddress3);
        password = findViewById(R.id.editTextTextPassword);
        confirmPassword = findViewById(R.id.editTextTextPasswordconfirmation);
    }
    fun signUpButton(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (password.text.toString() != confirmPassword.text.toString()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Password doesn't match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val response = withContext(Dispatchers.IO) {
                        networkHelper.register(username.text.toString(), email.text.toString(), password.text.toString())
                    }
                    withContext(Dispatchers.Main) {
                        Log.d("Data from Register: ", response.toString())
                        startActivity(intent)
                    }
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
    }

    // Register a User
    fun signInButton(view: View){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
    fun forgotPasswordButton(view: View){
        //to be designed
    }
    fun backButton(view: View){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}