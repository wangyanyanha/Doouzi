package com.doouzi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.doouzi.ui.FragmentJoke;
import com.doouzi.ui.MyMenuFragment;
import com.doouzi.ui.widget.PagerSlidingTabStrip;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.doouzi.util.JsonConvertUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private LeftDrawerLayout mLeftDrawerLayout;

    private ViewPager pager;
    private MyPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);

        FragmentManager fm = getSupportFragmentManager();
        MyMenuFragment mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);


        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        tabs.setTextColor(getResources().getColor(R.color.white));
        tabs.setIndicatorColor(getResources().getColor(R.color.white));

        pager.setOffscreenPageLimit(3);

        adapter.notifyDataSetChanged();

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());

        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        tabs.setOnTabChangeListener(new PagerSlidingTabStrip.OnTabChangeListener() {
            @Override
            public void onTabScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onTabScrollStateChanged(int state) {

            }

            @Override
            public void onTabSelected(int position) {

            }
        });


//        TextView tv_test = (TextView) findViewById(R.id.tv_test);
//        String test;
//        try {
//            //Return an AssetManager instance for your application's package
//
//            InputStream is=getAssets().open("test.txt");
//            int size = is.available();
//
//            // Read the entire asset into a local byte buffer.
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            // Convert the buffer into a string.
//            test = new String(buffer, "utf-8");
//
//        } catch (IOException e) {
//            // Should never happen!
//            throw new RuntimeException(e);
//        }
//
//        String conf = "{\n" +
//                "    \"content\":\"data.data.[*].group.content\",\n" +
//                "    \"url\":\"data.data.[*].group.large_image.url_list.[0].url\"\n" +
//                "}";
//
//        try {
//            JSONObject obj = new JSONObject(test);
//            JSONObject confobj = new JSONObject(conf);
//
//            JsonConvertUtil util = new JsonConvertUtil();
//            String result = util.Json2Json(obj, confobj);
//            Log.d("wy_", result);
//            tv_test.setText(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES;

        public Fragment MixFrag,JokeFrag, PicFrag;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES = new String[]{"杂交","段子","趣图"};
                MixFrag = new FragmentJoke();
                JokeFrag = new FragmentJoke();
                PicFrag = new FragmentJoke();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MixFrag;
                case 1:
                    return JokeFrag;
                case 2:
                    return PicFrag;
            }
            return null;
        }
    }

}
