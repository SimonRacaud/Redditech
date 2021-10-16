package my.epi.redditech.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import my.epi.redditech.fragment.HomePostTabFragment
import my.epi.redditech.fragment.HomeSubredditsTabFragment

/**
 * Home tab generator
 */
class HomeCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return HomePostTabFragment()
            1 -> return HomeSubredditsTabFragment()
        }
        return HomePostTabFragment()
    }
}
