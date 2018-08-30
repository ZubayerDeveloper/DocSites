package zubayer.docsites.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

    public Typeface font;
    ArrayList<String> titleArray;
    ArrayList<String> imageArray;
    TextView share;
    private Activity context;


    public ServiceAdapter(Activity context, ArrayList<String> titles1,ArrayList<String> imageArray) {

        super(context, R.layout.listview2,titles1);
        this.context=context;
        this.titleArray=titles1;
        this.imageArray=imageArray;

        font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.customlyst,null,true);


        TextView myTitle = (TextView) row.findViewById(R.id.idTitle);
        TextView copy=(TextView)row.findViewById(R.id.copy) ;
        myTitle.setTypeface(font);
        myTitle.setText(titleArray.get(position));
        myTitle.setTextColor(Color.parseColor("#123456"));
        share=(TextView)row.findViewById(R.id.ImageView);
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("dels/plain");
                intent.putExtra(Intent.EXTRA_TEXT,imageArray.get(position));
                context.startActivity(Intent.createChooser(intent,"Share using.."));
            }
        });
        share.setVisibility(View.GONE);
        copy.setVisibility(View.GONE);
        return row;
    }
}

