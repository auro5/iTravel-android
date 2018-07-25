package com.auro.android.itravel;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class TabInfo extends Fragment{
    String url;
    String id;
    TextView t1;
    TextView t11;
    TextView t2;
    TextView t22;
    TextView t3;
    TextView t33;
    TextView t4;
    TextView t44;
    TextView t5;
    TextView t55;

    TextView rt;
    RatingBar r;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.auro.android.itravel.R.layout.tinfo, container, false);
        id = getArguments().getString("id");
        url="http://auroex8-node.us-east-2.elasticbeanstalk.com/details?id=";
        url = url+id;

        t1 = rootView.findViewById(com.auro.android.itravel.R.id.textViewk);
        t11 = rootView.findViewById(com.auro.android.itravel.R.id.textView19);

        t2 = rootView.findViewById(com.auro.android.itravel.R.id.textView20);
        t22 = rootView.findViewById(com.auro.android.itravel.R.id.textView21);

        t3 = rootView.findViewById(com.auro.android.itravel.R.id.textView22);
        t33 = rootView.findViewById(com.auro.android.itravel.R.id.textView23);

        t4 = rootView.findViewById(com.auro.android.itravel.R.id.textView25);
        t44 = rootView.findViewById(com.auro.android.itravel.R.id.textView26);

        t5 = rootView.findViewById(com.auro.android.itravel.R.id.textView27);
        t55 = rootView.findViewById(com.auro.android.itravel.R.id.textView28);

        rt = rootView.findViewById(com.auro.android.itravel.R.id.textView24);
        r = rootView.findViewById(com.auro.android.itravel.R.id.rate);
        fillinfo();
        return rootView;
    }
    private void fillinfo() {
        System.out.println("----------------->>>>>>>>>>>>"+id);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Fetching Results");
        pd.show();

        JsonObjectRequest jar = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("----------------->>>>>>>>>>>>"+url);
                            pd.dismiss();

                        try {
                            //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject r1 = response.getJSONObject("result");
                            //Toast.makeText(getContext(), r1.toString(), Toast.LENGTH_SHORT).show();
                            if(r1.has("formatted_address")){

                                t11.setText(r1.getString("formatted_address"));

                            }
                            else{

                                t11.setText("No records");
                            }

                            if(r1.has("international_phone_number")){

                                t22.setText(r1.getString("international_phone_number"));

                            }
                            else{
                                t22.setText("No records");
                            }

                            if(r1.has("price_level")){

                                String price = r1.getString("price_level");

                                switch (price){
                                    case "0":
                                        t33.setText("0");
                                        break;
                                    case "1":
                                        t33.setText("$");
                                        break;
                                    case "2":
                                        t33.setText("$$");
                                        break;
                                    case "3":
                                        t33.setText("$$$");
                                        break;
                                    case "4":
                                        t33.setText("$$$$");
                                        break;
                                }


                            }
                            else{
                                t33.setText("No records");
                            }

                            if(r1.has("rating")){
                                rt.setVisibility(VISIBLE);
                                r.setRating(Float.parseFloat(r1.getString("rating")));
                                r.setVisibility(VISIBLE);
                            }
                            else{
                                rt.setVisibility(GONE);
                                r.setVisibility(GONE);
                            }

                            if(r1.has("url")){

                                t44.setText(r1.getString("url"));

                            }
                            else{
                                t44.setText("No records");
                            }

                            if(r1.has("website")){

                                t55.setText(r1.getString("website"));

                            }
                            else{
                                t55.setText("No records");
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


}
