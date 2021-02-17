package com.example.cabinetmedicinadefamiliefericita.cadremedicale;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;
import com.example.cabinetmedicinadefamiliefericita.fragments.RaportAnalizaDialogFragment;
import com.example.cabinetmedicinadefamiliefericita.fragments.RaportConsultatieDialogFragment;
import com.example.cabinetmedicinadefamiliefericita.utils.LayoutUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CadreMedicaleActivity extends AppCompatActivity {
    private static final int CADRE_ADD_REQUEST_CODE = 201;
    private static final int CADRE_EDIT_REQUEST_CODE = 202;
    private static final String CADRU_MEDICAL_KEY = "cadru_medical_key";

    private CadruMedicalService cadruMedicalService;

    private ListView lvMedici;
    private GridView gvAsistenti;
    private Button adaugaCadruBtn;

    private ArrayList<CadruMedical> listaMedici = new ArrayList<CadruMedical>();
    private ArrayList<CadruMedical> listaAsistenti = new ArrayList<CadruMedical>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadre_medicale);

        initializeComponents();

        // populare listaMedici si listaAsistenti din DB
        cadruMedicalService = new CadruMedicalService(getApplicationContext());
        cadruMedicalService.getAllMedici(getAllMediciFromDbCallback());
        cadruMedicalService.getAllAsistenti(getAllAsistentiFromDbCallback());
    }

    private void initializeComponents() {
        lvMedici = findViewById(R.id.medici_lv);
        gvAsistenti = findViewById(R.id.asistenti_gv);
        adaugaCadruBtn = findViewById(R.id.cadre_medicale_adauga_btn);

        // adauga adapter + click listeners
        adaugaMediciAdapter();
        lvMedici.setOnItemClickListener(medicItemClickListeners());
        lvMedici.setOnItemLongClickListener(deleteMedicEventListener());

        // adauga adapter + click listeners
        adaugaAsistentiAdapter();
        gvAsistenti.setOnItemClickListener(asistentItemClickListeners());
        gvAsistenti.setOnItemLongClickListener(deleteAsistentEventListener());

        adaugaCadruBtn.setOnClickListener(adaugaCadruBtnEventListener());
    }

    private AdapterView.OnItemClickListener medicItemClickListeners() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                CadruMedical medic = listaMedici.get(position);

                if (viewId == R.id.lv_row_medici_settings_iv) {
                    Intent intent = new Intent(getApplicationContext(), EditareCadruMedicalActivity.class);
                    intent.putExtra(CADRU_MEDICAL_KEY, medic);
                    startActivityForResult(intent, CADRE_EDIT_REQUEST_CODE);
                } else if (viewId == R.id.lv_row_medic_raport_consultatii_btn) {
                    RaportConsultatieDialogFragment.newInstance(medic).show(getSupportFragmentManager(), "dialog");
                }
            }
        };
    }

    private AdapterView.OnItemLongClickListener deleteMedicEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cadruMedicalService.delete(deleteMedicToDbCallback(position), listaMedici.get(position));
                return true;
            }
        };
    }

    private AdapterView.OnItemClickListener asistentItemClickListeners() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                CadruMedical asistent = listaAsistenti.get(position);

                if (viewId == R.id.lv_row_asistenti_settings_iv) {
                    Intent intent = new Intent(getApplicationContext(), EditareCadruMedicalActivity.class);
                    intent.putExtra(CADRU_MEDICAL_KEY, asistent);
                    startActivityForResult(intent, CADRE_EDIT_REQUEST_CODE);
                } else if (viewId == R.id.lv_row_asistenti_raport_analize_btn) {
                    RaportAnalizaDialogFragment.newInstance(asistent).show(getSupportFragmentManager(), "dialog");
                }
            }
        };
    }

    private AdapterView.OnItemLongClickListener deleteAsistentEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cadruMedicalService.delete(deleteAsistentToDbCallback(position), listaAsistenti.get(position));
                return true;
            }
        };
    }

    private void adaugaMediciAdapter() {
        CadreMedicaleMediciAdapter adapter = new CadreMedicaleMediciAdapter(getApplicationContext(),
                R.layout.lv_row_medici, listaMedici, getLayoutInflater());
        lvMedici.setAdapter(adapter);
    }

    private void adaugaAsistentiAdapter() {
        CadreMedicaleAsistentiAdapter adapter = new CadreMedicaleAsistentiAdapter(getApplicationContext(),
                R.layout.lv_row_asistenti, listaAsistenti, getLayoutInflater());
        gvAsistenti.setAdapter(adapter);
    }

    private void notifyLvMediciAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvMedici.getAdapter();
        adapter.notifyDataSetChanged();
        // vrem sa afisam toate elementele din listview, ca sa nu avem 2 scrolluri - unul al ferestrei mari
        // si altul al listview-ului, asa ca folosim functia de mai jos care calculeaza inaltimea listview-ului
        // in functie de cate elemente contine acesta si ii modifica height-ul
        LayoutUtils.setListViewHeightBasedOnChildren(lvMedici);
    }

    private void notifyLvAsistentiAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) gvAsistenti.getAdapter();
        adapter.notifyDataSetChanged();
        // vrem sa afisam toate elementele din gridview, ca sa nu avem 2 scrolluri - unul al ferestrei mari
        // si altul al listview-ului, asa ca folosim functia de mai jos care calculeaza inaltimea gridview-ului
        // in functie de cate elemente contine acesta si ii modifica height-ul
        LayoutUtils.setGridViewHeightBasedOnChildren(gvAsistenti, 2);
    }

    private View.OnClickListener adaugaCadruBtnEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdaugaCadruMedicalActivity.class);
                startActivityForResult(intent, CADRE_ADD_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (CADRE_ADD_REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
            CadruMedical cadruMedicalNou = (CadruMedical) data.getSerializableExtra(AdaugaCadruMedicalActivity.CADRU_MEDICAL_KEY);

            if (cadruMedicalNou != null) {
                boolean isMedic = cadruMedicalNou.getTip() != TipCadreMedicale.Asistent;
                cadruMedicalService.insert(insertCadruMedicalIntoDbCallback(isMedic), cadruMedicalNou);
            }

        } else if (CADRE_EDIT_REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
            CadruMedical cadruMedicalEditat = (CadruMedical) data.getSerializableExtra(EditareCadruMedicalActivity.CADRU_MEDICAL_KEY);

            if (cadruMedicalEditat != null) {
                cadruMedicalService.update(updateCadruMedicalIntoDbCallback(), cadruMedicalEditat);
            }
        }
    }

    private Callback<List<CadruMedical>> getAllMediciFromDbCallback() {
        return new Callback<List<CadruMedical>>() {
            @Override
            public void runResultOnUiThread(List<CadruMedical> result) {
                if (result != null) {
                    listaMedici.clear();
                    listaMedici.addAll(result);
                    notifyLvMediciAdapter();
                }
            }
        };
    }

    private Callback<List<CadruMedical>> getAllAsistentiFromDbCallback() {
        return new Callback<List<CadruMedical>>() {
            @Override
            public void runResultOnUiThread(List<CadruMedical> result) {
                if (result != null) {
                    listaAsistenti.clear();
                    listaAsistenti.addAll(result);
                    notifyLvAsistentiAdapter();
                }
            }
        };
    }

    private Callback<CadruMedical> insertCadruMedicalIntoDbCallback(final boolean isMedic) {
        return new Callback<CadruMedical>() {
            @Override
            public void runResultOnUiThread(CadruMedical result) {
                if (result != null) {
                    if (isMedic) {
                        Toast.makeText(getApplicationContext(), R.string.cadru_medical_medic_adaugat, Toast.LENGTH_SHORT).show();
                        listaMedici.add(result);
                        notifyLvMediciAdapter();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.cadru_medical_asistent_adaugat, Toast.LENGTH_SHORT).show();
                        listaAsistenti.add(result);
                        notifyLvAsistentiAdapter();
                    }
                }
            }
        };
    }

    private Callback<CadruMedical> updateCadruMedicalIntoDbCallback() {
        return new Callback<CadruMedical>() {
            @Override
            public void runResultOnUiThread(CadruMedical result) {
                if (result != null) {
                    boolean isMedic = result.getTip() != TipCadreMedicale.Asistent;

                    if (isMedic) {
                        Toast.makeText(getApplicationContext(), R.string.cadru_medical_medic_actualizat, Toast.LENGTH_SHORT).show();
                        listaMedici.set(getIndexOfCadruMedicalInList(listaMedici, result), result);
                        notifyLvMediciAdapter();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.cadru_medical_asistent_actualizat, Toast.LENGTH_SHORT).show();
                        listaAsistenti.set(getIndexOfCadruMedicalInList(listaAsistenti, result), result);
                        notifyLvAsistentiAdapter();
                    }
                }
            }
        };
    }

    private int getIndexOfCadruMedicalInList(List<CadruMedical> list, CadruMedical item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == item.getId()) {
                return i;
            }
        }

        return -1;
    }

    private Callback<Integer> deleteMedicToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    listaMedici.remove(position);
                    notifyLvMediciAdapter();
                }
            }
        };
    }

    private Callback<Integer> deleteAsistentToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    listaAsistenti.remove(position);
                    notifyLvAsistentiAdapter();
                }
            }
        };
    }
}