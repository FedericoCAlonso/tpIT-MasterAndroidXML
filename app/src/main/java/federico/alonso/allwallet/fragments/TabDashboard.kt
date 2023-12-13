package federico.alonso.allwallet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import federico.alonso.allwallet.R
import federico.alonso.allwallet.apis.ApiWallet
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.dataClases.Currency
import federico.alonso.allwallet.dataClases.Settings


class TabDashboard : Fragment() {

    private lateinit var user: CredentialManager
    private lateinit var apiWallet: ApiWallet



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        user = CredentialManager(requireContext())
        apiWallet = ApiWallet(user,requireContext())
        Settings.init(requireContext(), user)
       /* apiWallet.walletsLiveData.observe(viewLifecycleOwner, Observer {
            // Actualizar la vista del activity con el nuevo valor de las wallets
            updateUI(view)
        })*/

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
        val usdTotalTextView: TextView = view.findViewById(R.id.lblDashboardUSDTotal)
        val eurTotalTextView: TextView = view.findViewById(R.id.lblDashboardEURTotal)
        val btcTotalTextView: TextView = view.findViewById(R.id.lblDashboardBTCTotal)
        val arsTotalTextView: TextView = view.findViewById(R.id.lblDashboardARSTotal)
        val totalConsolidatedBalanceTextView: TextView =
            view.findViewById(R.id.lblTotalConsolidatedBalance)


        usdTotalTextView.text = apiWallet.wallets.totalBalanceBy(apiWallet.wallets.dollar).toString()
        eurTotalTextView.text = apiWallet.wallets.totalBalanceBy(apiWallet.wallets.euro).toString()
        btcTotalTextView.text = apiWallet.wallets.totalBalanceBy(apiWallet.wallets.btc).toString()
        arsTotalTextView.text = apiWallet.wallets.totalBalanceBy(apiWallet.wallets.peso).toString()
        val totalCurrencyToShow : Currency = when(Settings.currencyToTotalBalance){
            apiWallet.wallets.dollar.code   ->  apiWallet.wallets.dollar
            apiWallet.wallets.euro.code     ->  apiWallet.wallets.euro
            apiWallet.wallets.btc.code      ->  apiWallet.wallets.btc
            else                            ->  apiWallet.wallets.peso
        }
        totalConsolidatedBalanceTextView.text = apiWallet.utilFormatBalance(
            apiWallet.wallets.totalConsolidatedBalanceIn(totalCurrencyToShow),
            totalCurrencyToShow
        )





    }

}



