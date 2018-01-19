package zubayer.docsites;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ServiceConfirmation extends Activity {
    ArrayList<String> yearArray, volumeArray,navigatinArray,gazetteArray,yearUrls,navigationUrls, volumeUrls,gazetteUrls;
    ListView volume,navigation,gazettelist;
    MyAdapter gazetteAdapter;
    ServiceAdapter navigationAdapter,volumeAdapter;
    TextView text;
    AlertDialog Dialog,checkinternet;
    AlertDialog.Builder builder;
    VolumrParser volumeParser;
    NavigationParser navigationParser;
    GazetteParser gazetteParser;
    View m;
    EditText gazetteEdit;
    Button search;
    private AdView mAdView;
    String btxt, url, parentUrl,customUrl,yearName,monthName,resultUrl, paramTagForText,
            paramTagForLink, paramLink,filterContent;
    int i, textMin, textMax, linkBegin, linkEnd, aa,resultMin,resulttMax,reslinkBegin,reslinkEnd,monthposition,yearcounter;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_confirmation);

        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        mAdView = (AdView) findViewById(R.id.adViewGazette);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        volume =(ListView)findViewById(R.id.volume);
        navigation =(ListView)findViewById(R.id.navigation);
//        gazetteEdit =(EditText)findViewById(R.id.gazetteEdit);
//        search=(Button)findViewById(R.id.search);

        volumeArray =new ArrayList<>();
        volumeUrls =new ArrayList<>();
        navigatinArray =new ArrayList<>();
        navigationUrls =new ArrayList<>();
        gazetteArray =new ArrayList<>();
        gazetteUrls =new ArrayList<>();

        yearcounter=0;
        m = getLayoutInflater().inflate(R.layout.listview, null);
        gazettelist=(ListView)m.findViewById(R.id.ListView);
        progressBar=(ProgressBar)m.findViewById(R.id.progressBar);

        volumeAdapter =new ServiceAdapter(this, volumeArray, volumeUrls);
        navigationAdapter =new ServiceAdapter(this, navigatinArray, navigationUrls);
        gazetteAdapter=new MyAdapter(this, gazetteArray, gazetteUrls);

        builder = new AlertDialog.Builder(this);
        Dialog = builder.create();
        Dialog.setCancelable(false);
        Dialog.setButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                gazetteArray.clear();
                gazetteUrls.clear();
                try{
                    gazetteParser.cancel(true);
                }catch (Exception e){}
            }
        });
        Dialog.setView(m);
        parentUrl = "http://www.dpp.gov.bd/bgpress/index.php/document/weekly_gazettes/151/publication_date/";
        executeVolume();

        volume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                parentUrl=volumeUrls.get(position);
                executeGazette();
            }
        });
        navigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                volumeUrls.clear();
                volumeArray.clear();
                volume.setAdapter(volumeAdapter);
                parentUrl =navigationUrls.get(position);
                executeVolume();
                navigatinArray.clear();
                navigationUrls.clear();
                navigation.setAdapter(navigationAdapter);
            }
        });
        gazettelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent gazetteIntent=new Intent(ServiceConfirmation.this,Browser.class);
                gazetteIntent.putExtra("value",gazetteUrls.get(position));
                startActivity(gazetteIntent);
            }
        });
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String year=gazetteEdit.getText().toString();
//                executeVolume();
//                while (btxt.contains(year)==false){
//                    yearcounter=yearcounter+10;
//                    String add=Integer.toString(yearcounter);
//                    customUrl=parentUrl+add;
//                    parentUrl=customUrl;
//                }
//                executeVolume();
//            }
//        });
    }
    public void volumeExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                    int lBegin, int lEnd) {

        parentUrl = Url;
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
                volumeArray.add("\n"+btxt+"\n");
            }
            for (i = linkBegin; i < hrefs.size(); i++) {
                aa = i;
                Element li = hrefs.get(aa);
                url = li.attr(Attr);
                volumeUrls.add(url);
            }
        } catch (Exception e) {
        }
    }
    public void navigationExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                    int lBegin, int lEnd) {

        parentUrl = Url;
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
                navigatinArray.add(btxt);
            }
            for (i = linkBegin; i < hrefs.size(); i++) {
                aa = i;
                Element li = hrefs.get(aa);
                url = li.attr(Attr);
                navigationUrls.add(url);
            }
        } catch (Exception e) {
        }
    }
    public void gazetteExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                        int lBegin, int lEnd) {

        parentUrl = Url;
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
                Element li = hrefs.get(aa);
                btxt = link.text();
                url = li.attr(Attr);
                if(btxt.contains(getString(R.string.filterGazette))) {
                    gazetteArray.add(btxt);
                    gazetteUrls.add(url);
                }
            }
        } catch (Exception e) {
        }
    }
    public void speedExecutableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end,
                                    int lBegin, int lEnd) {

        parentUrl = Url;
        paramTagForLink = tagForLink;
        paramTagForText = TagForText;
        paramLink = Attr;
        textMin = begin;
        textMax = end;
        try {
            Document doc = Jsoup.connect(Url).get();
            Elements links = doc.select(TagForText);
            for (i = begin; i < links.size(); i++) {
                Element link = links.get(0);
                btxt = link.text();
            }
        } catch (Exception e) {
        }
    }

    class VolumrParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            volumeExecutableTag(parentUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                volume.setAdapter(volumeAdapter);
                progressDialog.dismiss();
                executeNavigation();
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
        }
    }
    class NavigationParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            navigationExecutableTag(parentUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                navigation.setAdapter(navigationAdapter);
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        finish();
                    }
                });
                checkinternet.setMessage("Check your network connection navigation");
                checkinternet.show();
            }
        }
    }
    class GazetteParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            gazetteExecutableTag(parentUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                gazettelist.setAdapter(gazetteAdapter);
                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        Dialog.dismiss();
                    }
                });
                checkinternet.setMessage("Check your network connection navigation");
                checkinternet.show();
            }
        }
    }
    class SpeedSearcher extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            volumeExecutableTag(parentUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if (btxt != null) {
                volume.setAdapter(volumeAdapter);
                progressDialog.dismiss();
                executeVolume();
            } else {
                progressDialog.dismiss();
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
        }
    }
    public  void executeVolume(){
        progressDialog = ProgressDialog.show(this, "", "Loading volumes...", true, true);
        volumeParser = new VolumrParser();
        paramTagForText = "#MyResult tr text.gazette_date";
        paramTagForLink = "#MyResult tr a";
        paramLink = "href";
        textMin = 0;
        linkBegin = 0;
        textMax = 10;
        linkEnd = 10;
        volumeParser.execute();
    }
    public  void executeNavigation(){
        progressDialog = ProgressDialog.show(this, "", "Loading navigation...", true, true);
        navigationParser = new NavigationParser();
        paramTagForText = "#pagination-bar a";
        paramTagForLink = "#pagination-bar a";
        paramLink = "href";
        textMin = 0;
        linkBegin = 0;
        textMax = 7;
        linkEnd = 7;
        navigationParser.execute();
    }
    public  void executeGazette(){
        gazetteParser = new GazetteParser();
        paramTagForText = "tr a";
        paramTagForLink = "tr a";
        paramLink = "href";
        textMin = 0;
        linkBegin = 0;
        textMax = 10;
        linkEnd = 10;
        gazetteParser.execute();
        Dialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }
    public  void speedSearch(){
        progressDialog = ProgressDialog.show(this, "", "Searching "+yearName+"...", true, true);
        volumeParser = new VolumrParser();
        paramTagForText = "#MyResult tr text.gazette_date";
        paramTagForLink = "#MyResult tr a";
        paramLink = "href";
        textMin = 0;
        linkBegin = 0;
        textMax = 10;
        linkEnd = 10;
        volumeParser.execute();
    }

    public  void browser(String inurl){
        final String uurl=inurl;
        try {
            boolean isChecked;
            SharedPreferences settings = getSharedPreferences("setting", 0);
            isChecked=settings.getBoolean("checked",false);
            if (isChecked == false) {
                Intent intent = new Intent(ServiceConfirmation.this, Browser.class);
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

        try{
            volumeParser.cancel(true);
            navigationParser.cancel(true);
            gazetteParser.cancel(true);


        }catch (Exception e){}
        super.onBackPressed();
    }
    private void loadYearAgain() {
        if (yearArray.isEmpty()) {
            checkinternet = builder.create();
            checkinternet.setMessage("You need to reload again");
            checkinternet.setCancelable(false);
            checkinternet.setButton("Reload", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    yearUrls.clear();
                    yearArray.clear();
                    progressDialog = ProgressDialog.show(ServiceConfirmation.this, "", "Loading gazette years...", true, true);
                    executeVolume();
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
    }

