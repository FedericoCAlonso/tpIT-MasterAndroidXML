package federico.alonso.allwallet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import federico.alonso.allwallet.R
import federico.alonso.allwallet.apis.ApiWallet
import federico.alonso.allwallet.credentialManager.CredentialManager



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

        updateUI(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        apiWallet = ApiWallet(user, requireContext())
        updateUI(requireView())
        Log.d("prueba", apiWallet.wallets.toJson())


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
        totalConsolidatedBalanceTextView.text = apiWallet.utilFormatBalance(
            apiWallet.wallets.totalConsolidatedBalanceIn(apiWallet.wallets.peso),
            apiWallet.wallets.peso
        )




    }

}



