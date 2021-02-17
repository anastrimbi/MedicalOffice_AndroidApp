package com.example.cabinetmedicinadefamiliefericita.pacienti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.database.service.PacientService;

import java.util.ArrayList;
import java.util.List;

public class PacientiActivity extends AppCompatActivity {
    private static final int PACIENTI_ADD_REQUEST_CODE = 501;
    private static final int PACIENTI_EDIT_REQUEST_CODE = 502;

    private PacientService pacientService;

    private ListView lvPacienti;
    private Button adaugaPacientBtn;
    private ArrayList<Pacient> listaPacienti = new ArrayList<Pacient>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacienti);

        initializeComponents();
        // populare listaPacienti din DB
        pacientService = new PacientService(getApplicationContext());
        pacientService.getAll(getAllFromDbCallback());
    }

    private void initializeComponents() {
        lvPacienti = findViewById(R.id.pacienti_lista);
        adaugaPacientBtn = findViewById(R.id.pacienti_adauga_btn);

        adaugaPacientiAdapter();

        adaugaPacientBtn.setOnClickListener(adaugaPacientBtnEventListener());

        lvPacienti.setOnItemClickListener(pacientItemClickListeners());
        lvPacienti.setOnItemLongClickListener(deletePacientEventListener());
    }

    private AdapterView.OnItemClickListener pacientItemClickListeners() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                Pacient pacient = listaPacienti.get(position);

                if (viewId == R.id.lv_row_pacienti_settings_iv) {
                    Intent intent = new Intent(getApplicationContext(), EditarePacientActivity.class);
                    intent.putExtra(EditarePacientActivity.EDIT_PACIENT_KEY, pacient);
                    startActivityForResult(intent, PACIENTI_EDIT_REQUEST_CODE);
                }
            }
        };
    }

    private AdapterView.OnItemLongClickListener deletePacientEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pacientService.delete(deletePacientToDbCallback(position), listaPacienti.get(position));
                return true;
            }
        };
    }

    private Callback<Integer> deletePacientToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    listaPacienti.remove(position);
                    notifyLvPacientiAdapter();
                }
            }
        };
    }

    private void adaugaPacientiAdapter() {
        PacientiAdapter adapter = new PacientiAdapter(getApplicationContext(),
                R.layout.lv_row_pacienti, listaPacienti, getLayoutInflater());
        lvPacienti.setAdapter(adapter);
    }

    private void notifyLvPacientiAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvPacienti.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener adaugaPacientBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdaugaPacientActivity.class);
                startActivityForResult(intent, PACIENTI_ADD_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if (PACIENTI_ADD_REQUEST_CODE == requestCode) {
                Pacient pacientNou = (Pacient) data.getSerializableExtra(AdaugaPacientActivity.NEW_PACIENT_KEY);
                if(pacientNou != null) {
                    pacientService.insert(insertIntoDbCallback(), pacientNou);
                }
            } else if (PACIENTI_EDIT_REQUEST_CODE == requestCode) {
                Pacient pacientEditat = (Pacient) data.getSerializableExtra(EditarePacientActivity.EDIT_PACIENT_KEY);
                if(pacientEditat != null) {
                    pacientService.update(updateIntoDbCallback(), pacientEditat);
                }
            }
        }

    }

    private Callback<List<Pacient>> getAllFromDbCallback() {
        return new Callback<List<Pacient>>() {
            @Override
            public void runResultOnUiThread(List<Pacient> result) {
                if (result != null) {
                    listaPacienti.clear();
                    listaPacienti.addAll(result);
                    notifyLvPacientiAdapter();
                }
            }
        };
    }

    private Callback<Pacient> insertIntoDbCallback() {
        return new Callback<Pacient>() {
            @Override
            public void runResultOnUiThread(Pacient result) {
                if (result != null) {
                    listaPacienti.add(result);
                    notifyLvPacientiAdapter();
                }
            }
        };
    }

    private Callback<Pacient> updateIntoDbCallback() {
        return new Callback<Pacient>() {
            @Override
            public void runResultOnUiThread(Pacient result) {
                if (result != null) {
                    listaPacienti.set(getIndexOfPacientInList(listaPacienti, result), result);
                    notifyLvPacientiAdapter();
                }
            }
        };
    }

    private int getIndexOfPacientInList(List<Pacient> list, Pacient item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == item.getId()) {
                return i;
            }
        }

        return -1;
    }

}