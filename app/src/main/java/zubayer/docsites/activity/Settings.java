package zubayer.docsites.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import me.anwarshahriar.calligrapher.Calligrapher;
import zubayer.docsites.R;

public class Settings extends Activity {

    CheckBox residency, notice, dghs, reultBcs, resultDept, resultSenior, regiDept, regiSenior, assistantSurgeon, juniorConsultant,
            seniorConsultant, assistantProfessor, associateProfessor, professor, civilSurgeon, adhoc, mohfw, deputation, leave,
            dgfp, ccdBirdem;
    SharedPreferences preferences;
    boolean checked;
    TextView heading, select, deselect;

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
        dgfp = (CheckBox) findViewById(R.id.dgfpSetting);
        ccdBirdem = (CheckBox) findViewById(R.id.ccdSetting);

        select = (TextView) findViewById(R.id.select);
        deselect = (TextView) findViewById(R.id.deselect);

        heading = (TextView) findViewById(R.id.settingHeading);
        int density = getResources().getDisplayMetrics().densityDpi;
        if (density <= DisplayMetrics.DENSITY_HIGH) {
            heading.setTextSize(20);
        }
        preferences = getSharedPreferences("notification", Context.MODE_PRIVATE);
        checked = preferences.getBoolean("residencyChecked", false);
        residency.setChecked(checked);
        if (residency.isChecked()) {
            residency.setTextColor(Color.parseColor("#000000"));

        } else {
            residency.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("noticeChecked", false);
        notice.setChecked(checked);
        if (notice.isChecked()) {
            notice.setTextColor(Color.parseColor("#000000"));

        } else {
            notice.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("dghsChecked", false);
        dghs.setChecked(checked);
        if (dghs.isChecked()) {
            dghs.setTextColor(Color.parseColor("#000000"));

        } else {
            dghs.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("reultBcsChecked", false);
        reultBcs.setChecked(checked);
        if (reultBcs.isChecked()) {
            reultBcs.setTextColor(Color.parseColor("#000000"));

        } else {
            reultBcs.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("resultDeptChecked", false);
        resultDept.setChecked(checked);
        if (resultDept.isChecked()) {
            resultDept.setTextColor(Color.parseColor("#000000"));

        } else {
            resultDept.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("resultSeniorChecked", false);
        resultSenior.setChecked(checked);
        if (resultSenior.isChecked()) {
            resultSenior.setTextColor(Color.parseColor("#000000"));

        } else {
            resultSenior.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("regiDeptChecked", false);
        regiDept.setChecked(checked);
        if (regiDept.isChecked()) {
            regiDept.setTextColor(Color.parseColor("#000000"));

        } else {
            regiDept.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("regiSeniorChecked", false);
        regiSenior.setChecked(checked);
        if (regiSenior.isChecked()) {
            regiSenior.setTextColor(Color.parseColor("#000000"));

        } else {
            regiSenior.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("assistantSurgeonChecked", false);
        assistantSurgeon.setChecked(checked);
        if (assistantSurgeon.isChecked()) {
            assistantSurgeon.setTextColor(Color.parseColor("#000000"));

        } else {
            assistantSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("juniorConsultantChecked", false);
        juniorConsultant.setChecked(checked);
        if (juniorConsultant.isChecked()) {
            juniorConsultant.setTextColor(Color.parseColor("#000000"));

        } else {
            juniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("seniorConsultantChecked", false);
        seniorConsultant.setChecked(checked);
        if (seniorConsultant.isChecked()) {
            seniorConsultant.setTextColor(Color.parseColor("#000000"));

        } else {
            seniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("assistantProfessorChecked", false);
        assistantProfessor.setChecked(checked);
        if (assistantProfessor.isChecked()) {
            assistantProfessor.setTextColor(Color.parseColor("#000000"));

        } else {
            assistantProfessor.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("associateProfessorChecked", false);
        associateProfessor.setChecked(checked);
        if (associateProfessor.isChecked()) {
            associateProfessor.setTextColor(Color.parseColor("#000000"));

        } else {
            associateProfessor.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("professorChecked", false);
        professor.setChecked(checked);
        if (professor.isChecked()) {
            professor.setTextColor(Color.parseColor("#000000"));

        } else {
            professor.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("civilSurgeonChecked", false);
        civilSurgeon.setChecked(checked);
        if (civilSurgeon.isChecked()) {
            civilSurgeon.setTextColor(Color.parseColor("#000000"));

        } else {
            civilSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("adhocChecked", false);
        adhoc.setChecked(checked);
        if (adhoc.isChecked()) {
            adhoc.setTextColor(Color.parseColor("#000000"));

        } else {
            adhoc.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("mohfwChecked", false);
        mohfw.setChecked(checked);
        if (mohfw.isChecked()) {
            mohfw.setTextColor(Color.parseColor("#000000"));

        } else {
            mohfw.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("deputationChecked", false);
        deputation.setChecked(checked);
        if (deputation.isChecked()) {
            deputation.setTextColor(Color.parseColor("#000000"));

        } else {
            deputation.setTextColor(Color.parseColor("#B4B4B4"));
        }

        checked = preferences.getBoolean("leaveChecked", false);
        leave.setChecked(checked);
        if (leave.isChecked()) {
            leave.setTextColor(Color.parseColor("#000000"));

        } else {
            leave.setTextColor(Color.parseColor("#B4B4B4"));
        }


        checked = preferences.getBoolean("leaveChecked", false);
        leave.setChecked(checked);
        if (leave.isChecked()) {
            leave.setTextColor(Color.parseColor("#000000"));

        } else {
            leave.setTextColor(Color.parseColor("#B4B4B4"));
        }


        checked = preferences.getBoolean("ccdChecked", false);
        ccdBirdem.setChecked(checked);
        if (ccdBirdem.isChecked()) {
            ccdBirdem.setTextColor(Color.parseColor("#000000"));

        } else {
            ccdBirdem.setTextColor(Color.parseColor("#B4B4B4"));
        }


        checked = preferences.getBoolean("dgfpChecked", false);
        dgfp.setChecked(checked);
        if (dgfp.isChecked()) {
            dgfp.setTextColor(Color.parseColor("#000000"));

        } else {
            dgfp.setTextColor(Color.parseColor("#B4B4B4"));
        }

        residency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (residency.isChecked()) {
                    residency.setChecked(true);
                    residency.setTextColor(Color.parseColor("#000000"));

                } else {
                    residency.setChecked(false);
                    residency.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("residencyChecked", residency.isChecked()).apply();
            }
        });

        notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (notice.isChecked()) {
                    notice.setChecked(true);
                    notice.setTextColor(Color.parseColor("#000000"));
                } else {
                    notice.setChecked(false);
                    notice.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("noticeChecked", notice.isChecked()).apply();
            }
        });

        dghs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dghs.isChecked()) {
                    dghs.setChecked(true);
                    dghs.setTextColor(Color.parseColor("#000000"));
                } else {
                    dghs.setChecked(false);
                    dghs.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("dghsChecked", dghs.isChecked()).apply();
            }
        });

        reultBcs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (reultBcs.isChecked()) {
                    reultBcs.setChecked(true);
                    reultBcs.setTextColor(Color.parseColor("#000000"));
                } else {
                    reultBcs.setChecked(false);
                    reultBcs.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("reultBcsChecked", reultBcs.isChecked()).apply();
            }
        });

        resultSenior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (resultSenior.isChecked()) {
                    resultSenior.setChecked(true);
                    resultSenior.setTextColor(Color.parseColor("#000000"));
                } else {
                    resultSenior.setChecked(false);
                    resultSenior.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("resultSeniorChecked", resultSenior.isChecked()).apply();
            }
        });

        resultDept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (resultDept.isChecked()) {
                    resultDept.setChecked(true);
                    resultDept.setTextColor(Color.parseColor("#000000"));
                } else {
                    resultDept.setChecked(false);
                    resultDept.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("resultDeptChecked", resultDept.isChecked()).apply();
            }
        });

        regiDept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (regiDept.isChecked()) {
                    regiDept.setChecked(true);
                    regiDept.setTextColor(Color.parseColor("#000000"));
                } else {
                    regiDept.setChecked(false);
                    regiDept.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("regiDeptChecked", regiDept.isChecked()).apply();
            }
        });

        regiSenior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (regiSenior.isChecked()) {
                    regiSenior.setChecked(true);
                    regiSenior.setTextColor(Color.parseColor("#000000"));
                } else {
                    regiSenior.setChecked(false);
                    regiSenior.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("regiSeniorChecked", regiSenior.isChecked()).apply();
            }
        });

        assistantSurgeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (assistantSurgeon.isChecked()) {
                    assistantSurgeon.setChecked(true);
                    assistantSurgeon.setTextColor(Color.parseColor("#000000"));
                } else {
                    assistantSurgeon.setChecked(false);
                    assistantSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("assistantSurgeonChecked", assistantSurgeon.isChecked()).apply();
            }
        });

        juniorConsultant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (juniorConsultant.isChecked()) {
                    juniorConsultant.setChecked(true);
                    juniorConsultant.setTextColor(Color.parseColor("#000000"));
                } else {
                    juniorConsultant.setChecked(false);
                    juniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("juniorConsultantChecked", juniorConsultant.isChecked()).apply();
            }
        });

        seniorConsultant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (seniorConsultant.isChecked()) {
                    seniorConsultant.setChecked(true);
                    seniorConsultant.setTextColor(Color.parseColor("#000000"));
                } else {
                    seniorConsultant.setChecked(false);
                    seniorConsultant.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("seniorConsultantChecked", seniorConsultant.isChecked()).apply();
            }
        });

        assistantProfessor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (assistantProfessor.isChecked()) {
                    assistantProfessor.setChecked(true);
                    assistantProfessor.setTextColor(Color.parseColor("#000000"));
                } else {
                    assistantProfessor.setChecked(false);
                    assistantProfessor.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("assistantProfessorChecked", assistantProfessor.isChecked()).apply();
            }
        });

        associateProfessor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (associateProfessor.isChecked()) {
                    associateProfessor.setChecked(true);
                    associateProfessor.setTextColor(Color.parseColor("#000000"));
                } else {
                    associateProfessor.setChecked(false);
                    associateProfessor.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("associateProfessorChecked", associateProfessor.isChecked()).apply();
            }
        });

        professor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (professor.isChecked()) {
                    professor.setChecked(true);
                    professor.setTextColor(Color.parseColor("#000000"));
                } else {
                    professor.setChecked(false);
                    professor.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("professorChecked", professor.isChecked()).apply();
            }
        });

        civilSurgeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (civilSurgeon.isChecked()) {
                    civilSurgeon.setChecked(true);
                    civilSurgeon.setTextColor(Color.parseColor("#000000"));
                } else {
                    civilSurgeon.setChecked(false);
                    civilSurgeon.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("civilSurgeonChecked", civilSurgeon.isChecked()).apply();
            }
        });

        adhoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (adhoc.isChecked()) {
                    adhoc.setChecked(true);
                    adhoc.setTextColor(Color.parseColor("#000000"));
                } else {
                    adhoc.setChecked(false);
                    adhoc.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("adhocChecked", adhoc.isChecked()).apply();
            }
        });

        mohfw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mohfw.isChecked()) {
                    mohfw.setChecked(true);
                    mohfw.setTextColor(Color.parseColor("#000000"));
                } else {
                    mohfw.setChecked(false);
                    mohfw.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("mohfwChecked", mohfw.isChecked()).apply();
            }
        });

        deputation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (deputation.isChecked()) {
                    deputation.setChecked(true);
                    deputation.setTextColor(Color.parseColor("#000000"));
                } else {
                    deputation.setChecked(false);
                    deputation.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("deputationChecked", deputation.isChecked()).apply();
            }
        });

        leave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (leave.isChecked()) {
                    leave.setChecked(true);
                    leave.setTextColor(Color.parseColor("#000000"));
                } else {
                    leave.setChecked(false);
                    leave.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("leaveChecked", leave.isChecked()).apply();
            }
        });

        ccdBirdem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ccdBirdem.isChecked()) {
                    ccdBirdem.setChecked(true);
                    ccdBirdem.setTextColor(Color.parseColor("#000000"));
                } else {
                    ccdBirdem.setChecked(false);
                    ccdBirdem.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("ccdChecked", ccdBirdem.isChecked()).apply();
            }
        });

        dgfp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dgfp.isChecked()) {
                    dgfp.setChecked(true);
                    dgfp.setTextColor(Color.parseColor("#000000"));
                } else {
                    dgfp.setChecked(false);
                    dgfp.setTextColor(Color.parseColor("#B4B4B4"));
                }
                preferences.edit().putBoolean("dgfpChecked", dgfp.isChecked()).apply();
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

    public void selectDeselect(CheckBox checkBoxName, boolean setTrueFalse, String putBooleanName) {

        checkBoxName.setChecked(setTrueFalse);
        if (setTrueFalse) {
            checkBoxName.setTextColor(Color.parseColor("#000000"));

        } else {
            checkBoxName.setTextColor(Color.parseColor("#B4B4B4"));
        }
        preferences.edit().putBoolean(putBooleanName, checkBoxName.isChecked()).apply();
        preferences.edit().apply();
    }

    public void selectAll() {
        selectDeselect(residency, true, "residencyChecked");
        selectDeselect(notice, true, "noticeChecked");
        selectDeselect(dghs, true, "dghsChecked");
        selectDeselect(reultBcs, true, "reultBcsChecked");
        selectDeselect(resultDept, true, "resultDeptChecked");
        selectDeselect(resultSenior, true, "resultSeniorChecked");
        selectDeselect(regiDept, true, "regiDeptChecked");
        selectDeselect(regiSenior, true, "regiSeniorChecked");
        selectDeselect(assistantSurgeon, true, "assistantSurgeonChecked");
        selectDeselect(juniorConsultant, true, "juniorConsultantChecked");
        selectDeselect(seniorConsultant, true, "seniorConsultantChecked");
        selectDeselect(assistantProfessor, true, "assistantProfessorChecked");
        selectDeselect(associateProfessor, true, "associateProfessorChecked");
        selectDeselect(professor, true, "professorChecked");
        selectDeselect(civilSurgeon, true, "civilSurgeonChecked");
        selectDeselect(adhoc, true, "adhocChecked");
        selectDeselect(mohfw, true, "mohfwChecked");
        selectDeselect(deputation, true, "deputationChecked");
        selectDeselect(leave, true, "leaveChecked");
        selectDeselect(ccdBirdem, true, "ccdChecked");
        selectDeselect(dgfp, true, "dgfpChecked");

    }

    public void deSelectAll() {
        selectDeselect(residency, false, "residencyChecked");
        selectDeselect(notice, false, "noticeChecked");
        selectDeselect(dghs, false, "dghsChecked");
        selectDeselect(reultBcs, false, "reultBcsChecked");
        selectDeselect(resultDept, false, "resultDeptChecked");
        selectDeselect(resultSenior, false, "resultSeniorChecked");
        selectDeselect(regiDept, false, "regiDeptChecked");
        selectDeselect(regiSenior, false, "regiSeniorChecked");
        selectDeselect(assistantSurgeon, false, "assistantSurgeonChecked");
        selectDeselect(juniorConsultant, false, "juniorConsultantChecked");
        selectDeselect(seniorConsultant, false, "seniorConsultantChecked");
        selectDeselect(assistantProfessor, false, "assistantProfessorChecked");
        selectDeselect(associateProfessor, false, "associateProfessorChecked");
        selectDeselect(professor, false, "professorChecked");
        selectDeselect(civilSurgeon, false, "civilSurgeonChecked");
        selectDeselect(adhoc, false, "adhocChecked");
        selectDeselect(mohfw, false, "mohfwChecked");
        selectDeselect(deputation, false, "deputationChecked");
        selectDeselect(leave, false, "leaveChecked");
        selectDeselect(ccdBirdem, false, "ccdChecked");
        selectDeselect(dgfp, false, "dgfpChecked");
    }
}



