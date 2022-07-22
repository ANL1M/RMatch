package ru.anlim.rmatch

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import ru.anlim.rmatch.logic.DBHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import ru.anlim.rmatch.logic.JsoupHelper
import ru.anlim.rmatch.fragments.FutureMatch
import ru.anlim.rmatch.fragments.LaLiga
import androidx.fragment.app.FragmentPagerAdapter
import ru.anlim.rmatch.fragments.LastMatch

class MainActivity : AppCompatActivity(), OnRefreshListener{
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    var currPage = 0
    var context:Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Инициализация сущностей
        initSwipeRefresh()
        currPage = 9 //произвольное число не равное номеру вкладки

        //Проверка наличия данных загрузка/отображение
        val dbHelper = DBHelper(this)
        if (dbHelper.dbReadResult("FutureMatch").isEmpty()) {
            onRefresh()
        } else {
            setResult()
        }
    }

    fun setResult() {
        mSwipeRefreshLayout.isRefreshing = false
        //Инициализация сущностей
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val mViewPager = findViewById<ViewPager>(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter
        mViewPager.currentItem = if (currPage == 9) 1 else currPage
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))
        mViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mSwipeRefreshLayout.isEnabled = position != 2
                currPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = true
        val jsoupHelper = JsoupHelper()
        jsoupHelper.execute()
    }

    open class PlaceholderFragment : Fragment() {
        companion object {
            fun newInstance(sectionNumber: Int): PlaceholderFragment? {
                var fragment: PlaceholderFragment? = null
                when (sectionNumber) {
                    0 -> fragment = LastMatch()
                    1 -> fragment = FutureMatch()
                    2 -> fragment = LaLiga()
                }
                return fragment
            }
        }
    }

    class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position)!!
        }

        override fun getCount(): Int {
            return 3
        }
    }

    private fun initSwipeRefresh() {
        mSwipeRefreshLayout = findViewById(R.id.swipe)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.real_blue,
            R.color.real_red,
            R.color.real_yellow
        )
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE)
        mSwipeRefreshLayout.setProgressViewOffset(true, 50, 300)
    }
}