package com.example.cabinetmedicinadefamiliefericita.consultatii;

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
import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.database.service.PacientService;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.List;

public class ConsultatiiAdapter extends ArrayAdapter<Consultatie> {
    private Context context;
    private List<Consultatie> consultatii;
    private LayoutInflater inflater;
    private int resource;

    private CadruMedicalService cadruMedicalService = new CadruMedicalService(getContext());
    private PacientService pacientService = new PacientService(getContext());


    public ConsultatiiAdapter(@NonNull Context context, int resource,
                              @NonNull List<Consultatie> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.consultatii = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Consultatie consultatie = consultatii.get(position);

        if (consultatie != null) {
            adaugaIdMedic(view, consultatie.getIdMedic());
            adaugaIdPacient(view, consultatie.getIdPacient());
            adaugaDataOraConsultatie(view, consultatie.getDataOra());
            adaugaTipConsultatie(view, DataUtils.getStringFromEnumName(consultatie.getTip(), context));
        }
        return view;
    }

    private void adaugaIdMedic(View view, long idMedic) {
        TextView textView = view.findViewById(R.id.lv_row_consultatii_id_medic_completat);
        cadruMedicalService.getNumeById(getNumeByIdFromDbCallback(textView), idMedic);
    }

    private void adaugaIdPacient(View view, long idPacient) {
        TextView textView = view.findViewById(R.id.lv_row_consultatii_id_pacient_completat);
        pacientService.getNumeById(getNumeByIdFromDbCallback(textView), idPacient);
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


    private void adaugaDataOraConsultatie(View view, long dataOraTimestamp) {
        TextView dataOraTv = view.findViewById(R.id.lv_row_consultatii_data_ora_completat);
        String dataOraStringFrumos = DataUtils.timestampToDateTimeStringFormat(dataOraTimestamp);
        populateTextViewContent(dataOraTv, dataOraStringFrumos);
    }

    private void adaugaTipConsultatie(View view, String tipConsultatie) {
        TextView tipTv = view.findViewById(R.id.lv_row_consultatii_tip_consultatie_completat);
        populateTextViewContent(tipTv, tipConsultatie);
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_fara_text);
        }
    }
}
