package com.auro.android.itravel;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;


// for place autocomplete used the code that Google provided on their github account which can be found here - https://github.com/googlesamples/android-play-places

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private final int code = 10;
    Double lat;
    Double longt;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.auro.android.itravel.R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(com.auro.android.itravel.R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Places Search");


       // askPermission();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(com.auro.android.itravel.R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(com.auro.android.itravel.R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(com.auro.android.itravel.R.layout.custom_tabsearch, null);
        tabOne.setText("Search");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(com.auro.android.itravel.R.layout.custom_tabfav, null);
        tabTwo.setText(" Favorites");
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }


    /*private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //no perm yet
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},code);
        }
        else{
            //have permission so do nothing
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        lat = location.getLatitude();
                        longt = location.getLongitude();
                        String s = lat.toString();
                        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                        System.out.println("------------------"+lat+"-------------");
                    }
                    else
                    {

                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case code:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    askPermission();
                }
        }
    }

*/
    //deleted placeholderfragment

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    TabSearch tab1 = new TabSearch();

                    return tab1;
                case 1:
                    TabFav tab2 = new TabFav();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }


    }



}
