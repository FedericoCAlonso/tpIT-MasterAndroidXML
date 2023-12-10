package federico.alonso.allwallet.dataClases

import android.util.Log
import com.google.gson.Gson


// Clases para usar la api https://api.bluelytics.com.ar/v2/latest
data class Bluelitics(
    var oficial: CurrencyExchange = CurrencyExchange(),
    var blue: CurrencyExchange = CurrencyExchange(),
    var oficial_euro: CurrencyExchange = CurrencyExchange(),
    var blue_euro: CurrencyExchange = CurrencyExchange(),
    var last_update: String =""
){
    constructor(json : String ) : this(){
        val bluelitics = Gson().fromJson(json, Bluelitics::class.java)?: Bluelitics()
        oficial = bluelitics.oficial
        blue = bluelitics.blue
        oficial_euro = bluelitics.oficial_euro
        blue_euro = bluelitics.blue_euro
        last_update = bluelitics.last_update
        Log.d("bluelitics", bluelitics.toString())
    }

    fun isDataOk() : Boolean{
        return last_update != ""
    }


}

data class CurrencyExchange(
    var value_avg: Double = 0.0,
    var value_sell: Double = 0.0,
    var value_buy: Double = 0.0
)
