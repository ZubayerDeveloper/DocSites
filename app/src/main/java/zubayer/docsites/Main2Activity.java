package zubayer.docsites;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.Process;
import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Main2Activity extends Activity {
    ArrayList<String> yearArray,resultArray,monthArray,ministryArray,yearUrls,monthUrls,listUrls;
    ListView result;
    Spinner yearSpinner,monthSpinner;
    MyAdapter resultAdapter;
    GadgetteAdapter yearAdapter,monthAdapter;
    TextView text;
    AlertDialog checkinternet;
    AlertDialog.Builder builder;
    YearParser pareseYear;
    YearNextParser yearNextParser;
    MonthParser pareseMonth;
    ResultParser parseResult;
    View m;
    private AdView mAdView;
    String btxt, url,urlText, yearUrl,yearUrlNext,monthUrl,yearName,monthName,resultUrl, paramTagForText,
            paramTagForLink, paramLink,filterContent,message,examName,pdfFilter,driveViewer;
    int i, textMin, textMax, linkBegin, linkEnd, aa,resultMin,resulttMax,reslinkBegin,reslinkEnd,monthposition;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAdView = (AdView) findViewById(R.id.adViewGazette);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        yearSpinner =(Spinner) findViewById(R.id.gazetteYear);
        monthSpinner =(Spinner) findViewById(R.id.gazetteMonth);
        result=(ListView)findViewById(R.id.gadgettes);
        yearArray=new ArrayList<>();
        monthArray=new ArrayList<>();
        ministryArray=new ArrayList<>();
        resultArray=new ArrayList<>();
        yearUrls = new ArrayList<>();
        monthUrls=new ArrayList<>();
        listUrls=new ArrayList<>();
        text=(TextView)findViewById(R.id.heading);
        builder = new AlertDialog.Builder(Main2Activity.this);
        m = getLayoutInflater().inflate(R.layout.listview, null);
        yearAdapter =new GadgetteAdapter(Main2Activity.this,yearArray);
        monthAdapter=new GadgetteAdapter(Main2Activity.this,monthArray);
        resultAdapter=new MyAdapter(Main2Activity.this,resultArray,listUrls);
        driveViewer="https://docs.google.com/viewer?url=";
        yearUrl = "http://www.dpp.gov.bd/bgpress/bangla/index.php/document/extraordinary_gazettes/285";
        yearUrlNext=yearUrl+"/publication_date/12";
        executeYear();
        progressDialog = ProgressDialog.show(this, "", "Loading gazette years...", true, true);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                message=yearArray.get(position);
                progressDialog.dismiss();
                monthArray.clear();
                monthUrls.clear();
                resultArray.clear();
                listUrls.clear();
                result.setAdapter(resultAdapter);
                monthSpinner.setAdapter(monthAdapter);
                monthUrl =yearUrls.get(position);
                yearName=yearArray.get(position);
                progressDialog = ProgressDialog.show(Main2Activity.this, "", "Loading gazette months for "+yearName, true, true);
                executeMonth();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                resultArray.clear();
                listUrls.clear();
                result.setAdapter(resultAdapter);
                resultUrl=monthUrls.get(position);
                monthName=monthArray.get(position);
                progressDialog = ProgressDialog.show(Main2Activity.this, "", "Searching "+monthName+" for Gazettes...", true, true);
                executeResult();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        filterContent=getIntent().getExtras().getString("examname");
        if(filterContent.contains(getString(R.string.filterSeniorScale))){
            examName=getString(R.string.seniorScaleOption);
            text.setText(examName);
        }else if(filterContent.contains(getString(R.string.filterDepartmental))){
            examName=getString(R.string.departmentalOption);
            text.setText(examName);
        }else if(filterContent.contains(getString(R.string.service))){
            examName=getString(R.string.serviceConfirmOption);
            text.setText(examName);
        }

        result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    pdfFilter = listUrls.get(position);
                    browser(pdfFilter);
                }catch (Exception e){
                    checkinternet=builder.create();
                    checkinternet.setMessage("Something went wrong, try again");
                    checkinternet.show();
                }
            }
        });
    }
    public void yearExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                  int lBegin, int lEnd) {

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
    public void monthExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                   int lBegin, int lEnd) {

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
    public void resultExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                    int lBegin, int lEnd) {

        resultUrl = Url;
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
                if(btxt.contains(filterContent)){
                    resultArray.add(btxt);
                }
            }
            for (i = reslinkBegin; i < hrefs.size(); i++) {
                aa = i;
                Element li = hrefs.get(aa);
                url = li.attr(Attr);
                urlText=li.text();
                if(urlText.contains(filterContent)){
                    listUrls.add(url);
                }
            }
        } catch (Exception e) {
        }
    }
    class YearParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            yearExecutableTag(yearUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }
        @Override
        protected void onCancelled() {
            yearArray.clear();
            yearUrls.clear();
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                yearUrl=yearUrlNext;
                executeYearNext();
                btxt=null;
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        loadYearAgain();
                    }
                });
                checkinternet.setMessage("Check your network connection");
                checkinternet.show();
            }
        }
    }
    class YearNextParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            yearExecutableTag(yearUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }
        @Override
        protected void onCancelled() {
            yearArray.clear();
            yearUrls.clear();
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                yearSpinner.setAdapter(yearAdapter);
                progressDialog.dismiss();
                btxt=null;
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        loadYearAgain();
                    }
                });
                checkinternet.setMessage("Check your network connection");
                checkinternet.show();
            }
        }
    }
    class MonthParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            monthExecutableTag(monthUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }
        @Override
        protected void onCancelled() {
            monthArray.clear();
            monthUrls.clear();
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                progressDialog.dismiss();
                monthSpinner.setAdapter(monthAdapter);
                btxt=null;
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        loadMonthAgain();
                        checkinternet.dismiss();
                    }
                });
                checkinternet.setMessage("Check your network connection");
                checkinternet.show();
            }
        }
    }
    class ResultParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            resultExecutableTag(resultUrl, paramTagForText, paramTagForLink, paramLink, resultMin, resulttMax, reslinkBegin, reslinkEnd);
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
            if (btxt != null) {
                result.setAdapter(resultAdapter);
                progressDialog.dismiss();
                btxt=null;
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });
                checkinternet.setMessage("Check your network connection");
                checkinternet.show();
            }
            if(resultArray.size()==0){
                if (monthArray.isEmpty()) {
                    checkinternet = builder.create();
                    checkinternet.setCancelable(false);
                    checkinternet.setButton("Reload", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            yearUrls.clear();
                            yearArray.clear();
                            executeYear();
                        }
                    });
                    checkinternet.setButton3("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    checkinternet.setMessage("You need to reload again");
                    checkinternet.show();
                }else {
                    checkinternet = builder.create();
                    checkinternet.setMessage("No Gazette published on "+monthName);
                    checkinternet.setButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                        }
                    });
                    checkinternet.show();
                }
            }
            progressDialog.dismiss();
        }
    }
    public  void executeYear(){
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
    public  void executeYearNext(){
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
        pareseMonth = new MonthParser();
        paramTagForText = "#MyResult tr";
        paramTagForLink = "#MyResult tr a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
        textMax = 12;
        linkEnd = 12;
        pareseMonth.execute();
    }
    private void executeResult() {
        parseResult = new ResultParser();
        paramTagForText = "#MyResult tr";
        paramTagForLink = "#MyResult tr a";
        paramLink = "href";
        resultMin = 1;
        reslinkBegin = 0;
        parseResult.execute();
    }

    private void loadYearAgain() {
        if (yearArray.isEmpty()) {
            checkinternet = builder.create();
            checkinternet.setMessage("You need to reload again");
            checkinternet.setCancelable(false);
            checkinternet.setButton("Reload", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    progressDialog.dismiss();
                    yearUrls.clear();
                    yearArray.clear();
                    progressDialog = ProgressDialog.show(Main2Activity.this, "", "Loading gazette years...", true, true);
                    executeYear();
                }
            });
            checkinternet.setButton3("Exit", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    finish();
                }
            });

            checkinternet.show();
        }
    }
    private void loadMonthAgain() {
        if (monthArray.isEmpty()) {
            checkinternet = builder.create();
            checkinternet.setButton("Reload", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    progressDialog.dismiss();
                    progressDialog=ProgressDialog.show(Main2Activity.this,"","Loading gazette months...",true,true);
                    yearUrls.clear();
                    yearArray.clear();
                    executeMonth();
                    checkinternet.dismiss();
                }
            });
            checkinternet.setButton3("Exit", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    finish();
                }
            });
            checkinternet.setMessage("You need to reload again");
            checkinternet.show();
        }else {
            checkinternet.dismiss();
        }
    }

    public  void browser(String inurl){
        final String uurl=inurl;
        try {
            boolean isChecked;
            SharedPreferences settings = getSharedPreferences("setting", 0);
            isChecked=settings.getBoolean("checked",false);
            if (isChecked == false) {
                Intent intent = new Intent(Main2Activity.this, Browser.class);
                intent.putExtra("value", inurl);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(inurl));
                startActivity(intent);
            }
        }catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        try {
            pareseYear.cancel(true);
            pareseMonth.cancel(true);
            parseResult.cancel(true);
        }catch (Exception e){}
        super.onBackPressed();
    }


}