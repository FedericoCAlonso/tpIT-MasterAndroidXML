package federico.alonso.allwallet.credentialManager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import federico.alonso.allwallet.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*Debería ser una clase singleton
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
        Log.d("login", "en setAutoLoginEnable isLogged $isLogged")
        if( !isLogged )
            return false
        autoLoginEnable = state
        savePreferences()
        return true
    }


    fun login(user: String, password: String, callback: (Boolean) -> Unit) {

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
        Log.d("login", "en logout")
        loggedUser=null
        isLogged=false
        autoLoginEnable=false
        clear()
    }

    private fun savePreferences() {
        Log.d("login", "en save preferences user $loggedUser")
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
            AppConstants.S_P_CREDENTIAL_MANAGER_AUTLOLOGIN_FIELD,
            autoLoginEnable
        )
        editor.apply()
    }
    private fun clear(){
        sharedPreferences.edit().clear().apply()
    }


}
/*

object Login {
    var loggedUser = ""
    var logged = false
    var autoLoginEnable = false
    var sharedPreferences : SharedPreferences?= null

    fun init(context: Context){
        sharedPreferences = context.getSharedPreferences(AppConstants.S_P_CREDENTIAL_MANAGER, Context.MODE_PRIVATE)
        val json = sharedPreferences?.getString("loginJson", "")?: ""
        parseJson(json)
        if(loggedUser.isEmpty()){
            autoLoginEnable = false
            logged = false
        }

    }
    fun setAutloLoginEnable(status : Boolean) {
        autoLoginEnable = status
    }
    fun isAutologinEnable() : Boolean = autoLoginEnable
    fun isLogged() : Boolean = logged
    fun logedUser() : String = loggedUser


    private fun toJson() : String{
        return "{" +
                "\"loggedUser\":$loggedUser," +
                "\"isLogged\":$logged," +
                "\"autoLoginEnable\":$autoLoginEnable" +
                "}"
    }
    private fun parseJson(jsonString: String) {
        val jsonParts = jsonString.substring(1, jsonString.length - 1).split(",")

        loggedUser = jsonParts[0].split(":").last().trim('"')
        logged = jsonParts[1].split(":").last().trim() == "true"
        autoLoginEnable = jsonParts[2].split(":").last().trim() == "true"
    }

    private fun savePreferences(){
        val editor = sharedPreferences?.edit()
        editor?.putString(
            "loginJson",
            toJson())?.apply()

    }
    private fun clear(){
        sharedPreferences?.edit()?.clear()?.apply()
    }


    fun login(user: String, password: String, callback: (Boolean) -> Unit) {

        // simulo el tiempo de conexión con el servidor
        runBlocking {
            launch {
                delay(3000)
                logged = (user == "Admin" && password == "123456")

                if (logged) {
                    loggedUser = user
                    savePreferences()
                }
                callback(logged)
            }

        }

    }
}

*/
