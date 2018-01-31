package zubayer.docsites;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Settings extends Activity {

    CheckBox residency,notice,dghs,reultBcs, resultDept, resultSenior,regiDept,regiSenior;
    SharedPreferences preferences;
    boolean checked;
    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        residency=(CheckBox)findViewById(R.id.residencySetting);
        notice=(CheckBox)findViewById(R.id.noticeSetting);
        dghs=(CheckBox)findViewById(R.id.DGHSSetting);
        reultBcs=(CheckBox)findViewById(R.id.bcsResultSetting);
        resultDept =(CheckBox)findViewById(R.id.deptResultSetting);
        resultSenior =(CheckBox)findViewById(R.id.seniorResultSetting);
        regiDept=(CheckBox)findViewById(R.id.regiDeptSetting);
        regiSenior=(CheckBox)findViewById(R.id.regiSeniorSetting);

        heading=(TextView)findViewById(R.id.settingHeading);
        int density=getResources().getDisplayMetrics().densityDpi;
        if(density<=DisplayMetrics.DENSITY_HIGH) {
            heading.setTextSize(20);
        }
        preferences=getSharedPreferences("residencySetting",0);
        checked=preferences.getBoolean("residencyChecked",false);
        residency.setChecked(checked);

        preferences=getSharedPreferences("noticeSetting",0);
        checked=preferences.getBoolean("noticeChecked",false);
        notice.setChecked(checked);

        preferences=getSharedPreferences("dghsSetting",0);
        checked=preferences.getBoolean("dghsChecked",false);
        dghs.setChecked(checked);

        preferences=getSharedPreferences("reultBcsSetting",0);
        checked=preferences.getBoolean("reultBcsChecked",false);
        reultBcs.setChecked(checked);

        preferences=getSharedPreferences("resultDeptSetting",0);
        checked=preferences.getBoolean("resultDeptChecked",false);
        resultDept.setChecked(checked);

        preferences=getSharedPreferences("resultSeniorSetting",0);
        checked=preferences.getBoolean("resultSeniorChecked",false);
        resultSenior.setChecked(checked);

        preferences=getSharedPreferences("regiDeptSetting",0);
        checked=preferences.getBoolean("regiDeptChecked",false);
        regiDept.setChecked(checked);

        preferences=getSharedPreferences("regiSeniorSetting",0);
        checked=preferences.getBoolean("regiSeniorChecked",false);
        regiSenior.setChecked(checked);

            residency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    SharedPreferences settings = getSharedPreferences("residencySetting", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    if(residency.isChecked()){
                        residency.setChecked(true);
                    }else {
                        residency.setChecked(false);
                    }
                    editor.putBoolean("residencyChecked", residency.isChecked());
                    editor.apply();
                }
            });

        notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("noticeSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(notice.isChecked()){
                    notice.setChecked(true);
                }else {
                    notice.setChecked(false);
                }
                editor.putBoolean("noticeChecked", notice.isChecked());
                editor.apply();
            }
        });

        dghs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("dghsSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(dghs.isChecked()){
                    dghs.setChecked(true);
                }else {
                    dghs.setChecked(false);
                }
                editor.putBoolean("dghsChecked", dghs.isChecked());
                editor.apply();
            }
        });

        reultBcs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("reultBcsSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(reultBcs.isChecked()){
                    reultBcs.setChecked(true);
                }else {
                    reultBcs.setChecked(false);
                }
                editor.putBoolean("reultBcsChecked", reultBcs.isChecked());
                editor.apply();
            }
        });

        resultSenior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("resultSeniorSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(resultSenior.isChecked()){
                    resultSenior.setChecked(true);
                }else {
                    resultSenior.setChecked(false);
                }
                editor.putBoolean("resultSeniorChecked", resultSenior.isChecked());
                editor.apply();
            }
        });

        resultDept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("resultDeptSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(resultDept.isChecked()){
                    resultDept.setChecked(true);
                }else {
                    resultDept.setChecked(false);
                }
                editor.putBoolean("resultDeptChecked", resultDept.isChecked());
                editor.apply();
            }
        });

        regiDept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("regiDeptSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(regiDept.isChecked()){
                    regiDept.setChecked(true);
                }else {
                    regiDept.setChecked(false);
                }
                editor.putBoolean("regiDeptChecked", regiDept.isChecked());
                editor.apply();
            }
        });

        regiSenior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("regiSeniorSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(regiSenior.isChecked()){
                    regiSenior.setChecked(true);
                }else {
                    regiSenior.setChecked(false);
                }
                editor.putBoolean("regiSeniorChecked", regiSenior.isChecked());
                editor.apply();
            }
        });
    }
}



