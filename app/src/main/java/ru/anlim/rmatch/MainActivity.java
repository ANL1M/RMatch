package ru.anlim.rmatch;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.HashMap;

import ru.anlim.rmatch.fragments.FutureMatch;
import ru.anlim.rmatch.fragments.LaLiga;
import ru.anlim.rmatch.fragments.LastMatch;
import ru.anlim.rmatch.logic.DBHelper;
import ru.anlim.rmatch.logic.JsoupHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        loadData();

        //Проверка наличия данных загрузка/отображение
        DBHelper dbHelper = new DBHelper(this);
        HashMap<String,String> hashMap = dbHelper.dbReadResult("FutureMatch");
        if(hashMap.isEmpty()){
            loadData();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = null;
            switch (sectionNumber){
                case 0:
                    fragment = new LastMatch();
                    break;
                case 1:
                    fragment = new FutureMatch();
                    break;
                case 2:
                    fragment = new LaLiga();
                    break;
            }

            return fragment;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void loadData(){
        JsoupHelper jsoupHelper = new JsoupHelper(this);
        jsoupHelper.execute(100);
    }
}
