package com.auro.android.itravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ListViewHolder>{

    int flag=0;

    private Context cont;
    private List<ListItem> list1;
    SharedPreferences shredPref;

    public MyAdapter(Context cont, List<ListItem> list1) {
        this.cont = cont;
        this.list1 = list1;
    }

    @NonNull
    @Override
    public MyAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View v = inflater.inflate(com.auro.android.itravel.R.layout.list_row, null);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ListViewHolder holder, int position) {
        shredPref = cont.getSharedPreferences("myFav", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= shredPref.edit();

        final ListItem list2 = list1.get(position);

        holder.t1.setText(list2.getPlaceName());
        holder.t2.setText(list2.getPlaceAddr());
        holder.t3.setText(list2.getPlaceID());
        System.out.println("x-->"+list2.getPlaceID());

        Glide.with(cont).load(list2.getCatImage()).into(holder.im1);

        String favCheck = shredPref.getString(list2.getPlaceID(),"");

        if(favCheck=="") {
            holder.im2.setImageDrawable(cont.getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_outline_black));
            list2.setFav(com.auro.android.itravel.R.drawable.heart_outline_black);
        }
        else{
            holder.im2.setImageDrawable(cont.getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_fill_red));
            list2.setFav(com.auro.android.itravel.R.drawable.heart_fill_red);

        }

        holder.im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (list2.getFav() == com.auro.android.itravel.R.drawable.heart_outline_black) {

                        JSONObject json = new JSONObject().put("name", list2.getPlaceName())
                                .put("addr",list2.getPlaceAddr())
                                .put("catImage",list2.getCatImage())
                                .put("favIcon",list2.getFav())
                                .put("id",list2.getPlaceID());

                        editor.putString(list2.getPlaceID(),json.toString());
                        list2.setFav(com.auro.android.itravel.R.drawable.heart_fill_red);
                        holder.im2.setImageDrawable(cont.getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_fill_red));
                        Toast.makeText(cont, list2.getPlaceName() + " was added to favorites", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.remove(list2.getPlaceID());
                        list2.setFav(com.auro.android.itravel.R.drawable.heart_outline_black);
                        holder.im2.setImageDrawable(cont.getResources().getDrawable(com.auro.android.itravel.R.drawable.heart_outline_black));
                        Toast.makeText(cont, list2.getPlaceName() + " was removed from favorites", Toast.LENGTH_SHORT).show();
                    }

                    editor.apply();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cont, Main2Activity.class);
                intent.putExtra("id", list2.getPlaceID());
                intent.putExtra("name", list2.getPlaceName());
                intent.putExtra("addr", list2.getPlaceAddr());
                intent.putExtra("cat", list2.getCatImage());
                cont.startActivity(intent);
            }
        });

        holder.t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cont, Main2Activity.class);
                intent.putExtra("id", list2.getPlaceID());
                intent.putExtra("name", list2.getPlaceName());
                intent.putExtra("addr", list2.getPlaceAddr());
                intent.putExtra("cat", list2.getCatImage());
                cont.startActivity(intent);
            }
        });

        holder.im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cont, Main2Activity.class);
                intent.putExtra("id", list2.getPlaceID());
                intent.putExtra("name", list2.getPlaceName());
                intent.putExtra("addr", list2.getPlaceAddr());
                intent.putExtra("cat", list2.getCatImage());
                cont.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView im1, im2;
        TextView t1, t2, t3;

        public ListViewHolder(View itemView) {
            super(itemView);

            im1 = itemView.findViewById(com.auro.android.itravel.R.id.catimg);
            im2 = itemView.findViewById(com.auro.android.itravel.R.id.heart);
            t1 = itemView.findViewById(com.auro.android.itravel.R.id.placename);
            t2 = itemView.findViewById(com.auro.android.itravel.R.id.addr);
            t3 = itemView.findViewById(com.auro.android.itravel.R.id.placeid);
        }
    }
}
