package com.example.cabinetmedicinadefamiliefericita.analize;

import android.app.ActionBar;
import android.content.Intent;
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
import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;;
import com.example.cabinetmedicinadefamiliefericita.database.service.AnalizaService;

import java.util.ArrayList;
import java.util.List;

public class AnalizeActivity extends AppCompatActivity {

    private static final int ANALIZE_ADD_REQUEST_CODE = 301;

    private AnalizaService analizaService;

    private ListView lvAnalize;
    private Button adaugaAnalizaBtn;
    private ArrayList<Analiza> listaAnalize = new ArrayList<Analiza>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analize);

        initializeComponents();
        // populare listaAnalize din DB
        analizaService = new AnalizaService(getApplicationContext());
        analizaService.getAll(getAllFromDbCallback());
    }

    private void initializeComponents() {
        lvAnalize = findViewById(R.id.analize_lista);
        adaugaAnalizaBtn = findViewById(R.id.analize_adauga_btn);

        adaugaAnalizeAdapter();

        adaugaAnalizaBtn.setOnClickListener(adaugaAnalizaBtnEventListener());


        lvAnalize.setOnItemLongClickListener(deleteAnalizaEventListener());
    }

    private AdapterView.OnItemLongClickListener deleteAnalizaEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                analizaService.delete(deleteAnalizaToDbCallback(position), listaAnalize.get(position));
                return true;
            }
        };
    }

    private Callback<Integer> deleteAnalizaToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    listaAnalize.remove(position);
                    notifyLvAnalizeAdapter();
                }
            }
        };
    }



    private void adaugaAnalizeAdapter() {
        AnalizeAdapter adapter = new AnalizeAdapter(getApplicationContext(),
                R.layout.lv_row_analize, listaAnalize, getLayoutInflater());
        lvAnalize.setAdapter(adapter);
    }

    private void notifyLvAnalizeAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvAnalize.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener adaugaAnalizaBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdaugaAnalizaActivity.class);
                startActivityForResult(intent, ANALIZE_ADD_REQUEST_CODE);
            }
        };
    }

    private Callback<List<Analiza>> getAllFromDbCallback() {
        return new Callback<List<Analiza>>() {
            @Override
            public void runResultOnUiThread(List<Analiza> result) {
                if (result != null) {
                    listaAnalize.clear();
                    listaAnalize.addAll(result);
                    notifyLvAnalizeAdapter();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ANALIZE_ADD_REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
            Analiza analizaNoua = (Analiza) data.getSerializableExtra(AdaugaAnalizaActivity.NEW_ANALIZA_KEY);
            analizaService.insert(insertAnalizaIntoDbCallback(), analizaNoua);
        }
    }

    private Callback<Analiza> insertAnalizaIntoDbCallback() {
        return new Callback<Analiza>() {
            @Override
            public void runResultOnUiThread(Analiza result) {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), R.string.analiza_adaugata, Toast.LENGTH_SHORT).show();
                    listaAnalize.add(result);
                    notifyLvAnalizeAdapter();
                }
            }
        };
    }


}