package zubayer.docsites.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zubayer.docsites.R;

import static android.widget.Toast.makeText;

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
        title.setText(heading.get(position));

        if (density < DisplayMetrics.DENSITY_HIGH) {
            title.setTextSize(25);
            elaborate.setTextSize(8);
            hint.setTextSize(6);
        }

        elaborate.setText(description.get(position));
        hint.setText(contents.get(position));
        title.setTypeface(font);
        elaborate.setTypeface(font);
        hint.setTypeface(font);
        switch (position){
            case 0: title.setTextColor(Color.parseColor("#516000"));
                break;
            case 1: title.setTextColor(Color.parseColor("#00AEFF"));
                break;
            case 2: title.setTextColor(Color.parseColor("#90770B"));
                break;
            case 3: title.setTextColor(Color.parseColor("#6A4002"));
                break;
            case 4: title.setTextColor(Color.parseColor("#A60C02"));
                break;
            case 5: title.setTextColor(Color.parseColor("#E8A40A"));
                break;
            case 6: title.setTextColor(Color.parseColor("#009990"));
                break;
            case 7: title.setTextColor(Color.parseColor("#0005f0"));
                break;
            case 8: title.setTextColor(Color.parseColor("#0689BA"));
                if (density < DisplayMetrics.DENSITY_HIGH) {
                    title.setTextSize(25);
                }else {
                    title.setTextSize(38);
                }

                break;
            case 9: title.setTextColor(Color.parseColor("#571500"));

                    if (density < DisplayMetrics.DENSITY_HIGH) {
                title.setText("MBBS/BDS");
                hint.setText(" \n ");
            }else {
                        title.setTextSize(25);
                    title.setText("MBBS/BDS"+"\n");
                    }
                break;
        }

        return row;
    }
}
