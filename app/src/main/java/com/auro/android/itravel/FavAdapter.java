package com.auro.android.itravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavHolder>{
    private Context cont;
    private List<FavItem> list1;
    SharedPreferences shredPref2;

    public FavAdapter(Context cont, List<FavItem> list1) {
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
                TabFav f = new TabFav();
                f.adapter.notifyDataSetChanged();
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
