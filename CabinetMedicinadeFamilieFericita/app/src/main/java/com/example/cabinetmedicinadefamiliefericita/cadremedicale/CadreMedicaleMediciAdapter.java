package com.example.cabinetmedicinadefamiliefericita.cadremedicale;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.retea.BitmapFromURL;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.List;
import java.util.concurrent.Callable;

public class CadreMedicaleMediciAdapter extends ArrayAdapter<CadruMedical> {
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private Context context;
    private List<CadruMedical> listaMedici;
    private LayoutInflater inflater;
    private int resource;

    public CadreMedicaleMediciAdapter(@NonNull Context context, int resource,
                                      @NonNull List<CadruMedical> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.listaMedici = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        CadruMedical medic = listaMedici.get(position);

        if (medic != null) {
            adaugaNumeMedic(view, medic.getNume());
            adaugaTipMedic(view, DataUtils.getStringFromEnumName(medic.getTip(), context));
            adaugaPozaMedic(view, medic.getPoza());

            // vrem ca atunci cand facem click pe butonul de raport consultatii, respectiv iconita de
            // edit, sa se intample ceva; pentru asta definim aici handleri pt fiecare, iar in
            // activity-ul principal o sa primim evenimentele definite dupa cum urmeaza:
            Button raportBtn = view.findViewById(R.id.lv_row_medic_raport_consultatii_btn);
            raportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // performItemClick o sa fie preluat de lvMedici.setOnItemClickListener din activitatea principala
                    ((ListView) parent).performItemClick(v, position, 0);
                }
            });

            ImageView editIv = view.findViewById(R.id.lv_row_medici_settings_iv);
            editIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // performItemClick o sa fie preluat de lvMedici.setOnItemClickListener din activitatea principala
                    ((ListView) parent).performItemClick(v, position, 0);
                }
            });
        }

        return view;
    }

    private void adaugaNumeMedic(View view, String numeMedic) {
        TextView numeMedicTv = view.findViewById(R.id.lv_row_nume_medic);
        populateTextViewContent(numeMedicTv, numeMedic);
    }

    private void adaugaTipMedic(View view, String tipMedic) {
        TextView tipMedicTv = view.findViewById(R.id.lv_row_tip_medic);
        populateTextViewContent(tipMedicTv, tipMedic);
    }

    private void adaugaPozaMedic(View view, String urlPoza) {
        ImageView pozaMedicIv = view.findViewById(R.id.lv_row_medici_medic_iv);

        if (URLUtil.isValidUrl(urlPoza)) {
            Callable<Bitmap> asyncOperation = new BitmapFromURL(urlPoza);
            Callback<Bitmap> mainThreadOperation = prelucrareDateImagine(pozaMedicIv);
            asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
        }
    }

    private Callback<Bitmap> prelucrareDateImagine(final ImageView pozaMedicIv) {
        return new Callback<Bitmap>() {
            @Override
            public void runResultOnUiThread(Bitmap bmp) {
                if (bmp != null) {
                    pozaMedicIv.setImageBitmap(bmp);
                }
            }
        };
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_fara_text);
        }
    }
}
