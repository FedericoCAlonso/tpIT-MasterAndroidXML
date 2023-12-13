package federico.alonso.allwallet.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.R


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(this, HomeActivity::class.java)

        val login = CredentialManager(this)
        Log.d("login","islogin ${login.isLogged()} is autologin ${login.isAutoLoginEnable()}")
        if (login.isLogged() && login.isAutoLoginEnable()) {
            startActivity(intent)
        }
        val userField = findViewById<EditText>(R.id.lbl_userName)
        val passwordField = findViewById<EditText>(R.id.lbl_password)
        val loginBtn = findViewById<Button>(R.id.btn_login)
        loginBtn.setOnClickListener {

            val userName = userField?.text?.toString()
            val password = passwordField?.text?.toString()

            login.login(
                userName ?: "",
                password ?: ""

            ) {
                if (it)
                    startActivity(intent)
                else
                    errorLogin()
            }
        }

    }

    private fun errorLogin() {
        Toast.makeText(
            this,
            this.getString(R.string.err_login),
            Toast.LENGTH_LONG
        ).show()
    }

}






