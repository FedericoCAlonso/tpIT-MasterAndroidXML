package federico.alonso.allwallet.dataClases

import com.google.gson.Gson

data class Cex(
    var e: String ="",
    var ok: String ="",
    private var data: MutableList<TickerData> = mutableListOf()
){
    constructor(json : String) : this() {
        val exchangeValues = Gson().fromJson(json, Cex::class.java)?: Cex("","")
        e = exchangeValues.e
        ok = exchangeValues.ok
        data = exchangeValues.data

    }
    fun isDataOk() : Boolean{
        return ok == "ok"
    }
    fun getPairLastValue( searchedPair : String) : Double?{
        for( values in data ){
            if(values.pair == searchedPair)
                return values.last.toDoubleOrNull()
        }
        return null
    }


}


data class TickerData(
    val timestamp: String,
    val pair: String,
    val low: String,
    val high: String,
    val last: String,
    val volume: String,
    val volume30d: String,
    val priceChange: String,
    val priceChangePercentage: String,
    val bid: Double,
    val ask: Double
)
