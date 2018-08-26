package zubayer.docsites.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import me.anwarshahriar.calligrapher.Calligrapher;
import zubayer.docsites.adapters.MyAdapter;
import zubayer.docsites.services.MyFirebseJobDidpatcher;
import zubayer.docsites.adapters.NotificationListAdapter;
import zubayer.docsites.R;

import static android.widget.Toast.makeText;

public class NotificationSummery extends Activity {
    ImageButton save, clear, undo, redo, about;
    AlertDialog Dialog, checkinternet;
    AlertDialog.Builder builder;
    ArrayList<String> dates, headings, urls, texts, missedNotifications,falseurls,notificatinCount;
    String date;
    View m;
    NotificationListAdapter adaptate;
    ListView notificationList;
    int size, textsize = 18;
    FabSpeedDial fab;
    private AdView mAdView;
    SharedPreferences preferences;
    boolean checked;
    MyAdapter adapter;
    ProgressBar progressBar;
    FabSpeedDial fabspeed;
    PendingIntent pendingIntent;
    AlarmManager manager;
    Intent newIntent;
    boolean wifiAvailable, mobileDataAvailable;
    FirebaseJobDispatcher jobDispatcher;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_summery);

        headings = new ArrayList<>();
        dates = new ArrayList<>();
        texts = new ArrayList<>();
        urls = new ArrayList<>();
        missedNotifications = new ArrayList<>();
        falseurls = new ArrayList<>();
        notificatinCount=new ArrayList<>();
        setFont();
        createAdView();
        readHeading();
        readDate();
        readText();
        readUrl();
        readMissedNotification();
        setListView();
        buildAlertDialogue();
        saveState();
        equilifyNotificationCount();
        notificationList = findViewById(R.id.notificationListView);
        notificationList.setSelector(R.drawable.bcsdept);
        adaptate = new NotificationListAdapter(this, headings, dates, texts, urls);
        notificationList.setAdapter(adaptate);

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(urls.get(position).length()==0){
                }else {
                    browser(urls.get(position));
                }
            }
        });


        fabspeed = (FabSpeedDial) findViewById(R.id.fabsummery);
        fabspeed.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.off:
                        Intent setting = new Intent(NotificationSummery.this, Settings.class);
                        startActivity(setting);
                        break;
                    case R.id.missed:
                        Dialog.show();
                        progressBar.setVisibility(View.GONE);
                        break;
                }
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    private void equilifyNotificationCount() {
        try {
            SharedPreferences finalsize=getSharedPreferences("finalNotificationCount",Context.MODE_PRIVATE);
            SharedPreferences oldsize=getSharedPreferences("oldNotificationCount",Context.MODE_PRIVATE);
            oldsize.edit().putInt("oldsize",finalsize.getInt("finalsize",0)).apply();
        }catch (Exception e){}
    }

    private void createAdView() {
        mAdView = (AdView) findViewById(R.id.adViewSummery);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void browser(String inurl) {
        final String uurl = inurl;
        try {
            preferences = getSharedPreferences("setting", 0);
            checked = preferences.getBoolean("checked", false);
            if (checked) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(inurl));
                startActivity(intent);
            } else {
                Intent intent = new Intent(NotificationSummery.this, Browser.class);
                intent.putExtra("value", inurl);
                startActivity(intent);

            }
        } catch (Exception e) {
        }
    }

    public void readMissedNotification() {
        try {
            FileInputStream read = openFileInput("missed");
            ObjectInputStream readarray = new ObjectInputStream(read);
            missedNotifications = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    public void readHeading() {
        try {
            FileInputStream read = openFileInput("notificationHeadings");
            ObjectInputStream readarray = new ObjectInputStream(read);
            headings = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    public void readDate() {
        try {
            FileInputStream read = openFileInput("notificationDates");
            ObjectInputStream readarray = new ObjectInputStream(read);
            dates = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    public void readText() {
        try {
            FileInputStream read = openFileInput("notificationTexts");
            ObjectInputStream readarray = new ObjectInputStream(read);
            texts = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    public void readUrl() {
        try {
            FileInputStream read = openFileInput("notificationUrls");
            ObjectInputStream readarray = new ObjectInputStream(read);
            urls = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    public void saveState() {
        try {
            FileOutputStream write = openFileOutput("notificationHeadings", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(headings);
            arrayoutput.writeObject(headings);
            arrayoutput.close();
            write.close();

        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationDates", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(dates);
            arrayoutput.writeObject(dates);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationTexts", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(texts);
            arrayoutput.writeObject(texts);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationUrls", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(urls);
            arrayoutput.writeObject(urls);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }

    }

    private void setFont() {
        Calligrapher font = new Calligrapher(NotificationSummery.this);
        font.setFont(NotificationSummery.this, "kalpurush.ttf", true);
    }

    private void buildAlertDialogue() {
        builder=new AlertDialog.Builder(NotificationSummery.this);
        Dialog = builder.create();
        Dialog.setCancelable(true);
        Dialog.setButton("close",new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
            }
        });
        Dialog.setButton3("Check Notificatins again", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    assert connectivityManager != null;
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    wifiAvailable = networkInfo.isConnected();
                    networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    mobileDataAvailable = networkInfo.isConnected();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!wifiAvailable && !mobileDataAvailable) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Turn on Wi-Fi or Mobile Data then try again.");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                        }
                    });

                    checkinternet.show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    try{
                        stopFirebaseJobDispatcher();
                        setFirebaseJobDispatcher();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    myToaster("Checking Notifications");
                    finish();
                }
            }
        });
        Dialog.setView(m);
    }

    private void setListView() {
        adapter = new MyAdapter(NotificationSummery.this, missedNotifications, falseurls);
        m = getLayoutInflater().inflate(R.layout.listview, null);
        notificationList = (ListView) m.findViewById(R.id.ListView);
        notificationList.setAdapter(adapter);
        progressBar = (ProgressBar) m.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    private void myToaster(String text) {
        Toast toast = makeText(NotificationSummery.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    private void limitArray(ArrayList<String> arrayName) {
        if (arrayName.size() > 200) {
            arrayName.remove(200);
        }
    }

    private void stopFirebaseJobDispatcher() {
        jobDispatcher=new FirebaseJobDispatcher(new GooglePlayDriver(this));
        try{
            jobDispatcher.cancel("tags");
        }catch (Exception e){}
    }

    private void setFirebaseJobDispatcher() {
        jobDispatcher=new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job=jobDispatcher.newJobBuilder()
                .setService(MyFirebseJobDidpatcher.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag("tags")
                .setTrigger(Trigger.executionWindow(0,21600))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK).build();
        jobDispatcher.mustSchedule(job);
    }
}