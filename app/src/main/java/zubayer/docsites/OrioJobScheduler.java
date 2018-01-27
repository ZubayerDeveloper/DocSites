package zubayer.docsites;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

public class OrioJobScheduler extends JobService{
    String btxt,url, paramUrl, paramTagForText, paramTagForLink, paramLink,previousSaved, oldSaved,newSave,driveViewer;
    int textMin,linkBegin;
    SharedPreferences preferences;
    boolean checked;
    PendingIntent pendingIntent;
    Intent myIntent;
    NotificationParser notificationParser=new NotificationParser();
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
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
        return false;
    }
    public class NotificationParser extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
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
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 0, myIntent, 0);
                        notification("channel_0","res","Residency/Non-Residency",btxt,0);
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
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 1, myIntent, 0);
                        notification("channel_1","notice","BSMMU Notice",btxt,1);
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
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.putExtra("value", url);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 2, myIntent, 0);
                        notification("channel_2","dghs","New from DGHS",btxt,2);
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
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 3, myIntent, 0);
                        notification("channel_3","dept","Departmental Exam",btxt,3);
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
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 4, myIntent, 0);
                        notification("channel_4","senior","Senior Scale Exam",btxt,4);
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
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", driveViewer + url);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 5, myIntent, 0);
                        notification("channel_5","bcs","BPSC:BCS Exam",btxt,5);
                        preferences.edit().putString("bcs", btxt).apply();
                    }
                }
                preferences=getSharedPreferences("regiDeptSetting",0);
                checked=preferences.getBoolean("regiDeptChecked",false);
                if(checked) {
                    regiDept();executableTag();
                    if(btxt.contains("expired")){
                    }else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", "http://dept.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 6, myIntent, 0);
                        notification("channel_6","deptregi","Departmental Exam Registration",getString(R.string.regidepttext),6);
                    }
                }
                preferences=getSharedPreferences("regiSeniorSetting",0);
                checked=preferences.getBoolean("regiSeniorChecked",false);
                if(checked) {
                    regiSenior();
                    executableTag();
                    if (btxt.contains("expired")) {
                    } else {
                        myIntent = new Intent(OrioJobScheduler.this, Browser.class);
                        myIntent.putExtra("value", "http://snsc.bpsc.gov.bd/node/apply");
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingIntent = PendingIntent.getActivity(OrioJobScheduler.this, 7, myIntent, 0);
                        notification("channel_7","regisenior","Senior Scale Exam Registration",getString(R.string.regiseniortext),7);
                    }
                }
            } catch (Exception e) {
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

    private void regiDept() {
        paramUrl = "http://dept.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramTagForLink = "p";
        paramLink = "href";
        textMin = 1;
        linkBegin = 1;
    }
    private void regiSenior() {
        paramUrl = "http://snsc.bpsc.gov.bd/node/apply";
        paramTagForText = "p";
        paramTagForLink = "p";
        paramLink = "href";
        textMin = 1;
        linkBegin = 1;
    }
    private void resultBCS() {
        paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr a";
        paramLink = "href";
        textMin = 1;
        linkBegin = 0;
    }
    private void resultDept() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "href";
        textMin = 1;
        linkBegin = 1;
    }
    private void resultSenior() {
        paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
        paramTagForText = "tr";
        paramTagForLink = "tr td a";
        paramLink = "href";
        textMin = 1;
        linkBegin = 1;
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
    private void newNotification(String preference){
        preferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        preferences.edit().putString(preference,btxt).apply();
        newSave=preferences.getString(preference,null);
    }
    private void oldNotification(String preference){
        preferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        oldSaved = preferences.getString(preference, null);
    }

    private void notification(String channel_id,String channel_name,String title, String text, int notify_id ){
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(OrioJobScheduler.this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setChannelId(channel_id).build();
            notificationManager.notify(notify_id, notification);
        }
    }
}
