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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import zubayer.docsites.R;
import zubayer.docsites.activity.Browser;
import zubayer.docsites.activity.NotificationSummery;

public class NotificationListAdapter extends ArrayAdapter<String> {
    private Typeface font;
    private ArrayList<String> names, dates, texts, url, seens;
    private Context context;
    private AlertDialog checkinternet;
    private Animation anim;

    public NotificationListAdapter(Context context,
                                   ArrayList<String> names,
                                   ArrayList<String> dates,
                                   ArrayList<String> text,
                                   ArrayList<String> seens,
                                   ArrayList<String> url) {

        super(context, R.layout.card_view, names);
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.texts = text;
        this.seens = seens;
        this.url = url;
        font = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        checkinternet = builder.create();
        checkinternet.setCancelable(true);
        checkinternet.setMessage("Delete notification?");
        anim= AnimationUtils.loadAnimation(context,R.anim.fade_in);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.card_view, null, true);
        }
        final TextView nam = convertView.findViewById(R.id.name);
        final TextView dat = convertView.findViewById(R.id.date);
        TextView delv = convertView.findViewById(R.id.delv);
        final TextView text = convertView.findViewById(R.id.text);
        final LinearLayout linearlayout = convertView.findViewById(R.id.linearlayout);
        nam.setTypeface(font);
        dat.setTypeface(font);
        text.setTypeface(font);
        nam.setText(names.get(position));
        dat.setText(dates.get(position));
        text.setText(texts.get(position));

        String color =seens.get(position);
        linearlayout.setBackgroundColor(Color.parseColor(color));
        linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seens.remove(position);
                seens.add(position, "#FFFFFF");
                saveSeen();
                notifyDataSetChanged();
                browser(url.get(position));
            }
        });
        delv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        linearlayout.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                names.remove(position);
                                dates.remove(position);
                                texts.remove(position);
                                url.remove(position);
                                seens.remove(position);
                                saveSeen();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });


                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete all", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        names.clear();
                        dates.clear();
                        texts.clear();
                        seens.clear();
                        url.clear();
                        notifyDataSetChanged();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });
                checkinternet.show();


            }
        });

        return convertView;
    }

    private void saveSeen() {
        try {
            FileOutputStream write = context.openFileOutput("notificationSeen", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(seens);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void browser(String inurl) {
        context.startActivity(new Intent(context, Browser.class).putExtra("value", inurl));
    }
}

