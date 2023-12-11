package federico.alonso.allwallet.credentialManager

import android.content.Context
import android.content.SharedPreferences
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*Debería ser una clase singleton con parámetros.
* TODO: Convertir esta clase en singleton
* */
class CredentialManager(
    private val context: Context,
    
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences(
        AppConstants.S_P_CREDENTIAL_MANAGER,
        Context.MODE_PRIVATE
    ),
    private var autoLoginEnable: Boolean = sharedPreferences.getBoolean(
    AppConstants.S_P_CREDENTIAL_MANAGER_AUTLOLOGIN_FIELD,
    false
    ),
    private var isLogged: Boolean = sharedPreferences.getBoolean(
    AppConstants.S_P_CREDENTIAL_MANAGER_ISLOGGED_FIELD,
    false
    ),
    private var loggedUser: String? = sharedPreferences.getString(
    AppConstants.S_P_CREDENTIAL_MANAGER_USER_FIELD,
    null
    )
){

    // Getter para evitar modificar por el usuario
    fun loggedUser() = loggedUser
    fun isLogged() = isLogged
    // Getter para evitar modificar por el usuario.
    fun isAutoLoginEnable() = autoLoginEnable

    fun setAutoLoginEnable(state : Boolean): Boolean{
        if( !isLogged )
            return false
        autoLoginEnable = state
        if (autoLoginEnable)
            savePreferences()
        return true
    }


    fun login(user: String, password: String, callback: (Boolean) -> Unit) {
        if( autoLoginEnable && isLogged ){
            callback(true)
            return
        }
        // simulo el tiempo de conexión con el servidor
        runBlocking {
            launch {
                delay(3000)
                isLogged = (user == "Admin" && password == "123456")

                if (isLogged) {
                    loggedUser = user
                    savePreferences()
                }
                callback(isLogged)
            }

        }

    }
    fun logout(){
        loggedUser=null
        isLogged=false
        autoLoginEnable=false
        clear()
    }

    private fun savePreferences() {
        val editor = sharedPreferences.edit()
        editor.putString(
            AppConstants.S_P_CREDENTIAL_MANAGER_USER_FIELD,
            loggedUser
        )
        editor.putBoolean(
            AppConstants.S_P_CREDENTIAL_MANAGER_ISLOGGED_FIELD,
            isLogged
        )
        editor.putBoolean(
            AppConstants.S_P_CREDENTIAL_MANAGER_ISLOGGED_FIELD,
            autoLoginEnable
        )
        editor.apply()
    }
    private fun clear(){
        sharedPreferences.edit().clear().apply()
    }


}

