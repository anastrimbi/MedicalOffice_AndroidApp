package com.example.cabinetmedicinadefamiliefericita.cadremedicale;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.enums.Gen;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class EditareCadruMedicalActivity extends AppCompatActivity {
    public static final String CADRU_MEDICAL_KEY = "cadru_medical_key";

    private TextInputEditText numeTiet;
    private Spinner tipSpn;
    private Spinner genSpn;
    private TextInputEditText urlPozaTiet;

    private Button salveazaBtn;

    private CadruMedical cadruMedical;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_cadru_medical);

        intent = getIntent();
        cadruMedical = (CadruMedical) intent.getSerializableExtra(CADRU_MEDICAL_KEY);

        initializeComponents();
        buildViewsFromCadruMedical();
    }

    private void initializeComponents() {
        numeTiet = findViewById(R.id.adauga_cadru_nume_tiet);
        tipSpn = findViewById(R.id.adauga_cadru_tip_spn);
        genSpn = findViewById(R.id.adauga_cadru_gen_spn);
        urlPozaTiet = findViewById(R.id.adauga_cadru_url_poza_tiet);

        salveazaBtn = findViewById(R.id.adauga_cadru_salveaza_btn);
        salveazaBtn.setOnClickListener(salveazaBtnEventListener());
        populareSpinnerTip();
        populareSpinnerGen();
    }

    private void buildViewsFromCadruMedical() {
        if (cadruMedical == null) {
            return;
        }

        numeTiet.setText(cadruMedical.getNume());
        setSpinnerSelection(DataUtils.getStringFromEnumName(cadruMedical.getGen(), getApplicationContext()), genSpn);
        setSpinnerSelection(DataUtils.getStringFromEnumName(cadruMedical.getTip(), getApplicationContext()), tipSpn);
        // dezactivam schimbarea tipului ca sa nu trebuiasca sa mutam dintr-o lista in alta in activitatea principala, alta logica, alt timp...
        tipSpn.setEnabled(false);
        urlPozaTiet.setText(cadruMedical.getPoza());
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

    private View.OnClickListener salveazaBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    CadruMedical cadruMedicalEditat = creeazaCadruMedicalDinFormular();
                    // transfer noul cadru catre activitatea de listare a cadrelor
                    intent.putExtra(CADRU_MEDICAL_KEY, cadruMedicalEditat);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private CadruMedical creeazaCadruMedicalDinFormular() {
        String nume = numeTiet.getText().toString();
        TipCadreMedicale tipCadruMedical = TipCadreMedicale.fromSpinnerSelection(getApplicationContext(), tipSpn.getSelectedItem().toString());
        Gen genCadruMedical = Gen.fromSpinnerSelection(getApplicationContext(), genSpn.getSelectedItem().toString());
        String urlPoza = urlPozaTiet.getText().toString();

        return new CadruMedical(cadruMedical.getId(), nume, tipCadruMedical, genCadruMedical, urlPoza);

    }

    private void populareSpinnerTip() {
        // populare spinner cu adapter
        List<String> itemListAdapter = TipCadreMedicale.toSpinnerList(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tipSpn.setAdapter(adapter);
    }

    private void populareSpinnerGen() {
        // populare spinner cu adapter
        List<String> itemListAdapter = Gen.toSpinnerList(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, itemListAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genSpn.setAdapter(adapter);
    }

    private boolean isTextEmpty(TextInputEditText tiedField) {
        Editable tiedFieldText = tiedField.getText();

        if (tiedFieldText == null || tiedFieldText.toString().trim().length() == 0) {
            return true;
        }

        return false;
    }

    private boolean validate() {
        if (isTextEmpty(numeTiet) || isTextEmpty(urlPozaTiet)) {
            Toast.makeText(getApplicationContext(),
                    R.string.cadru_medical_adaugare_text_gol,
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }
}
