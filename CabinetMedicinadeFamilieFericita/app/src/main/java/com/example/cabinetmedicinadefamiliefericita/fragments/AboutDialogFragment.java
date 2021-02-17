package com.example.cabinetmedicinadefamiliefericita.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.model.Clinica;
import com.example.cabinetmedicinadefamiliefericita.database.service.ClinicaService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AboutDialogFragment extends BottomSheetDialogFragment {
    private final static String CLINICA_ID_KEY = "clinica_id_key";

    private ClinicaService clinicaService;
    private TextView numeTv;
    private TextView descriereTv;
    private TextView adresaTv;
    private TextView contactTv;

    public static AboutDialogFragment newInstance(long idClinica) {
        final AboutDialogFragment fragment = new AboutDialogFragment();
        final Bundle args = new Bundle();

        args.putLong(CLINICA_ID_KEY, idClinica);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long idClinica = getArguments().getLong(CLINICA_ID_KEY);
        clinicaService = new ClinicaService(getContext());

        numeTv = view.findViewById(R.id.about_nume);
        descriereTv = view.findViewById(R.id.about_descriere);
        adresaTv = view.findViewById(R.id.about_adresa);
        contactTv = view.findViewById(R.id.about_contact);

        clinicaService.getOne(getOneByIdFromDbCallback(), idClinica);
    }

    private Callback<Clinica> getOneByIdFromDbCallback() {
        return new Callback<Clinica>() {
            @Override
            public void runResultOnUiThread(Clinica result) {
                if (result != null) {
                    numeTv.setText(result.getNume());
                    descriereTv.setText(result.getDescriere());
                    adresaTv.setText(result.getAdresa());
                    contactTv.setText(result.getContact());
                }
            }
        };
    }
}