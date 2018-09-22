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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zubayer.docsites.R;

import static android.widget.Toast.makeText;

public class MyAdapter extends ArrayAdapter<String> {

    private Typeface font;
    private ArrayList<String> titleArray;
    private ArrayList<String> imageArray;
    private Activity context;


    public MyAdapter(Activity context, ArrayList<String> titles1, ArrayList<String> imageArray) {

        super(context, R.layout.customlyst, titles1);
        this.context = context;
        this.titleArray = titles1;
        this.imageArray = imageArray;

        font = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.customlyst, null, true);
            notifyDataSetChanged();
        }
        TextView myTitle = (TextView) convertView.findViewById(R.id.idTitle);
        myTitle.setTypeface(font);
        myTitle.setText(titleArray.get(position));
        myTitle.setTextColor(Color.parseColor("#123456"));
        TextView image = (TextView) convertView.findViewById(R.id.ImageView);
        TextView copy = (TextView) convertView.findViewById(R.id.copy);
        try {
            image.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("dels/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, imageArray.get(position));
                    context.startActivity(Intent.createChooser(intent, "Share using.."));
                }
            });
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", imageArray.get(position));
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast toast = makeText(context, "Link copied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
        } catch (Exception e) {
        }
        if (imageArray.isEmpty()) {
            image.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            copy.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
        return convertView;
    }
}
