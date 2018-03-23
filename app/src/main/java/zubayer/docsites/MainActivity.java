package zubayer.docsites;

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
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.widget.Toast.makeText;

public class MainActivity extends Activity {
    AlertDialog Dialog, checkinternet;
    AlertDialog.Builder builder;
    View m;
    Calendar calendar;
    private AdView mAdView;
    AlarmManager manager;
    PendingIntent pendingIntent;
    ProgressBar progressBar;
    ListView list;
    GridView gridView;
    ArrayList<String> buttonTexts, urls, buttonHeadidng, buttonDescription, buttonHint, buttonTexts2,
            bsmmuOptions, bcpsOptions, dghsOptions, mohfwOptions, bpscOptions, gazetteOptions, bmdcOptions, resultOptions;
    MyAdapter adapter;
    GridAdapter gridAdapter;
    HtmlParser back;
    BcpsParser bcps;
    DghsParser dghsParser;
    DghsParser2 dghsParser2;
    DghsParser3 dghsParser3;
    ServiceParser serviceParser;
    UpdateChecker check;
    String btxt, newline, url, paramUrl, paramTagForText, paramTagForLink, paramLink,
            updateMessage, parseVersionCode, pdfFilter, driveViewer, filterContent, filterContent2;
    int position, textMin, textMax, linkBegin, linkEnd, versionCode;
    boolean bsmmuClicked, bcpsClicked, dghsClicked, mohfwClicked, bpscClicked, gazetteClicked,
            bmdcClicked, resultsClicked, applaunched, checkpop, wifiAvailable, mobileDataAvailable;
    MenuItem menuitem;
    SharedPreferences preferences;
    Intent newIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adViewCard);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        versionCode=BuildConfig.VERSION_CODE;
        preferences = getSharedPreferences("appLaunched", 0);
        applaunched = preferences.getBoolean("appLaunchedchecked", false);
        if (applaunched) {
        } else {
            selectAll();
        }
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        try {
            assert manager != null;
            manager.cancel(pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
        calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        newIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);


        bsmmuClicked = bcpsClicked = dghsClicked = mohfwClicked = bpscClicked = gazetteClicked = bmdcClicked = resultsClicked = false;
        String[] headingName = getResources().getStringArray(R.array.heading);
        String[] headingDescription = getResources().getStringArray(R.array.description);
        String[] hints = getResources().getStringArray(R.array.hints);
        String[] bsmmuOption = getResources().getStringArray(R.array.bsmmuOptions);
        String[] bcpsOption = getResources().getStringArray(R.array.bcpsOptions);
        String[] dghsOption = getResources().getStringArray(R.array.dghsOptions);
        String[] mohfwOption = getResources().getStringArray(R.array.mohfwOptions);
        String[] bpscOption = getResources().getStringArray(R.array.bpscOptions);
        String[] gazetteOption = getResources().getStringArray(R.array.gazetteOptions);
        String[] bmdcOption = getResources().getStringArray(R.array.bmdcOptions);
        String[] resultsOption = getResources().getStringArray(R.array.resultsOption);
        driveViewer = "https://docs.google.com/viewer?url=";
        buttonTexts = new ArrayList<>();
        buttonTexts2 = new ArrayList<>();
        urls = new ArrayList<>();
        buttonHeadidng = new ArrayList<>();
        buttonDescription = new ArrayList<>();
        buttonHint = new ArrayList<>();
        bsmmuOptions = new ArrayList<>();
        bcpsOptions = new ArrayList<>();
        dghsOptions = new ArrayList<>();
        mohfwOptions = new ArrayList<>();
        bpscOptions = new ArrayList<>();
        gazetteOptions = new ArrayList<>();
        bmdcOptions = new ArrayList<>();
        resultOptions = new ArrayList<>();
        Collections.addAll(bsmmuOptions, bsmmuOption);
        Collections.addAll(bcpsOptions, bcpsOption);
        Collections.addAll(dghsOptions, dghsOption);
        Collections.addAll(mohfwOptions, mohfwOption);
        Collections.addAll(bpscOptions, bpscOption);
        Collections.addAll(gazetteOptions, gazetteOption);
        Collections.addAll(bmdcOptions, bmdcOption);
        Collections.addAll(buttonHeadidng, headingName);
        Collections.addAll(buttonDescription, headingDescription);
        Collections.addAll(buttonHint, hints);
        Collections.addAll(resultOptions, resultsOption);
        adapter = new MyAdapter(MainActivity.this, buttonTexts, urls);
        gridAdapter = new GridAdapter(MainActivity.this, buttonHeadidng, buttonDescription, buttonHint);
        m = getLayoutInflater().inflate(R.layout.listview, null);
        list = (ListView) m.findViewById(R.id.ListView);
        gridView = (GridView) findViewById(R.id.grid);
        list.setAdapter(adapter);
        gridView.setAdapter(gridAdapter);
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
        progressBar = (ProgressBar) m.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
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
        Dialog = builder.create();
        Dialog.setCancelable(false);
        Dialog.setButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                bsmmuClicked = bcpsClicked = dghsClicked = mohfwClicked = bpscClicked = gazetteClicked = bmdcClicked = resultsClicked = false;
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
        checkSstorage();
        check = new UpdateChecker();
        check.execute();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Dialog.setTitle(buttonHeadidng.get(position));
                check = new UpdateChecker();
                switch (position) {
                    case 0:
                        check.execute();
                        loadBsmmuOptions();
                        bsmmuClicked = true;
                        break;
                    case 1:
                        loadBcpsOptions();
                        bcpsClicked = true;
                        check.execute();
                        break;
                    case 2:
                        loadDghsOptions();
                        dghsClicked = true;
                        check.execute();
                        break;
                    case 3:
                        loadMohfwOptions();
                        mohfwClicked = true;
                        check.execute();
                        break;
                    case 4:
                        loadBpscOptions();
                        bpscClicked = true;
                        check.execute();
                        break;
                    case 5:
                        loadGazetteOptions();
                        gazetteClicked = true;
                        check.execute();
                        break;
                    case 6:
                        loadBMDCOptions();
                        bmdcClicked = true;
                        check.execute();
                        break;
                    case 7:
                        loadResultOptions();
                        resultsClicked = true;
                        check.execute();
                        break;
                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Dialog.setTitle(buttonTexts.get(position));
                check = new UpdateChecker();
                check.execute();
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
                                filterContent = getString(R.string.seniorConsultant2);
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
        });
    }

    public boolean checkSstorage() {
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
                    if (checkSstorage()) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
            });
            checkinternet.show();
        }
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
            Elements hrefs = doc.select(tagForLink);
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
            for (int i = links.size()-11; i < links.size(); i++) {
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
            if (url != null) {
                progressBar.setVisibility(View.GONE);
                list.setAdapter(adapter);
            } else {
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
            if (url != null) {
                progressBar.setVisibility(View.GONE);
                list.setAdapter(adapter);
                dghsHomeLinks2();
            } else {
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
            if (url != null) {
                progressBar.setVisibility(View.GONE);
                list.setAdapter(adapter);
                dghsHomeLinks3();
            } else {
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
            if (url != null) {
                progressBar.setVisibility(View.GONE);
                list.setAdapter(adapter);
            } else {
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
            if (btxt != null) {
                progressBar.setVisibility(View.GONE);
                list.setAdapter(adapter);
            } else {
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
            }
        }
    }

    class BcpsParser extends AsyncTask<Void, Void, Void> {

        String URl = "http://www.bcpsbd.org/notice.php";
        String tagtext = "a";
        String taglink = "a";
        String Attrs = "onClick";
        String uRl = "";
        String prelingk = "http://www.bcpsbd.org/";

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
            if (uRl != null) {
                progressBar.setVisibility(View.GONE);
                list.setAdapter(adapter);
            } else {
                checkinternet = builder.create();
                checkinternet.setMessage("Check your network connection");
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

                } catch (Exception e) {
                }
                checkinternet.setCancelable(false);
                checkinternet.show();
            }
        }
    }

    class UpdateChecker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://drzubayerahmed.wordpress.com/2017/11/29/26/?preview=true").get();
                Elements links = doc.select("p");
                Element link = links.get(0);
                Element message = links.get(2);
                parseVersionCode = link.text();
                updateMessage = message.text();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (parseVersionCode != null) {
                Integer parseint = Integer.parseInt(parseVersionCode);
                if (parseint > versionCode) {
                    SharedPreferences prefs = getSharedPreferences("updateDocSite", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("yesno", true).apply();
                    prefs.edit().putString("updateMessage", updateMessage).apply();
                    checkUpdate();
                } else if (parseint == versionCode) {
                    SharedPreferences prefs = getSharedPreferences("updateDocSite", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("yesno", false).apply();
                    checkUpdate();
                } else if (parseint < versionCode) {
                    SharedPreferences prefs = getSharedPreferences("updateDocSite", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("yesno", false).apply();
                    checkUpdate();
                }
            } else {
                checkUpdate();
            }
        }

        @Override
        protected void onCancelled() {
            buttonTexts.clear();
            urls.clear();
            super.onCancelled();
        }

        public void checkUpdate() {
            try {
                SharedPreferences prefs = getSharedPreferences("updateDocSite", Context.MODE_PRIVATE);
                boolean b = prefs.getBoolean("yesno", false);
                updateMessage = prefs.getString("updateMessage", null);
                if (b) {
                    checkinternet = builder.create();
                    checkinternet.setButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface da, int but) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zubayer.docsites"));
                            startActivity(i);
                        }
                    });
                    checkinternet.setMessage(updateMessage);
                    checkinternet.setCancelable(false);
                    checkinternet.show();
                }
            } catch (Exception e) {
            }
        }

        public void myToast(String toasttText) {
            Toast toast = makeText(MainActivity.this, toasttText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem share = menu.findItem(R.id.share);
        menuitem = menu.findItem(R.id.item2);
        share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SharedPreferences settings = getSharedPreferences("setting", 0);
        boolean checked = settings.getBoolean("checked", false);
        menuitem.setChecked(checked);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent setting = new Intent(MainActivity.this, Settings.class);
                startActivity(setting);
                break;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=zubayer.docsites");
                startActivity(Intent.createChooser(intent, "Share using.."));
//                deleteAll();
                break;
            case R.id.about:
                buttonTexts.add(getString(R.string.about));
                Dialog.show();
                progressBar.setVisibility(View.GONE);

                break;
            case R.id.item2:
                if (item.getItemId() == R.id.item2) {
                    item.setChecked(!item.isChecked());
                    SharedPreferences settings = getSharedPreferences("setting", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("checked", item.isChecked());
                    editor.apply();
                }
                break;
            case R.id.rate:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zubayer.docsites"));
                startActivity(i);
                break;
            case R.id.check:
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                wifiAvailable = networkInfo.isConnected();
                networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                mobileDataAvailable = networkInfo.isConnected();
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
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 11, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    myToaster("Checking Notifications");
                }
                break;
//            case R.id.summery:
//                Intent summery = new Intent(this, NotificationSummery.class);
//                summery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(summery);
//                finish();
//                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        boolean pressed = false;
        if (!pressed) {
            checkinternet = builder.create();
            checkinternet.setButton("Send mail", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "zubayer.developer@gmail.com"));
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)+": "+ Build.MANUFACTURER + " " + Build.MODEL + " " + "(" + Build.VERSION.RELEASE + ")");
                    i.putExtra(Intent.EXTRA_TEXT, "Write here:");
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
        }
    }

    private void bsmmuHome() {
        pdfFilter = "http://www.bsmmu.edu.bd";
        browser(pdfFilter);
    }

    private void residency() {
        back = new HtmlParser();
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "a";
        paramTagForLink = "a";
        paramLink = "abs:href";
        textMin = 146;
        linkBegin = 146;
        textMax = 153;
        linkEnd = 153;
        position = 15;
        newline = "★★★";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void admission() {
        back = new HtmlParser();
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "a";
        paramTagForLink = "a";
        paramLink = "abs:href";
        textMin = 155;
        linkBegin = 155;
        textMax = 159;
        linkEnd = 159;
        position = 5;
        newline = "★★★";
        back.execute();
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
        pdfFilter = "http://www.bcpsbd.org/volume/";
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
        paramLink = "href";
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
        paramLink = "href";
        textMin = linkBegin = 15;
        textMax = linkEnd = 20;
        position = 7;
        newline = "wah";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void resultBCS() {
        back = new HtmlParser();
        paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr a";
        paramLink = "href";
        textMin = 1;
        textMax = 122;
        linkBegin = 0;
        linkEnd = 123;
        position = 125;
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void resultDept() {
        back = new HtmlParser();
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "href";
        textMin = 1;
        textMax = 26;
        linkBegin = 0;
        linkEnd = 26;
        position = 28;
        newline = "★ Click to download pdf:";
        back.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void resultSenior() {
        back = new HtmlParser();
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "href";
        textMin = 1;
        textMax = 38;
        linkBegin = 0;
        linkEnd = 38;
        position = 40;
        newline = "★ Click to download pdf:";
        back.execute();
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

    public void browser(String inurl) {
        final String uurl = inurl;
        try {
            if (menuitem.isChecked() == false) {
                Intent intent = new Intent(MainActivity.this, Browser.class);
                intent.putExtra("value", inurl);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(inurl));
                startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    private void myToaster(String text) {
        Toast toast = makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void selectDeselect(String preferenceName, String putBooleanName) {
        SharedPreferences settings = getSharedPreferences(preferenceName, 0);
        settings.edit().putBoolean(putBooleanName, true).apply();
    }
    public void deletePreference(String preferenceName, String key) {
        SharedPreferences settings = getSharedPreferences(preferenceName, 0);
        settings.edit().remove(key).apply();
    }

    public void selectAll() {
        selectDeselect("residencySetting","residencyChecked");
        selectDeselect("noticeSetting", "noticeChecked");
        selectDeselect("dghsSetting", "dghsChecked");
        selectDeselect("reultBcsSetting", "reultBcsChecked");
        selectDeselect("resultDeptSetting","resultDeptChecked");
        selectDeselect("resultSeniorSetting","resultSeniorChecked");
        selectDeselect("regiDeptSetting","regiDeptChecked");
        selectDeselect("regiSeniorSetting","regiSeniorChecked");
        selectDeselect("assistantSurgeonSetting","assistantSurgeonChecked");
        selectDeselect("juniorConsultantSetting","juniorConsultantChecked");
        selectDeselect("seniorConsultantSetting", "seniorConsultantChecked");
        selectDeselect("assistantProfessorSetting","assistantProfessorChecked");
        selectDeselect("associateProfessorSetting", "associateProfessorChecked");
        selectDeselect("professorSetting", "professorChecked");
        selectDeselect("civilSurgeonSetting","civilSurgeonChecked");
        selectDeselect("adhocSetting","adhocChecked");
        selectDeselect("mohfwSetting","mohfwChecked");
        selectDeselect("deputationSetting", "deputationChecked");
        selectDeselect("leaveSetting","leaveChecked");
        selectDeselect("appLaunched", "appLaunchedchecked");
    }

    public void deleteAll(){
        deletePreference("residencySetting","residencyChecked");
        deletePreference("noticeSetting","noticeChecked");
        deletePreference("dghsSetting","dghsChecked");
        deletePreference("reultBcsSetting","reultBcsChecked");
        deletePreference("resultDeptSetting","resultDeptChecked");
        deletePreference("resultSeniorSetting","resultSeniorChecked");
        deletePreference("regiDeptSetting","regiDeptChecked");
        deletePreference("regiSeniorSetting","regiSeniorChecked");
        deletePreference("assistantSurgeonSetting","assistantSurgeonChecked");
        deletePreference("juniorConsultantSetting","juniorConsultantChecked");
        deletePreference("seniorConsultantSetting","seniorConsultantChecked");
        deletePreference("assistantProfessorSetting","assistantProfessorChecked");
        deletePreference("associateProfessorSetting","associateProfessorChecked");
        deletePreference("professorSetting","professorChecked");
        deletePreference("civilSurgeonSetting","civilSurgeonChecked");
        deletePreference("adhocSetting","adhocChecked");
        deletePreference("mohfwSetting","mohfwChecked");
        deletePreference("deputationSetting","deputationChecked");
        deletePreference("leaveSetting","leaveChecked");
        deletePreference("appLaunched","appLaunchedchecked");
        deletePreference("wentToSetting","wentToSetting");
        myToaster("Preference deleted");
    }
}