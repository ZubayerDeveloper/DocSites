package zubayer.docsites.activity;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    TextView text;
    AlertDialog checkinternet;
    AlertDialog.Builder builder;
    YearParser pareseYear;
    YearNextParser yearNextParser;
    MonthParser monthParser;
    ResultParser resultParser;
    View m;
    String btxt, url, urlText, yearUrl, yearUrlNext, monthUrl, yearName, monthName, resultUrl, paramTagForText,
            paramTagForLink, paramLink, filterContent, message, examName, pdfFilter, driveViewer;
    int i, textMin, textMax, linkBegin, linkEnd, aa, resultMin, resulttMax, reslinkBegin, reslinkEnd;
    ProgressDialog progressDialog;
    Iterator<String> it, name;
    boolean wifiAvailable, mobileDataAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazette);

        createAddView();
        setFont();
        initializeAll();
        filterExamContent();
        executeYear();

        progressDialog = ProgressDialog.show(this, "", "Loading list of gazette years...", true, true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Wait", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        if (yearArray.isEmpty()) {
                            progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Loading list of gazette years...", true, true);
                        }
                    }
                });
                checkinternet.setButton3("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });
                checkinternet.setMessage("List of gazette years loading is in progress...");
                try {
                    checkinternet.show();
                } catch (Exception e) {
                }

            }
        });
        resultList.setAdapter(resultAdapter);

        yearList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                monthParser.cancel(true);
                resultParser.cancel(true);
                message = yearArray.get(position);
                progressDialog.dismiss();
                monthUrls.clear();
                monthArray.clear();
                resultArray.clear();
                listUrls.clear();
                resultList.setAdapter(resultAdapter);
                monthUrl = yearUrls.get(position);
                yearName = yearArray.get(position);
                executeMonth();
            }
        });


        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    pdfFilter = listUrls.get(position);
                    browser(pdfFilter);
                } catch (Exception e) {
                    checkinternet = builder.create();
                    checkinternet.setMessage("Something went wrong, try again");
                    try {
                        checkinternet.show();
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }

    public void yearExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end) {

        yearUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            Elements hrefs = doc.select(tagForLink);
            for (i = begin; i < end; i++) {
                aa = i;
                Element link = links.get(aa);
                btxt = link.text();
                yearArray.add(btxt);
            }
            for (i = linkBegin; i < linkEnd; i++) {
                aa = i;
                Element li = hrefs.get(aa);
                url = li.attr(Attr);
                yearUrls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void monthExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end) {

        monthUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            Elements hrefs = doc.select(tagForLink);
            for (i = begin; i < links.size(); i++) {
                aa = i;
                Element link = links.get(aa);
                btxt = link.text();
                monthArray.add(btxt);
            }
            for (i = linkBegin; i < hrefs.size(); i++) {
                aa = i;
                Element li = hrefs.get(aa);
                url = li.attr(Attr);
                monthUrls.add(url);
            }
        } catch (Exception e) {
        }
    }

    public void resultExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end) {
        try {
            Url = it.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        resultMin = begin;
        resulttMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            Elements hrefs = doc.select(tagForLink);
            for (i = begin; i < links.size(); i++) {
                aa = i;
                Element link = links.get(aa);
                btxt = link.text();
                if (btxt.contains(filterContent)) {
                    resultArray.add(btxt);
                }
            }
            for (i = reslinkBegin; i < hrefs.size(); i++) {
                aa = i;
                Element li = hrefs.get(aa);
                url = li.attr(Attr);
                urlText = li.text();
                if (urlText.contains(filterContent)) {
                    listUrls.add(url);
                }
            }
        } catch (Exception e) {
        }
    }

    class YearParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            yearExecutableTag(yearUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax);
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
            yearExecutableTag(yearUrlNext, paramTagForText, paramTagForLink, paramLink, textMin, textMax);
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
                progressDialog.dismiss();
                resultArray.add(getString(R.string.text));
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

    class MonthParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            monthExecutableTag(monthUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE,"Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                        executeMonth();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE,"Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                progressDialog.dismiss();
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

    class ResultParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            resultExecutableTag(resultUrl, paramTagForText, paramTagForLink, paramLink, resultMin, resulttMax);
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
            if (!dataconnected()) {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE,"Try again", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                        executeResult();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE,"Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                    }
                });

                try {
                    checkinternet.show();
                } catch (Exception e) {
                }
            } else if (btxt != null) {
                resultList.setAdapter(resultAdapter);
                progressDialog.dismiss();
                btxt=null;
                if (it.hasNext()) {
                    checkConnectivity();
                } else {
                    if (resultArray.size() == 0) {
                        checkinternet = builder.create();
                        checkinternet.setMessage("No Gazette published on " + yearName);
                        checkinternet.setButton("Ok", new DialogInterface.OnClickListener() {
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
                checkinternet.setButton(DialogInterface.BUTTON_POSITIVE,"Reload", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                        executeResult();
                    }
                });
                checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE,"Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.dismiss();
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
        linkBegin = 0;
        textMax = 12;
        linkEnd = 12;
        pareseYear.execute();
    }

    public void executeYearNext() {
        yearUrlNext = yearUrl + "/publication_date/12";
        yearNextParser = new YearNextParser();
        paramTagForText = "#MyResult tr";
        paramTagForLink = "#MyResult tr a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        textMax = 12;
        linkEnd = 12;
        yearNextParser.execute();
    }

    private void executeMonth() {
        monthParser = new MonthParser();
        progressDialog = ProgressDialog.show(GazetteActivity.this, "", "This may take some time, please wait..", true, true);
        paramTagForText = "#MyResult tr";
        paramTagForLink = "#MyResult tr a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        textMax = 12;
        linkEnd = 12;
        monthParser.execute();
    }

    private void executeResult() {
        progressDialog.dismiss();
        try {
            monthName = name.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Searching " + monthName, true, true);
        resultParser = new ResultParser();
        paramTagForText = "#MyResult tr";
        paramTagForLink = "#MyResult tr a";
        paramLink = "href";
        resultMin = 1;
        reslinkBegin = 0;
        resultParser.execute();
    }

    private void loadYearAgain() {
        progressDialog.dismiss();
        yearUrls.clear();
        yearArray.clear();
        progressDialog = ProgressDialog.show(GazetteActivity.this, "", "Loading gazette years...", true, false);
        executeYear();
    }

    private void browser(String inurl) {
        startActivity(new Intent(GazetteActivity.this, Browser.class).putExtra("value", inurl));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            pareseYear.cancel(true);
            yearNextParser.cancel(true);
            monthParser.cancel(true);
            resultParser.cancel(true);
            yearAdapter = null;
            yearArray = null;
            yearList = null;
            yearUrls = null;
            monthArray = null;
            monthUrls = null;
            resultAdapter = null;
            finish();
        } catch (Exception e) {
        }
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
            checkinternet.setButton(DialogInterface.BUTTON_POSITIVE,"Reload", new DialogInterface.OnClickListener() {
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
            checkinternet.setButton(DialogInterface.BUTTON_NEGATIVE,"Close", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                }
            });
            checkinternet.setButton3("close", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    progressDialog.dismiss();
                }
            });

            try {
                checkinternet.show();
            } catch (Exception e) {
            }
            progressDialog.dismiss();
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
        yearArray = new ArrayList<>();
        ministryArray = new ArrayList<>();
        resultArray = new ArrayList<>();
        yearUrls = new ArrayList<>();
        monthArray = new ArrayList<>();
        monthUrls = new ArrayList<>();
        listUrls = new ArrayList<>();
        builder = new AlertDialog.Builder(GazetteActivity.this);
        m = getLayoutInflater().inflate(R.layout.listview, null);
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
        if (filterContent.contains(getString(R.string.filterSeniorScale))) {
            examName = getString(R.string.seniorScaleOption);
            text.setText(examName);
        } else if (filterContent.contains(getString(R.string.filterDepartmental))) {
            examName = getString(R.string.departmentalOption);
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
}