package com.example.cabinetmedicinadefamiliefericita.consultatii;

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
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.database.service.PacientService;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class AdaugaConsultatieActivity extends AppCompatActivity {
    public static final String NEW_CONSULTATIE_KEY = "new_consultatie_key";

    private Spinner spnNumeMedic;
    private Spinner spnNumePacient;
    private DatePicker dpData;
    private TimePicker tpOra;
    private Spinner spnTip;
    private Button btnSave;

    private CadruMedicalService cadruMedicalService;
    private PacientService pacientService;
    private ArrayList<CadruMedical> listaMedici = new ArrayList<CadruMedical>();
    private ArrayList<Pacient> listaPacienti = new ArrayList<Pacient>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_consultatie);

        intent = getIntent();
        cadruMedicalService = new CadruMedicalService(getApplicationContext());
        cadruMedicalService.getAllMedici(getAllMediciFromDbCallback());
        pacientService = new PacientService((getApplicationContext()));
        pacientService.getAll(getAllPacientiFromDbCallback());
        initializeComponents();
    }

    private Callback<List<CadruMedical>> getAllMediciFromDbCallback() {
        return new Callback<List<CadruMedical>>() {
            @Override
            public void runResultOnUiThread(List<CadruMedical> result) {
                if (result != null) {
                    listaMedici.clear();
                    listaMedici.addAll(result);
                    populareSpinnerNumeMedic();
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
        spnNumeMedic = findViewById(R.id.adauga_consultatie_spn_id_medic);
        spnNumePacient = findViewById(R.id.adauga_consultatie_spn_id_pacient);
        spnTip = findViewById(R.id.adauga_consultatie_spn_tip);

        dpData = findViewById(R.id.adauga_consultatie_dp_data);
        tpOra = findViewById(R.id.adauga_consultatie_tp_ora);
        tpOra.setIs24HourView(true);

        btnSave = findViewById(R.id.adauga_consultatie_btn_save);
        btnSave.setOnClickListener(salveazaBtnEventListener());

        populareSpinnerTip();
    }


    private View.OnClickListener salveazaBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consultatie consultatie = creeazaConsultatieDinViews();
                intent.putExtra(NEW_CONSULTATIE_KEY, consultatie);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }


    private void populareSpinnerTip() {
        // populare spinner cu adapter
        List<String> itemListAdapter = TipConsultatie.toSpinnerList(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTip.setAdapter(adapter);
    }

    private void populareSpinnerNumeMedic() {
        // populare spinner cu adapter
        List<String> itemListAdapter = new ArrayList<>();
        for (CadruMedical medic : listaMedici) itemListAdapter.add(medic.getNume());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNumeMedic.setAdapter(adapter);
    }

    private void populareSpinnerNumePacient() {
        // populare spinner cu adapter
        List<String> itemListAdapter = new ArrayList<>();
        for (Pacient pacient : listaPacienti) itemListAdapter.add(pacient.getNume());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNumePacient.setAdapter(adapter);
    }

    private Consultatie creeazaConsultatieDinViews() {
        String numeMedic = spnNumeMedic.getSelectedItem().toString();
        String numePacient = spnNumePacient.getSelectedItem().toString();

        long idMedic = getIdMedicFromSpinnerByNume(numeMedic);
        long idPacient = getIdPacientFromSpinnerByNume(numePacient);
        long dataOra = DataUtils.dpTpToTimestamp(dpData, tpOra);
        TipConsultatie tip = TipConsultatie.fromSpinnerSelection(getApplicationContext(), spnTip.getSelectedItem().toString());

        return new Consultatie(idMedic, idPacient, dataOra , tip);
    }

    private long getIdMedicFromSpinnerByNume(String numeMedic) {
        for (CadruMedical medic : listaMedici) {
            if (medic.getNume().equals(numeMedic)) {
                return medic.getId();
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
