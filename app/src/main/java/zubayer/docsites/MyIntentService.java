package zubayer.docsites;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

public class MyIntentService extends IntentService {
    String btxt,url, paramUrl, paramTagForText, paramTagForLink, paramLink,previousSaved,driveViewer;
    int textMin, textMax, linkBegin;
    NotificationCompat.BigTextStyle bigTextStyle;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;
    PendingIntent pendingIntent;
    Intent myIntent;
    SharedPreferences preferences,settingPreference;
    public MyIntentService() {
        super("MyService");
    }
    boolean checked;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        bigTextStyle=new NotificationCompat.BigTextStyle();
        driveViewer="https://docs.google.com/viewer?url=";

        try {
            preferences=getSharedPreferences("residencySetting",0);
            checked=preferences.getBoolean("residencyChecked",false);
            if(checked) {
                residency();
                executableTag();
                preferences = getSharedPreferences("residency", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("residency", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {

                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
                    bigTextStyleNotification("BSMMU:Residency/Non-Residency", btxt);
                    notification("BSMMU:Residency/Non-Residency", btxt, 0);
                    preferences.edit().putString("residency", btxt).apply();
                }
            }
            preferences=getSharedPreferences("noticeSetting",0);
            checked=preferences.getBoolean("noticeChecked",false);
            if(checked) {
                bsmmuNotice();
                executableTag();
                preferences = getSharedPreferences("bsmmuNotice", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("bsmmuNotice", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {

                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 1, myIntent, 0);
                    bigTextStyleNotification("BSMMU Notice", btxt);
                    notification("BSMMU Notice", btxt, 1);
                    preferences.edit().putString("bsmmuNotice", btxt).apply();
                }
            }
            preferences=getSharedPreferences("dghsSetting",0);
            checked=preferences.getBoolean("dghsChecked",false);
            if(checked) {
                dghsHomeLinks();
                executableTag();
                preferences = getSharedPreferences("dghs", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("dghs", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {

                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    myIntent.putExtra("value", url);
                    pendingIntent = PendingIntent.getActivity(this, 2, myIntent, 0);
                    bigTextStyleNotification("New from DGHS", btxt);
                    notification("New from DGHS", btxt, 2);
                    preferences.edit().putString("dghs", btxt).apply();
                }
            }
            preferences=getSharedPreferences("resultDeptSetting",0);
            checked=preferences.getBoolean("resultDeptChecked",false);
            if(checked) {
                resultDept();
                executableTag();
                preferences = getSharedPreferences("dept", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("dept", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {

                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", driveViewer + url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 3, myIntent, 0);
                    bigTextStyleNotification("BPSC:Departmental Exam", btxt);
                    notification("BPSC:Departmental Exam", btxt, 3);
                    preferences.edit().putString("dept", btxt).apply();
                }
            }
            preferences=getSharedPreferences("resultSeniorSetting",0);
            checked=preferences.getBoolean("resultSeniorChecked",false);
            if(checked) {
                resultSenior();
                executableTag();
                preferences = getSharedPreferences("senior", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("senior", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {

                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", driveViewer + url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 4, myIntent, 0);
                    bigTextStyleNotification("BPSC:Senior Scale Exam", btxt);
                    notification("BPSC:Senior Scale Exam", btxt, 4);
                    preferences.edit().putString("senior", btxt).apply();
                }
            }
            preferences=getSharedPreferences("reultBcsSetting",0);
            checked=preferences.getBoolean("reultBcsChecked",false);
            if(checked) {
                resultBCS();
                executableTag();
                preferences = getSharedPreferences("bcs", Context.MODE_PRIVATE);
                previousSaved = preferences.getString("bcs", null);

                if (btxt.equalsIgnoreCase(previousSaved)) {

                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", driveViewer + url);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 5, myIntent, 0);
                    bigTextStyleNotification("BPSC:BCS Exam", btxt);
                    notification("BPSC:BCS Exam", btxt, 5);
                    preferences.edit().putString("bcs", btxt).apply();
                }
            }
            preferences=getSharedPreferences("regiDeptSetting",0);
            checked=preferences.getBoolean("regiDeptChecked",false);
            if(checked) {
                regiDept();executableTag();
                if(btxt.contains("expired")){
                }else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 6, myIntent, 0);
                    bigTextStyleNotification("Departmental Exam Registration", getString(R.string.regidepttext));
                    notification("Departmental Exam Registration", getString(R.string.regidepttext), 6);
                }
            }
            preferences=getSharedPreferences("regiSeniorSetting",0);
            checked=preferences.getBoolean("regiSeniorChecked",false);
            if(checked) {
                regiSenior();
                executableTag();
                if (btxt.contains("expired")) {
                } else {
                    myIntent = new Intent(this, Browser.class);
                    myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 7, myIntent, 0);
                    bigTextStyleNotification("Senior Scale Exam Registration", getString(R.string.regiseniortext));
                    notification("Senior Scale Exam Registration", getString(R.string.regiseniortext), 7);
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
        try {
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent newIntent = new Intent(MyIntentService.this, NotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MyIntentService.this, 11, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.cancel(pendingIntent);
        }catch (Exception e){}
    }

    private void residency() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "a";
        paramLink = "abs:href";
        textMin = 146;
    }
    private void bsmmuNotice() {
        paramUrl = "http://www.bsmmu.edu.bd";
        paramTagForText = "h3";
        paramLink = "abs:href";
        textMin = 0;
    }
    private void dghsHomeLinks() {
        paramUrl = "http://dghs.gov.bd/index.php/bd/";
        paramTagForText = "#system span";
        paramTagForLink = "#system a";
        paramLink = "abs:href";
        textMin = 0;
        linkBegin = 0;
    }

    private void regiDept() {
        paramUrl = "http://dept.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramLink = "href";
        textMin = 1;
    }
    private void regiSenior() {
        paramUrl = "http://snsc.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramLink = "href";
        textMin = 1;
    }
    private void resultBCS() {
        paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
        paramTagForText = "tr";
        paramLink = "href";
        textMin = 1;
    }
    private void resultDept() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramLink = "href";
        textMin = 1;
    }
    private void resultSenior() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramLink = "href";
        textMin = 1;
    }

    public void executableTag() {
        try {
            Document doc = Jsoup.connect(paramUrl).get();
            Elements links = doc.select(paramTagForText);
            Element link = links.get(textMin);
            btxt = link.text();
            url=link.select("a").attr(paramLink);
        } catch (Exception e) {
        }
    }

    private void notification(String title,String text, int id) {
        bigTextStyleNotification(title,text);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setStyle(bigTextStyle)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(id, notificationBuilder.build());
    }

    private void bigTextStyleNotification(String title,String text) {
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(text);
    }
}
