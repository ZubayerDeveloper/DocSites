package zubayer.docsites.activity;

import android.app.*;
import android.graphics.Color;
import android.os.*;
import android.content.*;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.internal.NavigationMenu;
import android.webkit.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import android.app.DownloadManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import zubayer.docsites.adapters.MyAdapter;
import zubayer.docsites.R;

import static android.widget.Toast.makeText;


public class Browser extends Activity {
    String urls;
    WebView website;
    ProgressBar progressbar;
    AlertDialog checkinternet;
    String driveViewer, pdfurl;
    AlertDialog Dialog;
    AlertDialog.Builder builder;
    SharedPreferences preferences;
    FabSpeedDial fab;
    ListView list;
    MyAdapter adapter;
    ProgressBar progressBar;
    View m;
    ArrayList<String> buttonTexts, urlss;
    Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        buttonTexts = new ArrayList<>();
        buttonTexts.add(getString(R.string.browseralert));
        urlss = new ArrayList<>();
        downloadButton = (Button) findViewById(R.id.downloadButton);

        setAdd();
        setProgressBar();
        initializeWebViewAndUrls();
        setFloatingActinButton();
        setListView();
        buildAlertDialogue();
        reloadAdviceDialogue();
        webViewSettings();
        filterUrlPDF();

        website.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("pdf")) {
                    Intent intent = new Intent(Browser.this, Browser.class);
                    intent.putExtra("value", url);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Browser.this, Browser.class);
                    intent.putExtra("value", url);
                    startActivity(intent);
                }
                return true;
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
                try {
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if (dm != null) {
                        dm.enqueue(request);
                    }
                } catch (Exception e) {
                    try {
                        Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intentNew);
                    } catch (ActivityNotFoundException eee) {
                        builder = new AlertDialog.Builder(Browser.this);
                        checkinternet = builder.create();
                        checkinternet.setCancelable(true);
                        checkinternet.setMessage("You need to download Google Chrome");
                        checkinternet.setButton("Download", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome"));
                                startActivity(i);
                            }
                        });
                        checkinternet.setButton3("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {

                            }
                        });
                        checkinternet.show();
                    }
                }
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 23) {
                    try {
                        Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                        startActivity(intentNew);
                    } catch (ActivityNotFoundException e) {
                        builder = new AlertDialog.Builder(Browser.this);
                        checkinternet = builder.create();
                        checkinternet.setCancelable(true);
                        checkinternet.setMessage("You need to download Google Chrome");
                        checkinternet.setButton("Download", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome"));
                                startActivity(i);
                            }
                        });
                        checkinternet.setButton3("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {

                            }
                        });
                        checkinternet.show();
                    }
                } else {
                    website.loadUrl(urls);
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

    private void setFloatingActinButton() {
        fab = (FabSpeedDial) findViewById(R.id.fabweb);
        fab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.reload:
                        website.reload();
                        loadProgressBar();
                        break;
                    case R.id.copy:
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("url", website.getUrl());
                        if (clipboardManager != null) {
                            clipboardManager.setPrimaryClip(clipData);
                        }
                        myToaster("Link copied");
                        break;
                    case R.id.externalBrowser:
                        try {
                            Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                            startActivity(intentNew);
                        } catch (ActivityNotFoundException e) {
                            builder = new AlertDialog.Builder(Browser.this);
                            checkinternet = builder.create();
                            checkinternet.setCancelable(true);
                            checkinternet.setMessage("You need to download Google Chrome");
                            checkinternet.setButton("Download", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome"));
                                    startActivity(i);
                                }
                            });
                            checkinternet.setButton3("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {

                                }
                            });
                            checkinternet.show();
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    private void webViewSettings() {
        website.getSettings().setUseWideViewPort(true);
        website.getSettings().setDatabaseEnabled(true);
        website.getSettings().setDomStorageEnabled(true);
        website.getSettings().setLightTouchEnabled(true);
        website.getSettings().setLoadsImagesAutomatically(true);
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
        website.setVerticalScrollBarEnabled(false);
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

    private void reloadAdviceDialogue() {
        preferences = getSharedPreferences("reload", 0);
        if (!preferences.getBoolean("got it", false)) {
            Dialog.show();
            preferences = getSharedPreferences("reload", 0);
            preferences.edit().putBoolean("got it", true).apply();
        }
    }

    private void myToaster(String text) {
        Toast toast = makeText(Browser.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setAdd() {
        AdView mAdView = (AdView) findViewById(R.id.adViewBrowser);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void filterUrlPDF() {
        if (urls.contains("pdf")) {
            downloadButton.setVisibility(View.VISIBLE);
            website.loadUrl(driveViewer + urls);
            loadProgressBar();
        } else if (urls.contains("download")) {
            if (Build.VERSION.SDK_INT >23) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(Color.parseColor("#305168"));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(urls));
            } else {
                website.loadUrl(urls);
                loadProgressBar();
            }
        } else {
            downloadButton.setVisibility(View.GONE);
            website.loadUrl(urls);
            loadProgressBar();
        }
    }

    private void setProgressBar() {
        progressbar = (ProgressBar) findViewById(R.id.progress);
        progressbar.setProgress(0);
    }

    private void initializeWebViewAndUrls() {
        website = (WebView) findViewById(R.id.WebView);
        driveViewer = "https://docs.google.com/viewer?url=";
        pdfurl = "https://docs.google.com/gview?embedded=true&url=";
        urls = getIntent().getExtras().getString("value");
    }

    private void buildAlertDialogue() {
        builder = new AlertDialog.Builder(this);
        Dialog = builder.create();
        Dialog.setButton("Got it", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {

            }
        });
        Dialog.setView(m);
    }

    private void setListView() {
        adapter = new MyAdapter(Browser.this, buttonTexts, urlss);
        m = getLayoutInflater().inflate(R.layout.listview, null);
        list = (ListView) m.findViewById(R.id.ListView);
        list.setAdapter(adapter);
        progressBar = (ProgressBar) m.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }
}
