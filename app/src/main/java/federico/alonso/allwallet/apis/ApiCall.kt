package federico.alonso.allwallet.apis

import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException



// Clases para usar la api https://cex.io/api/tickers/BTC/USD

class ApiCall(
    val url : String,
    private val onSuccess: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    fun performApiCall() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejo de errores
                Log.d("apiCall","error onFailure")
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        // Manejo de errores
                        Log.d("apiCall","error ${response.code}")
                        onError("Unexpected code ${response.code}")
                        return
                    }

                    // Procesamiento exitoso
                    val responseBody = response.body?.string() ?: ""
                    Log.d("apiCall","success ${responseBody}")
                    onSuccess(responseBody)
                }
            }
        })
    }
/*
class ApiCurrencyExchange(){
    private val client = OkHttpClient()

    fun run() {
        val request = Request.Builder()
            .url("https://api.bluelytics.com.ar/v2/latest")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        Log.d("Okhttp","$name: $value")
                    }

                    Log.d("Okhttp",response.body!!.string())
                }
            }
        })
    }

    }
*/
}