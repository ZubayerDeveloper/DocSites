package zubayer.docsites;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    private int id=0;
    ComponentName componentName;
    JobInfo.Builder jobInfoBuilder;
    JobScheduler jobScheduler;
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(Build.VERSION.SDK_INT>=26){
            componentName=new ComponentName(context,OrioJobScheduler.class);
            jobInfoBuilder=new JobInfo.Builder(id,componentName)
            .setMinimumLatency(1000)
            .setPersisted(true)
            .setOverrideDeadline(2000);
            jobScheduler=(JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(jobInfoBuilder.build());
        }else {
            Intent newIntent = new Intent(context, MyIntentService.class);
            newIntent.getAction();
            context.startService(newIntent);
        }
        }
    }