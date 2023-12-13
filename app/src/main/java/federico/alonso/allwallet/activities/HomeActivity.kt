package federico.alonso.allwallet.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.fragments.ViewPagerAdapter
import federico.alonso.allwallet.R
import federico.alonso.allwallet.R.drawable.logo2
import federico.alonso.allwallet.apis.ApiWallet
import federico.alonso.allwallet.dataClases.Settings
import federico.alonso.allwallet.dataClases.Wallet


class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var settingsWindow: PopupWindow


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.list,menu)
        return true

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        configToolbar()
        startTabsViews()
        setTabs()
        settingsWindow = createSettingsView()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val anchorView = findViewById<View>(R.id.view_pager)
                showSettingsWindow(anchorView)
                true
            }

            R.id.action_logout -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        val user = CredentialManager(this)
        ApiWallet(user,this).clear()
        user.logout()
        startActivity(intent)
    }

    private fun createSettingsView(): PopupWindow{
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val settingsView: View = inflater.inflate(R.layout.settings_popup,null)
        val settingsWindow = PopupWindow(settingsView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val user = CredentialManager(this)
        Settings.init(this,user)

        /*Llenado con configuración actual*/
        val usdRadioButton : RadioButton = settingsView.findViewById(R.id.btnSettingsUSD)
        val eurRadioButton : RadioButton = settingsView.findViewById(R.id.btnSettingsEUR)
        val btcRadioButton : RadioButton = settingsView.findViewById(R.id.btnSettingsBTC)
        val arsRadioButton : RadioButton = settingsView.findViewById(R.id.btnSettingsARS)
        val autoLoginEnable : CheckBox = settingsView.findViewById(R.id.chkSettingsAutoLoginEnable)
        val radioGroup : RadioGroup = settingsView.findViewById(R.id.radioGroupSettingsCurrency)
        when(Settings.currencyToTotalBalance){
            Wallet.DOLLAR.code  -> usdRadioButton.isChecked = true
            Wallet.EURO.code    -> eurRadioButton.isChecked = true
            Wallet.BTC.code     -> btcRadioButton.isChecked = true
            Wallet.PESO.code    -> arsRadioButton.isChecked = true
        }
        autoLoginEnable.isChecked = Settings.autoLoginEnable

        val btnCancelSettings: Button = settingsView.findViewById(R.id.btnSettingsCancel)
        btnCancelSettings.setOnClickListener {
            settingsWindow.dismiss()
        }
        val btnAcceptSettings : Button = settingsView.findViewById((R.id.btnSettingsAccept))
        btnAcceptSettings.setOnClickListener {
            Settings.currencyToTotalBalance = when(radioGroup.checkedRadioButtonId){
                R.id.btnSettingsUSD     ->  Wallet.DOLLAR.code
                R.id.btnSettingsEUR     ->  Wallet.EURO.code
                R.id.btnSettingsBTC     ->  Wallet.BTC.code
                R.id.btnSettingsARS     ->  Wallet.PESO.code
                else -> {
                        errorSettingsMsg()
                        return@setOnClickListener
                    }
                }
            Settings.autoLoginEnable = autoLoginEnable.isChecked
            Settings.save(user)
            settingsWindow.dismiss()
        }


        return settingsWindow
    }
    private fun errorSettingsMsg() {
        Toast.makeText(this, R.string.AddWalletErrorEmptyCurrency, Toast.LENGTH_SHORT).show()
    }
    private fun showSettingsWindow(anchorView : View){
            settingsWindow.showAtLocation(anchorView, Gravity.CENTER, 0,0)
    }

    private fun setTabs() {
        /* Vinculamos los tabs a la vista.
       La expresión lambda es para configurar el texto de cada pestaña.
       * */
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position){
                0 -> getString(R.string.DashboardTabTitle)
                1 -> getString(R.string.WalletsTabTitle)
                2 -> getString(R.string.InfoTabTitle)
                else -> ""
            }

        }.attach()
    }


    private fun startTabsViews(){
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager.adapter = ViewPagerAdapter(this, AppConstants.NUMBER_OF_TABS)

    }

    private fun configToolbar() {

        //this.title = AppConstants.APP_NAME

        // Hay que tener la precaución de importar la clase correcta
        // de toolbar. import androidx.appcompat.widget.Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val logo = AppCompatResources.getDrawable(this, logo2)
        toolbar.logo = logo


    }

}