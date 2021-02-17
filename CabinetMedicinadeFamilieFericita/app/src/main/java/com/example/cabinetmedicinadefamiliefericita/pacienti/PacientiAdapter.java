package com.example.cabinetmedicinadefamiliefericita.pacienti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.List;

public class PacientiAdapter extends ArrayAdapter<Pacient> {
    private Context context;
    private List<Pacient> pacienti;
    private LayoutInflater inflater;
    private int resource;

    public PacientiAdapter(@NonNull Context context, int resource,
                           @NonNull List<Pacient> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.pacienti = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Pacient pacient = pacienti.get(position);

        if (pacient != null) {
            adaugaNumePacient(view, pacient.getNume());
            adaugaCnpPacient(view, pacient.getCNP());
            adaugaGenPacient(view, DataUtils.getStringFromEnumName(pacient.getGen(), context));

            // vrem ca atunci cand facem click pe iconita de edit, sa se intample ceva;
            // pentru asta definim aici handleri pt iconita, iar in activity-ul principal
            // o sa primim evenimentul definit dupa cum urmeaza:
            ImageView pacientEditIv = view.findViewById(R.id.lv_row_pacienti_settings_iv);
            pacientEditIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // performItemClick o sa fie preluat de lvPacienti.setOnItemClickListener din activitatea principala
                    ((ListView) parent).performItemClick(v, position, 0);
                }
            });
        }
        return view;
    }


    private void adaugaNumePacient(View view, String numePacient) {
        TextView numePacientTv = view.findViewById(R.id.lv_row_nume_pacient_completat);
        populateTextViewContent(numePacientTv, numePacient);
    }

    private void adaugaCnpPacient(View view, long cnpPacient) {
        TextView textView = view.findViewById(R.id.lv_row_cnp_pacient_completat);
        String textViewString = String.valueOf(cnpPacient);
        populateTextViewContent(textView, textViewString);
    }

    private void adaugaGenPacient(View view, String genPacient) {
        TextView genPacientTv = view.findViewById(R.id.lv_row_gen_pacient_completat);
        populateTextViewContent(genPacientTv, genPacient);
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_fara_text);
        }
    }
}
