package federico.alonso.allwallet.dataClases

import com.google.gson.Gson

class Currency(
    // Nombre de la moneda
    val name: String,
    // Código  divisa, si es oficial corresponde al ISO 4217
    val code: String,
    // Cotizacion respecto al dolar estadounidense
    var usdRefValue: Double = 0.0

) {
    /*// Cotización respecto al dolar estadounidense
    private var _usdRefValue : Double? = null

    var usdRefValue: Double?
        get() {
            return _usdRefValue
        }
        set(value) {
            if ( value == null || value < 0)
                return
            else
                _usdRefValue = value

        }*/

        // 0 es un valor de error
    fun currencyConvertRate(unitTo: Currency): Double{
        return  if (
                unitTo.isUsdRefernceValueSetted() &&
                isUsdRefernceValueSetted()
            )
            unitTo.usdRefValue/usdRefValue
                else
                    0.0
    }

    fun toJson(): String = Gson().toJson(this)

    fun fromJson(json : String): Currency = Gson().fromJson(json, Currency::class.java)

    fun isEmpty(): Boolean = code == ""

    fun isUsdRefernceValueSetted() : Boolean = usdRefValue > 0.0

    override operator fun equals(other: Any?): Boolean {
        if( other !is Currency )
            return false
        if( this === other)
            return true
        return name == other.name && code == other.code
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + code.hashCode()
        return result
    }


}