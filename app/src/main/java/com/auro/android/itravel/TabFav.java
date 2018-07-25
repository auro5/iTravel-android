package com.auro.android.itravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TabFav extends Fragment {
    RecyclerView recyclerView;
    List<FavItem> favList;
    FavAdapter1 adapter;
    TextView t;
    int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.auro.android.itravel.R.layout.tabfav, container, false);

         t = (TextView) rootView.findViewById(com.auro.android.itravel.R.id.textView2);
        recyclerView = (RecyclerView) rootView.findViewById(com.auro.android.itravel.R.id.rec2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showfav();
        return rootView;
    }

    private void showfav() {
        File f = new File("/data/data/com.webt.auro.tabtry2/shared_prefs/myFav.xml");
        //SharedPreferences s1 = getActivity().getSharedPreferences("myFav", Context.MODE_PRIVATE);


        favList = new ArrayList<>();

        if (f.exists()) {
            Log.d("TAG1","file present");
            t.setVisibility(View.GONE);
            SharedPreferences shredPref1 = getActivity().getSharedPreferences("myFav", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor= shredPref1.edit();

            int len = shredPref1.getAll().size();
            if(len==0){

                t.setVisibility(View.VISIBLE);
                if(flag!=0){
                    while (favList.size() != 0){
                        favList.remove(0);
                    }
                    adapter = new FavAdapter1(getContext(), favList);
                    recyclerView.setAdapter(adapter);
                }

            }
            else{

                Map<String,?> entries = shredPref1.getAll();
                Set<String> keys = entries.keySet();

                for (String key : keys) {
                    String getValue = shredPref1.getString(key,"");

                    if(getValue!=""){
                        flag=1;
                        try{
                            JSONObject response = new JSONObject(getValue);
                            String icon = response.getString("catImage");
                            String name = response.getString("name");
                            String add = response.getString("addr");
                            String id = response.getString("id");
                            FavItem list = new FavItem(icon, name, add, id, com.auro.android.itravel.R.drawable.heart_fill_red);
                            favList.add(list);

                            adapter = new FavAdapter1(getContext(), favList);
                            recyclerView.setAdapter(adapter);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }
        else {
            t.setVisibility(View.VISIBLE);
            Log.d("TAG1","file absent");
        }//t.setText(strtext);

    }

    class FavAdapter1 extends RecyclerView.Adapter<FavAdapter1.FavHolder>{
        private Context cont;
        private List<FavItem> list1;
        SharedPreferences shredPref2;

        public FavAdapter1(Context cont, List<FavItem> list1) {
            this.cont = cont;
            this.list1 = list1;
        }

        @NonNull
        @Override
        public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(cont);
            View v = inflater.inflate(com.auro.android.itravel.R.layout.list_fav, null);
            return new FavHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull FavHolder holder, int position) {
            shredPref2 = cont.getSharedPreferences("myFav", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor= shredPref2.edit();

            final FavItem list2 = list1.get(position);

            holder.t1.setText(list2.getFavName());
            holder.t2.setText(list2.getFavAddr());
            holder.t3.setText(list2.getFavID());

            Glide.with(cont).load(list2.getFavImage()).into(holder.im1);
            holder.im2.setImageDrawable(cont.getResources().getDrawable(list2.getFavIcon()));

            holder.im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.remove(list2.getFavID());
                    editor.apply();
                    Toast.makeText(cont, list2.getFavName() + " was removed from favorites", Toast.LENGTH_SHORT).show();
                    while (favList.size() != 0){
                        favList.remove(0);
                    }
                    adapter.notifyDataSetChanged();
                    showfav();
                }
            });

            holder.t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cont, Main2Activity.class);
                    intent.putExtra("id", list2.getFavID());
                    intent.putExtra("name", list2.getFavName());
                    intent.putExtra("addr", list2.getFavAddr());
                    intent.putExtra("cat", list2.getFavImage());
                    cont.startActivity(intent);
                }
            });

            holder.t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cont, Main2Activity.class);
                    intent.putExtra("id", list2.getFavID());
                    intent.putExtra("name", list2.getFavName());
                    intent.putExtra("addr", list2.getFavAddr());
                    intent.putExtra("cat", list2.getFavImage());
                    cont.startActivity(intent);
                }
            });

            holder.im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cont, Main2Activity.class);
                    intent.putExtra("id", list2.getFavID());
                    intent.putExtra("name", list2.getFavName());
                    intent.putExtra("addr", list2.getFavAddr());
                    intent.putExtra("cat", list2.getFavImage());
                    cont.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list1.size();
        }

        public class FavHolder extends RecyclerView.ViewHolder {

            ImageView im1, im2;
            TextView t1, t2, t3;

            public FavHolder(View itemView) {
                super(itemView);

                im1 = itemView.findViewById(com.auro.android.itravel.R.id.favimg);
                im2 = itemView.findViewById(com.auro.android.itravel.R.id.favheart);
                t1 = itemView.findViewById(com.auro.android.itravel.R.id.favname);
                t2 = itemView.findViewById(com.auro.android.itravel.R.id.favaddr);
                t3 = itemView.findViewById(com.auro.android.itravel.R.id.favid);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        showfav();

    }


}

