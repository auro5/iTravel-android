package com.auro.android.itravel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabSearch extends Fragment implements AdapterView.OnItemSelectedListener{

    String[] category = {"Default","Airport","Amusement Park","Aquarium","Art Gallery","Bakery","Bar","Beauty Salon","Bowling Alley","Bus Station","Cafe","Campground","Car Rental","Casino","Lodging","Movie Theater","Museum","Night Club","Park","Parking","Restaurant","Shopping Mall","Stadium","Subway Station","Taxi Stand","Train Station","Transit Station","Travel Agency","Zoo"};
    String[] categoryValue = {"default","airport","amusement_park","aquarium","art_gallery","bakery","bar","beauty_salon","bowling_alley","bus_station","cafe","campground","car_rental","casino","lodging","movie_theater","museum","night_club","park","parking","restaurant","shopping_mall","stadium","subway_station","taxi_stand","train_station","transit_station","travel_agency","zoo"};
    String keyword="";
    String distance="";
    String cat="";
    double dist;
    double latit;
    double latit1;
    double latit2;
    double longit;
    double longit1;
    double longit2;

    EditText e1;
    EditText e2;
    Spinner spin;
    RadioButton CurL;
    RadioButton OtherL;

    TextView error1;
    TextView error2;

    String geoURL="http://auroex8-node.us-east-2.elasticbeanstalk.com/geocode?locterm=";

    AutoCompleteTextView a1;

    private Context cont;
    private Activity a;
    private FusedLocationProviderClient mFusedLocationClient;
    private final int code = 10;
    private PlaceAutocompleteAdapter mAutoAdapter;
    protected GeoDataClient mGeoDataClient;

    private static final LatLngBounds Bounds = new LatLngBounds(
            new LatLng(18.7763, 170.5957), new LatLng(71.5388001, -66.885417));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.auro.android.itravel.R.layout.tabsearch, container, false);
        askPermission();


        e1 = (EditText) rootView.findViewById(com.auro.android.itravel.R.id.editText);


        e2 = (EditText) rootView.findViewById(com.auro.android.itravel.R.id.editText2);

        error2  = rootView.findViewById(com.auro.android.itravel.R.id.err2);
        error1  = rootView.findViewById(com.auro.android.itravel.R.id.textView5);

        a1 = rootView.findViewById(com.auro.android.itravel.R.id.altLocation);
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        mAutoAdapter = new PlaceAutocompleteAdapter(getContext(),mGeoDataClient, Bounds, null);

        a1.setAdapter(mAutoAdapter);

        cont = this.getContext();
        a = this.getActivity();
        CurL = (RadioButton) rootView.findViewById(com.auro.android.itravel.R.id.radioButton);
        OtherL = (RadioButton) rootView.findViewById(com.auro.android.itravel.R.id.radioButton2);

        CurL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setEnabled(false);
                error2.setVisibility(View.GONE);
            }
        });

        OtherL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setEnabled(true);
            }
        });

        spin = (Spinner) rootView.findViewById(com.auro.android.itravel.R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        Button search = (Button) rootView.findViewById(com.auro.android.itravel.R.id.button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = e1.getText().toString();
                distance = e2.getText().toString();

                if(distance.length()==0)
                    dist = 10*1609.34;
               else
                   dist= Double.parseDouble(distance)*1609.34;

                if(CurL.isChecked()){

                    latit = latit1;
                    longit = longit1;
                    if(keyword.length()==0){
                        error1.setVisibility(View.VISIBLE);
                        Toast.makeText(cont, "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        error1.setVisibility(View.GONE);

                        Intent intent = new Intent(getContext(), ResultsPage.class);
                        intent.putExtra("key",keyword);
                        intent.putExtra("cat",cat);
                        intent.putExtra("dist",String.valueOf(dist));
                        intent.putExtra("latitude",String.valueOf(latit));
                        intent.putExtra("longitude",String.valueOf(longit));
                        startActivity(intent);
                    }
                }
                else{

                    String auto = a1.getText().toString();

                    if(auto.length()==0 && keyword.length()==0){
                        error1.setVisibility(View.VISIBLE);
                        error2.setVisibility(View.VISIBLE);
                        Toast.makeText(cont, "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    }
                    else
                        if(keyword.length()==0){
                            error1.setVisibility(View.VISIBLE);
                            Toast.makeText(cont, "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                        }
                        else
                            if(auto.length()==0){
                                error2.setVisibility(View.VISIBLE);
                                Toast.makeText(cont, "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                geoURL = geoURL+auto;

                                JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, geoURL, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    JSONArray r1 = response.getJSONArray("results");
                                                    JSONObject r2 = r1.getJSONObject(0);
                                                    JSONObject r3 = r2.getJSONObject("geometry");
                                                    JSONObject r4 = r3.getJSONObject("location");
                                                    latit2 = Double.parseDouble(r4.getString("lat"));
                                                    longit2 = Double.parseDouble(r4.getString("lng"));
                                                    latit = latit2;
                                                    longit = longit2;
                                                    error1.setVisibility(View.GONE);
                                                    error2.setVisibility(View.GONE);
                                                    Intent intent = new Intent(getContext(), ResultsPage.class);
                                                    intent.putExtra("key",keyword);
                                                    intent.putExtra("cat",cat);
                                                    intent.putExtra("dist",String.valueOf(dist));
                                                    intent.putExtra("latitude",String.valueOf(latit));
                                                    intent.putExtra("longitude",String.valueOf(longit));
                                                    startActivity(intent);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Volley.newRequestQueue(getContext()).add(jar);


                            }
                }

            }
        });

        Button clear = (Button) rootView.findViewById(com.auro.android.itravel.R.id.button2);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.getText().clear();
                e2.getText().clear();
                spin.setSelection(0);
                CurL.setChecked(true);
                error1.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);
            }
        });

        return rootView;

    }


    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        cat = categoryValue[position];
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }


    public void askPermission() {
    try {
        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //no perm yet

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, code);
        } else {
            //have permission so do nothing

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                       latit1 = location.getLatitude();
                       longit1 = location.getLongitude();
                        //Toast.makeText(getContext(), "Granted", Toast.LENGTH_SHORT).show();
                        //System.out.println("-----dinchak---" + l + "-----------------------");
                    } else {
                        //Toast.makeText(cont, "Grantedddddddddd", Toast.LENGTH_SHORT).show();
                        //askPermission();
                        System.out.println("-------------------------------");
                    }
                }
            });
        }
    }
    catch (Exception e){
        System.out.println("SOME  4 ERROR!!!!!!!");
    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case code:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this.getContext(), "Granted", Toast.LENGTH_SHORT).show();
                    askPermission();
                }
                else{
                    askPermission();
                }
        }
    }


}
