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
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.SessionManager
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
//BEGIN_Raj
class ActivityResetPassword : AppCompatActivity() {

    private lateinit var editTextPassword: EditText
    private lateinit var editTextNewPassword: EditText

    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var sessionManager: SessionManager
    private lateinit var email: String
    private  val networkHelper = NetworkHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextPassword = findViewById(R.id.editTextPassword)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)

        email = intent.getStringExtra("email").toString()

        loadingDialog = LoadingDialogFragment()
        sessionManager = SessionManager(this)
    }

    fun updatePassword(view: View) {
        val validationError = ValidationUtil.validateUpdatePassword(editTextPassword.text.toString(), editTextNewPassword.text.toString())

        if (validationError != null) {
            Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ActivityLogin::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                val response = networkHelper.updatePassword(email, editTextPassword.text.toString())
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Log.d("SERVER ERROR", "Login Failed - ${e}")
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
//END_Raj