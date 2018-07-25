package com.auro.android.itravel;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class TabPhoto extends Fragment{
    String id;
    ImageView im1;
    ImageView im2;
    ImageView im3;
    ImageView im4;
    ImageView im5;
    ImageView im6;
    ImageView im7;
    ImageView im8;
    ImageView im9;
    ImageView im10;
    TextView t1;
    private GeoDataClient mGeoDataClient;
    private Context ct;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.auro.android.itravel.R.layout.tphoto, container, false);
        id = getArguments().getString("id");
        im1 = rootView.findViewById(com.auro.android.itravel.R.id.image1);
        im2 = rootView.findViewById(com.auro.android.itravel.R.id.image2);
        im3 = rootView.findViewById(com.auro.android.itravel.R.id.image3);
        im4 = rootView.findViewById(com.auro.android.itravel.R.id.image4);
        im5 = rootView.findViewById(com.auro.android.itravel.R.id.image5);
        im6 = rootView.findViewById(com.auro.android.itravel.R.id.image6);
        im7 = rootView.findViewById(com.auro.android.itravel.R.id.image7);
        im8 = rootView.findViewById(com.auro.android.itravel.R.id.image8);
        im9 = rootView.findViewById(com.auro.android.itravel.R.id.image9);
        im10 = rootView.findViewById(com.auro.android.itravel.R.id.image10);

        t1 = rootView.findViewById(com.auro.android.itravel.R.id.nophoto);
        ct = this.getContext();
        getPhotos();

        return rootView;
    }

    private void getPhotos() {
        mGeoDataClient = Places.getGeoDataClient(ct, null);
        final String placeId = id;
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                //System.out.println(photoMetadataBuffer.getCount());
                int len = photoMetadataBuffer.getCount();
                if(len>0) {
                    for (int i = 0; i < len; i++) {
                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                        // Get the attribution text.
                        CharSequence attribution = photoMetadata.getAttributions();
                        // Get a full-size bitmap for the photo.
                        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                        final int finalI = i;
                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();
                                Bitmap bitmap = photo.getBitmap();
                                if (finalI == 0)
                                    im1.setImageBitmap(bitmap);

                                if (finalI == 1)
                                    im2.setImageBitmap(bitmap);

                                if (finalI == 2)
                                    im3.setImageBitmap(bitmap);

                                if (finalI == 3)
                                    im4.setImageBitmap(bitmap);

                                if (finalI == 4)
                                    im5.setImageBitmap(bitmap);

                                if (finalI == 5)
                                    im6.setImageBitmap(bitmap);

                                if (finalI == 6)
                                    im7.setImageBitmap(bitmap);

                                if (finalI == 7)
                                    im8.setImageBitmap(bitmap);

                                if (finalI == 8)
                                    im9.setImageBitmap(bitmap);

                                if (finalI == 9)
                                    im10.setImageBitmap(bitmap);
                            }
                        });
                    }//end of for
                }//end of if
                else{
                    t1.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
