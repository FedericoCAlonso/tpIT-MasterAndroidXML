package federico.alonso.allwallet.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import federico.alonso.allwallet.AppConstants
import federico.alonso.allwallet.credentialManager.CredentialManager
import federico.alonso.allwallet.fragments.ViewPagerAdapter
import federico.alonso.allwallet.R



class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.list,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        configToolbar()
        startTabsViews()
        setTabs()


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

        this.title = AppConstants.APP_NAME

        // Hay que tener la precaución de importar la clase correcta
        // de toolbar. import androidx.appcompat.widget.Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val user = CredentialManager(this)

    }

}