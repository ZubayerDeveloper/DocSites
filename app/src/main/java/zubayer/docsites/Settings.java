package zubayer.docsites;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Settings extends Activity {

    CheckBox residency,notice,dghs,reultBcs, resultDept, resultSenior,regiDept,regiSenior,assistantSurgeon,juniorConsultant,
            seniorConsultant,assistantProfessor,associateProfessor,professor, civilSurgeon,adhoc,mohfw;
    SharedPreferences preferences;
    boolean checked;
    TextView heading;
    Button select,deselect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        residency=(CheckBox)findViewById(R.id.residencySetting);
        notice=(CheckBox)findViewById(R.id.noticeSetting);
        dghs=(CheckBox)findViewById(R.id.DGHSSetting);
        reultBcs=(CheckBox)findViewById(R.id.bcsResultSetting);
        resultDept =(CheckBox)findViewById(R.id.deptResultSetting);
        resultSenior =(CheckBox)findViewById(R.id.seniorResultSetting);
        regiDept=(CheckBox)findViewById(R.id.regiDeptSetting);
        regiSenior=(CheckBox)findViewById(R.id.regiSeniorSetting);
        assistantSurgeon=(CheckBox)findViewById(R.id.assistantSurgeonSetting);
        juniorConsultant=(CheckBox)findViewById(R.id.juniorConsultantSetting);
        seniorConsultant =(CheckBox)findViewById(R.id.seniorConsultantSetting);
        assistantProfessor=(CheckBox)findViewById(R.id.assistantProfessorSetting);
        associateProfessor=(CheckBox)findViewById(R.id.associateProfessorSetting);
        professor=(CheckBox)findViewById(R.id.professorSetting);
        civilSurgeon =(CheckBox)findViewById(R.id.civilSergeonSetting);
        adhoc=(CheckBox)findViewById(R.id.adhocSetting);
        mohfw=(CheckBox)findViewById(R.id.mohfwSetting);
        select=(Button) findViewById(R.id.select);
        deselect=(Button) findViewById(R.id.deselect);

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

        preferences=getSharedPreferences("assistantSurgeonSetting",0);
        checked=preferences.getBoolean("assistantSurgeonChecked",false);
        assistantSurgeon.setChecked(checked);

        preferences=getSharedPreferences("juniorConsultantSetting",0);
        checked=preferences.getBoolean("juniorConsultantChecked",false);
        juniorConsultant.setChecked(checked);

        preferences=getSharedPreferences("seniorConsultantSetting",0);
        checked=preferences.getBoolean("seniorConsultantChecked",false);
        seniorConsultant.setChecked(checked);

        preferences=getSharedPreferences("assistantProfessorSetting",0);
        checked=preferences.getBoolean("assistantProfessorChecked",false);
        assistantProfessor.setChecked(checked);

        preferences=getSharedPreferences("associateProfessorSetting",0);
        checked=preferences.getBoolean("associateProfessorChecked",false);
        associateProfessor.setChecked(checked);

        preferences=getSharedPreferences("professorSetting",0);
        checked=preferences.getBoolean("professorChecked",false);
        professor.setChecked(checked);

        preferences=getSharedPreferences("civilSurgeonSetting",0);
        checked=preferences.getBoolean("civilSurgeonChecked",false);
        civilSurgeon.setChecked(checked);

        preferences=getSharedPreferences("adhocSetting",0);
        checked=preferences.getBoolean("adhocChecked",false);
        adhoc.setChecked(checked);

        preferences=getSharedPreferences("mohfwSetting",0);
        checked=preferences.getBoolean("mohfwChecked",false);
        mohfw.setChecked(checked);

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

        assistantSurgeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("assistantSurgeonSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(assistantSurgeon.isChecked()){
                    assistantSurgeon.setChecked(true);
                }else {
                    assistantSurgeon.setChecked(false);
                }
                editor.putBoolean("assistantSurgeonChecked", assistantSurgeon.isChecked());
                editor.apply();
            }
        });

        juniorConsultant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("juniorConsultantSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(juniorConsultant.isChecked()){
                    juniorConsultant.setChecked(true);
                }else {
                    juniorConsultant.setChecked(false);
                }
                editor.putBoolean("juniorConsultantChecked", juniorConsultant.isChecked());
                editor.apply();
            }
        });

        seniorConsultant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("seniorConsultantSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(seniorConsultant.isChecked()){
                    seniorConsultant.setChecked(true);
                }else {
                    seniorConsultant.setChecked(false);
                }
                editor.putBoolean("seniorConsultantChecked", seniorConsultant.isChecked());
                editor.apply();
            }
        });

        assistantProfessor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("assistantProfessorSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(assistantProfessor.isChecked()){
                    assistantProfessor.setChecked(true);
                }else {
                    assistantProfessor.setChecked(false);
                }
                editor.putBoolean("assistantProfessorChecked", assistantProfessor.isChecked());
                editor.apply();
            }
        });

        associateProfessor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("associateProfessorSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(associateProfessor.isChecked()){
                    associateProfessor.setChecked(true);
                }else {
                    associateProfessor.setChecked(false);
                }
                editor.putBoolean("associateProfessorChecked", associateProfessor.isChecked());
                editor.apply();
            }
        });

        professor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("professorSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(professor.isChecked()){
                    professor.setChecked(true);
                }else {
                    professor.setChecked(false);
                }
                editor.putBoolean("professorChecked", professor.isChecked());
                editor.apply();
            }
        });

        civilSurgeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("civilSurgeonSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(civilSurgeon.isChecked()){
                    civilSurgeon.setChecked(true);
                }else {
                    civilSurgeon.setChecked(false);
                }
                editor.putBoolean("civilSurgeonChecked", civilSurgeon.isChecked());
                editor.apply();
            }
        });

        adhoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("adhocSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(adhoc.isChecked()){
                    adhoc.setChecked(true);
                }else {
                    adhoc.setChecked(false);
                }
                editor.putBoolean("adhocChecked", adhoc.isChecked());
                editor.apply();
            }
        });

        mohfw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("mohfwSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(mohfw.isChecked()){
                    mohfw.setChecked(true);
                }else {
                    mohfw.setChecked(false);
                }
                editor.putBoolean("mohfwChecked", mohfw.isChecked());
                editor.apply();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDeselect("residencySetting",residency,true,"residencyChecked");
                selectDeselect("noticeSetting",notice,true,"noticeChecked");
                selectDeselect("dghsSetting",dghs,true,"dghsChecked");
                selectDeselect("reultBcsSetting",reultBcs,true,"reultBcsChecked");
                selectDeselect("resultDeptSetting",resultDept,true,"resultDeptChecked");
                selectDeselect("resultSeniorSetting",resultSenior,true,"resultSeniorChecked");
                selectDeselect("regiDeptSetting",regiDept,true,"regiDeptChecked");
                selectDeselect("regiSeniorSetting",regiSenior,true,"regiSeniorChecked");
                selectDeselect("assistantSurgeonSetting",assistantSurgeon,true,"assistantSurgeonChecked");
                selectDeselect("juniorConsultantSetting",juniorConsultant,true,"juniorConsultantChecked");
                selectDeselect("seniorConsultantSetting",seniorConsultant,true,"seniorConsultantChecked");
                selectDeselect("assistantProfessorSetting",assistantProfessor,true,"assistantProfessorChecked");
                selectDeselect("associateProfessorSetting",associateProfessor,true,"associateProfessorChecked");
                selectDeselect("professorSetting",professor,true,"professorChecked");
                selectDeselect("civilSurgeonSetting", civilSurgeon,true,"civilSurgeonChecked");
                selectDeselect("adhocSetting",adhoc,true,"adhocChecked");
                selectDeselect("mohfwSetting",mohfw,true,"mohfwChecked");
            }
        });
        deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDeselect("residencySetting",residency,false,"residencyChecked");
                selectDeselect("noticeSetting",notice,false,"noticeChecked");
                selectDeselect("dghsSetting",dghs,false,"dghsChecked");
                selectDeselect("reultBcsSetting",reultBcs,false,"reultBcsChecked");
                selectDeselect("resultDeptSetting",resultDept,false,"resultDeptChecked");
                selectDeselect("resultSeniorSetting",resultSenior,false,"resultSeniorChecked");
                selectDeselect("regiDeptSetting",regiDept,false,"regiDeptChecked");
                selectDeselect("regiSeniorSetting",regiSenior,false,"regiSeniorChecked");
                selectDeselect("assistantSurgeonSetting",assistantSurgeon,false,"assistantSurgeonChecked");
                selectDeselect("juniorConsultantSetting",juniorConsultant,false,"juniorConsultantChecked");
                selectDeselect("seniorConsultantSetting",seniorConsultant,false,"seniorConsultantChecked");
                selectDeselect("assistantProfessorSetting",assistantProfessor,false,"assistantProfessorChecked");
                selectDeselect("associateProfessorSetting",associateProfessor,false,"associateProfessorChecked");
                selectDeselect("professorSetting",professor,false,"professorChecked");
                selectDeselect("civilSurgeonSetting", civilSurgeon,false,"civilSurgeonChecked");
                selectDeselect("adhocSetting",adhoc,false,"adhocChecked");
                selectDeselect("mohfwSetting",mohfw,false,"mohfwChecked");
            }
        });
    }
    public void selectDeselect(String preferenceName,CheckBox checkBoxName,boolean setTrueFalse,String putBooleanName){
        SharedPreferences settings = getSharedPreferences(preferenceName, 0);
        SharedPreferences.Editor editor = settings.edit();
        checkBoxName.setChecked(setTrueFalse);
        editor.putBoolean(putBooleanName, checkBoxName.isChecked());
        editor.apply();
    }
}



