package com.auro.android.itravel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultsPage extends AppCompatActivity{
    String url = "http://auroex8-node.us-east-2.elasticbeanstalk.com/places?key=";
    Button p;
    Button n;
    RecyclerView recyclerView;
    List<ListItem> productList;
    List<ListItem> productList1;
    List<ListItem> productList2;

    TextView t;
    String url4;
    String url5;

    int flag=0;

    int count=0;//for displaying progress bar only the first time

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.auro.android.itravel.R.layout.result_page);
        setTitle("Search Results");
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        String cat = intent.getStringExtra("cat");
        String dist = intent.getStringExtra("dist");
        String latit = intent.getStringExtra("latitude");
        String longit = intent.getStringExtra("longitude");
        //Toast.makeText(this,dist, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        n = (Button) findViewById(com.auro.android.itravel.R.id.next);
        p = (Button) findViewById(com.auro.android.itravel.R.id.prev);
        t = (TextView) findViewById(com.auro.android.itravel.R.id.noresults);

        url = url+key+"&category="+cat+"&dist="+dist+"&latitude="+latit+"&longitude="+longit;
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(com.auro.android.itravel.R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productList1 = new ArrayList<>();
        productList2 = new ArrayList<>();
        loaditems();
    }

    private void loaditems() {
        p.setEnabled(false);

        final ProgressDialog pd = new ProgressDialog(this);
        if(count==0){
            pd.setMessage("Fetching Results");
            pd.show();
        }

        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(count==0)
                            pd.dismiss();

                        try {


                            JSONArray r1 = response.getJSONArray("results");
                            if (r1.length() == 0)
                            {
                                t.setVisibility(View.VISIBLE);
                                p.setVisibility(View.GONE);
                                n.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);

                            }
                            else{
                            for (int i = 0; i < r1.length(); i++) {
                                JSONObject r2 = r1.getJSONObject(i);
                                String icon = r2.getString("icon");
                                String name = r2.getString("name");
                                String add = r2.getString("vicinity");
                                String id = r2.getString("place_id");
                                System.out.println(id);
                                ListItem list = new ListItem(icon, name, add, id, com.auro.android.itravel.R.drawable.heart_outline_black);
                                productList.add(list);
                                productList1.add(list);
                                flag = 1;

                            }
                            final MyAdapter adapter = new MyAdapter(ResultsPage.this, productList);
                            recyclerView.setAdapter(adapter);

                            if (response.has("next_page_token")) {

                                final String token1 = response.getString("next_page_token");

                                n.setEnabled(true);

                                n.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        count = 1;
                                        //Intent i = new Intent(MainActivity.this, third.class);
                                        //startActivity(i);

                                        while (productList.size() != 0) {
                                            productList.remove(0);
                                        }

                                        adapter.notifyDataSetChanged();

                                        String url1 = "http://auroex8-node.us-east-2.elasticbeanstalk.com/nextpage?token=" + token1;
                                        nextPage(url1);
                                    }
                                });
                            }

                        }//end of else --> result length!=0
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResultsPage.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jar);
    }

    public void nextPage(String url1) {
        url4 = url1;
        url1=url4;
        final ProgressDialog pd = new ProgressDialog(this);
        if(count==1){
            pd.setMessage("Fetching Next Page");
            pd.show();
        }
        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(count==1)
                            pd.dismiss();
                        n.setEnabled(false);
                        p.setEnabled(true);

                        try {
                            productList=new ArrayList<>();
                            JSONArray r1 = response.getJSONArray("results");
                            for (int i = 0; i < r1.length(); i++) {
                                JSONObject r2 = r1.getJSONObject(i);
                                String icon = r2.getString("icon");
                                String name = r2.getString("name");
                                String add = r2.getString("vicinity");
                                String id = r2.getString("place_id");

                                ListItem list = new ListItem(icon, name, add, id, com.auro.android.itravel.R.drawable.heart_outline_black);
                                productList.add(list);
                                productList2.add(list);
                            }
                            final MyAdapter adapter1 = new MyAdapter(ResultsPage.this, productList);
                            recyclerView.setAdapter(adapter1);

                            p.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    while (productList.size() != 0)
                                        productList.remove(0);

                                    count=2;//to dismiss progressbar
                                    adapter1.notifyDataSetChanged();
                                    MyAdapter adapterO = new MyAdapter(ResultsPage.this, productList1);  //O stands for One
                                    p.setEnabled(false);
                                    n.setEnabled(true);
                                    recyclerView.setAdapter(adapterO);
                                }
                            });

                            if (response.has("next_page_token")) {
                                final String token2 = response.getString("next_page_token");
                                n.setEnabled(true);
                                n.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        count=2;
                                        //Intent i = new Intent(MainActivity.this, third.class);
                                        //startActivity(i);

                                        while (productList.size() != 0){
                                            productList.remove(0);
                                        }

                                        adapter1.notifyDataSetChanged();

                                        String url1 = "http://auroex8-node.us-east-2.elasticbeanstalk.com/nextpage?token=" + token2;
                                        nextPage1(url1);
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResultsPage.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jar);
    }

    public void nextPage1(String url1) {
        url5 = url1;
        url1=url5;
        final ProgressDialog pd = new ProgressDialog(this);
        if(count==2){
            pd.setMessage("Fetching Next Page");
            pd.show();
        }
        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(count==2)
                            pd.dismiss();
                        n.setEnabled(false);
                        p.setEnabled(true);

                        try {

                            JSONArray r1 = response.getJSONArray("results");
                            for (int i = 0; i < r1.length(); i++) {
                                JSONObject r2 = r1.getJSONObject(i);
                                String icon = r2.getString("icon");
                                String name = r2.getString("name");
                                String add = r2.getString("vicinity");
                                String id = r2.getString("place_id");

                                ListItem list = new ListItem(icon, name, add, id, com.auro.android.itravel.R.drawable.heart_outline_black);
                                productList.add(list);
                            }
                            final MyAdapter adapter2 = new MyAdapter(ResultsPage.this, productList);
                            recyclerView.setAdapter(adapter2);

                            p.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    while (productList.size() != 0)
                                        productList.remove(0);

                                    adapter2.notifyDataSetChanged();

                                    MyAdapter adapterT = new MyAdapter(ResultsPage.this, productList2);
                                    recyclerView.setAdapter(adapterT);
                                    p.setEnabled(true);
                                    n.setEnabled(true);
                                    count=3;
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResultsPage.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
            {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                return true;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    if(count==0)
    {
        loaditems();
        //count=1;
    }
    else
        if(count==1) {
            nextPage(url4);

         }
        else
            if(count==2) {
            nextPage1(url5);

            }
    }
}
