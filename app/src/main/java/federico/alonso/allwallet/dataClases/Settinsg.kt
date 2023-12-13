package federico.alonso.allwallet.dataClases

import android.content.Context
import android.content.SharedPreferences
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.credentialManager.CredentialManager

object Settings{
    var currencyToTotalBalance: String = Wallet.DOLLAR.code
    var autoLoginEnable : Boolean = false
    private  var  sharedPreferences: SharedPreferences? = null
    fun init(context: Context, user : CredentialManager){
        sharedPreferences = context.getSharedPreferences(user.loggedUser(), Context.MODE_PRIVATE)
        autoLoginEnable = user.isAutoLoginEnable()
        currencyToTotalBalance = sharedPreferences!!.
                    getString(AppConstants.S_P_SETTINGS,null)
                    ?:Wallet.DOLLAR.code
    }
    fun save(user: CredentialManager){
        user.setAutoLoginEnable(autoLoginEnable)
        sharedPreferences?.
        edit()?.
        putString( AppConstants.S_P_SETTINGS ,currencyToTotalBalance )?.
        apply()
    }
    fun clear(){
        sharedPreferences?.edit()?.clear()?.apply()
    }

}
