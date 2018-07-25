package com.auro.android.itravel;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TabRev extends Fragment implements AdapterView.OnItemSelectedListener{

    String[] reviews = {"Google reviews","Yelp reviews"};
    String[] order = {"Default order","Highest rating","Lowest rating","Most recent","Least recent"};
    Spinner s1;
    Spinner s2;
    String id;
    String url="http://auroex8-node.us-east-2.elasticbeanstalk.com/details?id=";
    String url1; //for google
    String url2=""; //for yelp
    String url3="";

    TextView t1;
    RecyclerView recyclerView;
    List<ReviewItem> revList;

    JSONArray rev = new JSONArray();
    JSONArray Yrev = new JSONArray();
    JSONObject r1 = new JSONObject();

    int flag1=0;
    int flag2=0;//for sorting

    ReviewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.auro.android.itravel.R.layout.trev, container, false);
        id = getArguments().getString("id");
        t1 = rootView.findViewById(com.auro.android.itravel.R.id.norev);

        s1 = (Spinner) rootView.findViewById(com.auro.android.itravel.R.id.spinner3);
        s1.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, reviews);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);

        s2 = (Spinner) rootView.findViewById(com.auro.android.itravel.R.id.spinner4);
        s2.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, order);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);

        recyclerView = (RecyclerView) rootView.findViewById(com.auro.android.itravel.R.id.rec1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        revList = new ArrayList<>();
        //showGoogleReviews();
        return rootView;
    }

    private void showGoogleReviews() {
        flag1=1;
        url1 = url+id;
        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            r1 = response.getJSONObject("result");
                            if(r1.has("reviews")){
                                rev = r1.getJSONArray("reviews");
                                if(rev.length()==0){
                                    t1.setVisibility(View.VISIBLE);
                                }
                                else{


                                    String orderOption = s2.getSelectedItem().toString();
                                    if(orderOption=="Default order")
                                        displayDefault(rev);
                                    if(orderOption=="Highest rating")
                                        displayHR(rev);
                                    if(orderOption=="Lowest rating")
                                        displayLR(rev);
                                    if(orderOption=="Most recent")
                                        displayMR(rev);
                                    if(orderOption=="Least recent")
                                        displayLeastR(rev);


                                }
                            }
                            else{
                                t1.setVisibility(View.VISIBLE);
                            }

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

    private void displayLeastR(JSONArray rev) {
        try{
            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < rev.length(); i++) {
                jsonValues.add(rev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "time";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < rev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }

            //cleam up previous
            while (revList.size() != 0){
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                String aname = r2.getString("author_name");
                String rrate = r2.getString("rating");
                String rtext = r2.getString("text");
                String rtime = r2.getString("time");
                String aurl="";
                if(r2.has("author_url")){
                    aurl = r2.getString("author_url");
                }
                String aphoto = "";
                if (r2.has("profile_photo_url")) {
                    aphoto = r2.getString("profile_photo_url");
                }
                ReviewItem list = new ReviewItem(aname, aphoto, rrate, rtext, rtime,aurl);
                revList.add(list);
            }
            ReviewAdapter adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void displayMR(JSONArray rev) {
        try{
            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < rev.length(); i++) {
                jsonValues.add(rev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "time";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return -valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < rev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
            //cleam up previous
            while (revList.size() != 0){
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                String aname = r2.getString("author_name");
                String rrate = r2.getString("rating");
                String rtext = r2.getString("text");
                String rtime = r2.getString("time");
                String aurl="";
                if(r2.has("author_url")){
                    aurl = r2.getString("author_url");
                }
                String aphoto = "";
                if (r2.has("profile_photo_url")) {
                    aphoto = r2.getString("profile_photo_url");
                }
                ReviewItem list = new ReviewItem(aname, aphoto, rrate, rtext, rtime,aurl);
                revList.add(list);
            }
            ReviewAdapter adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void displayLR(JSONArray rev) {
        try{
            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < rev.length(); i++) {
                jsonValues.add(rev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "rating";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < rev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
            //cleam up previous
            while (revList.size() != 0){
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                String aname = r2.getString("author_name");
                String rrate = r2.getString("rating");
                String rtext = r2.getString("text");
                String rtime = r2.getString("time");
                String aurl="";
                if(r2.has("author_url")){
                    aurl = r2.getString("author_url");
                }
                String aphoto = "";
                if (r2.has("profile_photo_url")) {
                    aphoto = r2.getString("profile_photo_url");
                }
                ReviewItem list = new ReviewItem(aname, aphoto, rrate, rtext, rtime,aurl);
                revList.add(list);
            }
            ReviewAdapter adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayHR(JSONArray rev) {
    try{
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < rev.length(); i++) {
            jsonValues.add(rev.getJSONObject(i));
        }

        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "rating";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA =  String.valueOf(a.getInt(KEY_NAME));
                    valB =  String.valueOf(b.getInt(KEY_NAME));
                }
                catch (JSONException e) {
                    //do something
                }

                return -valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < rev.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }

        while (revList.size() != 0){
            revList.remove(0);
        }
        adapter.notifyDataSetChanged();
        t1.setVisibility(View.GONE);
        for (int i = 0; i < sortedJsonArray.length(); i++) {
            JSONObject r2 = sortedJsonArray.getJSONObject(i);
            String aname = r2.getString("author_name");
            String rrate = r2.getString("rating");
            String rtext = r2.getString("text");
            String rtime = r2.getString("time");
            String aurl="";
            if(r2.has("author_url")){
                aurl = r2.getString("author_url");
            }
            String aphoto = "";
            if (r2.has("profile_photo_url")) {
                aphoto = r2.getString("profile_photo_url");
            }
            ReviewItem list = new ReviewItem(aname, aphoto, rrate, rtext, rtime,aurl);
            revList.add(list);
        }
        ReviewAdapter adapter = new ReviewAdapter(getContext(), revList);
        recyclerView.setAdapter(adapter);

    }
    catch (JSONException e) {
        e.printStackTrace();
        }
    }


    private void displayDefault(JSONArray rev) {
        try {
            if(flag2==1) {
                while (revList.size() != 0) {
                    revList.remove(0);
                }
                adapter.notifyDataSetChanged();
            }
            flag2=1;
            t1.setVisibility(View.GONE);
            for (int i = 0; i < rev.length(); i++) {
                JSONObject r2 = rev.getJSONObject(i);
                String aname = r2.getString("author_name");
                String rrate = r2.getString("rating");
                String rtext = r2.getString("text");
                String rtime = r2.getString("time");
                String aurl="";
                if(r2.has("author_url")){
                    aurl = r2.getString("author_url");
                }
                String aphoto = "";
                if (r2.has("profile_photo_url")) {
                    aphoto = r2.getString("profile_photo_url");
                }
                ReviewItem list = new ReviewItem(aname, aphoto, rrate, rtext, rtime,aurl);
                revList.add(list);
            }
            adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        changeReviewsOrder(position);
    }

    private void orderReviews(String o) {

        String whichRev = s1.getSelectedItem().toString();
       if(whichRev=="Google reviews"){

            if(o=="Default order")
            {
                displayDefault(rev);
            }
            if(o=="Highest rating")
            {
                displayHR(rev);
            }
            if(o=="Lowest rating")
            {
                displayLR(rev);
            }
            if(o=="Most recent")
            {
                displayMR(rev);
            }
            if(o=="Least recent")
            {
                displayLeastR(rev);
            }
        }

    }

    private void changeReviewsOrder(int position) {
        String r = s1.getSelectedItem().toString();
        String o = s2.getSelectedItem().toString();

        if(r=="Google reviews"){
            if(flag1==0)
                showGoogleReviews();
            else
                orderReviews(o);
        }
        else{
            showYelpReviews();
        }
        
    }

    private void showYelpReviews() {

        try {
            String Yname = r1.getString("name");
            if(Yname.length()>64)
                Yname = Yname.substring(0,65);

            String Yaddr =  r1.getString("formatted_address");
            Yaddr = Yaddr.substring(0,Yaddr.indexOf(","));
            if(Yaddr.length()>64)
                Yaddr = Yaddr.substring(0,65);

            String city="";
            String state="";
            String country="";

            JSONArray place = r1.getJSONArray("address_components");
            for(int i=0;i<place.length();i++){
                JSONObject element = place.getJSONObject(i);
                JSONArray element1 = element.getJSONArray("types");

                if(element1.getString(0).equals("locality"))
                    city=element.getString("short_name");

                if(element1.getString(0).equals("country"))
                    country=element.getString("short_name");

                if(element1.getString(0).equals("administrative_area_level_1"))
                    state=element.getString("short_name");

            }
            url2="http://auroex8-node.us-east-2.elasticbeanstalk.com/yelpbm?name=";
            url2 = url2+Yname+"&address="+Yaddr+"&city="+city+"&state="+state+"&country="+country;
            System.out.println("url2 "+url2);
            JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url2, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("4000000000000000");
                            try {
                                if (response.has("Error")) {
                                    while (revList.size() != 0) {
                                        revList.remove(0);
                                    }
                                    adapter.notifyDataSetChanged();

                                    t1.setVisibility(View.VISIBLE);
                                }
                                else{
                                    JSONArray bus = response.getJSONArray("businesses");
                                    if (bus.length() == 0) {
                                        System.out.println("5000000000000000");
                                        while (revList.size() != 0) {
                                            revList.remove(0);
                                        }
                                        adapter.notifyDataSetChanged();

                                        t1.setVisibility(View.VISIBLE);
                                    } else {

                                        JSONObject busrev = bus.getJSONObject(0);
                                        String revID = busrev.getString("id");
                                        url3="http://auroex8-node.us-east-2.elasticbeanstalk.com/yelpbr?id=";
                                        url3 = url3 + revID;

                                        showYelpReviews1();
                                }
                            }
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

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showYelpReviews1() {

        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Yrev = response.getJSONArray("reviews");
                            if(Yrev.length()==0){

                                t1.setVisibility(View.VISIBLE);
                            }
                            else{

                                String orderOption = s2.getSelectedItem().toString();
                                if(orderOption=="Default order")
                                    YdisplayDefault(Yrev);
                                if(orderOption=="Highest rating")
                                    YdisplayHR(Yrev);
                                if(orderOption=="Lowest rating")
                                    YdisplayLR(Yrev);
                                if(orderOption=="Most recent")
                                    YdisplayMR(Yrev);
                                if(orderOption=="Least recent")
                                    YdisplayLeastR(Yrev);
                            }
                        }
                        catch (JSONException e) {
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

    private void YdisplayLeastR(JSONArray yrev) {
        try {

            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < yrev.length(); i++) {
                jsonValues.add(yrev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "time_created";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < yrev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }



            while (revList.size() != 0) {
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                //String yurl = r2.getString("author_name");
                String ytext = r2.getString("text");
                String yrate = r2.getString("rating");
                String ytime = r2.getString("time_created");
                String yurl="";
                if(r2.has("url")){
                    yurl = r2.getString("url");
                }
                String yphoto = "";
                String yname = "";
                JSONObject user = r2.getJSONObject("user");
                if (user.has("image_url")) {
                    yphoto = user.getString("image_url");
                }
                yname = user.getString("name");

                ReviewItem list = new ReviewItem(yname, yphoto, yrate, ytext, ytime,yurl);
                revList.add(list);
            }
            adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void YdisplayMR(JSONArray yrev) {
        try {

            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < yrev.length(); i++) {
                jsonValues.add(yrev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "time_created";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return -valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < yrev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }



            while (revList.size() != 0) {
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                //String yurl = r2.getString("author_name");
                String ytext = r2.getString("text");
                String yrate = r2.getString("rating");
                String ytime = r2.getString("time_created");
                String yurl="";
                if(r2.has("url")){
                    yurl = r2.getString("url");
                }
                String yphoto = "";
                String yname = "";
                JSONObject user = r2.getJSONObject("user");
                if (user.has("image_url")) {
                    yphoto = user.getString("image_url");
                }
                yname = user.getString("name");

                ReviewItem list = new ReviewItem(yname, yphoto, yrate, ytext, ytime,yurl);
                revList.add(list);
            }
            adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void YdisplayLR(JSONArray yrev) {
        try {

            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < yrev.length(); i++) {
                jsonValues.add(yrev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "rating";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < yrev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }



            while (revList.size() != 0) {
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                //String yurl = r2.getString("author_name");
                String ytext = r2.getString("text");
                String yrate = r2.getString("rating");
                String ytime = r2.getString("time_created");
                String yurl="";
                if(r2.has("url")){
                    yurl = r2.getString("url");
                }
                String yphoto = "";
                String yname = "";
                JSONObject user = r2.getJSONObject("user");
                if (user.has("image_url")) {
                    yphoto = user.getString("image_url");
                }
                yname = user.getString("name");

                ReviewItem list = new ReviewItem(yname, yphoto, yrate, ytext, ytime,yurl);
                revList.add(list);
            }
            adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void YdisplayHR(JSONArray yrev) {
        try {

            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < yrev.length(); i++) {
                jsonValues.add(yrev.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "rating";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA =  String.valueOf(a.getInt(KEY_NAME));
                        valB =  String.valueOf(b.getInt(KEY_NAME));
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return -valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < yrev.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }



            while (revList.size() != 0) {
                revList.remove(0);
            }
            adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject r2 = sortedJsonArray.getJSONObject(i);
                //String yurl = r2.getString("author_name");
                String ytext = r2.getString("text");
                String yrate = r2.getString("rating");
                String ytime = r2.getString("time_created");
                String yurl="";
                if(r2.has("url")){
                    yurl = r2.getString("url");
                }
                String yphoto = "";
                String yname = "";
                JSONObject user = r2.getJSONObject("user");
                if (user.has("image_url")) {
                    yphoto = user.getString("image_url");
                }
                yname = user.getString("name");

                ReviewItem list = new ReviewItem(yname, yphoto, yrate, ytext, ytime,yurl);
                revList.add(list);
            }
            adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void YdisplayDefault(JSONArray yrev) {
        try {

                while (revList.size() != 0) {
                    revList.remove(0);
                }
                adapter.notifyDataSetChanged();
            t1.setVisibility(View.GONE);
            for (int i = 0; i < yrev.length(); i++) {
                JSONObject r2 = yrev.getJSONObject(i);
                //String yurl = r2.getString("author_name");
                String ytext = r2.getString("text");
                String yrate = r2.getString("rating");
                String ytime = r2.getString("time_created");
                String yurl="";
                if(r2.has("url")){
                    yurl = r2.getString("url");
                }
                String yphoto = "";
                String yname = "";
                JSONObject user = r2.getJSONObject("user");
                if (user.has("image_url")) {
                    yphoto = user.getString("image_url");
                }
                yname = user.getString("name");

                ReviewItem list = new ReviewItem(yname, yphoto, yrate, ytext, ytime,yurl);
                revList.add(list);
            }
            adapter = new ReviewAdapter(getContext(), revList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

