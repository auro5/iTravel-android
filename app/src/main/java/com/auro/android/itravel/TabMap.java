package com.auro.android.itravel;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TabMap extends Fragment implements OnMapReadyCallback, AdapterView.OnItemSelectedListener{
    String url="";
    String url1;
    String id;
    private Context c;
    String[] category = {"Driving","Bicycling","Transit","Walking"};
    String[] categoryVal = {"driving","bicycling","transit","walking"};
    EditText e1;
    Spinner spin;
    String mode;
    String name;
    String latit;
    String longit;
    String fromNAME="";

    String latit1;
    String longit1;
    SupportMapFragment mapFragment;
    SupportMapFragment mapFragment1;
    String LINE;
    private GoogleMap mMap;
    int flag=0;
    Polyline p = null;
    Marker m1 = null;
    Marker m2 = null;

    AutoCompleteTextView a1;
    private PlaceAutocompleteAdapter mAutoAdapter;
    protected GeoDataClient mGeoDataClient;

    private static final LatLngBounds Bounds = new LatLngBounds(
            new LatLng(18.7763, 170.5957), new LatLng(71.5388001, -66.885417));


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.auro.android.itravel.R.layout.tmap, container, false);
        id = getArguments().getString("id");
        url = "http://auroex8-node.us-east-2.elasticbeanstalk.com/details?id=";
        url = url+id;
        getLatLon();
        spin = (Spinner) rootView.findViewById(com.auro.android.itravel.R.id.spinner2);
        spin.setOnItemSelectedListener(this);


        a1 = rootView.findViewById(com.auro.android.itravel.R.id.mapAuto);
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        mAutoAdapter = new PlaceAutocompleteAdapter(getContext(),mGeoDataClient, Bounds, null);

        a1.setAdapter(mAutoAdapter);
        a1.setOnItemClickListener(matu);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);



        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(com.auro.android.itravel.R.id.map);

        //mapFragment.getMapAsync(this);

        return rootView;
    }

    private void getLatLon() {
        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject r1 = response.getJSONObject("result");
                            name = r1.getString("name");
                            JSONObject geometry = r1.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latit = location.getString("lat");
                            longit = location.getString("lng");

                            mapFragment.getMapAsync((OnMapReadyCallback) TabMap.this);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        LatLng place = new LatLng(Double.parseDouble(latit), Double.parseDouble(longit));
        googleMap.addMarker(new MarkerOptions().position(place)
                .title(name)).showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,13));
        mMap = googleMap;

    }
    protected GoogleMap getMap() {
        return mMap;
    }

    private void startDemo() {
        url1+=a1.getText().toString()+"&destination="+latit+","+longit+"&mode="+mode+"&key=AIzaSyC8QKLRnz5eB9_-Kae1nkF-rX6HSCSZ6O4";
        System.out.println(url1);
        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray r1 = response.getJSONArray("routes");
                            JSONObject r2 = r1.getJSONObject(0);
                            JSONArray legs = r2.getJSONArray("legs");
                            JSONObject r3 = legs.getJSONObject(0);
                            JSONObject startLOC = r3.getJSONObject("start_location");
                            latit1 = startLOC.getString("lat");  //from address
                            longit1 = startLOC.getString("lng");
                            JSONObject r4 = r2.getJSONObject("overview_polyline");
                            LINE = r4.getString("points");
                            startDemo1();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        url1="https://maps.googleapis.com/maps/api/directions/json?origin=";
        ///creat a ne mMap becos previous result overlapping
            mode = categoryVal[position];
            System.out.println("mmmmaaaaaaapppppp"+a1.getText().toString());
            if(a1.getText().toString().length()==0){

            }
            else
                startDemo();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void startDemo1(){

        List<LatLng> decodedPath = PolyUtil.decode(LINE);
        LatLng to = new LatLng(Double.parseDouble(latit), Double.parseDouble(longit));
        LatLng from = new LatLng(Double.parseDouble(latit1), Double.parseDouble(longit1));
        if(flag==1){
            m1.remove();
            m2.remove();
        }

        m1 = getMap().addMarker(new MarkerOptions().position(to));
        m2 = getMap().addMarker(new MarkerOptions().position(from).title(fromNAME));

        fromNAME="";
        fromNAME = a1.getText().toString();
        fromNAME = fromNAME.substring(0,fromNAME.indexOf(","));
        //getMap().addMarker(new MarkerOptions().position(to));
        //getMap().addMarker(new MarkerOptions().position(from));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(m1.getPosition());
        builder.include(m2.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 65;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        if(flag==0)
            p = getMap().addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.BLUE).width(15));

        if(flag==1) {
            p.remove();

            p = getMap().addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.BLUE).width(15));
        }

        flag=1;
        //getMap().addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.BLUE).width(15));
        //getMap().addMarker(new MarkerOptions().position(from).title(fromNAME)).showInfoWindow();

        getMap().moveCamera(cu);
    }
private AdapterView.OnItemClickListener matu = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("YAY");
        url1="https://maps.googleapis.com/maps/api/directions/json?origin=";
        startDemo();
    }
};
}

