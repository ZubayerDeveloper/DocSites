package zubayer.docsites;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

public class MyIntentService extends IntentService {
    String btxt, url, paramUrl, paramTagForText, paramTagForLink, paramLink, previousSaved, previousSaved2, driveViewer, filterContent, filterContent2;
    int textMin, linkBegin;
    NotificationCompat.BigTextStyle bigTextStyle;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;
    PendingIntent pendingIntent, pendingSetting;
    Intent myIntent, settingIntent, summeryIntent;
    SharedPreferences preferences, summeryPreference;

    public MyIntentService() {
        super("MyService");
    }

    boolean checked, enableSound, enableVibrate, wifiAvailable, mobileDataAvailable;
    ArrayList<String> buttonTexts = new ArrayList<>();
    ArrayList<String> buttonTexts2 = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> urls2 = new ArrayList<>();
    ArrayList<String> notificationHeadings = new ArrayList<>();
    ArrayList<String> notificationDates = new ArrayList<>();
    ArrayList<String> notificationTexts = new ArrayList<>();
    ArrayList<String> notificationUrls = new ArrayList<>();
    ArrayList<String> missedNotifications = new ArrayList<>();
    String title, text, date;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            checkConnectivity();
            readHeading();
            readDate();
            readText();
            readUrl();
            SharedPreferences oldsize = getSharedPreferences("oldNotificationCount", Context.MODE_PRIVATE);
            oldsize.edit().putInt("oldsize", notificationUrls.size()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        driveViewer = "https://docs.google.com/viewer?url=";

        try {
            preferences = getSharedPreferences("notificationSounds", 0);
            enableSound = preferences.getBoolean("notificationSoundChecked", false);

            preferences = getSharedPreferences("vibrations", 0);
            enableVibrate = preferences.getBoolean("vibrationChecked", false);

            preferences = getSharedPreferences("residencySetting", 0);
            checked = preferences.getBoolean("residencyChecked", false);

            summeryPreference = getSharedPreferences("forSummery", 0);

            if (checked) {
                residency();
                executableTag();
                preferences = getSharedPreferences("residency", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("residency", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton("BSMMU:Residency/Non-Residency");
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, "BSMMU:Residency/Non-Residency", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
                    bigTextStyleNotification("BSMMU:Residency/Non-Residency", btxt);
                    notification("BSMMU:Residency/Non-Residency", btxt, 0);
                    preferences.edit().putString("residency", btxt).apply();

                }
            }
            preferences = getSharedPreferences("noticeSetting", 0);
            checked = preferences.getBoolean("noticeChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                bsmmuNotice();
                executableTag();
                preferences = getSharedPreferences("bsmmuNotice", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("bsmmuNotice", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton("M.phil, Diploma exam notice");
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, "BSMMU Notice", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 1, myIntent, 0);
                    bigTextStyleNotification("BSMMU Notice", btxt);
                    notification("BSMMU Notice", btxt, 1);
                    preferences.edit().putString("bsmmuNotice", btxt).apply();

                }
            }
            preferences = getSharedPreferences("dghsSetting", 0);
            checked = preferences.getBoolean("dghsChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                dghsHomeLinks();
                executableTag();
                preferences = getSharedPreferences("dghs", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("dghs", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("3",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton("Notification from DGHS");
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, "DGHS", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 2, myIntent, 0);
                    bigTextStyleNotification("New from DGHS", btxt);
                    notification("New from DGHS", btxt, 2);
                    preferences.edit().putString("dghs", btxt).apply();

                }
            }
            preferences = getSharedPreferences("resultDeptSetting", 0);
            checked = preferences.getBoolean("resultDeptChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                resultDept();
                executableTag();
                preferences = getSharedPreferences("dept", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("dept", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("4",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton("Notice and results for Departmental Exam");
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, "Departmental Exam", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 3, myIntent, 0);
                    bigTextStyleNotification("BPSC:Departmental Exam", btxt);
                    notification("BPSC:Departmental Exam", btxt, 3);
                    preferences.edit().putString("dept", btxt).apply();

                }
            }
            preferences = getSharedPreferences("resultSeniorSetting", 0);
            checked = preferences.getBoolean("resultSeniorChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                resultSenior();
                executableTag();
                preferences = getSharedPreferences("senior", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("senior", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("5",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton("Notice and results for Senior Scale Exam");
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, "Senior Scale Exam", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 4, myIntent, 0);
                    bigTextStyleNotification("BPSC:Senior Scale Exam", btxt);
                    notification("BPSC:Senior Scale Exam", btxt, 4);
                    preferences.edit().putString("senior", btxt).apply();

                }
            }
            preferences = getSharedPreferences("reultBcsSetting", 0);
            checked = preferences.getBoolean("reultBcsChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                resultBCS();
                executableTag();
                preferences = getSharedPreferences("bcs", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("bcs", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("6",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton("BCS Exam");
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, "BCS Exam", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 5, myIntent, 0);
                    bigTextStyleNotification("BPSC:BCS Exam", btxt);
                    notification("BPSC:BCS Exam", btxt, 5);
                    preferences.edit().putString("bcs", btxt).apply();

                }
            }
            preferences = getSharedPreferences("regiDeptSetting", 0);
            checked = preferences.getBoolean("regiDeptChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                regiDeptStarts();
                executableTag();
                if (btxt.length() == 0) {
                    addToMissedNotificaton("Departmental exam online registration");
                    saveMissedNotificationList();
                } else if (btxt.contains("Section 1: Personal Details")) {
                    addToSymmery(getString(R.string.regideptStarted), "http://dept.bpsc.gov.bd/node/apply", "Departmental Exam", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 61, myIntent, 0);
                    bigTextStyleNotification("Departmental Exam", getString(R.string.regideptStarted));
                    notification("Departmental Exam", getString(R.string.regideptStarted), 61);

                    preferences = getSharedPreferences("regideptExpired", Context.MODE_PRIVATE);
                    preferences.edit().putString("deptExpired", "").apply();

                }
                btxt = "";
                url = "";
                regiDeptExpire();
                executableTag();
                preferences = getSharedPreferences("regideptExpired", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("deptExpired", null);
                if (btxt.length() == 0) {
                    addToMissedNotificaton("Departmental exam online registration");
                    saveMissedNotificationList();
                } else if (btxt.contains("expired")) {
                    if (btxt.equalsIgnoreCase(previousSaved) || btxt.length() == 0) {
                    } else {
                        addToSymmery(getString(R.string.regiExpired), "http://dept.bpsc.gov.bd/node/apply", "Departmental Exam", notificationDate());
                        saveState();
                        fianlNotificationCount();
                        myIntent = new Intent(this, Browser.class);
                        myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(this, 6, myIntent, 0);
                        bigTextStyleNotification("Departmental Exam", btxt);
                        notification("Departmental Exam", getString(R.string.regiExpired), 6);
                        preferences.edit().putString("deptExpired", btxt).apply();

                    }
                }
            }
            preferences = getSharedPreferences("regiSeniorSetting", 0);
            checked = preferences.getBoolean("regiSeniorChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                regiSeniorStsrts();
                executableTag();
                if (btxt.length() == 0) {
                    addToMissedNotificaton("Senior scale exam online registration");
                    saveMissedNotificationList();
                } else if (btxt.contains("Section 1: Personal Details")) {
                    addToSymmery(getString(R.string.regiseniortext), "http://snsc.bpsc.gov.bd/node/apply", "Senior Scale Exam", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 71, myIntent, 0);
                    bigTextStyleNotification("Senior Scale Exam", getString(R.string.regiseniortext));
                    notification("Senior Scale Exam", getString(R.string.regiseniortext), 71);

                    preferences = getSharedPreferences("regiSeniorExpired", Context.MODE_PRIVATE);
                    preferences.edit().putString("seniorExpired", "").apply();

                }
                btxt = "";
                url = "";
                regiSeniorExpre();
                executableTag();
                preferences = getSharedPreferences("regiSeniorExpired", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("seniorExpired", null);
                if (btxt.length() == 0) {
                    addToMissedNotificaton("Senior scale exam online registration");
                    saveMissedNotificationList();
                } else if (btxt.contains("expired")) {
                    if (btxt.equalsIgnoreCase(previousSaved)) {
                    } else {
                        addToSymmery(getString(R.string.regiExpired), "http://snsc.bpsc.gov.bd/node/apply", "Senior Scale Exam", notificationDate());
                        saveState();
                        fianlNotificationCount();
                        myIntent = new Intent(this, Browser.class);
                        myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(this, 7, myIntent, 0);
                        bigTextStyleNotification("Senior Scale Exam", btxt);
                        notification("Senior Scale Exam", getString(R.string.regiExpired), 7);
                        preferences.edit().putString("seniorExpired", btxt).apply();

                    }
                }
            }
            preferences = getSharedPreferences("assistantSurgeonSetting", 0);
            checked = preferences.getBoolean("assistantSurgeonChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.assistantSurgeon);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("assistantSurgeon", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("assistantSurgeon", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("11",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.assistantSurgeonSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.assistantSurgeonSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 8, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.assistantSurgeonSetting), btxt);
                    notification(getString(R.string.assistantSurgeonSetting), btxt, 8);
                    preferences.edit().putString("assistantSurgeon", btxt).apply();

                }
            }
            preferences = getSharedPreferences("juniorConsultantSetting", 0);
            checked = preferences.getBoolean("juniorConsultantChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.juniorConsultant);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("juniorConsultant", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("juniorConsultant", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("12",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.juniorConsultantSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.juniorConsultantSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 9, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.juniorConsultantSetting), btxt);
                    notification(getString(R.string.juniorConsultantSetting), btxt, 9);
                    preferences.edit().putString("juniorConsultant", btxt).apply();

                }
            }
            preferences = getSharedPreferences("seniorConsultantSetting", 0);
            checked = preferences.getBoolean("seniorConsultantChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.seniorConsultant);
                filterContent2 = getString(R.string.seniorConsultant2);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("seniorConsultant", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("seniorConsultant", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("13",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.seniorConsultantSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.seniorConsultantSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 10, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.seniorConsultantSetting), btxt);
                    notification(getString(R.string.seniorConsultantSetting), btxt, 10);
                    preferences.edit().putString("seniorConsultant", btxt).apply();

                }
                btxt = "";
                url = "";
                serviceConfirmTag2();
                preferences = getSharedPreferences("seniorConsultant2", Context.MODE_PRIVATE);
                previousSaved2 = preferences.getString("seniorConsultant2", null);
                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("14",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.seniorConsultantSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.seniorConsultantSetting) + ":", notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 10, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.seniorConsultantSetting), btxt);
                    notification(getString(R.string.seniorConsultantSetting), btxt, 101);
                    preferences.edit().putString("seniorConsultant2", btxt).apply();

                }
            }
            preferences = getSharedPreferences("assistantProfessorSetting", 0);
            checked = preferences.getBoolean("assistantProfessorChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.assistantProfessor);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("assistantProfessor", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("assistantProfessor", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("15",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.assistantProfessorSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.assistantProfessorSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 11, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.assistantProfessorSetting), btxt);
                    notification(getString(R.string.assistantProfessorSetting), btxt, 11);
                    preferences.edit().putString("assistantProfessor", btxt).apply();

                }
            }
            preferences = getSharedPreferences("associateProfessorSetting", 0);
            checked = preferences.getBoolean("associateProfessorChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.associateProfessor);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("associateProfessor", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("associateProfessor", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("16",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.associateProfessorSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.associateProfessorSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 12, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.associateProfessorSetting), btxt);
                    notification(getString(R.string.associateProfessorSetting), btxt, 12);
                    preferences.edit().putString("associateProfessor", btxt).apply();

                }
            }
            preferences = getSharedPreferences("professorSetting", 0);
            checked = preferences.getBoolean("professorChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.professor);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("professor", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("professor", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("17",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.professorSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.professorSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 13, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.professorSetting), btxt);
                    notification(getString(R.string.professorSetting), btxt, 13);
                    preferences.edit().putString("professor", btxt).apply();

                }
            }
            preferences = getSharedPreferences("civilSurgeonSetting", 0);
            checked = preferences.getBoolean("civilSurgeonChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.civilSurgeon);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("civilSurgeon", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("civilSurgeon", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("18",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.civilSurgeonSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.civilSurgeonSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 14, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.civilSurgeonSetting), btxt);
                    notification(getString(R.string.civilSurgeonSetting), btxt, 14);
                    preferences.edit().putString("civilSurgeon", btxt).apply();

                }
            }
            preferences = getSharedPreferences("adhocSetting", 0);
            checked = preferences.getBoolean("adhocChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = getString(R.string.adhoc);
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("adhoc", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("adhoc", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("19",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.adhocSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.adhocSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 15, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.adhocSetting), btxt);
                    notification(getString(R.string.adhocSetting), btxt, 15);
                    preferences.edit().putString("adhoc", btxt).apply();

                }
            }
            preferences = getSharedPreferences("mohfwSetting", 0);
            checked = preferences.getBoolean("mohfwChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = "Per";
                executeService();
                serviceConfirmTag();
                preferences = getSharedPreferences("mohfw", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("mohfw", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("20",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.mohfwSetting));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.mohfwSetting), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 16, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.mohfwSetting), btxt);
                    notification(getString(R.string.mohfwSetting), btxt, 16);
                    preferences.edit().putString("mohfw", buttonTexts.get(0)).apply();

                }
            }

            preferences = getSharedPreferences("deputationSetting", 0);
            checked = preferences.getBoolean("deputationChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = "ME-";
                executeDeputation();
                serviceConfirmTag();
                preferences = getSharedPreferences("deputation", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("deputation", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("21",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.deputationOrders));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.deputationOrders), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 17, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.deputationOrders), btxt);
                    notification(getString(R.string.deputationOrders), btxt, 17);
                    preferences.edit().putString("deputation", btxt).apply();

                }
            }

            preferences = getSharedPreferences("leaveSetting", 0);
            checked = preferences.getBoolean("leaveChecked", false);
            if (checked) {
                btxt = "";
                url = "";
                filterContent = "HR-";
                executeLeave();
                serviceConfirmTag();
                preferences = getSharedPreferences("leave", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("leave", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {
//                    summeryIntent.putExtra("22",getString(R.string.residencySetting));
                } else if (btxt.length() == 0) {
                    addToMissedNotificaton(getString(R.string.leaveOpion));
                    saveMissedNotificationList();
                } else {
                    addToSymmery(btxt, url, getString(R.string.leaveOpion), notificationDate());
                    saveState();
                    fianlNotificationCount();
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 18, myIntent, 0);
                    bigTextStyleNotification(getString(R.string.leaveOpion), btxt);
                    notification(getString(R.string.leaveOpion), btxt, 18);
                    preferences.edit().putString("leave", btxt).apply();

                }
            }

        } catch (Exception e) {
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationTexts.size() != 0) {
//            addToSymmery("", "", notificationDate(), "");
            saveState();
            try {
                fianlNotificationCount();
            } catch (Exception e) {
            }
        }
        clearArray();
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent newIntent = new Intent(MyIntentService.this, NotificationReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(MyIntentService.this, 11, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        try {
//            assert manager != null;
//            manager.cancel(pendingIntent);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        summeryIntent.setAction(Intent.ACTION_MAIN);
//        summeryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        summeryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ComponentName cn = new ComponentName(this, NotificationSummery.class);
//        summeryIntent.setComponent(cn);
//        startActivity(summeryIntent);
    }

    private void residency() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "a";
        paramLink = "abs:href";
        textMin = 146;
    }

    private void bsmmuNotice() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "#tab4 h3";
        paramLink = "abs:href";
        textMin = 0;
    }

    private void dghsHomeLinks() {
        paramUrl = "http://dghs.gov.bd/index.php/bd/";
        paramTagForText = "#system a";
        paramTagForLink = "#system a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
    }

    private void regiDeptExpire() {
        paramUrl = "http://dept.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramLink = "href";
        textMin = 1;
    }

    private void regiDeptStarts() {
        paramUrl = "http://dept.bpsc.gov.bd/node/apply";
        paramTagForText = "h6";
        paramLink = "href";
        textMin = 1;
    }

    private void regiSeniorExpre() {
        paramUrl = "http://snsc.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramLink = "href";
        textMin = 1;
    }

    private void regiSeniorStsrts() {
        paramUrl = "http://snsc.bpsc.gov.bd/node/apply";
        paramTagForText = "h6";
        paramLink = "href";
        textMin = 1;
    }

    private void resultBCS() {
        paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
        paramTagForText = "tr";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void resultDept() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void resultSenior() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramLink = "abs:href";
        textMin = 1;
    }

    private void executeService() {
        paramUrl = "http://mohfw.gov.bd/index.php?option=com_content&view=article&id=111:bcs-health&catid=38:bcs-health&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 12;
    }

    private void executeDeputation() {
        paramUrl = "http://www.mohfw.gov.bd/index.php?option=com_content&view=article&id=61%3Amedical-education&catid=46%3Amedical-education&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 29;
    }

    private void executeLeave() {
        paramUrl = "http://mohfw.gov.bd/index.php?option=com_content&view=article&id=121%3Aearn-leave&catid=101%3Aearn-leave-ex-bangladesh-leave&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 29;
    }

    private void serviceConfirmTag() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            int textMax = links.size();
            for (int i = textMin; i <= textMax; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(paramLink);
                if (btxt.contains(filterContent)) {
                    buttonTexts.add(btxt);
                    urls.add(url);
                    textMax = i;
                }
            }
            btxt = buttonTexts.get(0);
            url = urls.get(0);
            buttonTexts.clear();
            urls.clear();
        } catch (Exception e) {
        }
    }

    private void serviceConfirmTag2() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            int textMax = links.size();
            for (int i = textMin; i <= textMax; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(paramLink);
                if (btxt.contains(filterContent2)) {
                    buttonTexts2.add(btxt);
                    urls2.add(url);
                    textMax = i;
                }
            }
            btxt = buttonTexts2.get(0);
            url = urls2.get(0);
            buttonTexts2.clear();
            urls.clear();
        } catch (Exception e) {
        }
    }

    private void executableTag() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            btxt = links.get(textMin).text();
            url = links.get(textMin).select("a").attr(paramLink);
        } catch (Exception e) {
        }
    }

    private void notification(String title, String text, int id) {
        bigTextStyleNotification(title, text);
        settingIntent = new Intent(this, Settings.class);
        settingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingSetting = PendingIntent.getActivity(this, 1, settingIntent, 0);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
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
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        if (enableSound) {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
            mp.start();
        }

        if (enableVibrate) {
            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(1000);
                vibrator.vibrate(1000);
            }
        }
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        }
        if (wakeLock != null) {
            wakeLock.acquire(100);
        }
        notificationManager.notify(id, notificationBuilder.build());
    }

    private void bigTextStyleNotification(String title, String text) {
        bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(text);
    }

    private void checkConnectivity() {
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
                bigTextStyleNotification(title, text);
                settingIntent = new Intent(this, MainActivity.class);
                settingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingSetting = PendingIntent.getActivity(this, 111, settingIntent, 0);
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setColor(0xff990000)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setWhen(System.currentTimeMillis())
                        .setPriority(Notification.PRIORITY_MAX)
                        .setLights(0xff990000, 300, 100)
                        .setContentIntent(pendingSetting)
                        .setStyle(bigTextStyle)
                        .setSmallIcon(R.mipmap.ic_launcher);

                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = null;
                if (pm != null) {
                    wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
                }
                if (wakeLock != null) {
                    wakeLock.acquire(100);
                }
                notificationManager.notify(111, notificationBuilder.build());
            }
        }
    }

    private void saveState() {
        try {
            FileOutputStream write = openFileOutput("notificationHeadings", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationHeadings);
            arrayoutput.close();
            write.close();

        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationDates", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationDates);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationTexts", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationTexts);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationUrls", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            arrayoutput.writeObject(notificationUrls);
            arrayoutput.close();
            write.close();
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

    private void clearArray() {
        notificationTexts.clear();
        notificationUrls.clear();
        notificationDates.clear();
        notificationHeadings.clear();
    }

    private void readHeading() {
        try {
            FileInputStream read = openFileInput("notificationHeadings");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationHeadings = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    private void readDate() {
        try {
            FileInputStream read = openFileInput("notificationDates");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationDates = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    private void readText() {
        try {
            FileInputStream read = openFileInput("notificationTexts");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationTexts = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    private void readUrl() {
        try {
            FileInputStream read = openFileInput("notificationUrls");
            ObjectInputStream readarray = new ObjectInputStream(read);
            notificationUrls = (ArrayList<String>) readarray.readObject();
            readarray.close();
            read.close();
        } catch (Exception e) {
        }
    }

    private void fianlNotificationCount() {
        SharedPreferences oldsize = getSharedPreferences("finalNotificationCount", Context.MODE_PRIVATE);
        oldsize.edit().putInt("finalsize", notificationUrls.size()).apply();
    }

}