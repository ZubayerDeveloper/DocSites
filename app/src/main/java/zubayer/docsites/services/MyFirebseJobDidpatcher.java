package zubayer.docsites.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import zubayer.docsites.R;
import zubayer.docsites.activity.Browser;
import zubayer.docsites.activity.MainActivity;
import zubayer.docsites.activity.Settings;
import zubayer.docsites.adapters.NotificationListAdapter;

public class MyFirebseJobDidpatcher extends JobService {
    String btxt, url, paramUrl, paramTagForText, paramTagForLink, paramLink, previousSaved,
            filterContent, filterContent2, driveViewer, title, text, date;
    int textMin, linkBegin;
    SharedPreferences preferences;
    boolean checked, wifiAvailable, mobileDataAvailable;
    PendingIntent pendingIntent, pendingSetting;
    Intent myIntent, settingIntent;
    ArrayList<String> notificationHeadings = new ArrayList<>();
    ArrayList<String> notificationDates = new ArrayList<>();
    ArrayList<String> notificationTexts = new ArrayList<>();
    ArrayList<String> notificationUrls = new ArrayList<>();
    ArrayList<String> missedNotifications = new ArrayList<>();
    ArrayList<String> notificationSeens = new ArrayList<>();
    NotificationParser notificationParser;
    int n = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            checkConnectivity();
            readNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onStartJob(final JobParameters job) {

        notificationParser = new NotificationParser() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(job, true);
                try {
                    if (notificationTexts.size() != 0) {
//                        addToSymmery("", "", notificationDate(), "");
                        saveState();
                    }
                    clearArray();
                } catch (Exception e) {
                }
            }
        };
        notificationParser.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    public class NotificationParser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            driveViewer = "https://docs.google.com/viewer?url=";
            preferences = getSharedPreferences("notification", Context.MODE_PRIVATE);
            try {

                checked = preferences.getBoolean("residencyChecked", false);

                if (checked) {
                    bsmmuTag();
                    previousSaved = preferences.getString("residency", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("BSMMU:Residency/Non-Residency");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "BSMMU:Residency/Non-Residency", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 0, myIntent, 0);
                        notification("channel_0", "res", "Residency/Non-Residency", btxt, 0);
                        preferences.edit().putString("residency", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("noticeChecked", false);
                if (checked) {
                    bsmmuNotice();
                    executableTag();
                    previousSaved = preferences.getString("bsmmuNotice", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("M.phil, Diploma exam notice");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "BSMMU academic Notice", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 1, myIntent, 0);
                        notification("channel_1", "notice", "BSMMU academic Notice", btxt, 1);
                        preferences.edit().putString("bsmmuNotice", btxt).apply();

                    }
                    bsmmuNoticeAdministrative();
                    executableTag();
                    previousSaved = preferences.getString("bsmmuNoticeAdmin", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("Administrative notice");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "BSMMU Administrative Notice", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 199, myIntent, 0);
                        notification("channel_199", "notice", "BSMMU Administrative Notice", btxt, 199);
                        preferences.edit().putString("bsmmuNoticeAdmin", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("dghsChecked", false);
                if (checked) {
                    dghsHomeLinks();
                    executableTag();
                    previousSaved = preferences.getString("dghs", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("Notification from DGHS");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "DGHS", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 2, myIntent, 0);
                        notification("channel_2", "dghs", "New from DGHS", btxt, 2);
                        preferences.edit().putString("dghs", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("resultDeptChecked", false);
                if (checked) {
                    resultDept();
                    executableTag();
                    previousSaved = preferences.getString("dept", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("Notice and results for Departmental Exam");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "Departmental Exam", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 3, myIntent, 0);
                        notification("channel_3", "dept", "Departmental Exam Notice", btxt, 3);
                        preferences.edit().putString("dept", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("resultSeniorChecked", false);
                if (checked) {
                    resultSenior();
                    executableTag();
                    previousSaved = preferences.getString("senior", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("Notice and results for Senior Scale Exam");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "Senior Scale Exam", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 4, myIntent, 0);
                        notification("channel_4", "senior", "Senior Scale Exam Notice", btxt, 4);
                        preferences.edit().putString("senior", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("reultBcsChecked", false);
                if (checked) {
                    resultBCS();
                    executableTag();
                    previousSaved = preferences.getString("bcs", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("BCS Exam");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "BCS Exam", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 5, myIntent, 0);
                        notification("channel_5", "bcs", "BPSC:BCS Exam", btxt, 5);
                        preferences.edit().putString("bcs", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("regiDeptChecked", false);
                if (checked) {
                    regiDeptStarts();
                    executableTag();
                    if (btxt.length() == 0) {
                        addToMissedNotificaton("Departmental exam online registration");
                        saveMissedNotificationList();
                    } else if (btxt.contains("Section 1: Personal Details")) {
                        addToSymmery(getString(R.string.regideptStarted), "http://dept.bpsc.gov.bd/node/apply", "Departmental Exam", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 61, myIntent, 0);
                        notification("channel_61", "deptstarts", "Departmental Exam", getString(R.string.regideptStarted), 61);
                        preferences.edit().remove("regideptExpired").apply();

                    }
                    regiDeptExpire();
                    executableTag();
                    previousSaved = preferences.getString("deptExpired", null);
                    if (btxt.length() == 0) {
                        addToMissedNotificaton("Departmental exam online registration");
                        saveMissedNotificationList();
                    } else if (btxt.contains("expired")) {
                        if (btxt.equalsIgnoreCase(previousSaved)) {
                        } else {
                            addToSymmery(getString(R.string.regiExpired), "http://dept.bpsc.gov.bd/node/apply", "Departmental Exam", notificationDate());
                            saveState();
                            finalNotificationCount();
                            myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                            myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 6, myIntent, 0);
                            notification("channel_6", "deptexpires", "Departmental Exam", getString(R.string.regiExpired), 6);
                            preferences.edit().putString("deptExpired", btxt).apply();

                        }
                    }
                }
                checked = preferences.getBoolean("regiSeniorChecked", false);
                if (checked) {
                    regiSeniorStsrts();
                    executableTag();
                    if (btxt.length() == 0) {
                        addToMissedNotificaton("Senior scale exam online registration");
                        saveMissedNotificationList();
                    } else if (btxt.contains("Section 1: Personal Details")) {
                        addToSymmery(getString(R.string.regiseniortext), "http://snsc.bpsc.gov.bd/node/apply", "Senior Scale Exam", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 71, myIntent, 0);
                        notification("channel_71", "seniorstarts", "Senior Scale Exam", getString(R.string.regiseniortext), 71);

                        preferences.edit().remove("regiSeniorExpired").apply();

                    }
                    regiSeniorExpre();
                    executableTag();
                    previousSaved = preferences.getString("seniorExpired", null);
                    if (btxt.length() == 0) {
                        addToMissedNotificaton("Senior scale exam online registration");
                        saveMissedNotificationList();
                    } else if (btxt.contains("expired")) {
                        if (btxt.equalsIgnoreCase(previousSaved)) {
                        } else {
                            addToSymmery(getString(R.string.regiExpired), "http://snsc.bpsc.gov.bd/node/apply", "Senior Scale Exam", notificationDate());
                            saveState();
                            finalNotificationCount();
                            myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                            myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 7, myIntent, 0);
                            notification("channel_7", "seniorexires", "Senior Scale Exam", getString(R.string.regiExpired), 7);
                            preferences.edit().putString("seniorExpired", btxt).apply();

                        }
                    }
                }
                checked = preferences.getBoolean("assistantSurgeonChecked", false);
                if (checked) {
                    filterContent = getString(R.string.assistantSurgeon);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("assistantSurgeon", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.assistantSurgeonSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.assistantSurgeonSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 8, myIntent, 0);
                        notification("channel_8", "assistantSurgeon", getString(R.string.assistantSurgeonSetting), btxt, 8);
                        preferences.edit().putString("assistantSurgeon", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("juniorConsultantChecked", false);
                if (checked) {
                    filterContent = getString(R.string.juniorConsultant);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("juniorConsultant", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.juniorConsultantSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.juniorConsultantSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 9, myIntent, 0);
                        notification("channel_9", "juniorConsultant", getString(R.string.juniorConsultantSetting), btxt, 9);
                        preferences.edit().putString("juniorConsultant", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("seniorConsultantChecked", false);
                if (checked) {
                    filterContent = getString(R.string.seniorConsultant);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("seniorConsultant", null);


                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.seniorConsultantSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.seniorConsultantSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 10, myIntent, 0);
                        notification("channel_10", "seniorConsultant", getString(R.string.seniorConsultantSetting), btxt, 10);
                        preferences.edit().putString("seniorConsultant", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("assistantProfessorChecked", false);
                if (checked) {
                    filterContent = getString(R.string.assistantProfessor);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("assistantProfessor", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.assistantProfessorSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.assistantProfessorSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 11, myIntent, 0);
                        notification("channel_11", "assistantProfessor", getString(R.string.assistantProfessorSetting), btxt, 11);
                        preferences.edit().putString("assistantProfessor", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("associateProfessorChecked", false);
                if (checked) {
                    filterContent = getString(R.string.associateProfessor);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("associateProfessor", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.associateProfessorSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.associateProfessorSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 12, myIntent, 0);
                        notification("channel_12", "associateProfessor", getString(R.string.associateProfessorSetting), btxt, 12);
                        preferences.edit().putString("associateProfessor", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("professorChecked", false);
                if (checked) {
                    filterContent = getString(R.string.professor);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("professor", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.professorSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.professorSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 13, myIntent, 0);
                        notification("channel_13", "professor", getString(R.string.professorSetting), btxt, 13);
                        preferences.edit().putString("professor", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("civilSurgeonChecked", false);
                if (checked) {
                    filterContent = getString(R.string.civilSurgeon);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("civilSurgeon", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.civilSurgeonSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.civilSurgeonSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 14, myIntent, 0);
                        notification("channel_14", "civilSurgeon", getString(R.string.civilSurgeonSetting), btxt, 14);
                        preferences.edit().putString("civilSurgeon", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("adhocChecked", false);
                if (checked) {
                    filterContent = getString(R.string.adhoc);
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("adhoc", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.adhocSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.adhocSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 15, myIntent, 0);
                        notification("channel_15", "adhoc", getString(R.string.adhocSetting), btxt, 15);
                        preferences.edit().putString("adhoc", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("mohfwChecked", false);
                if (checked) {
                    filterContent = "Per";
                    filterContent2 = "aaaaaaa";
                    executeService();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("mohfw", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.mohfwSetting));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.mohfwSetting), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 16, myIntent, 0);
                        notification("channel_16", "mohfw", getString(R.string.mohfwSetting), btxt, 16);
                        preferences.edit().putString("mohfw", btxt).apply();

                    }
                }
                checked = preferences.getBoolean("deputationChecked", false);
                if (checked) {
                    filterContent = "ME-";
                    executeDeputation();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("deputation", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.deputationOrders));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.deputationOrders), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 17, myIntent, 0);
                        notification("channel_17", "deputation", getString(R.string.deputationOrders), btxt, 17);
                        preferences.edit().putString("deputation", btxt).apply();

                    }
                }

                checked = preferences.getBoolean("leaveChecked", false);
                if (checked) {
                    filterContent = "HR-";
                    executeLeave();
                    serviceConfirmTag();
                    previousSaved = preferences.getString("leave", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.leaveOpion));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.leaveOpion), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 18, myIntent, 0);
                        notification("channel_18", "leave", getString(R.string.leaveOpion), btxt, 18);
                        preferences.edit().putString("leave", btxt).apply();

                    }
                }

                checked = preferences.getBoolean("ccdChecked", false);

                if (checked) {
                    executeCCD1();
                    executableTag();
                    previousSaved = preferences.getString("ccd", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("CCD Notice");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "CCD Notice", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 19, myIntent, 0);
                        notification("channel_19", "ccd", "CCD Notice", btxt, 19);
                        preferences.edit().putString("ccd", btxt).apply();

                    }

                    executeCCD2();
                    executableTag();
                    previousSaved = preferences.getString("ccd2", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton("CCD Notice");
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, "CCD Notice", notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 20, myIntent, 0);
                        notification("channel_20", "ccd2", "CCD Notice", btxt, 20);
                        preferences.edit().putString("ccd2", btxt).apply();

                    }
                }

                checked = preferences.getBoolean("dgfpChecked", false);

                if (checked) {
                    dgfpOrder();
                    executableTag();
                    previousSaved = preferences.getString("dgfp1", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.dgfpOrderTitle));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.dgfpOrderTitle), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 21, myIntent, 0);
                        notification("channel_21", "dgfp1", getString(R.string.dgfpOrderTitle), btxt, 21);
                        preferences.edit().putString("dgfp1", btxt).apply();

                    }
                    dgfpNotice();
                    executableTag();
                    previousSaved = preferences.getString("dgfp2", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.dgfpNoticeTitle));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.dgfpNoticeTitle), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 22, myIntent, 0);
                        notification("channel_22", "dgfp2", getString(R.string.dgfpNoticeTitle), btxt, 22);
                        preferences.edit().putString("dgfp2", btxt).apply();

                    }
                    dgfpNOC();
                    executableTag();
                    previousSaved = preferences.getString("dgfp3", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {
                    } else if (btxt.length() == 0) {
                        addToMissedNotificaton(getString(R.string.dgfpNOCTitle));
                        saveMissedNotificationList();
                    } else {
                        addToSymmery(btxt, url, getString(R.string.dgfpNOCTitle), notificationDate());
                        saveState();
                        finalNotificationCount();
                        myIntent = new Intent(MyFirebseJobDidpatcher.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 23, myIntent, 0);
                        notification("channel_23", "dgfp3", getString(R.string.dgfpNOCTitle), btxt, 23);
                        preferences.edit().putString("dgfp3", btxt).apply();

                    }
                }

            } catch (Exception ignored) {
            }
            return null;
        }

    }

    private void bsmmuNotice() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "#tab4 h3";
        paramTagForLink = "h3 a";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void bsmmuNoticeAdministrative() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "#tab2 h3";
        paramTagForLink = "h3 a";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void dghsHomeLinks() {
        paramUrl = "http://dghs.gov.bd/index.php/bd/";
        paramTagForText = "#system a";
        paramTagForLink = "#system a";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void regiDeptExpire() {
        paramUrl = "http://dept.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void regiDeptStarts() {
        paramUrl = "http://dept.bpsc.gov.bd/node/apply";
        paramTagForText = "h6";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void regiSeniorExpre() {
        paramUrl = "http://snsc.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void regiSeniorStsrts() {
        paramUrl = "http://snsc.bpsc.gov.bd/node/apply";
        paramTagForText = "h6";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void resultBCS() {
        paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr a";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void resultDept() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void resultSenior() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void executeService() {
        paramUrl = "http://mohfw.gov.bd/index.php?option=com_content&view=article&id=111:bcs-health&catid=38:bcs-health&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void executeDeputation() {
        paramUrl = "http://www.mohfw.gov.bd/index.php?option=com_content&view=article&id=61%3Amedical-education&catid=46%3Amedical-education&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void executeLeave() {
        paramUrl = "http://mohfw.gov.bd/index.php?option=com_content&view=article&id=121%3Aearn-leave&catid=101%3Aearn-leave-ex-bangladesh-leave&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void executeCCD1() {
        paramUrl = "http://www.badas-dlp.org/";
        paramTagForText = "tr td a";
        paramLink = "abs:href";
        textMin = 9;
    }

    private void executeCCD2() {
        paramUrl = "http://www.badas-dlp.org/";
        paramTagForText = "tr td p";
        paramLink = "abs:href";
        textMin = 2;
    }

    private void dgfpOrder() {
        paramUrl = "http://dgfp.gov.bd/site/view/office_order/অফিস-আদেশ";
        paramTagForText = "tr";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void dgfpNotice() {
        paramUrl = "http://dgfp.gov.bd/site/view/notices/নোটিশ";
        paramTagForText = "tr";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void dgfpNOC() {
        paramUrl = "http://dgfp.gov.bd/site/view/publications/এনওসি /এনওসি";
        paramTagForText = "tr";
        paramLink = "abs:href";
        textMin = 1;
    }

    public void serviceConfirmTag() {
        try {
            btxt = "";
            url = "";
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            int textMax = links.size();
            for (int i = textMin; i <= textMax; i++) {
                Element link = links.get(i);
                if (link.text().contains(filterContent)) {
                    btxt = link.text();
                    url = link.select("a").attr(paramLink);
                    break;
                }

            }
        } catch (Exception e) {
        }
    }

    public void executableTag() {
        try {
            btxt = "";
            url = "";
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            Element link = links.get(textMin);
            btxt = link.text();
            url = links.get(textMin).select("a").attr(paramLink);

        } catch (Exception e) {
        }
    }

    private void bsmmuTag() {
        try {
            btxt = "";
            url = "";
            Document doc = Jsoup.connect("http://bsmmu.edu.bd/").get();
            Elements links = doc.select("a");
            linkBegin = 0;
            for (int i = linkBegin; i < links.size(); i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr("abs:href");
                if (btxt.contains("Residency/Non Res.")) {
                    linkBegin = i + 1;
                    break;
                }
            }
                Element link = links.get(linkBegin);
                btxt = link.text();
                url = link.select("a").attr("abs:href");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notification(String channel_id, String channel_name, String title, String text, int notify_id) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH);
            channel.shouldShowLights();
            channel.shouldVibrate();
            channel.canShowBadge();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = new NotificationCompat.Builder(MyFirebseJobDidpatcher.this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setColor(0xff990000)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{0, 300, 300, 300})
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setChannelId(channel_id).build();
            notification.ledARGB = 0xff990000;
            notification.ledOnMS = 500;
            notification.ledOffMS = 100;
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = null;
            if (pm != null) {
                wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "docsites:wakeLock");
            }
            if (wakeLock != null) {
                wakeLock.acquire(100);
            }
            notificationManager.notify(notify_id, notification);
        } else {
            notification2(title, text, notify_id);
        }
    }

    private void notification2(String title, String text, int id) {
        NotificationCompat.BigTextStyle bigTextStyle;
        bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(text);

        settingIntent = new Intent(this, Settings.class);
        settingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingSetting = PendingIntent.getActivity(this, 1, settingIntent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .addAction(0, "Turn off notification", pendingSetting)
                .setColor(0xff990000)
                .setVibrate(new long[]{0, 300, 300, 300})
                .setLights(Color.GREEN, 1000, 1000)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setStyle(bigTextStyle)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground));
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "docsites:wakeLock");
        }
        if (wakeLock != null) {
            wakeLock.acquire(100);
        }
        notificationManager.notify(id, notificationBuilder.build());
    }

    public void checkConnectivity() {
        preferences = getSharedPreferences("connectivity", 0);
        checked = preferences.getBoolean("connectivityChecked", false);
        if (checked) {
            title = "Turn on Data";
            text = getString(R.string.noData);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            wifiAvailable = networkInfo.isConnected();
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            mobileDataAvailable = networkInfo.isConnected();
            if (!wifiAvailable && !mobileDataAvailable) {
                myIntent = new Intent(MyFirebseJobDidpatcher.this, MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(MyFirebseJobDidpatcher.this, 112, myIntent, 0);
                notification("nodata", "dataNotFound", title, text, 113);
            }
        }
    }

    private void saveState() {
        try {
            FileOutputStream write = openFileOutput("notificationHeading", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationHeadings);
            arrayoutput.close();
            write.close();

        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationDate", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationDates);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationText", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationTexts);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationUrl", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationUrls);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationColor", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationSeens);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
    }

    private void readNotification() {
        try {
            FileInputStream read = openFileInput("notificationHeading");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationHeadings = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
        try {
            FileInputStream read = openFileInput("notificationDate");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationDates = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
        try {
            FileInputStream read = openFileInput("notificationText");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationTexts = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
        try {
            FileInputStream read = openFileInput("notificationUrl");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationUrls = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
        try {
            FileInputStream read = openFileInput("notificationColor");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationSeens = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    private String notificationDate() {
        long mydate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy h:mm a");
        date = sdf.format(mydate);
        return date;
    }

    private void addToSymmery(String notifiactionText, String notificationUrl,
                              String notificationHeading, String notificationDate) {
        notificationTexts.add(0, notifiactionText);
        notificationUrls.add(0, notificationUrl);
        notificationDates.add(0, notificationDate);
        notificationHeadings.add(0, notificationHeading);
        notificationSeens.add(0, "#F7EDD0");
    }

    private void clearArray() {
        notificationTexts.clear();
        notificationUrls.clear();
        notificationDates.clear();
        notificationHeadings.clear();
        notificationSeens.clear();
    }

    private void addToMissedNotificaton(String missedNotification) {
        missedNotifications.add(0, missedNotification);
    }

    private void saveMissedNotificationList() {
        try {
            FileOutputStream write = openFileOutput("missed", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(missedNotifications);
            arrayoutput.close();
            write.close();

        } catch (Exception e) {
        }
    }

    private void finalNotificationCount() {
        n++;
        SharedPreferences oldsize = getSharedPreferences("finalNotificationCount", Context.MODE_PRIVATE);
        oldsize.edit().putInt("finalsize", n).apply();
    }
}
