package zubayer.docsites.activity;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.*;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import me.anwarshahriar.calligrapher.Calligrapher;
import zubayer.docsites.adapters.GazetteAdapter;
import zubayer.docsites.adapters.MyAdapter;
import zubayer.docsites.R;

public class GazetteActivity extends Activity {
    ArrayList<String> yearArray, monthArray, resultArray, ministryArray, yearUrls, monthUrls, listUrls;
    ListView yearList, resultList;
    MyAdapter resultAdapter;
    GazetteAdapter yearAdapter;
    TextView text, yearHeading, resultHeading;
    AlertDialog checkinternet;
    AlertDialog.Builder builder;
    YearParser pareseYear;
    YearNextParser yearNextParser;
    MonthParser monthParser;
    ResultParser resultParser;
    VolumrParser volumeParser;
    NavigationParser navigationParser;
    GazetteParser gazetteParser;
    String btxt, url, parentUrl, yearUrl, yearUrlNext, monthUrl, yearName, monthName, resultUrl, paramTagForText,
            paramTagForLink, paramLink, filterContent, message, examName, pdfFilter, driveViewer;
    int textMin, linkBegin, resultMin, reslinkBegin;
    ProgressDialog progressDialog;
    Iterator<String> it, name;
    boolean wifiAvailable, mobileDataAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazette);

        createAddView();
        setFont();
        initializeAll();
        filterExamContent();
        if (filterContent.equals(getString(R.string.weeklyGazetteHeading))) {
            parentUrl = "http://www.dpp.gov.bd/bgpress/index.php/document/weekly_gazettes/151";
            yearHeading.setText(getString(R.string.volumeNavigation));
            resultHeading.setText(getString(R.string.volume));
            executeVolume();
        } else {
            progressDialog = ProgressDialog.show(this, "", "Loading list of gazette years...", true, true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setMessage("List of gazette years loading is in progress...");
                    checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Wait", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            if (yearArray.isEmpty()) {
                                progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Loading list of gazette years...", true, true);
                            }
                        }
                    });
                    checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            pareseYear.cancel(true);
                            yearNextParser.cancel(true);
                            monthParser.cancel(true);
                            resultParser.cancel(true);
                            dismissProgressDialog();
                            finish();
                        }
                    });

                    try {
                        checkinternet.show();
                    } catch (Exception e) {
                    }
                }
            });
            executeYear();
        }

        yearList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filterContent.equals(getString(R.string.weeklyGazetteHeading))) {
                    resultArray.clear();
                    listUrls.clear();
                    yearList.setAdapter(yearAdapter);
                    yearAdapter.notifyDataSetChanged();
                    parentUrl = yearUrls.get(position);
                    btxt = null;
                    yearUrls.clear();
                    yearArray.clear();
                    resultList.setAdapter(resultAdapter);
                    resultAdapter.notifyDataSetChanged();
                    executeVolume();

                } else {
                    monthParser.cancel(true);
                    resultParser.cancel(true);
                    message = yearArray.get(position);
                    progressDialog.dismiss();
                    monthUrls.clear();
                    monthArray.clear();
                    resultArray.clear();
                    listUrls.clear();
                    resultList.setAdapter(resultAdapter);
                    resultAdapter.notifyDataSetChanged();
                    monthUrl = yearUrls.get(position);
                    yearName = yearArray.get(position);
                    executeMonth();
                }

            }
        });

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (filterContent.equals(getString(R.string.weeklyGazetteHeading))) {
                    parentUrl = listUrls.get(position);
                    browser("async",parentUrl);
//                    btxt = null;
//                    executeGazette();
                } else {
                    try {
                        pdfFilter = listUrls.get(position);
                        browser("value",pdfFilter);
                    } catch (Exception e) {
                        checkinternet = builder.create();
                        checkinternet.setMessage("Something went wrong, try again");
                        try {
                            checkinternet.show();
                        } catch (Exception ex) {
                        }
                    }

                }
            }
        });
    }

    public void yearExecutableTag(String Url, String TagForText, String Attr, int begin) {

        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < 12; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                yearArray.add(btxt);
                yearUrls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void monthExecutableTag(String TagForText, String Attr, int begin) {

        try {
            Document doc = Jsoup.connect(monthUrl).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                monthArray.add(btxt);
                monthUrls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void resultExecutableTag(String Url, String TagForText, String Attr, int begin) {
        try {
            Url = it.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                if (btxt.contains(filterContent)) {
                    resultArray.add(btxt);
                    listUrls.add(url);
                }
            }
        } catch (Exception e) {
        }
    }

    public void volumeExecutableTag(String TagForText, String Attr, int begin) {
        try {
            Document doc = Jsoup.connect(parentUrl).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(Attr);
                if (btxt.contains("Vol")) {
                    resultArray.add(btxt);
                    listUrls.add(url);
                }

            }
        } catch (Exception e) {
        }
    }

    public void navigationExecutableTag(String TagForText, String Attr, int begin) {

        try {
            Document doc = Jsoup.connect(parentUrl).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.attr(Attr);
                yearArray.add(btxt);
                yearUrls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void gazetteExecutableTag(String TagForText, String Attr, int begin) {
        try {
            Document doc = Jsoup.connect(parentUrl).get();
            Elements links = doc.select(TagForText);
            for (int i = begin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.attr(Attr);
                if (btxt.contains(getString(R.string.filterGazette))) {
                    monthUrls.add(url);
                }
            }
        } catch (Exception e) {
        }
    }

    class YearParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            yearExecutableTag(yearUrl, paramTagForText, paramLink, textMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            if (yearArray != null && yearUrls != null) {
                yearArray.clear();
                yearUrls.clear();
            }
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            dismissProgressDialog();
            super.onPostExecute(b);
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                        loadYearAgain();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                yearList.setAdapter(yearAdapter);
                yearAdapter.notifyDataSetChanged();
                executeYearNext();
                btxt = null;
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Website is not responding");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                        loadYearAgain();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    class YearNextParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            yearExecutableTag(yearUrlNext, paramTagForText, paramLink, textMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            if (yearArray != null && yearUrls != null) {
                yearArray.clear();
                yearUrls.clear();
            }
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            dismissProgressDialog();
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        loadYearAgain();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                resultArray.add(getString(R.string.text));
                yearList.setAdapter(yearAdapter);
                resultList.setAdapter(resultAdapter);
                yearAdapter.notifyDataSetChanged();

            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Website is not responding");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        loadYearAgain();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    class MonthParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            monthExecutableTag(paramTagForText, paramLink, textMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            dismissProgressDialog();
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeMonth();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                it = monthUrls.iterator();
                name = monthArray.iterator();
                btxt = null;
                executeResult();
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Website is not responding");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeMonth();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    class ResultParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            resultExecutableTag(resultUrl, paramTagForText, paramLink, resultMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            resultArray.clear();
            listUrls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            dismissProgressDialog();
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeResult();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                resultList.setAdapter(resultAdapter);
                resultAdapter.notifyDataSetChanged();
                btxt = null;
                if (it.hasNext()) {
                    checkConnectivity();
                } else {
                    if (resultArray.size() == 0) {
                        checkinternet = builder.create();
                        checkinternet.setMessage("No Gazette published on " + yearName);
                        checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                            }
                        });
                        try {
                            checkinternet.show();
                        } catch (Exception e) {
                        }
                    }
                }
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Website is not responding");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeResult();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    class VolumrParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            volumeExecutableTag(paramTagForText, paramLink, textMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            volumeParser.cancel(true);
            monthArray.clear();
            monthUrls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            dismissProgressDialog();
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        monthUrls.clear();
                        monthArray.clear();
                        yearList.setAdapter(yearAdapter);
                        yearAdapter.notifyDataSetChanged();
                        btxt = null;
                        executeVolume();
                        yearUrls.clear();
                        yearArray.clear();
                        resultList.setAdapter(resultAdapter);
                        resultAdapter.notifyDataSetChanged();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                resultList.setAdapter(resultAdapter);
                resultAdapter.notifyDataSetChanged();
                btxt = null;
                executeNavigation();
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Website is not responding");
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        monthUrls.clear();
                        monthArray.clear();
                        yearList.setAdapter(yearAdapter);
                        resultAdapter.notifyDataSetChanged();
                        btxt = null;
                        executeVolume();
                        yearUrls.clear();
                        yearArray.clear();
                        resultList.setAdapter(resultAdapter);
                        resultAdapter.notifyDataSetChanged();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    class NavigationParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            navigationExecutableTag(paramTagForText, paramLink, textMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            navigationParser.cancel(true);
            monthArray.clear();
            monthUrls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            dismissProgressDialog();
//            saveResults();
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeNavigation();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {

                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                yearList.setAdapter(yearAdapter);
                yearAdapter.notifyDataSetChanged();
                btxt = null;
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Next pages are not loaded");
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeNavigation();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    class GazetteParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            gazetteExecutableTag(paramTagForText, paramLink, textMin);
            return null;
        }

        @Override
        protected void onCancelled() {
            gazetteParser.cancel(true);
            resultArray.clear();
            listUrls.clear();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            dismissProgressDialog();
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeGazette();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {

                    }
                });
                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                browser("value",monthUrls.get(0));
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Website is not responding");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        executeGazette();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {

                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            }
        }
    }

    public void executeYear() {
        yearUrl = "http://www.dpp.gov.bd/bgpress/bangla/index.php/document/extraordinary_gazettes/285";
        pareseYear = new YearParser();
        paramTagForText = "#MyResult tr";
        paramTagForLink = "#MyResult tr a";
        paramLink = "abs:href";
        textMin = 0;
        pareseYear.execute();
    }

    public void executeYearNext() {
        dismissProgressDialog();
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Wait a few more moment..", true, true);
        }
        yearUrlNext = yearUrl + "/publication_date/12";
        yearNextParser = new YearNextParser();
        paramTagForText = "#MyResult tr";
        paramLink = "abs:href";
        textMin = 0;
        yearNextParser.execute();
    }

    private void executeMonth() {
        monthParser = new MonthParser();
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(GazetteActivity.this, "", "This may take some time, please wait..", true, true);
        }
        paramTagForText = "#MyResult tr";
        paramLink = "abs:href";
        textMin = 0;
        monthParser.execute();
    }

    private void executeResult() {
        dismissProgressDialog();
        try {
            monthName = name.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Searching " + monthName, true, true);
        }
        resultParser = new ResultParser();
        paramTagForText = "#MyResult tr";
        paramLink = "href";
        resultMin = 1;
        reslinkBegin = 0;
        resultParser.execute();
    }

    private void loadYearAgain() {
        progressDialog.dismiss();
        yearUrls.clear();
        yearArray.clear();
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Loading gazette years...", true, true);
        }
        executeYear();
    }

    public void executeVolume() {
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(this, "", "Loading volumes...", true, true);
        }
        volumeParser = new VolumrParser();
        paramTagForText = "#MyResult tr";
        paramLink = "abs:href";
        textMin = 0;
        volumeParser.execute();
    }

    public void executeNavigation() {
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(this, "", "Loading next pages...", true, true);
        }
        navigationParser = new NavigationParser();
        paramTagForText = "#pagination-bar a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        navigationParser.execute();
    }

    public void executeGazette() {
        gazetteParser = new GazetteParser();
        paramTagForText = "tr a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        gazetteParser.execute();
        if (!GazetteActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(this, "", "Loading Gazettes...", true, true);
        }
    }

    private void browser(String key,String inurl) {
        startActivity(new Intent(GazetteActivity.this, Browser.class).putExtra(key, inurl));
    }

    @Override
    public void onBackPressed() {

        try {
            volumeParser.cancel(true);
            navigationParser.cancel(true);
            gazetteParser.cancel(true);
            pareseYear.cancel(true);
            yearNextParser.cancel(true);
            monthParser.cancel(true);
            resultParser.cancel(true);
            dismissProgressDialog();
        } catch (Exception e) {
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        try {
            volumeParser.cancel(true);
            navigationParser.cancel(true);
            gazetteParser.cancel(true);
            pareseYear.cancel(true);
            yearNextParser.cancel(true);
            monthParser.cancel(true);
            resultParser.cancel(true);
            dismissProgressDialog();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        readResult();
        resultAdapter.notifyDataSetChanged();
        yearAdapter.notifyDataSetChanged();
    }

    private void checkConnectivity() {
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
            checkinternet.setMessage("Data connection interrupted!");
            checkinternet.setButton(DialogInterface.BUTTON_POSITIVE, "Reload", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    progressDialog.dismiss();
                    resultArray.clear();
                    listUrls.clear();
                    monthArray.clear();
                    monthUrls.clear();
                    resultList.setAdapter(resultAdapter);
                    executeMonth();
                }
            });
            checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                }
            });
            checkinternet.setButton3("close", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    dismissProgressDialog();
                }
            });

            try {
                checkinternet.show();
            } catch (Exception e) {
            }
            dismissProgressDialog();
        } else {
            executeResult();
        }
    }

    private void createAddView() {
        AdView mAdView = (AdView) findViewById(R.id.adViewGazette);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setFont() {
        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
    }

    private void initializeAll() {
        yearList = (ListView) findViewById(R.id.year);
        resultList = (ListView) findViewById(R.id.result);
        text = (TextView) findViewById(R.id.heading);
        yearHeading = (TextView) findViewById(R.id.yearHeading);
        resultHeading = (TextView) findViewById(R.id.resultHeading);
        yearArray = new ArrayList<>();
        ministryArray = new ArrayList<>();
        resultArray = new ArrayList<>();
        yearUrls = new ArrayList<>();
        monthArray = new ArrayList<>();
        monthUrls = new ArrayList<>();
        listUrls = new ArrayList<>();
        builder = new AlertDialog.Builder(GazetteActivity.this);
        yearAdapter = new GazetteAdapter(GazetteActivity.this, yearArray);
        resultAdapter = new MyAdapter(GazetteActivity.this, resultArray, listUrls);
        driveViewer = "https://docs.google.com/viewer?url=";
        monthParser = new MonthParser();
        pareseYear = new YearParser();
        yearNextParser = new YearNextParser();
        resultParser = new ResultParser();
    }

    private void filterExamContent() {
        filterContent = getIntent().getExtras().getString("examname");
        if (filterContent.equals(getString(R.string.filterSeniorScale))) {
            examName = getString(R.string.seniorScaleOption);
            text.setText(examName);
        } else if (filterContent.equals(getString(R.string.filterDepartmental))) {
            examName = getString(R.string.departmentalOption);
            text.setText(examName);
        } else {
            examName = getString(R.string.weeklyGazetteHeading);
            text.setText(examName);
        }
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

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

//    private void saveResults() {
//        try {
//            FileOutputStream write = openFileOutput("resultarray", Context.MODE_PRIVATE);
//            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
//            arrayoutput.writeObject(resultArray);
//            arrayoutput.close();
//            write.close();
//
//        } catch (Exception e) {
//        }
//        try {
//            FileOutputStream write = openFileOutput("listurls", Context.MODE_PRIVATE);
//            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
//            arrayoutput.writeObject(listUrls);
//            arrayoutput.close();
//            write.close();
//        } catch (Exception e) {
//        }
//        try {
//            FileOutputStream write = openFileOutput("yeararray", Context.MODE_PRIVATE);
//            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
//            arrayoutput.writeObject(yearArray);
//            arrayoutput.close();
//            write.close();
//        } catch (Exception e) {
//        }
//        try {
//            FileOutputStream write = openFileOutput("yearurls", Context.MODE_PRIVATE);
//            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
//            arrayoutput.writeObject(yearUrls);
//            arrayoutput.close();
//            write.close();
//        } catch (Exception e) {
//        }
//    }
//
//    private void readResult() {
//        try {
//            FileInputStream read = openFileInput("resultarray");
//            ObjectInputStream readarray = new ObjectInputStream(read);
//            resultArray = (ArrayList<String>) readarray.readObject();
//            readarray.close();
//            read.close();
//        } catch (Exception e) {
//        }
//        try {
//            FileInputStream read = openFileInput("listurls");
//            ObjectInputStream readarray = new ObjectInputStream(read);
//            listUrls = (ArrayList<String>) readarray.readObject();
//            readarray.close();
//            read.close();
//        } catch (Exception e) {
//        }
//        try {
//            FileInputStream read = openFileInput("yeararray");
//            ObjectInputStream readarray = new ObjectInputStream(read);
//            yearArray = (ArrayList<String>) readarray.readObject();
//            readarray.close();
//            read.close();
//        } catch (Exception e) {
//        }
//        try {
//            FileInputStream read = openFileInput("yearurls");
//            ObjectInputStream readarray = new ObjectInputStream(read);
//            yearUrls = (ArrayList<String>) readarray.readObject();
//            readarray.close();
//            read.close();
//        } catch (Exception e) {
//        }
//        resultList.setAdapter(resultAdapter);
//        yearList.setAdapter(yearAdapter);
//    }
}