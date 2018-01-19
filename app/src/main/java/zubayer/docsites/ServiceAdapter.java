package zubayer.docsites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ZYbyer on 17.1.18.
 */

public class ServiceAdapter extends ArrayAdapter<String> {

    public Typeface font;
    ArrayList<String> titleArray;
    ArrayList<String> imageArray;
    ImageView image;
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
        myTitle.setTypeface(font);
        myTitle.setText(titleArray.get(position));
        myTitle.setTextColor(Color.parseColor("#123456"));
        image=(ImageView)row.findViewById(R.id.ImageView);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,imageArray.get(position));
                context.startActivity(Intent.createChooser(intent,"Share using.."));
            }
        });
            image.setVisibility(View.GONE);
        return row;
    }
}

