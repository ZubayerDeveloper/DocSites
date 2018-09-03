package zubayer.docsites.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class NotificationListAdapter extends ArrayAdapter<String> {
    public Typeface font;
        ArrayList<String> names;
        ArrayList<String> dates;
        ArrayList<String> del;
        ArrayList<String> texts;
        Activity context;
    AlertDialog  checkinternet;
    AlertDialog.Builder builder;

    public NotificationListAdapter(Activity context, ArrayList<String> names,
                                   ArrayList<String> dates, ArrayList<String> text, ArrayList<String> del) {

        super(context, R.layout.card_view, names);
        this.context=context;
        this.names=names;
        this.dates=dates;
        this.del = del;
        this.texts=text;
        font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
        builder = new AlertDialog.Builder(context);
        checkinternet = builder.create();
        checkinternet.setCancelable(true);
        checkinternet.setMessage("Sure you want to delete?");
        }
@Override
public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(R.layout.card_view,null);
        TextView nam = row.findViewById(R.id.name);
        TextView dat = row.findViewById(R.id.date);
        TextView delv = row.findViewById(R.id.delv);
        TextView text = row.findViewById(R.id.text);
        nam.setTypeface(font);
        dat.setTypeface(font);
        text.setTypeface(font);
        nam.setText(names.get(position));
        dat.setText(dates.get(position));
        text.setText(texts.get(position));

        delv.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
    checkinternet.setButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(final DialogInterface dialog, int id) {
            names.remove(position);
            dates.remove(position);
            del.remove(position);
            texts.remove(position);
            notifyDataSetChanged();
        }
    });
    checkinternet.setButton3("Delete all", new DialogInterface.OnClickListener() {
        public void onClick(final DialogInterface dialog, int id) {
            names.clear();
            dates.clear();
            del.clear();
            texts.clear();
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
        return  row;
        }

@Override
public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
        }
        }
