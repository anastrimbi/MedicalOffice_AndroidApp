package com.example.cabinetmedicinadefamiliefericita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cabinetmedicinadefamiliefericita.analize.AnalizeActivity;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.cadremedicale.CadreMedicaleActivity;
import com.example.cabinetmedicinadefamiliefericita.consultatii.ConsultatiiActivity;
import com.example.cabinetmedicinadefamiliefericita.fragments.AboutDialogFragment;
import com.example.cabinetmedicinadefamiliefericita.fragments.RaportConsultatieDialogFragment;
import com.example.cabinetmedicinadefamiliefericita.fragments.SettingsDialogFragment;
import com.example.cabinetmedicinadefamiliefericita.pacienti.PacientiActivity;
import com.example.cabinetmedicinadefamiliefericita.retea.HttpManager;
import com.example.cabinetmedicinadefamiliefericita.welcome.InitialDataJSONParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Callable;

public class WelcomeActivity extends AppCompatActivity {
    private static final int CADRE_REQUEST_CODE = 101;
    private static final int PACIENTI_REQUEST_CODE = 102;
    private static final int CONSULTATII_REQUEST_CODE = 103;
    private static final int ANALIZE_REQUEST_CODE = 104;
    private static final String INITIAL_DATA_JSON = "https://jsonkeeper.com/b/KHSM";

    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private Button cadreMedicaleBtn;
    private Button pacientiBtn;
    private Button consultatiiBtn;
    private Button analizeBtn;
    private FloatingActionButton fabSettings;
    private FloatingActionButton fabAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initializeComponents();
        // De preferat ar fi sa avem un buton prin care resetam db-ul la date initiale din json
        // dar acum nu mai e timp asa ca rulam preluarea si reintroducerea datelor in db
        // la fiecare deschidere de aplicatie... -- operatiile vor inlocui datele sau vor se vor anula
        preluareDateInitialeJSON();
    }

    private void initializeComponents() {
        cadreMedicaleBtn = findViewById(R.id.welcome_cadreMedicale_btn);
        pacientiBtn = findViewById(R.id.welcome_pacienti_btn);
        consultatiiBtn = findViewById(R.id.welcome_consultatii_btn);
        analizeBtn = findViewById(R.id.welcome_analize_btn);
        fabSettings = findViewById(R.id.fab_welcome_settings);
        fabAboutUs = findViewById(R.id.fab_welcome_about_us);

        cadreMedicaleBtn.setOnClickListener(cadreMedicaleBtnEventListener());
        pacientiBtn.setOnClickListener(pacientiBtnEventListener());
        consultatiiBtn.setOnClickListener(consultatiiBtnEventListener());
        analizeBtn.setOnClickListener(analizeBtnEventListener());
        fabSettings.setOnClickListener(settingsEventListener());
        fabAboutUs.setOnClickListener(aboutUsEventListener());
    }


    private void preluareDateInitialeJSON() {
        Callable<String> asyncOperation = new HttpManager(INITIAL_DATA_JSON);
        Callback<String> mainThreadOperation = prelucrareDateInitialeJSON();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
    }

    private Callback<String> prelucrareDateInitialeJSON() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                InitialDataJSONParser.fromJson(result, getApplicationContext());
            }
        };
    }

    private View.OnClickListener aboutUsEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long idClinica = 1;
                AboutDialogFragment.newInstance(idClinica).show(getSupportFragmentManager(), "dialog");
            }
        };
    }

    private View.OnClickListener settingsEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
            }
        };
    }

    private View.OnClickListener cadreMedicaleBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadreMedicaleActivity.class);
                startActivityForResult(intent, CADRE_REQUEST_CODE);
            }
        };
    }

    private View.OnClickListener pacientiBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PacientiActivity.class);
                startActivityForResult(intent, PACIENTI_REQUEST_CODE);
            }
        };
    }

    private View.OnClickListener consultatiiBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsultatiiActivity.class);
                startActivityForResult(intent, CONSULTATII_REQUEST_CODE);
            }
        };
    }

    private View.OnClickListener analizeBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnalizeActivity.class);
                startActivityForResult(intent, ANALIZE_REQUEST_CODE);
            }
        };
    }
}