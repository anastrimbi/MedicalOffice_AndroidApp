package com.example.cabinetmedicinadefamiliefericita.cadremedicale;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.AsyncTaskRunner;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.retea.BitmapFromURL;

import java.util.List;
import java.util.concurrent.Callable;

public class CadreMedicaleAsistentiAdapter extends ArrayAdapter<CadruMedical> {
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private Context context;
    private List<CadruMedical> listaAsistenti;
    private LayoutInflater inflater;
    private int resource;

    public CadreMedicaleAsistentiAdapter(@NonNull Context context, int resource,
                                         @NonNull List<CadruMedical> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.listaAsistenti = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        CadruMedical asistent = listaAsistenti.get(position);

        if (asistent != null) {
            adaugaNumeAsistent(view, asistent.getNume());
            adaugaPozaAsistent(view, asistent.getPoza());

            // vrem ca atunci cand facem click pe butonul de raport analize, respectiv iconita de
            // edit, sa se intample ceva; pentru asta definim aici handleri pt fiecare, iar in
            // activity-ul principal o sa primim evenimentele definite dupa cum urmeaza:
            Button raportBtn = view.findViewById(R.id.lv_row_asistenti_raport_analize_btn);
            raportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // performItemClick o sa fie preluat de lvMedici.setOnItemClickListener din activitatea principala
                    ((GridView) parent).performItemClick(v, position, 0);
                }
            });

            ImageView editIv = view.findViewById(R.id.lv_row_asistenti_settings_iv);
            editIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // performItemClick o sa fie preluat de lvMedici.setOnItemClickListener din activitatea principala
                    ((GridView) parent).performItemClick(v, position, 0);
                }
            });
        }
        return view;
    }

    private void adaugaNumeAsistent(View view, String numeAsistent) {
        TextView numeAsistentTv = view.findViewById(R.id.lv_row_nume_asistent);
        populateTextViewContent(numeAsistentTv, numeAsistent);
    }

    private void adaugaPozaAsistent(View view, String urlPoza) {
        ImageView pozaAsistentIv = view.findViewById(R.id.lv_row_asistenti_asistent_iv);

        if (URLUtil.isValidUrl(urlPoza)) {
            Callable<Bitmap> asyncOperation = new BitmapFromURL(urlPoza);
            Callback<Bitmap> mainThreadOperation = prelucrareDateImagine(pozaAsistentIv);
            asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
        }
    }

    private Callback<Bitmap> prelucrareDateImagine(final ImageView pozaAsistentIv) {
        return new Callback<Bitmap>() {
            @Override
            public void runResultOnUiThread(Bitmap bmp) {
                if (bmp != null) {
                    pozaAsistentIv.setImageBitmap(bmp);
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
