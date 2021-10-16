package my.epi.redditech.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import my.epi.redditech.R
import my.epi.redditech.adapter.HomeCollectionAdapter

/**
 * Tabs controller
 */
class CollectionHomeFragment : Fragment() {
    private lateinit var HomeCollectionAdapter: HomeCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private val tabNames = arrayOf("Posts", "My Subreddits")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        HomeCollectionAdapter = HomeCollectionAdapter(this)
        viewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = HomeCollectionAdapter

        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.home_tablayout)

        // That create tablayout tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }
}