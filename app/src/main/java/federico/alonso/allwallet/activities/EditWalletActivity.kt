package federico.alonso.allwallet.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.R
import federico.alonso.allwallet.apis.ApiWallet
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.dataClases.Currency
import federico.alonso.allwallet.dataClases.Wallet

class EditWalletActivity : AppCompatActivity() {
    private var editPos = 0
    private lateinit var apiWallet : ApiWallet
    private lateinit var user : CredentialManager
    private lateinit var lblWalletProvider: TextInputEditText
    private lateinit var lblWalletName: TextInputEditText
    private lateinit var lblWalletBalance: TextInputEditText
    private lateinit var btnInputUSD: RadioButton
    private lateinit var btnInputEUR: RadioButton
    private lateinit var btnInputBTC: RadioButton
    private lateinit var btnInputARS: RadioButton
    private lateinit var btnDeleteWallet : Button
    private lateinit var applyButton : Button


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_and_edit_wallet)

        editPos = intent.getIntExtra(AppConstants.INTENT_EXTRA_WALLET_POS_NAME,-1)
        val isEdit = intent.getBooleanExtra(AppConstants.INTENT_EXTRA_EDIT_STATUS_NAME, false)

        if( editPos == -1 && isEdit)
            exit(AppConstants.RESULT_OP_CANCELED)
        Log.d("prueba","edit pos $editPos")

        setElements()

        if( isEdit )
            putWalletValuesOnView()

        // Botón de cancelar
        findViewById<Button>(R.id.btnAddWalletButtonCancel).setOnClickListener {
            exit(AppConstants.RESULT_OP_CANCELED)
        }

        applyButton.setOnClickListener {

            val wallet = Wallet()
            getDataFromForm(wallet)

            if (validateData(wallet))
                return@setOnClickListener

            if ( isEdit)
                apiWallet.wallets[editPos] = wallet
            else
                apiWallet.wallets.addWallet(wallet)

            exit(
                if( isEdit)
                    AppConstants.RESULT_OP_EDITED
                else
                    AppConstants.RESULT_OP_ADDED
            )
        }


        btnDeleteWallet.setOnClickListener {
            apiWallet.wallets.removeWallet(editPos)
            exit(AppConstants.RESULT_OP_DELETED)
        }
    }

    private fun validateData(wallet: Wallet): Boolean {
        if (wallet.provider!!.isEmpty()) {
            Toast.makeText(this, R.string.AddWalletErrorEmptyProvider, Toast.LENGTH_SHORT).show()
            return true
        }
        if (wallet.name!!.isEmpty()) {
            Toast.makeText(this, R.string.AddWalletErrorEmptyName, Toast.LENGTH_SHORT).show()
            return true
        }
        if (wallet.currencyUnit!!.isEmpty()) {
            Toast.makeText(this, R.string.AddWalletErrorEmptyCurrency, Toast.LENGTH_SHORT).show()
            return true
        }
        if (wallet.balance == null)
            wallet.balance = 0.0
        return false
    }

    private fun getDataFromForm(wallet: Wallet) {
        wallet.provider = lblWalletProvider.text.toString().trim() ?: ""
        wallet.name = lblWalletName.text.toString().trim() ?: ""
        wallet.currencyUnit = when {
            btnInputUSD.isChecked -> Wallet.DOLLAR
            btnInputEUR.isChecked -> Wallet.EURO
            btnInputBTC.isChecked -> Wallet.BTC
            btnInputARS.isChecked -> Wallet.PESO
            else -> Currency("", "")
        }
        wallet.balance = lblWalletBalance.text.toString().trim().toDoubleOrNull()
    }

    private fun setElements() {
        lblWalletProvider = findViewById(R.id.lblInputWalletProvider)
        lblWalletName = findViewById(R.id.lblInputWalletName)
        lblWalletBalance = findViewById(R.id.lblInputWalletBalance)
        btnInputUSD = findViewById(R.id.btnInputUSD)
        btnInputEUR = findViewById(R.id.btnInputEUR)
        btnInputBTC = findViewById(R.id.btnInputBTC)
        btnInputARS = findViewById(R.id.btnInputARS)
        applyButton = findViewById(R.id.btnAddWalletApply)
        btnDeleteWallet = findViewById(R.id.btnDeleteWallet)
        user = CredentialManager(this)
        apiWallet = ApiWallet(user, this)
    }

    private fun putWalletValuesOnView() {
        findViewById<TextView>(R.id.lblAddWalletTitle).text = getString(R.string.edit_wallet_title)

        // Hago visible al botón de eliminar
        btnDeleteWallet.visibility = View.VISIBLE

        lblWalletProvider.setText(apiWallet.wallets[editPos]?.provider ?: "")
        lblWalletName.setText(apiWallet.wallets[editPos]?.name ?: "")
        lblWalletBalance.setText(apiWallet.wallets[editPos]?.balance.toString())
        when (apiWallet.wallets[editPos]?.currencyUnit) {
            Wallet.DOLLAR -> btnInputUSD.isChecked = true
            Wallet.EURO -> btnInputEUR.isChecked = true
            Wallet.BTC -> btnInputBTC.isChecked = true
            Wallet.PESO -> btnInputARS.isChecked = true
        }
    }

    private fun exit(resultOp : Int) {
        val intentBack = Intent()
        val idRString : Int = when(resultOp){
                AppConstants.RESULT_OP_ADDED ->
                    AppConstants.RESUTL_OP_DICTIONARY_MSGS[AppConstants.RESULT_OP_ADDED]
                AppConstants.RESULT_OP_DELETED ->
                    AppConstants.RESUTL_OP_DICTIONARY_MSGS[AppConstants.RESULT_OP_DELETED]
                AppConstants.RESULT_OP_EDITED ->
                    AppConstants.RESUTL_OP_DICTIONARY_MSGS[AppConstants.RESULT_OP_EDITED]
            else -> AppConstants.RESUTL_OP_DICTIONARY_MSGS[AppConstants.RESULT_OP_CANCELED]
        }

        if( resultOp != AppConstants.RESULT_OP_CANCELED)
            apiWallet.save()

        Toast.makeText(this, idRString, Toast.LENGTH_SHORT).show()
        setResult(resultOp, intentBack)
        finish()


    }

}
