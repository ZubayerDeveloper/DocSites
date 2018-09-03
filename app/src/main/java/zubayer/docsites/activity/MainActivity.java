package zubayer.docsites.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import me.anwarshahriar.calligrapher.Calligrapher;
import zubayer.docsites.BuildConfig;
import zubayer.docsites.adapters.GridAdapter;
import zubayer.docsites.adapters.MyAdapter;
import zubayer.docsites.services.MyFirebseJobDidpatcher;
import zubayer.docsites.R;
import zubayer.docsites.services.NotificationReceiver;

import static android.widget.Toast.makeText;

public class MainActivity extends Activity {
    AlertDialog Dialog, checkinternet;
    AlertDialog.Builder builder;
    AdView mAdView;
    View m;
    ProgressBar progressBar;
    ListView list;
    LinearLayoutManager manager;
    FirebaseDatabase database;
    DatabaseReference rootReference, updateReference;
    GridView gridView;
    ArrayList<String> buttonTexts, urls, buttonHeadidng, buttonDescription, buttonHint, buttonTexts2,
            bsmmuOptions, bcpsOptions, dghsOptions, mohfwOptions, bpscOptions, gazetteOptions, bmdcOptions,
            resultOptions, dgfpOptions, ccdOptions, oldNotificatinCount, queryID, queryname;
    MyAdapter adapter;
    GridAdapter gridAdapter;
    HtmlParser back;
    BSMMU parsebsmmu;
    BSMMU2 parsebsmmu2;
    BcpsParser bcps;
    BpscParser bpscParser;
    DghsParser dghsParser;
    DghsParser2 dghsParser2;
    DghsParser3 dghsParser3;
    ServiceParser serviceParser;
    CcdParser ccdParser;
    CcdParser2 ccdParser2;
    String btxt, newline, url, paramUrl, paramTagForText, paramTagForLink, paramLink,
            pdfFilter, driveViewer, filterContent, filterContent2,
            notificationNumberText, queryNotification;
    int position, textMin, textMax, linkBegin, linkEnd, versionCode, oldNotificatinSize, bsmmubegin, bsmmuend;
    boolean bsmmuClicked, bcpsClicked, dghsClicked, mohfwClicked, bpscClicked, gazetteClicked, ccdClicked, dgfpClicked,
            bmdcClicked, resultsClicked, applaunched, checkpop, checked, wifiAvailable, mobileDataAvailable, newNotifications;
    SharedPreferences preferences, notificationPreference;
    FabSpeedDial fab;
    ImageButton notificationSummery;
    FloatingActionButton forum;
    Button showNotificationNumber;
    FirebaseJobDispatcher jobDispatcher;
    TextView updateNotifier, forumhelpNotify,appVersion;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeWidgetVariable();
        createGridView();
        setFont(this, this);
        adjustScreenSize();
        manageSettings();
        loadButtonOptions();
        createAdView();
        checkStoragePermission();
        checkApplaunched();
        stopFirebaseJobDispatcher();
        setFirebaseJobDispatcher(300, 21600);
        setListView();
        buildAlertDialogue();
        checkAppUpdates();
        newForumPost();
        readNotificationCount();
        forumSubscription();
        setAlarm();
        getFCMdataPlayLoad();
        updateNotifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zubayer.docsites")));

            }
        });
        fab = (FabSpeedDial) findViewById(R.id.fabs);
        fab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.settings:
                        Intent setting = new Intent(MainActivity.this, Settings.class);
                        startActivity(setting);
                        break;
                    case R.id.about:
                        buttonTexts.add(getString(R.string.about));
                        Dialog.setTitle("App Developer:");
                        Dialog.show();
                        progressBar.setVisibility(View.GONE);

                        break;
                    case R.id.check:
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
                            try {
                                stopFirebaseJobDispatcher();
                                setFirebaseJobDispatcher(1, 21600);
                                myToaster(MainActivity.this, "Checking Notifications");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onMenuClosed() {
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("newQuery", Context.MODE_PRIVATE);
                if(queryID.size()!=0){
                preferences.edit().putString("query", queryID.get(0)).apply();
                }
                startActivity(new Intent(MainActivity.this, Forum.class));
            }
        });
        forumhelpNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("newQuery", Context.MODE_PRIVATE);
                preferences.edit().putString("query", queryID.get(0)).apply();
                startActivity(new Intent(MainActivity.this, Forum.class));
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                readNotificationCount();
                gridViewOptionLoader(position);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listViewOptionLoader(position);
            }
        });
        notificationSummery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences oldsize = getSharedPreferences("finalNotificationCount", Context.MODE_PRIVATE);
                oldsize.edit().putInt("finalsize", 0).apply();
                Intent summery = new Intent(MainActivity.this, NotificationSummery.class);
                startActivity(summery);
            }
        });
        showNotificationNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences oldsize = getSharedPreferences("finalNotificationCount", Context.MODE_PRIVATE);
                oldsize.edit().putInt("finalsize", 0).apply();
                Intent summery = new Intent(MainActivity.this, NotificationSummery.class);
                startActivity(summery);
            }
        });

        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkIntent != null && appLinkData != null) {
            browser(appLinkData.toString());
        }

    }

    private void getFCMdataPlayLoad() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String postID = intent.getExtras().getString("postID");
            String putBack = intent.getExtras().getString("intent");
            if (postID != null) {
                startActivity(new Intent(MainActivity.this, Reply.class).putExtra("postID", postID).putExtra("intent", putBack));
            }
        }
    }


    private void stopFirebaseJobDispatcher() {
        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        try {
            jobDispatcher.cancel("tags");
        } catch (Exception e) {
        }
    }

    private void setFirebaseJobDispatcher(int when, int interval) {
        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job = jobDispatcher.newJobBuilder()
                .setService(MyFirebseJobDidpatcher.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag("tags")
                .setTrigger(Trigger.executionWindow(when, interval))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK).build();
        jobDispatcher.mustSchedule(job);
    }


    public boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            checkinternet = builder.create();
            checkinternet.setMessage("You need to allow permission to download files from internet");
            checkinternet.setCancelable(false);
            checkinternet.setButton("Allow permission", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (checkStoragePermission()) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
            });
            checkinternet.show();
        }
    }

    private void loadccdOptions() {
        buttonTexts.addAll(ccdOptions);
        Dialog.show();
    }

    private void loaddgfpOptions() {
        buttonTexts.addAll(dgfpOptions);
        Dialog.show();
    }

    private void loadResultOptions() {
        buttonTexts.addAll(resultOptions);
        Dialog.show();
    }

    private void loadBMDCOptions() {
        buttonTexts.addAll(bmdcOptions);
        Dialog.show();
    }

    private void loadGazetteOptions() {
        buttonTexts.addAll(gazetteOptions);
        Dialog.show();
    }

    private void loadBpscOptions() {
        buttonTexts.addAll(bpscOptions);
        Dialog.show();
    }

    private void loadMohfwOptions() {
        buttonTexts.addAll(mohfwOptions);
        Dialog.show();
    }

    private void loadDghsOptions() {
        buttonTexts.addAll(dghsOptions);
        Dialog.show();
    }

    private void loadBcpsOptions() {
        buttonTexts.addAll(bcpsOptions);
        Dialog.show();
    }

    private void loadBsmmuOptions() {
        buttonTexts.addAll(bsmmuOptions);
        Dialog.show();
    }

    public void ccdExecutableTag(String Url, String TagForText, String tagForLink,
                                 String Attr, int begin, int end, int lBegin, int lEnd) {

        paramUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < end; i++) {
                Element link = links.get(i);
                if (link.text() != "") {
                    btxt = link.text();
                    url = link.select("a").attr(Attr);
                }

                buttonTexts.add(btxt);
                urls.add(url);
            }
            buttonTexts.add(0, getString(R.string.homePage));
            urls.add(0, "http://www.badas-dlp.org/");
        } catch (Exception e) {
        }
    }

    public void ccdExecutableTag2(String Url, String TagForText, String tagForLink,
                                  String Attr, int begin, int end, int lBegin, int lEnd) {

        paramUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < end; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = "http://www.badas-dlp.org/";
                buttonTexts.add(btxt);
                urls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void bpscTag(String Url, String TagForText, String tagForLink,
                        String Attr, int begin, int end, int lBegin, int lEnd) {

        paramUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                buttonTexts.add(btxt);
                urls.add(url);
            }
            buttonTexts.add(position, newline);
            urls.add(position, newline);
        } catch (Exception e) {
        }
    }

    public void executableTag(String Url, String TagForText, String tagForLink,
                              String Attr, int begin, int end, int lBegin, int lEnd) {

        paramUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < end; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                buttonTexts.add(btxt);
                urls.add(url);
            }
            buttonTexts.add(position, newline);
            urls.add(position, newline);
        } catch (Exception e) {
        }
    }

    public void dghsTag(String Url, String TagForText, String tagForLink,
                        String Attr, int begin, int end, int lBegin, int lEnd) {

        paramUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                buttonTexts.add(btxt);
                urls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void dghsTag2(String Url, String TagForText, String tagForLink,
                         String Attr, int begin, int end, int lBegin, int lEnd) {

        paramUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = links.size() - 11; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                buttonTexts.add(btxt);
                urls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void serviceConfirmTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                  int lBegin, int lEnd) {
        Url = "http://mohfw.gov.bd/index.php?option=com_content&view=article&id=111:bcs-health&catid=38:bcs-health&Itemid=&lang=en";
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);

            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                if (btxt.contains(filterContent)) {
                    buttonTexts.add(btxt);
                    urls.add(url);
                }
                if (btxt.contains(filterContent2)) {
                    buttonTexts.add(btxt);
                    urls.add(url);
                }
            }


        } catch (Exception e) {
        }
    }

    class CcdParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ccdExecutableTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    checkinternet.show();
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                        ccdNotices2();
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class CcdParser2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ccdExecutableTag2(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    checkinternet.show();
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class BpscParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            bpscTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class HtmlParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            executableTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class BSMMU extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect("http://bsmmu.edu.bd/").get();
                Elements links = doc.select("a");
                bsmmubegin = 0;
                for (int i = bsmmubegin; i < links.size(); i++) {
                    Element link = links.get(i);
                    btxt = link.text();
                    url = link.select("a").attr("abs:href");
                    if (btxt.contains("Residency/Non Res.")) {
                        bsmmubegin = i + 1;
                        bsmmuend = bsmmubegin + 8;
                        break;
                    }
                }
                for (int i = bsmmubegin; i < bsmmuend; i++) {
                    Element link = links.get(i);
                    btxt = link.text();
                    url = link.select("a").attr("abs:href");
                    buttonTexts.add(btxt);
                    urls.add(url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class BSMMU2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect("http://bsmmu.edu.bd/").get();
                Elements links = doc.select("a");
                bsmmubegin = 0;
                for (int i = bsmmubegin; i < links.size(); i++) {
                    Element link = links.get(i);
                    btxt = link.text();
                    url = link.select("a").attr("abs:href");
                    if (btxt.contains("Admission and e-Reg.")) {
                        bsmmubegin = i + 1;
                        bsmmuend = bsmmubegin + 3;
                        break;
                    }
                }
                for (int i = bsmmubegin; i < bsmmuend; i++) {
                    Element link = links.get(i);
                    btxt = link.text();
                    url = link.select("a").attr("abs:href");
                    buttonTexts.add(btxt);
                    urls.add(url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class DghsParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            dghsTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                    dghsHomeLinks2();
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class DghsParser2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            dghsTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                    dghsHomeLinks3();
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class DghsParser3 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            dghsTag2(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (url != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not Responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class ServiceParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            serviceConfirmTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (btxt != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    class BcpsParser extends AsyncTask<Void, Void, Void> {

        String URl = "http://www.bcpsbd.org/notice.php";
        String tagtext = "a";
        String taglink = "a";
        String Attrs = "onClick";
        String uRl = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(URl).get();
                Elements links = doc.select(tagtext);
                Elements hrefs = doc.select(taglink);
                for (int i = 5; i < hrefs.size(); i++) {
                    Element link = links.get(i);
                    btxt = link.text();
                    buttonTexts.add(btxt);
                }
                for (int i = 5; i < hrefs.size(); i++) {
                    Element li = hrefs.get(i);
                    uRl = li.attr(Attrs);
                    urls.add(stringmaker());
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        public String stringmaker() {
            ArrayList<String> chars = new ArrayList<String>();
            int start = 0;
            int end = 0;
            int i = 0;
            char ch;
            String s = uRl;
            String build;
            String newstring = "";
            String finalString = "";
            String prelink = "http://www.bcpsbd.org/";
            String finalLink = "";
            for (i = 0; i < s.length(); i++) {
                ch = s.charAt(i);
                build = Character.toString(ch);
                chars.add(build);
            }
            for (int ii = 0; ii < s.length(); ii++) {
                if (chars.get(ii).equals("(")) {
                    start = ii + 2;
                }
                if (chars.get(ii).equals(")")) {
                    end = ii - 1;
                }
            }
            for (int ia = 0; ia < chars.size(); ia++) {
                newstring += chars.get(ia);
            }
            finalString = newstring.substring(start, end);
            finalLink = prelink + finalString;
            return finalLink;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            try {
                if (!dataconnected()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("Check your network connection");
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            url = null;
                            Dialog.dismiss();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (uRl != null) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        list.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                } else {
                    checkinternet = builder.create();
                    checkinternet.setMessage("Website is not responding");
                    checkinternet.setCancelable(false);
                    checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            buttonTexts.clear();
                            urls.clear();
                            Dialog.dismiss();
                            btxt = null;
                        }
                    });
                    try {
                        checkinternet.setCancelable(false);
                        checkinternet.show();
                    } catch (Exception e) {
                    }

                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean pressed = false;
        if (!pressed) {
            checkinternet = builder.create();
            checkinternet.setButton("Send mail", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "zubayer.developer@gmail.com"));
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + ": ");
                    i.putExtra(Intent.EXTRA_TEXT, "Write here:" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "Sent from: " + Build.MANUFACTURER + " " + Build.MODEL + " " + "(" + Build.VERSION.RELEASE + ")");
                    startActivity(i);
                }
            });
            checkinternet.setButton3("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            try {
                checkinternet.setMessage(getText(R.string.exit));
            } catch (Exception e) {
            }
            checkinternet.show();
        } else {
            super.onBackPressed();
            readNotificationCount();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        readNotificationCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readNotificationCount();
        newForumPost();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        readNotificationCount();
    }

    private void bsmmuHome() {
        pdfFilter = "http://www.bsmmu.edu.bd";
        browser(pdfFilter);
    }

    private void residency() {
        parsebsmmu = new BSMMU();
        parsebsmmu.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void admission() {
        parsebsmmu2 = new BSMMU2();
        parsebsmmu2.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void bsmmuNotice() {
        back = new HtmlParser();
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "#tab4 h3";
        paramTagForLink = "h3 a";
        paramLink = "abs:href";
        textMin = 0;
        textMax = 34;
        linkBegin = 0;
        linkEnd = 34;
        position = 15;
        newline = "★Administrative notice★";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void bcpsHome() {
        pdfFilter = "http://www.bcpsbd.org";
        browser(pdfFilter);
    }

    private void bcpsNotice() {
        pdfFilter = "http://www.bcpsbd.org/notice.php";
        browser(pdfFilter);
    }

    private void bcpsGuideline() {
        bcps = new BcpsParser();
        back = new HtmlParser();
        paramUrl = "http://www.bcpsbd.org/notice.php";
        paramTagForText = "a";
        paramTagForLink = "a";
        paramLink = "abs:href";
        textMin = 2;
        linkBegin = 2;
        textMax = 4;
        linkEnd = 4;
        position = 15;
        back.execute();
        bcps.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void fcpsPart1Regi() {
        pdfFilter = "http://www.bcpsbd.org/p1_reg/index.php";
        browser(pdfFilter);
    }

    private void fcpsResults() {
        pdfFilter = "http://www.bcpsbd.org/result/";
        browser(pdfFilter);
    }

    private void dghsHome() {
        pdfFilter = "http://dghs.gov.bd/index.php/bd/";
        browser(pdfFilter);
    }

    private void dghsHomeLinks() {
        dghsParser = new DghsParser();
        paramUrl = "http://dghs.gov.bd/index.php/bd/";
        paramTagForText = "#system h1 a";
        paramTagForLink = "#system h1 a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        textMax = 17;
        linkEnd = 17;
        position = 19;
        newline = "★★★";
        dghsParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dghsHomeLinks2() {
        dghsParser2 = new DghsParser2();
        paramUrl = "http://dghs.gov.bd/index.php/bd/";
        paramTagForText = "#system li a";
        paramTagForLink = "#system li a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        dghsParser2.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dghsHomeLinks3() {
        dghsParser3 = new DghsParser3();
        paramUrl = "http://dghs.gov.bd/index.php/bd/";
        paramTagForText = "#system a";
        paramTagForLink = "#system a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        dghsParser3.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void postingOrders() {
        back = new HtmlParser();
        paramUrl = "http://dghs.gov.bd/index.php/bd/?option=com_content&view=article&layout=edit&id=570";
        paramTagForText = "#system a";
        paramTagForLink = "#system a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        textMax = 4;
        linkEnd = 4;
        position = 5;
        newline = "★★★";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void HRM() {
        pdfFilter = "http://hrm.dghs.gov.bd/auth/signin";
        browser(pdfFilter);
    }

    private void mohfwHome() {
        pdfFilter = "http://www.mohfw.gov.bd/index.php?option=com_content&view=frontpage&Itemid=1&lang=en";
        browser(pdfFilter);
    }

    private void deputation() {
        pdfFilter = "http://www.mohfw.gov.bd/index.php?option=com_docman&task=doc_download&gid=3189&lang=en";
        browser(pdfFilter);
    }

    private void bpscHpme() {
        pdfFilter = "http://www.bpsc.gov.bd";
        browser(pdfFilter);
    }

    private void regiBCS() {
        pdfFilter = "http://bpsc.teletalk.com.bd";
        browser(pdfFilter);
    }

    private void regiDept() {
        back = new HtmlParser();
        paramUrl = "http://dept.bpsc.gov.bd";
        paramTagForText = "h5";
        paramTagForLink = "h5 a";
        paramLink = "abs:href";
        textMin = 1;
        linkBegin = 0;
        textMax = 7;
        linkEnd = 7;
        position = 8;
        newline = "";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void regiSenior() {
        back = new HtmlParser();
        paramUrl = "http://snsc.bpsc.gov.bd";
        paramTagForText = "a";
        paramTagForLink = "a";
        paramLink = "abs:href";
        textMin = linkBegin = 15;
        textMax = linkEnd = 20;
        position = 7;
        newline = "wah";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void resultBCS() {
        bpscParser = new BpscParser();
        paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr a";
        paramLink = "abs:href";
        textMin = 1;
        textMax = 122;
        linkBegin = 0;
        linkEnd = 123;
        position = 125;
        bpscParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void resultDept() {
        bpscParser = new BpscParser();
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        textMax = 40;
        linkBegin = 0;
        linkEnd = 40;
        position = 42;
        newline = "★ Click to download pdf:";
        bpscParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void resultSenior() {
        bpscParser = new BpscParser();
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        textMax = 38;
        linkBegin = 0;
        linkEnd = 38;
        position = 40;
        newline = "★ Click to download pdf:";
        bpscParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void bpscForms() {
        back = new HtmlParser();
        paramUrl = "http://bpsc.gov.bd";
        paramTagForText = "a";
        paramTagForLink = "#box-5 a";
        paramLink = "abs:href";
        textMin = 77;
        linkBegin = 0;
        textMax = 81;
        linkEnd = 7;
        position = 9;
        newline = "★ Click to dowload pdf file:";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void seniorGazette() {
        Intent intent = new Intent(MainActivity.this, GazetteActivity.class);
        intent.putExtra("examname", getString(R.string.filterSeniorScale));
        startActivity(intent);
    }

    private void departmentalGazette() {
        Intent intent = new Intent(MainActivity.this, GazetteActivity.class);
        intent.putExtra("examname", getString(R.string.filterDepartmental));
        startActivity(intent);
    }

    private void weeklyGazette() {
        Intent intent = new Intent(MainActivity.this, WeeklyGazettes.class);
        startActivity(intent);
    }

    private void serviceConfirmGazette() {
        executeService();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void executeService() {
        serviceParser = new ServiceParser();
        paramTagForText = "table tbody tr td table tbody tr td table tbody tr";
        paramTagForLink = "table tbody tr td table tbody tr td table tbody tr a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        serviceParser.execute();
    }

    private void bmdcHpme() {
        pdfFilter = "http://bmdc.org.bd/";
        browser(pdfFilter);
    }

    private void bmdcDownloadableForms() {
        pdfFilter = "http://bmdc.org.bd/all-forms-download/";
        browser(pdfFilter);
    }

    private void findDoctor() {
        pdfFilter = "http://bmdc.org.bd/doctors-info/";
        browser(pdfFilter);
    }

    private void bdsResult() {
        pdfFilter = "http://volume.dghs.gov.bd/bds/";
        browser(pdfFilter);
    }

    private void mbbsResult() {
        pdfFilter = "http://volume.dghs.gov.bd/mbbs/";
        browser(pdfFilter);
    }

    private void dgfpHome() {
        pdfFilter = "http://dgfp.gov.bd/";
        browser(pdfFilter);
    }

    private void ccdNotices1() {
        ccdParser = new CcdParser();
        paramUrl = "http://www.badas-dlp.org/";
        paramTagForText = "tr td a";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 9;
        linkBegin = 9;
        textMax = 14;
        linkEnd = 14;
        ccdParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void ccdNotices2() {
        ccdParser2 = new CcdParser2();
        paramUrl = "http://www.badas-dlp.org/";
        paramTagForText = "tr td p";
        paramTagForLink = "tr td p";
        paramLink = "abs:href";
        textMin = 2;
        textMax = 3;
        linkBegin = 2;
        linkEnd = 3;
        position = 5;
        ccdParser2.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dgfpOrder() {
        bpscParser = new BpscParser();
        paramUrl = "http://dgfp.gov.bd/site/view/office_order/অফিস-আদেশ";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        textMax = 122;
        linkBegin = 1;
        linkEnd = 123;
        position = 125;
        bpscParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dgfpNotice() {
        bpscParser = new BpscParser();
        paramUrl = "http://dgfp.gov.bd/site/view/notices/নোটিশ";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        textMax = 122;
        linkBegin = 1;
        linkEnd = 123;
        position = 125;
        bpscParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dgfpNOC() {
        bpscParser = new BpscParser();
        paramUrl = "http://dgfp.gov.bd/site/view/publications/এনওসি /এনওসি";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        textMax = 122;
        linkBegin = 0;
        linkEnd = 123;
        position = 125;
        bpscParser.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void browser(String inurl) {
        try {
            preferences = getSharedPreferences("setting", 0);
            checked = preferences.getBoolean("checked", false);
            if (checked) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(inurl));
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, Browser.class);
                intent.putExtra("value", inurl);
                startActivity(intent);

            }
        } catch (Exception e) {
        }
    }

    private void myToaster(Context context, String text) {
        Toast toast = makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void selectDeselect(String preferenceName, String putBooleanName) {
        SharedPreferences settings = getSharedPreferences(preferenceName, 0);
        settings.edit().putBoolean(putBooleanName, true).apply();
    }

    public void selectAll() {
        selectDeselect("residencySetting", "residencyChecked");
        selectDeselect("noticeSetting", "noticeChecked");
        selectDeselect("dghsSetting", "dghsChecked");
        selectDeselect("reultBcsSetting", "reultBcsChecked");
        selectDeselect("resultDeptSetting", "resultDeptChecked");
        selectDeselect("resultSeniorSetting", "resultSeniorChecked");
        selectDeselect("regiDeptSetting", "regiDeptChecked");
        selectDeselect("regiSeniorSetting", "regiSeniorChecked");
        selectDeselect("assistantSurgeonSetting", "assistantSurgeonChecked");
        selectDeselect("juniorConsultantSetting", "juniorConsultantChecked");
        selectDeselect("seniorConsultantSetting", "seniorConsultantChecked");
        selectDeselect("assistantProfessorSetting", "assistantProfessorChecked");
        selectDeselect("associateProfessorSetting", "associateProfessorChecked");
        selectDeselect("professorSetting", "professorChecked");
        selectDeselect("civilSurgeonSetting", "civilSurgeonChecked");
        selectDeselect("adhocSetting", "adhocChecked");
        selectDeselect("mohfwSetting", "mohfwChecked");
        selectDeselect("deputationSetting", "deputationChecked");
        selectDeselect("dgfpSetting", "dgfpChecked");
        selectDeselect("ccdSetting", "ccdChecked");
        selectDeselect("leaveSetting", "leaveChecked");
        selectDeselect("appLaunched", "appLaunchedchecked");
    }

    private void adjustScreenSize() {
        int density = getResources().getDisplayMetrics().densityDpi;
        if (density >= DisplayMetrics.DENSITY_XXHIGH) {
            gridView.setHorizontalSpacing(22);
            gridView.setVerticalSpacing(22);
        } else if (density < DisplayMetrics.DENSITY_HIGH) {
            gridView.setHorizontalSpacing(5);
            gridView.setVerticalSpacing(5);
        } else {
            gridView.setHorizontalSpacing(10);
            gridView.setVerticalSpacing(10);
        }
    }

    private void loadButtonHeading() {
        buttonHeadidng = new ArrayList<>();
        buttonDescription = new ArrayList<>();
        buttonHint = new ArrayList<>();
        String[] headingName = getResources().getStringArray(R.array.heading);
        String[] headingDescription = getResources().getStringArray(R.array.description);
        String[] hints = getResources().getStringArray(R.array.hints);
        Collections.addAll(buttonHeadidng, headingName);
        Collections.addAll(buttonDescription, headingDescription);
        Collections.addAll(buttonHint, hints);
    }

    private void loadButtonOptions() {
        String[] bsmmuOption = getResources().getStringArray(R.array.bsmmuOptions);
        String[] bcpsOption = getResources().getStringArray(R.array.bcpsOptions);
        String[] dghsOption = getResources().getStringArray(R.array.dghsOptions);
        String[] mohfwOption = getResources().getStringArray(R.array.mohfwOptions);
        String[] bpscOption = getResources().getStringArray(R.array.bpscOptions);
        String[] gazetteOption = getResources().getStringArray(R.array.gazetteOptions);
        String[] bmdcOption = getResources().getStringArray(R.array.bmdcOptions);
        String[] resultsOption = getResources().getStringArray(R.array.resultsOption);
        String[] dgfpOption = getResources().getStringArray(R.array.dgfpOptions);
//        String[] ccdOption = getResources().getStringArray(R.array.ccdOptions);
        driveViewer = "https://docs.google.com/viewer?url=";
        buttonTexts = new ArrayList<>();
        buttonTexts2 = new ArrayList<>();
        urls = new ArrayList<>();
        bsmmuOptions = new ArrayList<>();
        bcpsOptions = new ArrayList<>();
        dghsOptions = new ArrayList<>();
        mohfwOptions = new ArrayList<>();
        bpscOptions = new ArrayList<>();
        gazetteOptions = new ArrayList<>();
        bmdcOptions = new ArrayList<>();
        resultOptions = new ArrayList<>();
        dgfpOptions = new ArrayList<>();
        ccdOptions = new ArrayList<>();
        oldNotificatinCount = new ArrayList<>();
        Collections.addAll(bsmmuOptions, bsmmuOption);
        Collections.addAll(bcpsOptions, bcpsOption);
        Collections.addAll(dghsOptions, dghsOption);
        Collections.addAll(mohfwOptions, mohfwOption);
        Collections.addAll(bpscOptions, bpscOption);
        Collections.addAll(gazetteOptions, gazetteOption);
        Collections.addAll(bmdcOptions, bmdcOption);
        Collections.addAll(resultOptions, resultsOption);
        Collections.addAll(dgfpOptions, dgfpOption);
//        Collections.addAll(ccdOptions, ccdOption);
    }

    private void manageSettings() {
        builder = new AlertDialog.Builder(MainActivity.this);
        checkinternet = builder.create();
        checkinternet.setCancelable(false);
        checkinternet.setMessage(getString(R.string.settingDialog));
        checkinternet.setButton("Go to setting", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                preferences = getSharedPreferences("wentToSetting", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("wentToSetting", true).apply();
                Intent settingIntent = new Intent(MainActivity.this, Settings.class);
                startActivity(settingIntent);
            }
        });
        preferences = getSharedPreferences("wentToSetting", 0);
        checkpop = preferences.getBoolean("wentToSetting", false);
        if (checkpop) {

        } else {
            checkinternet.show();
        }
    }

    private void checkApplaunched() {
        versionCode = BuildConfig.VERSION_CODE;
        preferences = getSharedPreferences("appLaunched", 0);
        applaunched = preferences.getBoolean("appLaunchedchecked", false);
        if (applaunched) {
        } else {
            selectAll();
        }
    }

    private void forumSubscription() {
        boolean unsubscribed;
        notificationPreference = getSharedPreferences("forum_notification", Context.MODE_PRIVATE);
        unsubscribed = notificationPreference.getBoolean("unsubscribed_post_noti", false);
        if (!unsubscribed) {
            subscribeTopic();
        }
    }

    public void setFont(Context context, Activity activity) {
        Calligrapher font = new Calligrapher(context);
        font.setFont(activity, "kalpurush.ttf", true);
    }

    private void buildAlertDialogue() {
        Dialog = builder.create();
        Dialog.setCancelable(false);
        Dialog.setButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                bsmmuClicked = bcpsClicked = dghsClicked = mohfwClicked = bpscClicked = gazetteClicked = bmdcClicked = resultsClicked = ccdClicked = dgfpClicked = false;
                buttonTexts.clear();
                urls.clear();
                progressBar.setVisibility(View.GONE);
                try {
                    back.cancel(true);
                    bcps.cancel(true);
                    serviceParser.cancel(true);
                } catch (Exception e) {
                }
            }
        });
        Dialog.setView(m);
    }

    private void setListView() {
        bsmmuClicked = bcpsClicked = dghsClicked = mohfwClicked = bpscClicked = gazetteClicked = bmdcClicked = resultsClicked = ccdClicked = dgfpClicked = false;
        adapter = new MyAdapter(MainActivity.this, buttonTexts, urls);
        m = getLayoutInflater().inflate(R.layout.listview, null);
        list = (ListView) m.findViewById(R.id.ListView);
        manager = new LinearLayoutManager(MainActivity.this);
        list.setAdapter(adapter);
        progressBar = (ProgressBar) m.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    private void createGridView() {
        gridView = (GridView) findViewById(R.id.grid);
        loadButtonHeading();
        gridAdapter = new GridAdapter(MainActivity.this, buttonHeadidng, buttonDescription, buttonHint);
        gridView.setAdapter(gridAdapter);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    fab.show();
                    forum.show();
                    newForumPost();
                    if (newNotifications) {
                        forumhelpNotify.setVisibility(View.VISIBLE);
                    }

                } else {
                    fab.hide();
                    forum.hide();
                    forumhelpNotify.setVisibility(View.GONE);
                }

            }
        });
    }

    private void createAdView() {
        mAdView = (AdView) findViewById(R.id.adViewCard);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void checkAppUpdates() {
        updateReference = database.getReference();
        updateReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("docUpdateVersion").getValue(Integer.class) > versionCode) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage(dataSnapshot.child("docUpdateMessage").getValue(String.class));
                    checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zubayer.docsites")));
                        }
                    });
                    checkinternet.show();
                }
                if (dataSnapshot.child("docNotifyVersion").getValue(Integer.class) > versionCode) {
                    updateNotifier.setVisibility(View.VISIBLE);
                    updateNotifier.setText(dataSnapshot.child("docNotifyMessage").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listViewOptionLoader(int position) {
        Dialog.setTitle(buttonTexts.get(position));
        if (urls.isEmpty()) {
            if (bsmmuClicked) {
                switch (position) {
                    case 0:
                        bsmmuHome();
                        break;
                    case 1:
                        buttonTexts.clear();
                        residency();
                        Dialog.show();
                        break;
                    case 2:
                        buttonTexts.clear();
                        admission();
                        Dialog.show();
                        break;
                    case 3:
                        buttonTexts.clear();
                        bsmmuNotice();
                        Dialog.show();
                        break;
                }
            }
            if (bcpsClicked) {
                switch (position) {
                    case 0:
                        bcpsHome();
                        break;
                    case 1:
                        bcpsNotice();
                        break;
                    case 2:
                        buttonTexts.clear();
                        bcpsGuideline();
                        Dialog.show();
                        break;
                    case 3:
                        fcpsPart1Regi();
                        break;
                    case 4:
                        fcpsResults();
                        break;
                }
            }
            if (dghsClicked) {
                switch (position) {
                    case 0:
                        dghsHome();
                        break;
                    case 1:
                        buttonTexts.clear();
                        dghsHomeLinks();
                        Dialog.show();
                        break;
                    case 2:
                        buttonTexts.clear();
                        postingOrders();
                        Dialog.show();
                        break;
                    case 3:
                        HRM();
                        break;
                }
            }
            if (mohfwClicked) {
                switch (position) {
                    case 0:
                        mohfwHome();
                        break;
                    case 1:
                        deputation();
                        break;
                    case 2:
                        browser("http://www.mohfw.gov.bd/index.php?option=com_content&view=article&id=61%3Amedical-education&catid=46%3Amedical-education&Itemid=&lang=en");
                        break;
                    case 3:
                        buttonTexts.clear();
                        filterContent = getString(R.string.assistantSurgeon);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 4:
                        buttonTexts.clear();
                        filterContent = getString(R.string.juniorConsultant);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 5:
                        buttonTexts.clear();
                        filterContent = "uuuuuuuu";
                        filterContent2 = getString(R.string.seniorConsultant);
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 6:
                        buttonTexts.clear();
                        filterContent = getString(R.string.assistantProfessor);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 7:
                        buttonTexts.clear();
                        filterContent = getString(R.string.associateProfessor);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 8:
                        buttonTexts.clear();
                        filterContent = getString(R.string.professor);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 9:
                        buttonTexts.clear();
                        filterContent = getString(R.string.civilSurgeon);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 10:
                        buttonTexts.clear();
                        filterContent = getString(R.string.adhoc);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 11:
                        browser("http://mohfw.gov.bd/index.php?option=com_content&view=article&id=121%3Aearn-leave&catid=101%3Aearn-leave-ex-bangladesh-leave&Itemid=&lang=en");
                        break;
                }
            }
            if (bpscClicked) {
                switch (position) {
                    case 0:
                        bpscHpme();
                        break;
                    case 1:
                        regiBCS();
                        break;
                    case 2:
                        buttonTexts.clear();
                        resultBCS();
                        Dialog.show();
                        break;
                    case 3:
                        buttonTexts.clear();
                        bpscForms();
                        Dialog.show();
                        break;
                    case 4:
                        buttonTexts.clear();
                        filterContent = getString(R.string.assistantSurgeon);
                        filterContent2 = "aaaaaaa";
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 5:
                        buttonTexts.clear();
                        regiDept();
                        Dialog.show();
                        break;
                    case 6:
                        buttonTexts.clear();
                        resultDept();
                        Dialog.show();
                        break;
                    case 7:
                        buttonTexts.clear();
                        regiSenior();
                        Dialog.show();
                        break;
                    case 8:
                        buttonTexts.clear();
                        resultSenior();
                        Dialog.show();
                        break;
                }
            }
            if (gazetteClicked) {
                switch (position) {
                    case 0:
                        seniorGazette();
                        break;
                    case 1:
                        departmentalGazette();
                        break;
                    case 2:
                        buttonTexts.clear();
                        filterContent = getString(R.string.service);
                        filterContent2 = getString(R.string.service2);
                        serviceConfirmGazette();
                        Dialog.show();
                        break;
                    case 3:
                        weeklyGazette();
                        break;
                }
            }
            if (bmdcClicked) {
                switch (position) {
                    case 0:
                        bmdcHpme();
                        break;
                    case 1:
                        findDoctor();
                        break;
                    case 2:
                        bmdcDownloadableForms();
                        break;
                }
            }
            if (resultsClicked) {
                switch (position) {
                    case 0:
                        mbbsResult();
                        break;
                    case 1:
                        bdsResult();
                        break;
                }
            }
            if (dgfpClicked) {
                switch (position) {
                    case 0:
                        dgfpHome();
                        break;
                    case 1:
                        buttonTexts.clear();
                        dgfpOrder();
                        break;
                    case 2:
                        buttonTexts.clear();
                        dgfpNotice();
                        break;
                    case 3:
                        buttonTexts.clear();
                        dgfpNOC();
                        break;
                }
            }

        } else {
            try {
                for (int i = 0; i < urls.size(); i++) {
                    if (position == i) {
                        pdfFilter = urls.get(position);
                        browser(pdfFilter);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void gridViewOptionLoader(int position) {
        Dialog.setTitle(buttonHeadidng.get(position));
        switch (position) {
            case 0:
                loadBsmmuOptions();
                bsmmuClicked = true;
                break;
            case 1:
                loadBcpsOptions();
                bcpsClicked = true;
                break;
            case 2:
                loadDghsOptions();
                dghsClicked = true;
                break;
            case 3:
                loadMohfwOptions();
                mohfwClicked = true;
                break;
            case 4:
                loadBpscOptions();
                bpscClicked = true;
                break;
            case 5:
                loadGazetteOptions();
                gazetteClicked = true;
                break;
            case 6:
                loaddgfpOptions();
                dgfpClicked = true;
                break;
            case 7:
                loadccdOptions();
                ccdClicked = true;
                ccdNotices1();

                break;
            case 8:
                loadBMDCOptions();
                bmdcClicked = true;
                break;
            case 9:
                loadResultOptions();
                resultsClicked = true;
                break;

        }
    }

    private void readNotificationCount() {
        try {
            SharedPreferences oldsize = getSharedPreferences("finalNotificationCount", Context.MODE_PRIVATE);
            oldNotificatinSize = oldsize.getInt("finalsize", 0);

            notificationNumberText = Integer.toString(oldNotificatinSize);
            if (oldNotificatinSize == 0) {
                showNotificationNumber.setVisibility(View.GONE);
                newNotifications = false;
            } else {
                newNotifications = true;
                showNotificationNumber.setVisibility(View.VISIBLE);
                showNotificationNumber.setText(notificationNumberText);
            }

        } catch (Exception e) {
        }
    }

    private void initializeWidgetVariable() {
        notificationSummery = (ImageButton) findViewById(R.id.notificationSumery);
        forum = (FloatingActionButton) findViewById(R.id.forum);
        showNotificationNumber = (Button) findViewById(R.id.notificationCount);
        forumhelpNotify = (TextView) findViewById(R.id.forumhelpNotify);
        updateNotifier = (TextView) findViewById(R.id.updateNotifier);
        appVersion= (TextView) findViewById(R.id.version);
        appVersion.setText(getString(R.string.app_name)+" "+BuildConfig.VERSION_NAME);
        updateNotifier.setVisibility(View.GONE);
        forumhelpNotify.setVisibility(View.GONE);
        showNotificationNumber.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance();
    }

    private boolean dataconnected() {
        boolean dataConnected = false;
        boolean wifiIsAvailable, mobileDataIsAvailable;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            wifiIsAvailable = networkInfo.isConnected();
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            mobileDataIsAvailable = networkInfo.isConnected();
            dataConnected = wifiIsAvailable || mobileDataIsAvailable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataConnected;
    }

    private void subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("forum").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void newForumPost() {
        queryID = new ArrayList<>();
        queryname = new ArrayList<>();
        rootReference = database.getReference().child("user");
        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    queryID.add(0, snapshot.getKey());
                    queryname.add(0, snapshot.child("first_name").getValue(String.class));
                }

                SharedPreferences preferences = getSharedPreferences("newQuery", Context.MODE_PRIVATE);
                queryNotification = preferences.getString("query", null);
                if (!queryID.get(0).equals(queryNotification)&&queryID.size()!=0) {
                    forumhelpNotify.setVisibility(View.VISIBLE);
                    forumhelpNotify.setText(queryname.get(0) + " asked...");
                } else {
                    forumhelpNotify.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setAlarm() {
        Intent newIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            assert manager != null;
            manager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 1);

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

    }
}