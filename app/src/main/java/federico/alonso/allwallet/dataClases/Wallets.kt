package federico.alonso.allwallet.dataClases

import android.util.Log
import com.google.gson.Gson as Gson

class Wallet (
    var name : String? = null,
    var provider : String?=null,
    var balance: Double? = null,
    var currencyUnit: Currency? = null

){
    constructor(json: String): this(){
        val walletFromJS = Gson().fromJson(json, Wallet::class.java)
        name = walletFromJS.name
        provider = walletFromJS.provider
        balance = walletFromJS.balance
        currencyUnit = walletFromJS.currencyUnit
    }
    companion object ManagedCurrency{
        val DOLLAR = Currency(
            "Dólar",
            "USD",
            )

        val EURO  = Currency(
            "Euro",
            "EUR",
        )

        val BTC = Currency(
            "Bitcoin",
            "BTC"
        )
        val PESO = Currency(
            "Peso Argentino",
            "ARS"
        )

        val LIST  = listOf<Currency>(DOLLAR, EURO, BTC, PESO)
    }

    override fun equals( other : Any?): Boolean {
        if ( this === other)
            return true
        if ( other !is Wallet )
            return false
        return name == other.name &&
                provider == other.provider  &&
                balance == other.balance &&
                currencyUnit?.code == other.currencyUnit?.code
    }
    fun toJson(): String = Gson().toJson(this)

    fun isEmpty(): Boolean = name == null

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (provider?.hashCode() ?: 0)
        result = 31 * result + (balance?.hashCode() ?: 0)
        result = 31 * result + (currencyUnit?.hashCode() ?: 0)
        return result
    }
}


class Wallets(){
    private var wallets = mutableListOf<Wallet>()
    val dollar = Wallet.DOLLAR
    val euro = Wallet.EURO
    val btc = Wallet.BTC
    val peso = Wallet.PESO

    constructor(json: String): this(){
        wallets = Gson().fromJson(json, Wallets::class.java)?.wallets?: wallets
        for (wallet in wallets )
            boundToWalletsGlobalCurrencys(wallet)
        Log.d("Wallets","wallets json ${toJson()}")
    }

    // agreaga a la lista solo si no hay otra igual
    fun addWallet( newWallet : Wallet) : Boolean {
        if ( wallets.find { it == newWallet } == null  ){
            boundToWalletsGlobalCurrencys(newWallet)
            wallets.add(newWallet)
            return true
        }
        return false


    }

    private fun boundToWalletsGlobalCurrencys(newWallet: Wallet) {
        newWallet.currencyUnit = when (newWallet.currencyUnit) {
            dollar -> dollar
            euro -> euro
            btc -> btc
            peso -> peso
            else -> dollar
        }
    }

    fun removeWallet( toRemove : Wallet ): Boolean {
        return wallets.remove(toRemove)
    }
    fun removeWallet(toRemove: Int): Wallet {
        return wallets.removeAt(toRemove)
    }

    operator fun get(index : Int): Wallet? {
       return if( wallets.size < index ) null else wallets[index]
    }
    operator fun set(editPos: Int, value: Wallet) {
        if( editPos < wallets.size){
            wallets[editPos] = value
        }
    }
    operator fun iterator(): Iterator <Wallet> = wallets.iterator()

    operator fun plusAssign( newWallet : Wallet) {
        this.addWallet(newWallet)
    }
    operator fun minusAssign( toRemove : Wallet) {
        this.removeWallet(toRemove)
    }

    fun toJson(): String = Gson().toJson(this)

    fun size(): Int =  wallets.size

    // Esta función suma los totales por moneda seleccionada
    // no hace ninguna conversión, acumula si cumple con
    // la condición que tengan la misma divisa
    fun totalBalanceBy(
        currencyToTotalize : Currency,
        currencyToTransform : Currency? = null
    ) : Double {
        val transformRate = currencyToTransform?.currencyConvertRate(
                    currencyToTotalize
                )?: 1.0

        var sum = 0.0
        for (wallet in wallets ){
            sum += if(wallet.currencyUnit == currencyToTotalize)
                wallet.balance?: 0.0
            else 0.0
        }
        return sum * transformRate
    }


    fun totalConsolidatedBalanceIn( currency : Currency) : Double{
        val usdTotal = totalBalanceBy(dollar , currency)
        val btcTotal = totalBalanceBy(btc,currency)
        val eurTotal = totalBalanceBy(euro, currency)
        val pesoTotal = totalBalanceBy(peso, currency)
        Log.d("totalConsolidated", euro.usdRefValue.toString())

        return usdTotal + btcTotal + eurTotal + pesoTotal

    }





}