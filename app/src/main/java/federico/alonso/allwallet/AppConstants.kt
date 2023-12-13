package federico.alonso.allwallet

/*
* Archivo de constantes de la app.
* */
object AppConstants {


    const val API_CEX_BTC_USD_PAIR = "BTC:USD"
    const val API_BLUELITICS_URL = "https://api.bluelytics.com.ar/v2/latest"
    const val API_CEX_URL = "https://cex.io/api/tickers/BTC/USD"

    //Credential manager constants
    const val S_P_CREDENTIAL_MANAGER_USER_FIELD = "user"
    const val S_P_CREDENTIAL_MANAGER_ISLOGGED_FIELD = "isLogged"
    const val S_P_CREDENTIAL_MANAGER_AUTLOLOGIN_FIELD = "autoLoginEnable"
    const val S_P_CREDENTIAL_MANAGER = "credential_manager"
    const val S_P_SETTINGS: String = "settings"

    // tabWallet constants
    const val INTENT_EXTRA_EDIT_STATUS_NAME = "edit_wallet_edit_status"
    const val INTENT_EXTRA_WALLET_POS_NAME = "wallet_edit_pos"
    const val RESULT_OP_CANCELED = 0
    const val RESULT_OP_EDITED = 1
    const val RESULT_OP_DELETED = 2
    const val RESULT_OP_ADDED = 3
    val RESUTL_OP_DICTIONARY_MSGS = listOf(
        R.string.EditWalletCanceled,
        R.string.EditWalletSuccess,
        R.string.DeleteWalletSuccess,
        R.string.AddWalletSuccess,
    )

    // Nombre de la App
    const val APP_NAME = "AllWallet"

    //Configuración de pestañas
    const val NUMBER_OF_TABS = 3


    // Nombre del campo de json de las billeteras
    const val S_P_WALLETS_FIELD = "userWallets"

}