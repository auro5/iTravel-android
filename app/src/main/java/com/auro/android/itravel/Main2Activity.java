package com.auro.android.itravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String id;
    String name;
    String addr;
    String faddr;
    String cat;
    String website="";

    ImageView twitter;
    ImageView whiteFAV;
    String url;
    String twitterUrl;
    TextView t;
    SharedPreferences shredPref;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.auro.android.itravel.R.layout.activity_main2);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        url="http://auroex8-node.us-east-2.elasticbeanstalk.com/details?id=";
        url = url+id;

        name = intent.getStringExtra("name");
        faddr = intent.getStringExtra("addr");
        cat = intent.getStringExtra("cat");

        Toolbar toolbar = (Toolbar) findViewById(com.auro.android.itravel.R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle(name);//24
        t = findViewById(com.auro.android.itravel.R.id.toolTitle);
        twitter = findViewById(com.auro.android.itravel.R.id.twitter);
        whiteFAV = findViewById(com.auro.android.itravel.R.id.toolFav);

        if(name.length()>20){
            String name1 = name.substring(0,24) +"...";
            t.setText(name1);
        }
        else{
            t.setText(name);
        }
        getinfoTwitFav();

        shredPref = getSharedPreferences("myFav", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= shredPref.edit();

        String favCheck = shredPref.getString(id,"");
        if(favCheck=="") {
            whiteFAV.setImageDrawable(getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_outline_white));
            flag=0;
        }
        else{
            whiteFAV.setImageDrawable(getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_fill_white));
            flag=1;
        }

        whiteFAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (flag==0) {

                        JSONObject json = new JSONObject().put("name", name)
                                .put("addr",faddr)
                                .put("catImage",cat)
                                .put("favIcon", com.auro.android.itravel.R.drawable.heart_fill_red)
                                .put("id",id);

                        editor.putString(id,json.toString());

                        whiteFAV.setImageDrawable(getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_fill_white));
                        Toast.makeText(Main2Activity.this, name + " was added to favorites", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.remove(id);

                        whiteFAV.setImageDrawable(getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_outline_white));
                        Toast.makeText(Main2Activity.this, name + " was removed from favorites", Toast.LENGTH_SHORT).show();
                    }

                    editor.apply();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(com.auro.android.itravel.R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(com.auro.android.itravel.R.id.tabs1);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(com.auro.android.itravel.R.layout.custom_tabinfo, null);
        tabOne.setText(" INFO");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(com.auro.android.itravel.R.layout.custom_tabphoto, null);
        tabTwo.setText(" PHOTOS");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(com.auro.android.itravel.R.layout.custom_tabmap, null);
        tabThree.setText(" MAP");
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(com.auro.android.itravel.R.layout.custom_tabrev, null);
        tabFour.setText(" REVIEWS");
        tabLayout.getTabAt(3).setCustomView(tabFour);


    }

    private void getinfoTwitFav() {
        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           JSONObject r1 = response.getJSONObject("result");
                            addr = r1.getString("formatted_address");
                            website = r1.getString("website");
                            twitterUrl = "https://twitter.com/intent/tweet?text=Check out " + name + " located at " + addr + ".\n" +"Website: "+ website;

                            twitter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                    browserIntent.setData(Uri.parse(twitterUrl));
                                    startActivity(browserIntent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main2Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(Main2Activity.this).add(jar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
        }


        return super.onOptionsItemSelected(item);
    }


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
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    TabInfo tab1 = new TabInfo();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", id);
                    TabPhoto tab2 = new TabPhoto();
                    tab2.setArguments(bundle1);
                    return tab2;
                case 2:
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("id", id);
                    TabMap tab3 = new TabMap();
                    tab3.setArguments(bundle3);
                    return tab3;
                case 3:
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("id", id);
                    TabRev tab4 = new TabRev();
                    tab4.setArguments(bundle4);
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }
}
