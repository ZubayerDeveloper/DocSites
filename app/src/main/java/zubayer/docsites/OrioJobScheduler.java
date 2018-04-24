package zubayer.docsites;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

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

public class OrioJobScheduler extends JobService{
    String btxt,url, paramUrl, paramTagForText, paramTagForLink, paramLink,previousSaved,previousSaved2,
            filterContent,filterContent2,driveViewer,title,text,date;
    int textMin,linkBegin;
    SharedPreferences preferences;
    boolean checked,enableSound,enableVibrate,wifiAvailable,mobileDataAvailable;
    PendingIntent pendingIntent;
    Intent myIntent,summeryIntent;
    NotificationParser notificationParser=new NotificationParser();
    ArrayList<String> buttonTexts2=new ArrayList<>();
    ArrayList<String>urls2=new ArrayList<>();
    ArrayList<String> buttonTexts=new ArrayList<>();
    ArrayList<String>urls=new ArrayList<>();
    ArrayList<String> notificationHeadings = new ArrayList<>();
    ArrayList<String> notificationDates = new ArrayList<>();
    ArrayList<String> notificationTexts = new ArrayList<>();
    ArrayList<String> notificationUrls = new ArrayList<>();
    ArrayList<String> missedNotifications = new ArrayList<>();

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        checkConnectivity();
        summeryIntent = new Intent(getApplicationContext(), NotificationSummery.class);
        readHeading();readDate();readText();readUrl();
        notificationParser=new NotificationParser(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(jobParameters,false);
                try {
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent newIntent = new Intent(OrioJobScheduler.this, NotificationReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(OrioJobScheduler.this, 11, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.cancel(pendingIntent);
                }catch (Exception e){}
            }
        };
        notificationParser.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        notificationParser.cancel(true);
        clearArray();
        summeryIntent.setAction(Intent.ACTION_MAIN);
        summeryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        summeryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn=new ComponentName(this,NotificationSummery.class);
        summeryIntent.setComponent(cn);
        startActivity(summeryIntent);
        return false;
    }
    public class NotificationParser extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            driveViewer="https://docs.google.com/viewer?url=";

            try {
                preferences=getSharedPreferences("notificationSound",0);
                enableSound=preferences.getBoolean("notificationSoundChecked",false);

                preferences=getSharedPreferences("vibration",0);
                enableVibrate=preferences.getBoolean("vibrationChecked",false);

                preferences=getSharedPreferences("residencySetting",0);
                checked=preferences.getBoolean("residencyChecked",false);

                addToSymmery(" ", " ", " ", " ");
                saveState();
                if(checked) {
                    btxt = "";
                    url="";
                    residency();
                    executableTag();
                    preferences = getSharedPreferences("residency", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("residency", null);

                    if (btxt.equalsIgnoreCase(previousSaved)){
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    }else if(btxt.length()==0) {
                        addToMissedNotificaton("BSMMU:Residency/Non-Residency");
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 0, myIntent, 0);
                        notification("channel_0","res","Residency/Non-Residency",btxt,0);
                        preferences.edit().putString("residency", btxt).apply();
                        addToSymmery(btxt, url, "BSMMU:Residency/Non-Residency", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("noticeSetting",0);
                checked=preferences.getBoolean("noticeChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    bsmmuNotice();
                    executableTag();
                    preferences = getSharedPreferences("bsmmuNotice", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("bsmmuNotice", null);

                    if (btxt.equalsIgnoreCase(previousSaved)){
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    }else if(btxt.length()==0) {
                        addToMissedNotificaton("M.phil, Diploma exam notice");
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 1, myIntent, 0);
                        notification("channel_1","notice","BSMMU Notice",btxt,1);
                        preferences.edit().putString("bsmmuNotice", btxt).apply();
                        addToSymmery(btxt, url, "BSMMU Notice", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("dghsSetting",0);
                checked=preferences.getBoolean("dghsChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    dghsHomeLinks();
                    executableTag();
                    preferences = getSharedPreferences("dghs", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("dghs", null);

                    if (btxt.equalsIgnoreCase(previousSaved)){
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    }else if(btxt.length()==0) {
                        addToMissedNotificaton("Notification from DGHS");
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 2, myIntent, 0);
                        notification("channel_2","dghs","New from DGHS",btxt,2);
                        preferences.edit().putString("dghs", btxt).apply();
                        addToSymmery(btxt, url, "DGHS", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("resultDeptSetting",0);
                checked=preferences.getBoolean("resultDeptChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    resultDept();
                    executableTag();
                    preferences = getSharedPreferences("dept", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("dept", null);

                    if (btxt.equalsIgnoreCase(previousSaved)){
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    }else if(btxt.length()==0) {
                        addToMissedNotificaton("Notice and results for Departmental Exam");
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 3, myIntent, 0);
                        notification("channel_3","dept","Departmental Exam Notice",btxt,3);
                        preferences.edit().putString("dept", btxt).apply();
                        addToSymmery(btxt, url, "Departmental Exam", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("resultSeniorSetting",0);
                checked=preferences.getBoolean("resultSeniorChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    resultSenior();
                    executableTag();
                    preferences = getSharedPreferences("senior", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("senior", null);

                    if (btxt.equalsIgnoreCase(previousSaved)){
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    }else if(btxt.length()==0) {
                        addToMissedNotificaton("Notice and results for Senior Scale Exam");
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 4, myIntent, 0);
                        notification("channel_4","senior","Senior Scale Exam Notice",btxt,4);
                        preferences.edit().putString("senior", btxt).apply();
                        addToSymmery(btxt, url, "Senior Scale Exam", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("reultBcsSetting",0);
                checked=preferences.getBoolean("reultBcsChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    resultBCS();
                    executableTag();
                    preferences = getSharedPreferences("bcs", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("bcs", null);

                    if (btxt.equalsIgnoreCase(previousSaved)){
//                    summeryIntent.putExtra("1",getString(R.string.residencySetting));
                    }else if(btxt.length()==0) {
                        addToMissedNotificaton("BCS Exam");
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 5, myIntent, 0);
                        notification("channel_5","bcs","BPSC:BCS Exam",btxt,5);
                        preferences.edit().putString("bcs", btxt).apply();
                        addToSymmery(btxt, url, "BCS Exam", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("regiDeptSetting",0);
                checked=preferences.getBoolean("regiDeptChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    regiDeptStarts();
                    executableTag();
                    if(btxt.length()==0) {
                        addToMissedNotificaton("Departmental exam online registration");
                        saveMissedNotificationList();
                    }else if(btxt.contains("Section 1: Personal Details")){
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 61, myIntent, 0);
                        notification("channel_61","deptstarts","Departmental Exam", getString(R.string.regideptStarted), 61);

                        preferences = getSharedPreferences("regideptExpired", Context.MODE_PRIVATE);
                        preferences.edit().remove("regideptExpired").apply();
                        addToSymmery(getString(R.string.regideptStarted), "http://dept.bpsc.gov.bd/node/apply", "Departmental Exam", notificationDate());
                        saveState();
                        saveState();
                    }else {

                    }
                    btxt = "";
                    url="";
                    regiDeptExpire();
                    executableTag();
                    preferences = getSharedPreferences("regideptExpired", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("deptExpired", null);
                    if(btxt.length()==0) {
                        addToMissedNotificaton("Departmental exam online registration");
                        saveMissedNotificationList();
                    }else if(btxt.contains("expired")){
                        if(btxt.equalsIgnoreCase(previousSaved)) {
                        }else {
                            myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                            myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 6, myIntent, 0);
                            notification("channel_6","deptexpires","Departmental Exam", getString(R.string.regiExpired), 6);
                            preferences.edit().putString("deptExpired", btxt).apply();
                            addToSymmery(getString(R.string.regiseniortext), "http://dept.bpsc.gov.bd/node/apply", "Departmental Exam", notificationDate());
                            saveState();
                        }
                    }
                }
                preferences=getSharedPreferences("regiSeniorSetting",0);
                checked=preferences.getBoolean("regiSeniorChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    regiSeniorStsrts();
                    executableTag();
                    if(btxt.length()==0) {
                        addToMissedNotificaton("Senior scale exam online registration");
                        saveMissedNotificationList();
                    }else if(btxt.contains("Section 1: Personal Details")){
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 71, myIntent, 0);
                        notification("channel_71","seniorstarts","Senior Scale Exam", getString(R.string.regiExpired), 71);

                        preferences = getSharedPreferences("regiSeniorExpired", Context.MODE_PRIVATE);
                        preferences.edit().remove("regiSeniorExpired").apply();
                        addToSymmery(getString(R.string.regiseniortext), "http://snsc.bpsc.gov.bd/node/apply", "Senior Scale Exam", notificationDate());
                        saveState();
                    } else {

                    }
                    btxt = "";
                    url="";
                    regiSeniorExpre();
                    executableTag();
                    preferences = getSharedPreferences("regiSeniorExpired", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("seniorExpired", null);
                    if(btxt.length()==0) {
                        addToMissedNotificaton("Senior scale exam online registration");
                        saveMissedNotificationList();
                    }else if (btxt.contains("expired")) {
                        if (btxt.equalsIgnoreCase(previousSaved)) {
                        } else {
                            myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                            myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 7, myIntent, 0);
                            notification("channel_7","seniorexires","Senior Scale Exam", getString(R.string.regiExpired), 7);
                            preferences.edit().putString("seniorExpired", btxt).apply();
                            addToSymmery(getString(R.string.regiExpired), "http://snsc.bpsc.gov.bd/node/apply", "Senior Scale Exam", notificationDate());
                            saveState();
                        }
                    }
                }
                preferences=getSharedPreferences("assistantSurgeonSetting",0);
                checked=preferences.getBoolean("assistantSurgeonChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.assistantSurgeon);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("assistantSurgeon", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("assistantSurgeon", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.assistantSurgeonSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 8, myIntent, 0);
                        notification("channel_8","assistantSurgeon",getString(R.string.assistantSurgeonSetting),btxt,8);
                        preferences.edit().putString("assistantSurgeon", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.assistantSurgeonSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("juniorConsultantSetting",0);
                checked=preferences.getBoolean("juniorConsultantChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.juniorConsultant);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("juniorConsultant", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("juniorConsultant", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.juniorConsultantSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 9, myIntent, 0);
                        notification("channel_9","juniorConsultant",getString(R.string.juniorConsultantSetting),btxt,9);
                        preferences.edit().putString("juniorConsultant", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.juniorConsultantSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("seniorConsultantSetting",0);
                checked=preferences.getBoolean("seniorConsultantChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.seniorConsultant);
                    filterContent2=getString(R.string.seniorConsultant2);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("seniorConsultant", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("seniorConsultant", null);


                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.seniorConsultantSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 10, myIntent, 0);
                        notification("channel_10","seniorConsultant",getString(R.string.seniorConsultantSetting),btxt,10);
                        preferences.edit().putString("seniorConsultant", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.seniorConsultantSetting), notificationDate());
                        saveState();
                    }
                    btxt = "";
                    url="";
                    serviceConfirmTag2();
                    preferences = getSharedPreferences("seniorConsultant2", Context.MODE_PRIVATE);
                    previousSaved2 = preferences.getString("seniorConsultant2", null);
                    if (btxt.equalsIgnoreCase(previousSaved2)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.seniorConsultantSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 101, myIntent, 0);
                        notification("channel_101","seniorConsultant2",getString(R.string.seniorConsultantSetting),btxt,101);
                        preferences.edit().putString("seniorConsultant2", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.seniorConsultantSetting) + ":", notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("assistantProfessorSetting",0);
                checked=preferences.getBoolean("assistantProfessorChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.assistantProfessor);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("assistantProfessor", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("assistantProfessor", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.assistantProfessorSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 11, myIntent, 0);
                        notification("channel_11","assistantProfessor",getString(R.string.assistantProfessorSetting),btxt,11);
                        preferences.edit().putString("assistantProfessor", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.assistantProfessorSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("associateProfessorSetting",0);
                checked=preferences.getBoolean("associateProfessorChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.associateProfessor);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("associateProfessorSetting", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("associateProfessor", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    } else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.associateProfessorSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 12, myIntent, 0);
                        notification("channel_12","associateProfessor",getString(R.string.associateProfessorSetting),btxt,12);
                        preferences.edit().putString("associateProfessor", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.associateProfessorSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("professorSetting",0);
                checked=preferences.getBoolean("professorChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.professor);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("professor", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("professor", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.professorSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 13, myIntent, 0);
                        notification("channel_13","professor",getString(R.string.professorSetting),btxt,13);
                        preferences.edit().putString("professor", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.professorSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("civilSurgeonSetting",0);
                checked=preferences.getBoolean("civilSurgeonChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.civilSurgeon);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("civilSurgeon", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("civilSurgeon", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.civilSurgeonSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 14, myIntent, 0);
                        notification("channel_14","civilSurgeon",getString(R.string.civilSurgeonSetting),btxt,14);
                        preferences.edit().putString("civilSurgeon", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.civilSurgeonSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("adhocSetting",0);
                checked=preferences.getBoolean("adhocChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent=getString(R.string.adhoc);
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("adhoc", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("adhoc", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.adhocSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 15, myIntent, 0);
                        notification("channel_15","adhoc",getString(R.string.adhocSetting),btxt,15);
                        preferences.edit().putString("adhoc", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.adhocSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("mohfwSetting",0);
                checked=preferences.getBoolean("mohfwChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent="Per";
                    filterContent2="aaaaaaa";
                    executeService();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("mohfw", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("mohfw", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.mohfwSetting));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 16, myIntent, 0);
                        notification("channel_16","mohfw",getString(R.string.mohfwSetting),btxt,16);
                        preferences.edit().putString("mohfw", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.mohfwSetting), notificationDate());
                        saveState();
                    }
                }
                preferences=getSharedPreferences("deputationSetting",0);
                checked=preferences.getBoolean("deputationChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent="ME-";
                    executeDeputation();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("deputation", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("deputation", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.deputationOrders));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 17, myIntent, 0);
                        notification("channel_17","deputation",getString(R.string.deputationOrders), btxt, 17);
                        preferences.edit().putString("deputation", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.deputationOrders), notificationDate());
                        saveState();
                    }
                }

                preferences=getSharedPreferences("leaveSetting",0);
                checked=preferences.getBoolean("leaveChecked",false);
                if(checked) {
                    btxt = "";
                    url="";
                    filterContent="HR-";
                    executeLeave();
                    serviceConfirmTag();
                    preferences = getSharedPreferences("leave", Context.MODE_PRIVATE);
                    previousSaved = preferences.getString("leave", null);

                    if (btxt.equalsIgnoreCase(previousSaved)) {

                    }else if (btxt.length()==0) {
                        addToMissedNotificaton(getString(R.string.leaveOpion));
                        saveMissedNotificationList();
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 18, myIntent, 0);
                        notification("channel_18","leave",getString(R.string.leaveOpion), btxt, 18);
                        preferences.edit().putString("leave", btxt).apply();
                        addToSymmery(btxt, url, getString(R.string.leaveOpion), notificationDate());
                        saveState();
                    }
                }
                addToSymmery(" ", " ", notificationDate(), " ");
                saveState();
            } catch (Exception ignored) {
            }
            return null;
        }

    }
    private void residency() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "a";
        paramTagForLink = "a";
        paramLink = "abs:href";
        textMin = 146;
        linkBegin = 146;
    }
    private void bsmmuNotice() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "h3";
        paramTagForLink = "h3 a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
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
        linkBegin = 0;
    }
    private void resultDept() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        linkBegin = 1;
    }
    private void resultSenior() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "abs:href";
        textMin = 1;
        linkBegin = 1;
    }
    private void executeService() {
        paramUrl="http://mohfw.gov.bd/index.php?option=com_content&view=article&id=111:bcs-health&catid=38:bcs-health&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 12;
    }
    private void executeDeputation() {
        paramUrl="http://www.mohfw.gov.bd/index.php?option=com_content&view=article&id=61%3Amedical-education&catid=46%3Amedical-education&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 29;
    }
    private void executeLeave() {
        paramUrl="http://mohfw.gov.bd/index.php?option=com_content&view=article&id=121%3Aearn-leave&catid=101%3Aearn-leave-ex-bangladesh-leave&Itemid=&lang=en";
        paramTagForText = "#wrapper table tbody tr td table tbody tr td table tbody tr";
        paramLink = "abs:href";
        textMin = 29;
    }
    public void serviceConfirmTag() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            int textMax=links.size();
            for (int i = textMin; i <=textMax; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(paramLink);
                if (btxt.contains(filterContent)) {
                    buttonTexts.add(btxt);
                    urls.add(url);
                    textMax=i;
                }
            }
            btxt=buttonTexts.get(0);
            url=urls.get(0);
            buttonTexts.clear();
            urls.clear();
        } catch (Exception e) {
        }
    }
    public void serviceConfirmTag2() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            int textMax=links.size();
            for (int i = textMin; i <=textMax; i++) {
                Element link = links.get(i);
                btxt = link.text();
                url = link.select("a").attr(paramLink);
                if (btxt.contains(filterContent2)) {
                    buttonTexts2.add(btxt);
                    urls2.add(url);
                    textMax=i;
                }
            }
            btxt=buttonTexts2.get(0);
            url=urls2.get(0);
            buttonTexts2.clear();
            urls2.clear();
        } catch (Exception e) {
        }
    }
    public void executableTag() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            Elements hrefs = doc.select(paramTagForLink);

            Element link = links.get(textMin);
            btxt = link.text();

            Element li = hrefs.get(linkBegin);
            url = li.attr(paramLink);

        } catch (Exception e) {
        }
    }

    private void notification(String channel_id,String channel_name,String title, String text, int notify_id ){
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH);
            channel.shouldShowLights();
            channel.shouldVibrate();
            channel.canShowBadge();
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = new NotificationCompat.Builder(OrioJobScheduler.this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setColor(0xff990000)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{0,300,300,300})
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setChannelId(channel_id).build();
            notification.ledARGB=0xff990000;
            notification.ledOnMS=500;
            notification.ledOffMS=100;
            if(enableSound) {
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                mp.start();
            }

            if(enableVibrate) {
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(700);
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
            notificationManager.notify(notify_id, notification);
        }
    }
    public void checkConnectivity(){
        title="Turn on Data";
        text=getString(R.string.noData);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        wifiAvailable = networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mobileDataAvailable = networkInfo.isConnected();
        if (!wifiAvailable&& !mobileDataAvailable) {
            myIntent = new Intent(OrioJobScheduler.this, MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 112, myIntent, 0);
            notification("nodata","dataNotFound",title,text,113);
        }
    }
    private void saveState() {
        try {
            FileOutputStream write = openFileOutput("notificationHeadings", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(notificationHeadings);
            arrayoutput.writeObject(notificationHeadings);
            arrayoutput.close();
            write.close();

        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationDates", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(notificationDates);
            arrayoutput.writeObject(notificationDates);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationTexts", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(notificationTexts);
            arrayoutput.writeObject(notificationTexts);
            arrayoutput.close();
            write.close();
        } catch (Exception e) {
        }
        try {
            FileOutputStream write = openFileOutput("notificationUrls", Context.MODE_PRIVATE);
            ObjectOutputStream arrayoutput = new ObjectOutputStream(write);
            limitArray(notificationUrls);
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

    private void limitArray(ArrayList<String> arrayName){
        if(arrayName.size()>200){
            arrayName.remove(200);
        }
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
}