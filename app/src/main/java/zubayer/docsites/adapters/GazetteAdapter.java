package zubayer.docsites.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zubayer.docsites.R;

public class GazetteAdapter extends ArrayAdapter<String> {

    public Typeface font;
    ArrayList<String> titleArray;
    private Activity context;


    public GazetteAdapter(Activity context, ArrayList<String> titleArray) {

        super(context, R.layout.gadgette_layout, titleArray);
        this.context = context;
        this.titleArray = titleArray;

        font = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.gadgette_layout, null, true);

        TextView myTitle = (TextView) row.findViewById(R.id.idTitle);
        myTitle.setTypeface(font);
        myTitle.setText(titleArray.get(position));
        myTitle.setTextColor(Color.parseColor("#0689BA"));
        notifyDataSetChanged();
        return row;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

