package com.example.cabinetmedicinadefamiliefericita.analize;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.database.service.PacientService;
import com.example.cabinetmedicinadefamiliefericita.enums.TesteAnaliza;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class AdaugaAnalizaActivity extends AppCompatActivity {
    public static final String NEW_ANALIZA_KEY = "new_analiza_key";

    private Spinner spnNumeAsistent;
    private Spinner spnNumePacient;
    private DatePicker dpData;
    private TimePicker tpOra;
    private Spinner spnTest;
    private Button btnSave;

    private CadruMedicalService cadruMedicalService;
    private PacientService pacientService;
    private ArrayList<CadruMedical> listaAsistenti = new ArrayList<CadruMedical>();
    private ArrayList<Pacient> listaPacienti = new ArrayList<Pacient>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_analiza);

        intent = getIntent();
        cadruMedicalService = new CadruMedicalService(getApplicationContext());
        cadruMedicalService.getAllAsistenti(getAllAsistentiFromDbCallback());
        pacientService = new PacientService(getApplicationContext());
        pacientService.getAll(getAllPacientiFromDbCallback());
        initializeComponents();
    }

    private Callback<List<CadruMedical>> getAllAsistentiFromDbCallback() {
        return new Callback<List<CadruMedical>>() {
            @Override
            public void runResultOnUiThread(List<CadruMedical> result) {
                if (result != null) {
                    listaAsistenti.clear();
                    listaAsistenti.addAll(result);
                    populareSpinnerNumeAsistent();
                }
            }
        };
    }

    private Callback<List<Pacient>> getAllPacientiFromDbCallback() {
        return new Callback<List<Pacient>>() {
            @Override
            public void runResultOnUiThread(List<Pacient> result) {
                if (result != null) {
                    listaPacienti.clear();
                    listaPacienti.addAll(result);
                    populareSpinnerNumePacient();
                }
            }
        };
    }


    private void initializeComponents() {
        spnNumeAsistent = findViewById(R.id.adauga_analiza_spn_id_asistent);
        spnNumePacient = findViewById(R.id.adauga_analiza_spn_id_pacient);
        spnTest = findViewById(R.id.adauga_analiza_spn_test);

        dpData = findViewById(R.id.adauga_analiza_dp_data);
        tpOra = findViewById(R.id.adauga_analiza_tp_ora);
        tpOra.setIs24HourView(true);

        btnSave = findViewById(R.id.adauga_analiza_btn_save);
        btnSave.setOnClickListener(salveazaBtnEventListener());

        populareSpinnerTest();
    }

    private View.OnClickListener salveazaBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Analiza analiza = creeazaAnalizaDinViews();
                    intent.putExtra(NEW_ANALIZA_KEY, analiza);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        };
    }


    private void populareSpinnerTest() {
        // populare spinner cu adapter
        List<String> itemListAdapter = TesteAnaliza.toSpinnerList(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTest.setAdapter(adapter);
    }

    private void populareSpinnerNumeAsistent() {
        // populare spinner cu adapter
        List<String> itemListAdapter = new ArrayList<>();
        for (CadruMedical asistent : listaAsistenti) itemListAdapter.add(asistent.getNume());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNumeAsistent.setAdapter(adapter);
    }

    private void populareSpinnerNumePacient() {
        // populare spinner cu adapter
        List<String> itemListAdapter = new ArrayList<>();
        for (Pacient pacient : listaPacienti) itemListAdapter.add(pacient.getNume());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNumePacient.setAdapter(adapter);
    }

    private Analiza creeazaAnalizaDinViews() {
        String numeAsistent = spnNumeAsistent.getSelectedItem().toString();
        String numePacient = spnNumePacient.getSelectedItem().toString();

        long idAsistent = getIdAsistentFromSpinnerByNume(numeAsistent);
        long idPacient = getIdPacientFromSpinnerByNume(numePacient);
        long dataOra = DataUtils.dpTpToTimestamp(dpData, tpOra);
        TesteAnaliza test = TesteAnaliza.fromSpinnerSelection(getApplicationContext(), spnTest.getSelectedItem().toString());

        return new Analiza(idAsistent, idPacient, dataOra , test);
    }

    private long getIdAsistentFromSpinnerByNume(String numeAsistent) {
        for (CadruMedical asistent : listaAsistenti) {
            if (asistent.getNume().equals(numeAsistent)) {
                return asistent.getId();
            }
        }

        return -1;
    }

    private long getIdPacientFromSpinnerByNume(String numePacient) {
        for (Pacient pacient : listaPacienti) {
            if (pacient.getNume().equals(numePacient)) {
                return pacient.getId();
            }
        }

        return -1;
    }
}
