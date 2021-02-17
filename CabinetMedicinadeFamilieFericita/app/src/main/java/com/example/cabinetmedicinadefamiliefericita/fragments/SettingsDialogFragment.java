package com.example.cabinetmedicinadefamiliefericita.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.cadremedicale.CadreMedicaleActivity;
import com.example.cabinetmedicinadefamiliefericita.database.FirebaseDatabase;
import com.example.cabinetmedicinadefamiliefericita.settings.AppSettings;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsDialogFragment extends BottomSheetDialogFragment {
    private FirebaseDatabase firebaseDatabase;
    private AppSettings appSettings;
    private CheckBox programariWeCbox;
    private CheckBox medicPrimarVarstniciCbox;
    private NumberPicker oraMaximaAnalizeNp;
    private ImageView saveToFirebaseIv;
    private ImageView restoreFromFirebaseIv;

    public static SettingsDialogFragment newInstance() {
        final SettingsDialogFragment fragment = new SettingsDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        programariWeCbox = view.findViewById(R.id.intrebare_1_checkBox);
        medicPrimarVarstniciCbox = view.findViewById(R.id.intrebare_2_checkBox);
        oraMaximaAnalizeNp = view.findViewById(R.id.oraMaximaAnalize);
        saveToFirebaseIv = view.findViewById(R.id.settings_sync_save);
        restoreFromFirebaseIv = view.findViewById(R.id.settings_sync_restore);

        firebaseDatabase = FirebaseDatabase.getInstance();
        appSettings = new AppSettings(getContext());

        saveToFirebaseIv.setOnClickListener(saveToFirebaseEventListener());
        restoreFromFirebaseIv.setOnClickListener(restoreFromFirebaseEventListener());

        createViewFromSharablePreferances();
    }

    private View.OnClickListener saveToFirebaseEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.upsert(appSettings);
            }
        };
    }

    private View.OnClickListener restoreFromFirebaseEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.attachDataChangeEventListener(dataChangeCallback(), appSettings.getId());
            }
        };
    }

    private Callback<AppSettings> dataChangeCallback() {
        return new Callback<AppSettings>() {
            @Override
            public void runResultOnUiThread(AppSettings result) {
                //primim raspunsul de la attachDataChangeEventListener
                //declansat de fiecare data cand se produc modificari asupra bazei de date
                if (result != null) {
                    appSettings.setProgramWe(result.isProgramWe());
                    appSettings.setVarstniciDoarMedicPrimar(result.isVarstniciDoarMedicPrimar());
                    appSettings.setOraMaximaAnalize(result.getOraMaximaAnalize());
                    populareViewCuDateSalvate();
                }
            }
        };
    }

    private void populareViewCuDateSalvate() {
        // populam setarile cu valori din shared preferences
        programariWeCbox.setChecked(appSettings.isProgramWe());
        medicPrimarVarstniciCbox.setChecked(appSettings.isVarstniciDoarMedicPrimar());
        oraMaximaAnalizeNp.setValue(appSettings.getOraMaximaAnalizeForNumberPicker());
    }

    private void createViewFromSharablePreferances() {
        // setup number picker for ora maxima analize
        oraMaximaAnalizeNp.setWrapSelectorWheel(false);
        oraMaximaAnalizeNp.setMinValue(0);
        oraMaximaAnalizeNp.setMaxValue(AppSettings.ORA_MAXIMA_ANALIZE_VALUES.length - 1);
        oraMaximaAnalizeNp.setDisplayedValues(AppSettings.ORA_MAXIMA_ANALIZE_VALUES);

        // populam setarile cu valori din shared preferences
        populareViewCuDateSalvate();

        // salvam setarile imediat ce detectam o schimbare
        programariWeCbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setProgramWe(isChecked);
                Toast.makeText(getContext(), R.string.settings_save, Toast.LENGTH_SHORT).show();
            }
        });

        medicPrimarVarstniciCbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setVarstniciDoarMedicPrimar(isChecked);
                Toast.makeText(getContext(), R.string.settings_save, Toast.LENGTH_SHORT).show();
            }
        });

        oraMaximaAnalizeNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                appSettings.setOraMaximaAnalizeFromNumberPicker(newVal);
                Toast.makeText(getContext(), R.string.settings_save, Toast.LENGTH_SHORT).show();
            }
        });
    }
}