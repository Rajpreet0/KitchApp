package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

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
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.fragments.LoadingDialogFragment
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class ActivityCreateAcount : AppCompatActivity() {

    // BEGIN: Raj
    private  val networkHelper = NetworkHelper()
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var loadingDialog: LoadingDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_createaccount)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingDialog = LoadingDialogFragment()
        // END: Raj

        // BEGIN: Daria
        username = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etPasswordconfirmation);
        // END: Daria

    }

    // BEGIN: Raj
    fun signUpButton(view: View){

        val validationError = ValidationUtil.validateRegisterInput(
            username.text.toString(),
            email.text.toString(),
            password.text.toString(),
            confirmPassword.text.toString())

        if (validationError != null) {
            Toast.makeText(applicationContext, validationError, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ActivityLogin::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                    val response = withContext(Dispatchers.IO) {
                        networkHelper.register(username.text.toString(), email.text.toString(), password.text.toString())
                    }
                    withContext(Dispatchers.Main) {
                        loadingDialog.dismiss()
                        Toast.makeText(applicationContext, "Successfully registered! Please login", Toast.LENGTH_SHORT).show()
                        Log.d("Data from Register: ", response.toString())
                        startActivity(intent)
                        finish()
                    }
            } catch (e: IOException) {
                Log.d("SERVER ERROR", "${e}")
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "User can't be registered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    // END: Raj

    // BEGIN: Ron
    // Register a User
    fun signInButton(view: View){                                                                   //go to login page
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
    }

    fun backButton(view: View){                                                                     //go back to login page
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
    }
}

// END: Ron