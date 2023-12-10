package federico.alonso.allwallet.fragments


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import federico.alonso.allwallet.R
import federico.alonso.allwallet.apis.ApiWallet


class WalletsAdapter(
    private val apiWallets: ApiWallet,
    val onButtonClick : (position: Int) -> Unit
) : RecyclerView.Adapter<WalletsAdapter.WalletViewHolder>() {



    // Esta clase solo está para heredar la representacion del item, que hereda
    // desde RecyclerView.ViewHolder. Lo unico que hace es llamar al constructor
    // padre, que genera nuestra nueva clase WalletViewHolder
    inner class WalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val button: Button? = itemView.findViewById(R.id.btnWalletEdit)
        init {

            button?.setOnClickListener {
                if( adapterPosition != RecyclerView.NO_POSITION ) {
                    onButtonClick(adapterPosition)
              }
            }
        }

    }


    //En este método, se infla el view que luego se va a llenar en onBindViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        // siempre hay que poner attachToRoot false porque el recycler gestiona cuando se muestra.
        // acá solo lo preparamos.

        val view = LayoutInflater.
                    from(parent.context).
                    inflate(R.layout.item_wallets, parent,false)
        return WalletViewHolder(view)
    }

    override fun getItemCount(): Int {
        return apiWallets.wallets.size()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val name = holder.itemView.findViewById<TextView>(R.id.lblWalletsItemWalletName)
        val provider = holder.itemView.findViewById<TextView>(R.id.lblWalletsItemProvider)
        val balance = holder.itemView.findViewById<TextView>(R.id.lblWalletsItemBalance)
        name.text = apiWallets.wallets[position]?.name ?: ""
        provider.text = apiWallets.wallets[position]?.provider ?: ""
        balance.text = apiWallets.formatBalanceByPosition(position)

    }



}