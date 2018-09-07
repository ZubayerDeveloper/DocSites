package zubayer.docsites.activity;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import zubayer.docsites.adapters.MyAdapter;
import zubayer.docsites.R;
import zubayer.docsites.adapters.ServiceAdapter;

public class WeeklyGazettes extends Activity {
    ArrayList<String> volumeArray,navigatinArray,gazetteArray,navigationUrls, volumeUrls,gazetteUrls;
    ListView volume,navigation,gazettelist;
    MyAdapter gazetteAdapter;
    ServiceAdapter navigationAdapter,volumeAdapter;
    AlertDialog checkinternet;
    AlertDialog.Builder builder;
    VolumrParser volumeParser;
    NavigationParser navigationParser;
    GazetteParser gazetteParser;
    View m;
    private AdView mAdView;
    String btxt, url, parentUrl,paramTagForText,paramTagForLink, paramLink;
    int i, textMin, textMax, linkBegin, linkEnd, aa,yearcounter,gazetteUrlPosition;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_gazettes);

        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        mAdView = (AdView) findViewById(R.id.adViewGazette);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        volume =(ListView)findViewById(R.id.volume);
        navigation =(ListView)findViewById(R.id.navigation);

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

        parentUrl = "http://www.dpp.gov.bd/bgpress/index.php/document/weekly_gazettes/151";

        executeVolume();

        volume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                parentUrl=volumeUrls.get(position);
                gazetteUrlPosition=position;
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
                Intent gazetteIntent=new Intent(WeeklyGazettes.this,Browser.class);
                gazetteIntent.putExtra("value",gazetteUrls.get(position));
                startActivity(gazetteIntent);
            }
        });
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
            for (i = begin; i < links.size()-3; i++) {
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
                    gazetteArray.add(getString(R.string.dialogueText));
                    gazetteUrls.add(url);
                }
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
        protected void onCancelled() {
            volumeArray.clear();
            volumeUrls.clear();
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if(!dataconnected()){
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                }catch (Exception e){}
                progressBar.setVisibility(View.GONE);
            }else if (btxt != null) {
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
                checkinternet.setMessage("Website is not responding");
                try {
                    checkinternet.show();
                }catch (Exception e){}
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
        protected void onCancelled() {
            navigatinArray.clear();
            navigationUrls.clear();
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if(!dataconnected()){
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });

                try {
                    checkinternet.show();
                }catch (Exception e){}
                progressBar.setVisibility(View.GONE);
            }else if (btxt != null) {
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
                checkinternet.setMessage("Website is not responding");
                try {
                    checkinternet.show();
                }catch (Exception e){}
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
        protected void onCancelled() {
            gazetteArray.clear();
            gazetteUrls.clear();
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Void b) {
            super.onPostExecute(b);
            if(!dataconnected()){
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setMessage("Check your network connection");
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });
                try {
                    checkinternet.show();
                }catch (Exception e){}
                progressBar.setVisibility(View.GONE);
            }else if (btxt != null) {
                gazettelist.setAdapter(gazetteAdapter);
                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                browser(gazetteUrls.get(0));
            } else {
                checkinternet = builder.create();
                checkinternet.setCancelable(false);
                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                });
                checkinternet.setMessage("Website is not responding");
                try {
                    checkinternet.show();
                }catch (Exception e){}
            }
        }
    }
    public  void executeVolume(){
        progressDialog = ProgressDialog.show(this, "", "Loading volumes...", true, true);
        volumeParser = new VolumrParser();
        paramTagForText = "#MyResult tr";
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
        progressDialog = ProgressDialog.show(this, "", "Loading Gazettes...", true, true);
    }

    private void browser(String inurl) {
        startActivity(new Intent(WeeklyGazettes.this, Browser.class).putExtra("value", inurl));
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

    private boolean dataconnected() {
        boolean dataConnected=false;
        boolean wifiIsAvailable,mobileDataIsAvailable;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            wifiIsAvailable = networkInfo.isConnected();
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            mobileDataIsAvailable = networkInfo.isConnected();
            dataConnected=wifiIsAvailable||mobileDataIsAvailable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataConnected;
    }
    }

