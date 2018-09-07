package zubayer.docsites.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import zubayer.docsites.R;

public class GridAdapter extends ArrayAdapter<String> {
    ArrayList<String> heading,description,contents;
    Activity context;
    LayoutInflater inflater;
    public Typeface font;
    int density;

    public GridAdapter(@NonNull Activity context, ArrayList<String> heading,ArrayList<String> description,ArrayList<String> contents) {
        super(context, R.layout.grid,heading);
        this.heading=heading;
        this.description=description;
        this.contents=contents;
        this.context=context;
        density = context.getResources().getDisplayMetrics().densityDpi;
        font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater=context.getLayoutInflater();
        View row=inflater.inflate(R.layout.grid,null,true);
        TextView title=(TextView)row.findViewById(R.id.bsmmu1);
        TextView elaborate=(TextView)row.findViewById(R.id.bsmmu2);
        TextView hint=(TextView)row.findViewById(R.id.bsmmu3);
        ImageView backgroundImage=(ImageView)row.findViewById(R.id.backgroundImage);
        title.setText(heading.get(position));

        if (density < DisplayMetrics.DENSITY_HIGH) {
            title.setTextSize(25);
            elaborate.setTextSize(8);
            hint.setTextSize(6);
        }

        elaborate.setText(description.get(position));
        hint.setText(contents.get(position));
        elaborate.setTypeface(font);
        hint.setTypeface(font);
        switch (position){
//            case 0:
//                Glide.with(context).load(R.drawable.bsmmulogo).into(backgroundImage);
//                break;
//            case 1: Glide.with(context).load(R.drawable.bcpslogo).into(backgroundImage);
//                break;
//            case 2: Glide.with(context).load(R.drawable.dghslogo).into(backgroundImage);
//                break;
//            case 3: Glide.with(context).load(R.drawable.govtlogo).into(backgroundImage);
//                break;
//            case 4: Glide.with(context).load(R.drawable.govtlogo).into(backgroundImage);
//                break;
//            case 5: Glide.with(context).load(R.drawable.govtlogo).into(backgroundImage);
//                break;
//            case 6: Glide.with(context).load(R.drawable.dgfplogo).into(backgroundImage);
//                break;
//            case 7: Glide.with(context).load(R.drawable.dlplogo).into(backgroundImage);
//                break;
//            case 8: Glide.with(context).load(R.drawable.bmdclogo).into(backgroundImage);
//                break;
//            case 9: Glide.with(context).load(R.drawable.medicallogo).into(backgroundImage);
//                break;
            case 0:
                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342915895?alt=media&token=274a9d19-64f1-410e-8da7-924e4f9f60cf").into(backgroundImage);
                break;
            case 1: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342932044?alt=media&token=06a51b0b-16e5-44c5-b92c-19e4fe709a8b").into(backgroundImage);
                break;
            case 2: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342948323?alt=media&token=5d4c6c41-4185-4ad9-8d64-8183057cfc8a").into(backgroundImage);
                break;
            case 3: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342971955?alt=media&token=09705bb8-51bd-46a0-950d-edc8502f2ca1").into(backgroundImage);
                break;
            case 4: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342971955?alt=media&token=09705bb8-51bd-46a0-950d-edc8502f2ca1").into(backgroundImage);
                break;
            case 5: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342971955?alt=media&token=09705bb8-51bd-46a0-950d-edc8502f2ca1").into(backgroundImage);
                break;
            case 6: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536342990795?alt=media&token=57ab2ed1-d15d-44bc-bb90-14abd29f27f6").into(backgroundImage);
                break;
            case 7: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536343007728?alt=media&token=7078ecf0-c333-4d6c-a58a-24329077330f").into(backgroundImage);
                break;
            case 8: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536343061379?alt=media&token=a11e0a6b-1c5f-43f0-b571-d1e9478442a8").into(backgroundImage);
                break;
            case 9: Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/docsites-c20b8.appspot.com/o/uiImage%2F1536343022885?alt=media&token=81449d24-9943-4299-b38f-7b668233e5bd").into(backgroundImage);
                break;
        }

        return row;
    }
}
