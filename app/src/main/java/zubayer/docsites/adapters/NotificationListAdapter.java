package zubayer.docsites.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import zubayer.docsites.R;
import zubayer.docsites.activity.Browser;
import zubayer.docsites.activity.NotificationSummery;

public class NotificationListAdapter extends ArrayAdapter<String> {
    public Typeface font;
    ArrayList<String> names, dates,texts, url;
    ArrayList<Boolean> seen;
    Activity context;
    AlertDialog checkinternet;
    AlertDialog.Builder builder;

    public NotificationListAdapter(Activity context,
                                   ArrayList<String> names,
                                   ArrayList<String> dates,
                                   ArrayList<String> text,
                                   ArrayList<Boolean> seen,
                                   ArrayList<String> url) {

        super(context, R.layout.card_view, names);
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.texts = text;
        this.seen = seen;
        this.url = url;
        font = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
        builder = new AlertDialog.Builder(context);
        checkinternet = builder.create();
        checkinternet.setCancelable(true);
        checkinternet.setMessage("Sure you want to delete?");
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.card_view, null);
        TextView nam = row.findViewById(R.id.name);
        TextView dat = row.findViewById(R.id.date);
        TextView delv = row.findViewById(R.id.delv);
        TextView text = row.findViewById(R.id.text);
        final LinearLayout linearlayout = row.findViewById(R.id.linearlayout);
        nam.setTypeface(font);
        dat.setTypeface(font);
        text.setTypeface(font);
        nam.setText(names.get(position));
        dat.setText(dates.get(position));
        text.setText(texts.get(position));

        if (seen.get(position)) {
            linearlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                saveSeen(position);
                browser(url.get(position));
                notifyDataSetChanged();
            }
        });
        delv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinternet.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        names.remove(position);
                        dates.remove(position);
                        texts.remove(position);
                        seen.remove(position);
                        url.remove(position);
                        saveSeen(position);
                        notifyDataSetChanged();
                    }
                });
                checkinternet.setButton3("Delete all", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        names.clear();
                        dates.clear();
                        texts.clear();
                        seen.clear();
                        url.clear();
                        notifyDataSetChanged();
                    }
                });
                checkinternet.setButton2("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });
                checkinternet.show();


            }
        });
        return row;
    }
    private void saveSeen(int position) {
        try {
            seen.remove(position);
            seen.add(position,true);
            FileOutputStream write = context.openFileOutput("notificationSeen", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(seen);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
    }
    public void browser(String inurl) {
        context.startActivity(new Intent(context, Browser.class).putExtra("value", inurl));
    }
}

