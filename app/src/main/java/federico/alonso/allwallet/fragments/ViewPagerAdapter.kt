package federico.alonso.allwallet.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(private val activity: AppCompatActivity, private val itemsCount: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {

        val fragmentSelected = when (position) {
            0 -> TabDashboard()
            1 -> TabWallets()
            2 -> TabInfo()
            else -> TabDashboard()
        }
        fragmentSelected.arguments = Bundle().apply {
            putInt("position", position)
        }
        return fragmentSelected
    }
}