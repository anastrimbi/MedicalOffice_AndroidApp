package com.example.cabinetmedicinadefamiliefericita.pacienti;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.enums.Gen;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class EditarePacientActivity extends AppCompatActivity {
    public static final String EDIT_PACIENT_KEY = "edit_pacient_key";

    private Pacient pacient;
    private TextInputEditText tietNume;
    private TextInputEditText tietCnp;
    private TextInputEditText tietVarsta;
    private Spinner spnGen;

    private Button btnSave;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Pacienti);
        setContentView(R.layout.activity_adauga_pacient);

        intent = getIntent();
        pacient = (Pacient) intent.getSerializableExtra(EDIT_PACIENT_KEY);
        initializeComponents();
        buildViewsFromPacient();
    }

    private void initializeComponents() {
        tietNume = findViewById(R.id.adauga_pacient_tiet_nume_pacient);
        tietCnp = findViewById(R.id.adauga_pacient_tiet_cnp);
        tietVarsta = findViewById(R.id.adauga_pacient_tiet_varsta);
        spnGen = findViewById(R.id.adauga_pacient_spn_gen);

        btnSave = findViewById(R.id.adauga_pacient_btn_save);
        btnSave.setOnClickListener(btnSaveEventListener());
        populareSpinnerGen();
    }

    private void buildViewsFromPacient() {
        if (pacient == null) {
            return;
        }

        tietNume.setText(pacient.getNume());
        tietCnp.setText(String.valueOf(pacient.getCNP()));
        tietVarsta.setText(String.valueOf(pacient.getVarsta()));

        setSpinnerSelection(DataUtils.getStringFromEnumName(pacient.getGen(), getApplicationContext()), spnGen);
    }

    private void setSpinnerSelection(String dbValue, Spinner spn) {
        ArrayAdapter adapter = (ArrayAdapter) spn.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            String item = (String) adapter.getItem(i);
            if (item != null && item.equals(dbValue)) {
                spn.setSelection(i);
                break;
            }
        }
    }

    private View.OnClickListener btnSaveEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Pacient pacientNou = creeazaPacientDinFormular();
                    intent.putExtra(EDIT_PACIENT_KEY, pacientNou);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Pacient creeazaPacientDinFormular() {
        String nume = tietNume.getText().toString();
        long cnp = Long.parseLong(tietCnp.getText().toString());
        int varsta = Integer.parseInt(tietVarsta.getText().toString());
        Gen genPacient = Gen.fromSpinnerSelection(getApplicationContext(), spnGen.getSelectedItem().toString());

        return new Pacient(pacient.getId(), nume, cnp, varsta, genPacient);
    }

    private void populareSpinnerGen() {
        // populare spinner cu adapter
        List<String> itemListAdapter = Gen.toSpinnerList(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnGen.setAdapter(adapter);
    }


    private boolean isTextEmpty(TextInputEditText tiedField) {
        Editable tiedFieldText = tiedField.getText();

        if (tiedFieldText == null || tiedFieldText.toString().trim().length() == 0) {
            return true;
        }

        return false;
    }

    private boolean validate() {

        //TODO: mai multe validari
        if (isTextEmpty(tietNume) || isTextEmpty(tietCnp) || isTextEmpty(tietVarsta)) {
            Toast.makeText(getApplicationContext(),
                    R.string.adauga_pacient_empty_text,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }
}
