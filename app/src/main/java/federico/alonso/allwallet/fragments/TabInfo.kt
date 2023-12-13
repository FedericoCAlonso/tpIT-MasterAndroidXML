package federico.alonso.allwallet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import federico.alonso.allwallet.R
import federico.alonso.allwallet.apis.ApiWallet
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.dataClases.Settings


// Importa las clases necesarias

class TabInfo : Fragment() {

    private lateinit var textViewUSD: TextView
    private lateinit var textViewEuro: TextView
    private lateinit var textViewBTC: TextView
    private lateinit var textViewARS: TextView
    private lateinit var apiWallet: ApiWallet
    private lateinit var user : CredentialManager

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        user = CredentialManager(requireContext())
        apiWallet = ApiWallet(user,requireContext())
        Settings.init(requireContext(), user)

        // Inicializa los TextView
        textViewUSD = view.findViewById(R.id.textViewUSD)
        textViewEuro = view.findViewById(R.id.textViewEuro)
        textViewBTC = view.findViewById(R.id.textViewBTC)
        textViewARS = view.findViewById(R.id.textViewARS)

        // Obtiene la moneda seleccionada desde Settings.currencySelected
        val currencySelected = when(Settings.currencyToTotalBalance){
            apiWallet.wallets.dollar.code   -> apiWallet.wallets.dollar
            apiWallet.wallets.euro.code   -> apiWallet.wallets.euro
            apiWallet.wallets.btc.code   -> apiWallet.wallets.btc
            else                        -> apiWallet.wallets.peso
        }

        // Muestra las cotizaciones
        textViewUSD.text = apiWallet.wallets.dollar.code +
                ": " +
                "${apiWallet.wallets.dollar.currencyConvertRate(currencySelected)}"
        textViewEuro.text = apiWallet.wallets.euro.code +
                ": " +
                "${apiWallet.wallets.euro.currencyConvertRate(currencySelected)}"
        textViewBTC.text = apiWallet.wallets.btc.code +
                ": " +
                "${apiWallet.wallets.btc.currencyConvertRate(currencySelected)}"
        textViewARS.text = apiWallet.wallets.peso.code +
                ": " +
                "${apiWallet.wallets.peso.currencyConvertRate(currencySelected)}"
        updateUI(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        apiWallet = ApiWallet(user, requireContext())
        Settings.init(requireContext(), user)
        updateUI(requireView())

    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(view: View) {
        // Obtiene la moneda seleccionada desde Settings.currencySelected
        val currencySelected = when(Settings.currencyToTotalBalance){
            apiWallet.wallets.dollar.code   -> apiWallet.wallets.dollar
            apiWallet.wallets.euro.code   -> apiWallet.wallets.euro
            apiWallet.wallets.btc.code   -> apiWallet.wallets.btc
            else                        -> apiWallet.wallets.peso
        }

        // Muestra las cotizaciones
        textViewUSD.text = apiWallet.wallets.dollar.code +
                ": " +
                "${apiWallet.wallets.dollar.currencyConvertRate(currencySelected)}"
        textViewEuro.text = apiWallet.wallets.euro.code +
                ": " +
                "${apiWallet.wallets.euro.currencyConvertRate(currencySelected)}"
        textViewBTC.text = apiWallet.wallets.btc.code +
                ": " +
                "${apiWallet.wallets.btc.currencyConvertRate(currencySelected)}"
        textViewARS.text = apiWallet.wallets.peso.code +
                ": " +
                "${apiWallet.wallets.peso.currencyConvertRate(currencySelected)}"

    }
}
