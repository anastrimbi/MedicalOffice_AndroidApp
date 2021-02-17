package com.example.cabinetmedicinadefamiliefericita.consultatii;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.database.service.ConsultatieService;

import java.util.ArrayList;
import java.util.List;

public class ConsultatiiActivity extends AppCompatActivity {
    private static final int CONSULTATII_ADD_REQUEST_CODE = 401;

    private ConsultatieService consultatieService;

    private ListView lvConsultatii;
    private Button adaugaConsultatieBtn;
    private ArrayList<Consultatie> listaConsultatii = new ArrayList<Consultatie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultatii);

        initializeComponents();
        // populare listaConsultatii din DB
        consultatieService = new ConsultatieService(getApplicationContext());
        consultatieService.getAll(getAllFromDbCallback());
    }

    private void initializeComponents() {
        lvConsultatii = findViewById(R.id.consultatii_lista);
        adaugaConsultatieBtn = findViewById(R.id.consultatii_adauga_btn);

        adaugaConsultatiiAdapter();

        adaugaConsultatieBtn.setOnClickListener(adaugaConsultatieBtnEventListener());

        lvConsultatii.setOnItemLongClickListener(deleteConsultatieEventListener());
    }

    private AdapterView.OnItemLongClickListener deleteConsultatieEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                consultatieService.delete(deleteConsultatieToDbCallback(position), listaConsultatii.get(position));
                return true;
            }
        };
    }

    private Callback<Integer> deleteConsultatieToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    listaConsultatii.remove(position);
                    notifyLvConsultatiiAdapter();
                }
            }
        };
    }

    private void adaugaConsultatiiAdapter() {
        ConsultatiiAdapter adapter = new ConsultatiiAdapter(getApplicationContext(),
                R.layout.lv_row_consultatie, listaConsultatii, getLayoutInflater());
        lvConsultatii.setAdapter(adapter);
    }

    private void notifyLvConsultatiiAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvConsultatii.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener adaugaConsultatieBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdaugaConsultatieActivity.class);
                startActivityForResult(intent, CONSULTATII_ADD_REQUEST_CODE);
            }
        };
    }

    private Callback<List<Consultatie>> getAllFromDbCallback() {
        return new Callback<List<Consultatie>>() {
            @Override
            public void runResultOnUiThread(List<Consultatie> result) {
                if (result != null) {
                    listaConsultatii.clear();
                    listaConsultatii.addAll(result);
                    notifyLvConsultatiiAdapter();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (CONSULTATII_ADD_REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
            Consultatie consulatieNoua = (Consultatie) data.getSerializableExtra(AdaugaConsultatieActivity.NEW_CONSULTATIE_KEY);
            consultatieService.insert(insertConsultatieIntoDbCallback(), consulatieNoua);
        }
    }

    private Callback<Consultatie> insertConsultatieIntoDbCallback() {
        return new Callback<Consultatie>() {
            @Override
            public void runResultOnUiThread(Consultatie result) {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), R.string.consultatie_adaugata, Toast.LENGTH_SHORT).show();
                    listaConsultatii.add(result);
                    notifyLvConsultatiiAdapter();
                }
            }
        };
    }

}