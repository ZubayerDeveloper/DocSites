package zubayer.docsites;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Settings extends Activity {

    CheckBox residency, notice, dghs, reultBcs, resultDept, resultSenior, regiDept, regiSenior, assistantSurgeon, juniorConsultant,
            seniorConsultant, assistantProfessor, associateProfessor, professor, civilSurgeon, adhoc, mohfw, deputation, leave, sound,
            vibration, openInBrowser, connectivityAlert, dgfp, ccdBirdem;
    SharedPreferences preferences;
    boolean checked, enableSound, enableVibrate;
    TextView heading;
    Button select, deselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Calligrapher font = new Calligrapher(this);
        font.setFont(this, "kalpurush.ttf", true);
        residency = (CheckBox) findViewById(R.id.residencySetting);
        notice = (CheckBox) findViewById(R.id.noticeSetting);
        dghs = (CheckBox) findViewById(R.id.DGHSSetting);
        reultBcs = (CheckBox) findViewById(R.id.bcsResultSetting);
        resultDept = (CheckBox) findViewById(R.id.deptResultSetting);
        resultSenior = (CheckBox) findViewById(R.id.seniorResultSetting);
        regiDept = (CheckBox) findViewById(R.id.regiDeptSetting);
        regiSenior = (CheckBox) findViewById(R.id.regiSeniorSetting);
        assistantSurgeon = (CheckBox) findViewById(R.id.assistantSurgeonSetting);
        juniorConsultant = (CheckBox) findViewById(R.id.juniorConsultantSetting);
        seniorConsultant = (CheckBox) findViewById(R.id.seniorConsultantSetting);
        assistantProfessor = (CheckBox) findViewById(R.id.assistantProfessorSetting);
        associateProfessor = (CheckBox) findViewById(R.id.associateProfessorSetting);
        professor = (CheckBox) findViewById(R.id.professorSetting);
        civilSurgeon = (CheckBox) findViewById(R.id.civilSergeonSetting);
        adhoc = (CheckBox) findViewById(R.id.adhocSetting);
        mohfw = (CheckBox) findViewById(R.id.mohfwSetting);
        deputation = (CheckBox) findViewById(R.id.deputationSetting);
        leave = (CheckBox) findViewById(R.id.leaveSetting);
        sound = (CheckBox) findViewById(R.id.soundSetting);
        vibration = (CheckBox) findViewById(R.id.vibrateSetting);
        openInBrowser = (CheckBox) findViewById(R.id.openInbrowser);
        connectivityAlert = (CheckBox) findViewById(R.id.connectivity);
        dgfp = (CheckBox) findViewById(R.id.dgfpSetting);
        ccdBirdem = (CheckBox) findViewById(R.id.ccdSetting);

        select = (Button) findViewById(R.id.select);
        deselect = (Button) findViewById(R.id.deselect);

        heading = (TextView) findViewById(R.id.settingHeading);
        int density = getResources().getDisplayMetrics().densityDpi;
        if (density <= DisplayMetrics.DENSITY_HIGH) {
            heading.setTextSize(20);
        }
        preferences = getSharedPreferences("residencySetting", 0);
        checked = preferences.getBoolean("residencyChecked", false);
        residency.setChecked(checked);
        if (residency.isChecked()) {
            residency.setTextColor(Color.parseColor("#000000"));

        } else {
            residency.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("noticeSetting", 0);
        checked = preferences.getBoolean("noticeChecked", false);
        notice.setChecked(checked);
        if (notice.isChecked()) {
            notice.setTextColor(Color.parseColor("#000000"));

        } else {
            notice.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("dghsSetting", 0);
        checked = preferences.getBoolean("dghsChecked", false);
        dghs.setChecked(checked);
        if (dghs.isChecked()) {
            dghs.setTextColor(Color.parseColor("#000000"));

        } else {
            dghs.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("reultBcsSetting", 0);
        checked = preferences.getBoolean("reultBcsChecked", false);
        reultBcs.setChecked(checked);
        if (reultBcs.isChecked()) {
            reultBcs.setTextColor(Color.parseColor("#000000"));

        } else {
            reultBcs.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("resultDeptSetting", 0);
        checked = preferences.getBoolean("resultDeptChecked", false);
        resultDept.setChecked(checked);
        if (resultDept.isChecked()) {
            resultDept.setTextColor(Color.parseColor("#000000"));

        } else {
            resultDept.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("resultSeniorSetting", 0);
        checked = preferences.getBoolean("resultSeniorChecked", false);
        resultSenior.setChecked(checked);
        if (resultSenior.isChecked()) {
            resultSenior.setTextColor(Color.parseColor("#000000"));

        } else {
            resultSenior.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("regiDeptSetting", 0);
        checked = preferences.getBoolean("regiDeptChecked", false);
        regiDept.setChecked(checked);
        if (regiDept.isChecked()) {
            regiDept.setTextColor(Color.parseColor("#000000"));

        } else {
            regiDept.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("regiSeniorSetting", 0);
        checked = preferences.getBoolean("regiSeniorChecked", false);
        regiSenior.setChecked(checked);
        if (regiSenior.isChecked()) {
            regiSenior.setTextColor(Color.parseColor("#000000"));

        } else {
            regiSenior.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("assistantSurgeonSetting", 0);
        checked = preferences.getBoolean("assistantSurgeonChecked", false);
        assistantSurgeon.setChecked(checked);
        if (assistantSurgeon.isChecked()) {
            assistantSurgeon.setTextColor(Color.parseColor("#000000"));

        } else {
            assistantSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("juniorConsultantSetting", 0);
        checked = preferences.getBoolean("juniorConsultantChecked", false);
        juniorConsultant.setChecked(checked);
        if (juniorConsultant.isChecked()) {
            juniorConsultant.setTextColor(Color.parseColor("#000000"));

        } else {
            juniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("seniorConsultantSetting", 0);
        checked = preferences.getBoolean("seniorConsultantChecked", false);
        seniorConsultant.setChecked(checked);
        if (seniorConsultant.isChecked()) {
            seniorConsultant.setTextColor(Color.parseColor("#000000"));

        } else {
            seniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("assistantProfessorSetting", 0);
        checked = preferences.getBoolean("assistantProfessorChecked", false);
        assistantProfessor.setChecked(checked);
        if (assistantProfessor.isChecked()) {
            assistantProfessor.setTextColor(Color.parseColor("#000000"));

        } else {
            assistantProfessor.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("associateProfessorSetting", 0);
        checked = preferences.getBoolean("associateProfessorChecked", false);
        associateProfessor.setChecked(checked);
        if (associateProfessor.isChecked()) {
            associateProfessor.setTextColor(Color.parseColor("#000000"));

        } else {
            associateProfessor.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("professorSetting", 0);
        checked = preferences.getBoolean("professorChecked", false);
        professor.setChecked(checked);
        if (professor.isChecked()) {
            professor.setTextColor(Color.parseColor("#000000"));

        } else {
            professor.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("civilSurgeonSetting", 0);
        checked = preferences.getBoolean("civilSurgeonChecked", false);
        civilSurgeon.setChecked(checked);
        if (civilSurgeon.isChecked()) {
            civilSurgeon.setTextColor(Color.parseColor("#000000"));

        } else {
            civilSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("adhocSetting", 0);
        checked = preferences.getBoolean("adhocChecked", false);
        adhoc.setChecked(checked);
        if (adhoc.isChecked()) {
            adhoc.setTextColor(Color.parseColor("#000000"));

        } else {
            adhoc.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("mohfwSetting", 0);
        checked = preferences.getBoolean("mohfwChecked", false);
        mohfw.setChecked(checked);
        if (mohfw.isChecked()) {
            mohfw.setTextColor(Color.parseColor("#000000"));

        } else {
            mohfw.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("deputationSetting", 0);
        checked = preferences.getBoolean("deputationChecked", false);
        deputation.setChecked(checked);
        if (deputation.isChecked()) {
            deputation.setTextColor(Color.parseColor("#000000"));

        } else {
            deputation.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences = getSharedPreferences("leaveSetting", 0);
        checked = preferences.getBoolean("leaveChecked", false);
        leave.setChecked(checked);
        if (leave.isChecked()) {
            leave.setTextColor(Color.parseColor("#000000"));

        } else {
            leave.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("leaveSetting", 0);
        checked = preferences.getBoolean("leaveChecked", false);
        leave.setChecked(checked);
        if (leave.isChecked()) {
            leave.setTextColor(Color.parseColor("#000000"));

        } else {
            leave.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("ccdSetting", 0);
        checked = preferences.getBoolean("ccdChecked", false);
        ccdBirdem.setChecked(checked);
        if (ccdBirdem.isChecked()) {
            ccdBirdem.setTextColor(Color.parseColor("#000000"));

        } else {
            ccdBirdem.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("dgfpSetting", 0);
        checked = preferences.getBoolean("dgfpChecked", false);
        dgfp.setChecked(checked);
        if (dgfp.isChecked()) {
            dgfp.setTextColor(Color.parseColor("#000000"));

        } else {
            dgfp.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("notificationSounds", 0);
        enableSound = preferences.getBoolean("notificationSoundChecked", false);
        sound.setChecked(enableSound);
        if (sound.isChecked()) {
            sound.setTextColor(Color.parseColor("#AA7E02"));

        } else {
            sound.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("vibrations", 0);
        enableVibrate = preferences.getBoolean("vibrationChecked", false);
        vibration.setChecked(enableVibrate);
        if (vibration.isChecked()) {
            vibration.setTextColor(Color.parseColor("#59038D"));

        } else {
            vibration.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("setting", 0);
        checked = preferences.getBoolean("checked", false);
        openInBrowser.setChecked(checked);
        if (openInBrowser.isChecked()) {
            openInBrowser.setTextColor(Color.parseColor("#890000"));

        } else {
            openInBrowser.setTextColor(Color.parseColor("#B4B4B4"));
        }

        preferences = getSharedPreferences("connectivity", 0);
        checked = preferences.getBoolean("connectivityChecked", false);
        connectivityAlert.setChecked(checked);
        if (connectivityAlert.isChecked()) {
            connectivityAlert.setTextColor(Color.parseColor("#890000"));

        } else {
            connectivityAlert.setTextColor(Color.parseColor("#B4B4B4"));
        }


        residency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("residencySetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (residency.isChecked()) {
                    residency.setChecked(true);
                    residency.setTextColor(Color.parseColor("#000000"));

                } else {
                    residency.setChecked(false);
                    residency.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (notice.isChecked()) {
                    notice.setChecked(true);
                    notice.setTextColor(Color.parseColor("#000000"));
                } else {
                    notice.setChecked(false);
                    notice.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (dghs.isChecked()) {
                    dghs.setChecked(true);
                    dghs.setTextColor(Color.parseColor("#000000"));
                } else {
                    dghs.setChecked(false);
                    dghs.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (reultBcs.isChecked()) {
                    reultBcs.setChecked(true);
                    reultBcs.setTextColor(Color.parseColor("#000000"));
                } else {
                    reultBcs.setChecked(false);
                    reultBcs.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (resultSenior.isChecked()) {
                    resultSenior.setChecked(true);
                    resultSenior.setTextColor(Color.parseColor("#000000"));
                } else {
                    resultSenior.setChecked(false);
                    resultSenior.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (resultDept.isChecked()) {
                    resultDept.setChecked(true);
                    resultDept.setTextColor(Color.parseColor("#000000"));
                } else {
                    resultDept.setChecked(false);
                    resultDept.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (regiDept.isChecked()) {
                    regiDept.setChecked(true);
                    regiDept.setTextColor(Color.parseColor("#000000"));
                } else {
                    regiDept.setChecked(false);
                    regiDept.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (regiSenior.isChecked()) {
                    regiSenior.setChecked(true);
                    regiSenior.setTextColor(Color.parseColor("#000000"));
                } else {
                    regiSenior.setChecked(false);
                    regiSenior.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (assistantSurgeon.isChecked()) {
                    assistantSurgeon.setChecked(true);
                    assistantSurgeon.setTextColor(Color.parseColor("#000000"));
                } else {
                    assistantSurgeon.setChecked(false);
                    assistantSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (juniorConsultant.isChecked()) {
                    juniorConsultant.setChecked(true);
                    juniorConsultant.setTextColor(Color.parseColor("#000000"));
                } else {
                    juniorConsultant.setChecked(false);
                    juniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (seniorConsultant.isChecked()) {
                    seniorConsultant.setChecked(true);
                    seniorConsultant.setTextColor(Color.parseColor("#000000"));
                } else {
                    seniorConsultant.setChecked(false);
                    seniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (assistantProfessor.isChecked()) {
                    assistantProfessor.setChecked(true);
                    assistantProfessor.setTextColor(Color.parseColor("#000000"));
                } else {
                    assistantProfessor.setChecked(false);
                    assistantProfessor.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (associateProfessor.isChecked()) {
                    associateProfessor.setChecked(true);
                    associateProfessor.setTextColor(Color.parseColor("#000000"));
                } else {
                    associateProfessor.setChecked(false);
                    associateProfessor.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (professor.isChecked()) {
                    professor.setChecked(true);
                    professor.setTextColor(Color.parseColor("#000000"));
                } else {
                    professor.setChecked(false);
                    professor.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (civilSurgeon.isChecked()) {
                    civilSurgeon.setChecked(true);
                    civilSurgeon.setTextColor(Color.parseColor("#000000"));
                } else {
                    civilSurgeon.setChecked(false);
                    civilSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (adhoc.isChecked()) {
                    adhoc.setChecked(true);
                    adhoc.setTextColor(Color.parseColor("#000000"));
                } else {
                    adhoc.setChecked(false);
                    adhoc.setTextColor(Color.parseColor("#B4B4B4"));
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
                if (mohfw.isChecked()) {
                    mohfw.setChecked(true);
                    mohfw.setTextColor(Color.parseColor("#000000"));
                } else {
                    mohfw.setChecked(false);
                    mohfw.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("mohfwChecked", mohfw.isChecked());
                editor.apply();
            }
        });

        deputation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("deputationSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (deputation.isChecked()) {
                    deputation.setChecked(true);
                    deputation.setTextColor(Color.parseColor("#000000"));
                } else {
                    deputation.setChecked(false);
                    deputation.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("deputationChecked", deputation.isChecked());
                editor.apply();
            }
        });

        leave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("leaveSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (leave.isChecked()) {
                    leave.setChecked(true);
                    leave.setTextColor(Color.parseColor("#000000"));
                } else {
                    leave.setChecked(false);
                    leave.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("leaveChecked", leave.isChecked());
                editor.apply();
            }
        });

        ccdBirdem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("ccdSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (ccdBirdem.isChecked()) {
                    ccdBirdem.setChecked(true);
                    ccdBirdem.setTextColor(Color.parseColor("#000000"));
                } else {
                    ccdBirdem.setChecked(false);
                    ccdBirdem.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("ccdChecked", ccdBirdem.isChecked());
                editor.apply();
            }
        });

        dgfp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("dgfpSetting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (dgfp.isChecked()) {
                    dgfp.setChecked(true);
                    dgfp.setTextColor(Color.parseColor("#000000"));
                } else {
                    dgfp.setChecked(false);
                    dgfp.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("dgfpChecked", dgfp.isChecked());
                editor.apply();
            }
        });
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences soundsettings = getSharedPreferences("notificationSounds", 0);
                SharedPreferences.Editor soundeditor = soundsettings.edit();
                if (sound.isChecked()) {
                    sound.setChecked(true);
                    sound.setTextColor(Color.parseColor("#AA7E02"));
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                    mp.start();
                } else {
                    sound.setChecked(false);
                    sound.setTextColor(Color.parseColor("#B4B4B4"));
                }
                soundeditor.putBoolean("notificationSoundChecked", sound.isChecked());
                soundeditor.apply();
            }
        });
        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences vibrationsettings = getSharedPreferences("vibrations", 0);
                SharedPreferences.Editor vibrateEditor = vibrationsettings.edit();
                if (vibration.isChecked()) {
                    vibration.setChecked(true);
                    vibration.setTextColor(Color.parseColor("#59038D"));
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(1000);
                        vibrator.vibrate(1000);
                    }
                } else {
                    vibration.setChecked(false);
                    vibration.setTextColor(Color.parseColor("#B4B4B4"));
                }
                vibrateEditor.putBoolean("vibrationChecked", vibration.isChecked());
                vibrateEditor.apply();
            }
        });
        openInBrowser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (openInBrowser.isChecked()) {
                    openInBrowser.setChecked(true);
                    openInBrowser.setTextColor(Color.parseColor("#890000"));
                } else {
                    openInBrowser.setChecked(false);
                    openInBrowser.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("checked", openInBrowser.isChecked());
                editor.apply();
            }
        });
        connectivityAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("connectivity", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (connectivityAlert.isChecked()) {
                    connectivityAlert.setChecked(true);
                    connectivityAlert.setTextColor(Color.parseColor("#890000"));
                } else {
                    connectivityAlert.setChecked(false);
                    connectivityAlert.setTextColor(Color.parseColor("#B4B4B4"));
                }
                editor.putBoolean("connectivityChecked", connectivityAlert.isChecked());
                editor.apply();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAll();
            }
        });
        deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deSelectAll();
            }
        });
    }

    public void selectDeselect(String preferenceName, CheckBox checkBoxName, boolean setTrueFalse, String putBooleanName) {
        SharedPreferences settings = getSharedPreferences(preferenceName, 0);
        SharedPreferences.Editor editor = settings.edit();
        checkBoxName.setChecked(setTrueFalse);
        if (setTrueFalse) {
            checkBoxName.setTextColor(Color.parseColor("#000000"));

        } else {
            checkBoxName.setTextColor(Color.parseColor("#B4B4B4"));
        }
        editor.putBoolean(putBooleanName, checkBoxName.isChecked());
        editor.apply();
    }

    public void selectAll() {
        selectDeselect("residencySetting", residency, true, "residencyChecked");
        selectDeselect("noticeSetting", notice, true, "noticeChecked");
        selectDeselect("dghsSetting", dghs, true, "dghsChecked");
        selectDeselect("reultBcsSetting", reultBcs, true, "reultBcsChecked");
        selectDeselect("resultDeptSetting", resultDept, true, "resultDeptChecked");
        selectDeselect("resultSeniorSetting", resultSenior, true, "resultSeniorChecked");
        selectDeselect("regiDeptSetting", regiDept, true, "regiDeptChecked");
        selectDeselect("regiSeniorSetting", regiSenior, true, "regiSeniorChecked");
        selectDeselect("assistantSurgeonSetting", assistantSurgeon, true, "assistantSurgeonChecked");
        selectDeselect("juniorConsultantSetting", juniorConsultant, true, "juniorConsultantChecked");
        selectDeselect("seniorConsultantSetting", seniorConsultant, true, "seniorConsultantChecked");
        selectDeselect("assistantProfessorSetting", assistantProfessor, true, "assistantProfessorChecked");
        selectDeselect("associateProfessorSetting", associateProfessor, true, "associateProfessorChecked");
        selectDeselect("professorSetting", professor, true, "professorChecked");
        selectDeselect("civilSurgeonSetting", civilSurgeon, true, "civilSurgeonChecked");
        selectDeselect("adhocSetting", adhoc, true, "adhocChecked");
        selectDeselect("mohfwSetting", mohfw, true, "mohfwChecked");
        selectDeselect("deputationSetting", deputation, true, "deputationChecked");
        selectDeselect("leaveSetting", leave, true, "leaveChecked");
        selectDeselect("ccdSetting", ccdBirdem, true, "ccdChecked");
        selectDeselect("dgfpSetting", dgfp, true, "dgfpChecked");

    }

    public void deSelectAll() {
        selectDeselect("residencySetting", residency, false, "residencyChecked");
        selectDeselect("noticeSetting", notice, false, "noticeChecked");
        selectDeselect("dghsSetting", dghs, false, "dghsChecked");
        selectDeselect("reultBcsSetting", reultBcs, false, "reultBcsChecked");
        selectDeselect("resultDeptSetting", resultDept, false, "resultDeptChecked");
        selectDeselect("resultSeniorSetting", resultSenior, false, "resultSeniorChecked");
        selectDeselect("regiDeptSetting", regiDept, false, "regiDeptChecked");
        selectDeselect("regiSeniorSetting", regiSenior, false, "regiSeniorChecked");
        selectDeselect("assistantSurgeonSetting", assistantSurgeon, false, "assistantSurgeonChecked");
        selectDeselect("juniorConsultantSetting", juniorConsultant, false, "juniorConsultantChecked");
        selectDeselect("seniorConsultantSetting", seniorConsultant, false, "seniorConsultantChecked");
        selectDeselect("assistantProfessorSetting", assistantProfessor, false, "assistantProfessorChecked");
        selectDeselect("associateProfessorSetting", associateProfessor, false, "associateProfessorChecked");
        selectDeselect("professorSetting", professor, false, "professorChecked");
        selectDeselect("civilSurgeonSetting", civilSurgeon, false, "civilSurgeonChecked");
        selectDeselect("adhocSetting", adhoc, false, "adhocChecked");
        selectDeselect("mohfwSetting", mohfw, false, "mohfwChecked");
        selectDeselect("deputationSetting", deputation, false, "deputationChecked");
        selectDeselect("leaveSetting", leave, false, "leaveChecked");
        selectDeselect("ccdSetting", ccdBirdem, false, "ccdChecked");
        selectDeselect("dgfpSetting", dgfp, false, "dgfpChecked");
    }
}



