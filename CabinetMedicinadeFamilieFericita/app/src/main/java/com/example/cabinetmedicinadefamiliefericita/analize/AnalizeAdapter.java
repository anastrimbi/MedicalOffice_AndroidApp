package com.example.cabinetmedicinadefamiliefericita.analize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.database.service.PacientService;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.List;

public class AnalizeAdapter extends ArrayAdapter<Analiza> {
    private Context context;
    private List<Analiza> analize;
    private LayoutInflater inflater;
    private int resource;

    private PacientService pacientService = new PacientService(getContext());
    private CadruMedicalService cadruMedicalService = new CadruMedicalService(getContext());


    public AnalizeAdapter(@NonNull Context context, int resource,
                          @NonNull List<Analiza> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.analize = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Analiza analiza = analize.get(position);

        if (analiza != null) {
            adaugaIdPacient(view, analiza.getIdPacient());
            adaugaIdAsistent(view, analiza.getIdAsistent());
            adaugaTest(view, DataUtils.getStringFromEnumName(analiza.getTest(), context));
        }
        return view;
    }

    private void adaugaIdPacient(View view, long idPacient) {
        TextView textView = view.findViewById(R.id.lv_row_id_pacient_completat);
        pacientService.getNumeById(getNumeByIdFromDbCallback(textView), idPacient);
    }
    

    private void adaugaIdAsistent(View view, long idAsistent) {
        TextView textView = view.findViewById(R.id.lv_row_id_asistent_completat);
        cadruMedicalService.getNumeById(getNumeByIdFromDbCallback(textView), idAsistent);
    }


    private Callback<String> getNumeByIdFromDbCallback(final TextView textView) {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                if (result != null) {
                    populateTextViewContent(textView, result);
                }
            }
        };
    }

    private void adaugaTest(View view, String test) {
        TextView testTv = view.findViewById(R.id.lv_row_test_completat);
        populateTextViewContent(testTv, test);
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_fara_text);
        }
    }
}
