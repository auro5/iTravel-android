package com.auro.android.itravel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{

    private Context cont;
    private List<ReviewItem> list1;

    public ReviewAdapter(Context cont, List<ReviewItem> list1) {
        this.cont = cont;
        this.list1 = list1;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View v = inflater.inflate(com.auro.android.itravel.R.layout.list_rev, null);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder holder, int position) {
        ReviewItem list2 = list1.get(position);

        final String url = list2.getRid();
        holder.t1.setText(list2.getAname());
        String rTime=list2.getAtime();
        if(!rTime.contains(" ")){
            Long milli = Long.parseLong(rTime) *1000;
            System.out.println(milli);
            Date date = new Date(milli);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rTime = format.format(date);
        }
        holder.t2.setText(rTime);
        holder.t3.setText(list2.getAtext());

        Glide.with(cont).load(list2.getAphoto()).into(holder.im1);
        holder.r1.setRating(Float.parseFloat(list2.getArating()));

        holder.t1.setMovementMethod(LinkMovementMethod.getInstance());
        holder.t2.setMovementMethod(LinkMovementMethod.getInstance());
        holder.t3.setMovementMethod(LinkMovementMethod.getInstance());


        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                cont.startActivity(browserIntent);
            }
        });

        holder.t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                cont.startActivity(browserIntent);
            }
        });

        holder.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                cont.startActivity(browserIntent);
            }
        });

        holder.im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                cont.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        ImageView im1;
        TextView t1,t2,t3,t4;
        RatingBar r1;

        public ReviewHolder(View itemView) {
            super(itemView);

            im1 = itemView.findViewById(com.auro.android.itravel.R.id.authorimg);
            t1 = itemView.findViewById(com.auro.android.itravel.R.id.authorname);
            t2 = itemView.findViewById(com.auro.android.itravel.R.id.revdate);
            t3 = itemView.findViewById(com.auro.android.itravel.R.id.revtext);
            t4 = itemView.findViewById(com.auro.android.itravel.R.id.revid);
            r1 = itemView.findViewById(com.auro.android.itravel.R.id.revrate);
        }
    }
}
