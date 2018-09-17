package zubayer.docsites.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zubayer.docsites.R;

import static android.widget.Toast.makeText;

/**
 * Created by ZYbyer on 17.1.18.
 */

public class ServiceAdapter extends ArrayAdapter<String> {

    private Typeface font;
    private ArrayList<String> titleArray;
    private Activity context;

    public ServiceAdapter(Activity context, ArrayList<String> titles1) {

        super(context, R.layout.listview2,titles1);
        this.context=context;
        this.titleArray=titles1;

        font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView,@NonNull ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.customlyst, null, true);
        }
        TextView myTitle = (TextView) convertView.findViewById(R.id.idTitle);
        TextView copy=(TextView)convertView.findViewById(R.id.copy) ;
        myTitle.setTypeface(font);
        myTitle.setText(titleArray.get(position));
        myTitle.setTextColor(Color.parseColor("#123456"));
        TextView share = (TextView) convertView.findViewById(R.id.ImageView);
        share.setVisibility(View.GONE);
        copy.setVisibility(View.GONE);
        notifyDataSetChanged();
        return convertView;
    }
}

