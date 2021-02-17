package com.example.cabinetmedicinadefamiliefericita.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class AppSettings implements Serializable {
    public static final String SHARED_PREF_FILE_NAME = "cabinetSharedPref";
    public static final String PROGRAM_WE = "program_weekend";
    public static final String VARSTNICI_DOAR_MEDIC_PRIMAR = "varstnici_doar_medic_primar";
    public static final String ORA_MAXIMA_ANALIZE = "ora_maxima_analize";
    public static final String[] ORA_MAXIMA_ANALIZE_VALUES = new String [] {"7","8","9","10","11","12","13"};

    // valori default pt setarile din shared pref.
    public static final boolean PROGRAM_WE_DEFAULT = true;
    public static final boolean VARSTNICI_DOAR_MEDIC_PRIMAR_DEFAULT = false;
    public static final int ORA_MAXIMA_ANALIZE_DEFAULT_VAL_POS = 4;

    private SharedPreferences preferences;
    private String id = "app_1";
    private boolean programWe;
    private boolean varstniciDoarMedicPrimar;
    private int oraMaximaAnalize = 11;

    public AppSettings() {}

    public AppSettings(Context context) {
        this.preferences = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        this.programWe = preferences.getBoolean(PROGRAM_WE, PROGRAM_WE_DEFAULT);
        this.varstniciDoarMedicPrimar = preferences.getBoolean(VARSTNICI_DOAR_MEDIC_PRIMAR, VARSTNICI_DOAR_MEDIC_PRIMAR_DEFAULT);
        this.oraMaximaAnalize = preferences.getInt(ORA_MAXIMA_ANALIZE, Integer.parseInt(ORA_MAXIMA_ANALIZE_VALUES[ORA_MAXIMA_ANALIZE_DEFAULT_VAL_POS]));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isProgramWe() {
        return programWe;
    }

    public void setProgramWe(boolean programWe) {
        if (this.preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PROGRAM_WE, programWe);
            editor.apply();
        }

        this.programWe = programWe;
    }

    public boolean isVarstniciDoarMedicPrimar() {
        return varstniciDoarMedicPrimar;
    }

    public void setVarstniciDoarMedicPrimar(boolean varstniciDoarMedicPrimar) {
        if (this.preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(VARSTNICI_DOAR_MEDIC_PRIMAR, varstniciDoarMedicPrimar);
            editor.apply();
        }

        this.varstniciDoarMedicPrimar = varstniciDoarMedicPrimar;
    }

    public int getOraMaximaAnalize() {
        return oraMaximaAnalize;
    }

    public void setOraMaximaAnalize(int oraMaximaAnalize) {
        if (this.preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(ORA_MAXIMA_ANALIZE, oraMaximaAnalize);
            editor.apply();
        }

        this.oraMaximaAnalize = oraMaximaAnalize;
    }

    @Exclude
    public int getOraMaximaAnalizeForNumberPicker() {
        // pentru numberPicker ne trebuie indexul din ORA_MAXIMA_ANALIZE_VALUES asociat valorii
        int oraMaximaAnalizeIndex = -1;

        for (int i = 0; i < ORA_MAXIMA_ANALIZE_VALUES.length; i++) {
            if (ORA_MAXIMA_ANALIZE_VALUES[i].equals(String.valueOf(this.getOraMaximaAnalize()))) {
                oraMaximaAnalizeIndex = i;
                break;
            }
        }

        return oraMaximaAnalizeIndex;
    }

    public void setOraMaximaAnalizeFromNumberPicker(int oraMaximaAnalizeNumberPickerValue) {
        // valoarea selectata din NumberPicker e indexul din ORA_MAXIMA_ANALIZE_VALUES, deci trebuie
        // sa luam valoarea pe baza indexului
        int oraMaximaAnalizeValue = Integer.valueOf(ORA_MAXIMA_ANALIZE_VALUES[oraMaximaAnalizeNumberPickerValue]);
        this.setOraMaximaAnalize(oraMaximaAnalizeValue);
    }

    @Override
    public String toString() {
        return "AppSettings{" +
                "id='" + id + '\'' +
                ", programWe=" + programWe +
                ", varstniciDoarMedicPrimar=" + varstniciDoarMedicPrimar +
                ", oraMaximaAnalize=" + oraMaximaAnalize +
                '}';
    }
}
