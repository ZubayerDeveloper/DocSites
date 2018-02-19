package zubayer.docsites;

import android.app.*;
import android.os.*;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.webkit.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import java.util.zip.*;
import android.app.DownloadManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import me.anwarshahriar.calligrapher.Calligrapher;


public class Browser extends Activity {
    String urls;
    WebView website;
    ProgressBar progressbar;
    MenuItem item2;
    private AdView mAdView;
    String driveViewer,pdfurl;
    ImageButton download;
    Button reload;
    AlertDialog checkinternet;
    AlertDialog.Builder builder;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        mAdView = (AdView) findViewById(R.id.adViewBrowser);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        progressbar = (ProgressBar) findViewById(R.id.progress);
        progressbar.setProgress(0);
        website = (WebView) findViewById(R.id.WebView);
        driveViewer="https://docs.google.com/viewer?url=";
        pdfurl="https://docs.google.com/gview?embedded=true&url=";
        urls=getIntent().getExtras().getString("value");
        website.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url ) {
               if(url.contains("pdf")) {
                   Intent intent = new Intent(Browser.this, Browser.class);
                   intent.putExtra("value", url);
                   startActivity(intent);
               } else  {
                   Intent intent = new Intent(Browser.this, Browser.class);
                   intent.putExtra("value", url);
                   startActivity(intent);
               }
                return true;
            }
        });
        builder=new AlertDialog.Builder(this);
        checkinternet=builder.create();
        checkinternet.setMessage(getString(R.string.browseralert));
        checkinternet.setCancelable(false);
        checkinternet.setButton("Got it", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                preferences=getSharedPreferences("reload", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("gotit", true).apply();
            }
        });
        preferences=getSharedPreferences("reload", 0);
        if(preferences.getBoolean("gotit",false)){

        }else {
            checkinternet.show();
        }
        website.getSettings().setUseWideViewPort(true);
        website.getSettings().setDatabaseEnabled(true);
        website.getSettings().setDomStorageEnabled(true);
        //website.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        website.getSettings().setLightTouchEnabled(true);
        website.getSettings().setLoadsImagesAutomatically(true);
//        website.getSettings().setMediaPlaybackRequiresUserGesture(true);
        website.getSettings().setPluginState(WebSettings.PluginState.ON);
        website.getSettings().setSaveFormData(true);
        website.getSettings().setSavePassword(true);
        website.getSettings().setJavaScriptEnabled(true);
        website.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        website.getSettings().setLoadWithOverviewMode(true);
        website.getSettings().setAllowContentAccess(true);
        website.getSettings().setAllowFileAccess(true);
        website.getSettings().setAllowFileAccessFromFileURLs(true);
        website.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        website.getSettings().setAllowUniversalAccessFromFileURLs(true);
        website.getSettings().setAppCacheEnabled(true);
        website.getSettings().setBuiltInZoomControls(true);
        website.getSettings().setSupportMultipleWindows(false);

        download=(ImageButton)findViewById(R.id.download);
        reload=(Button)findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                website.reload();
                loadProgressBar();
            }
        });
        website.setVerticalScrollBarEnabled(false);

        if(urls.contains("pdf")){
            download.setVisibility(View.VISIBLE);
            website.loadUrl(driveViewer+urls);
            loadProgressBar();
        }else {
            download.setVisibility(View.GONE);
            website.loadUrl(urls);
            loadProgressBar();
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                website.loadUrl(urls);
                loadProgressBar();
            }
        });

        website.setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if (dm != null) {
                    dm.enqueue(request);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (website.canGoBack()) {
            website.goBack();
            progressbar.setProgress(0);
            loadProgressBar();
        } else {
            finish();
            super.onBackPressed();
        }
    }

    public void loadProgressBar() {
        website.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressbar.setMax(100);
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(progress);
                if (progress == 100) {
                    progressbar.setVisibility(View.GONE);

                }
                super.onProgressChanged(view, progress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit:
                Intent intent=new Intent(this,CardView.class);
                startActivity(intent);
               finish();
                break;
            case R.id.stop:
                website.stopLoading();
                progressbar.setVisibility(View.GONE);
                break;
            case R.id.reload:
                website.reload();
                loadProgressBar();
                break;
            case R.id.copy:
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("url",website.getUrl());
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(clipData);
                }
                break;
            case R.id.externalBrowser:
                Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                startActivity(intentNew);
                break;
        }
        return  true;
    }
}
