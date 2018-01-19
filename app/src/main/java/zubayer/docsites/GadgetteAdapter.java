package zubayer.docsites;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GadgetteAdapter extends ArrayAdapter<String> {

public Typeface font;
ArrayList<String> titleArray;
private Activity context;


public GadgetteAdapter(Activity context, ArrayList<String> titleArray) {

        super(context, R.layout.gadgette_layout,titleArray);
        this.context=context;
        this.titleArray=titleArray;

        font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
        }
@Override
public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(R.layout.gadgette_layout,null,true);

        TextView myTitle=(TextView)row.findViewById(R.id.idTitle);
        myTitle.setTypeface(font);
        myTitle.setText(titleArray.get(position));
        myTitle.setTextColor(Color.parseColor("#0689BA"));
        //pica.with(activity).load(imageArray.get(position)).into(image);
        return row;
        }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

