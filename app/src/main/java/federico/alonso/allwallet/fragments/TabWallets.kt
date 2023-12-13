package federico.alonso.allwallet.fragments

import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import federico.alonso.allwallet.apis.ApiWallet
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.credentialManager.CredentialManager

import federico.alonso.allwallet.R
import federico.alonso.allwallet.activities.EditWalletActivity


class TabWallets() : Fragment() {

    private lateinit var apiWallet: ApiWallet
    private lateinit var user : CredentialManager
    private lateinit var walletsAdapter : RecyclerView.Adapter<WalletsAdapter.WalletViewHolder>
    private var walletEditPosition  = -1
    private val  launcher = registerForActivityResult(
        ActivityResultContracts.
        StartActivityForResult()
        ){ onResult(it)}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wallets, container, false)
        user = CredentialManager(requireContext())
        apiWallet = ApiWallet(user, requireContext())

        walletsAdapter = WalletsAdapter(apiWallet) {
            val editWalletIntent = Intent(requireContext(), EditWalletActivity::class.java)
            walletEditPosition = it
            editWalletIntent.putExtra(AppConstants.INTENT_EXTRA_WALLET_POS_NAME, it)
            editWalletIntent.putExtra(AppConstants.INTENT_EXTRA_EDIT_STATUS_NAME, true)
            launcher.launch(editWalletIntent)
        }

        val recyclerWallet = view.findViewById<RecyclerView>(R.id.recycler_wallets)
        recyclerWallet.setHasFixedSize(false)
        recyclerWallet.adapter = walletsAdapter
        recyclerWallet.layoutManager = LinearLayoutManager(requireContext())


        val addWalletButton =
            view.
            findViewById<FloatingActionButton>(
                R.id.btnFragmentWalletsAddWallet
            )

        addWalletButton.setOnClickListener {
            val editWalletIntent = Intent(requireContext(), EditWalletActivity::class.java)
            launcher.launch(editWalletIntent)
        }

        return view
    }

    private fun onResult(activityResult: ActivityResult) {
        apiWallet.updateWallets()
        when(activityResult.resultCode){
           AppConstants.RESULT_OP_ADDED ->
               walletsAdapter.notifyItemInserted(apiWallet.wallets.size() - 1)
           AppConstants.RESULT_OP_DELETED ->
               walletsAdapter.notifyItemRemoved(walletEditPosition)
           AppConstants.RESULT_OP_EDITED ->
               walletsAdapter.notifyItemChanged(walletEditPosition)
        }
    }
}




