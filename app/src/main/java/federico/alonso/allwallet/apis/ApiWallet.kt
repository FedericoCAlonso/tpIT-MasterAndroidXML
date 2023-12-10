package federico.alonso.allwallet.apis

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.dataClases.Bluelitics
import federico.alonso.allwallet.dataClases.Cex
import federico.alonso.allwallet.dataClases.Wallet
import federico.alonso.allwallet.dataClases.Wallets

class ApiWallet(
    private val user: CredentialManager,
    private val context: Context,
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            user.loggedUser(),
            Context.MODE_PRIVATE
        )

) {
    lateinit var wallets: Wallets
    lateinit private var cex : Cex
    lateinit private var bluelitics : Bluelitics
    init {
        updateWallets()
    }

    fun save(){
        sharedPreferences.edit().
            putString(
                AppConstants.S_P_WALLETS_FIELD,
                wallets.toJson()
            ).
            apply()
    }
    fun updateWallets(){
        wallets = Wallets(sharedPreferences.getString(
            AppConstants.S_P_WALLETS_FIELD, ""
        )!!)
        updateExchangeCurrency()

    }


    private fun cexOnSuccess(json: String) {
        cex = Cex(json)
        val btc = cex.getPairLastValue(AppConstants.API_CEX_BTC_USD_PAIR)?: 0.0
        for(wallet in wallets) {
            if (wallet.currencyUnit == Wallet.BTC)
                wallet.currencyUnit!!.usdRefValue = btc
        }
        save()

    }

    private fun conetionError(error: String) {
        Log.d("conectionError", error)

    }

    private fun blueliticsOnSuccess(json: String) {
        bluelitics = Bluelitics(json)
        val euro = bluelitics.oficial_euro.value_avg
        val peso = bluelitics.oficial.value_avg

        for(wallet in wallets){

            wallet.currencyUnit!!.usdRefValue =  when(wallet.currencyUnit){
                Wallet.DOLLAR -> 1.0
                Wallet.EURO -> euro
                Wallet.PESO -> peso
                else -> wallet.currencyUnit!!.usdRefValue
            }
        }
        save()

    }

    private fun updateExchangeCurrency(){

        ApiCall(AppConstants.API_CEX_URL,
            {
                cexOnSuccess(it)
            },
            {
                conetionError(it)
            }
        ).performApiCall()
        ApiCall(AppConstants.API_BLUELITICS_URL,
            {
                blueliticsOnSuccess(it)
            },
            {
                conetionError(it)
            }
        ).performApiCall()


    }


}