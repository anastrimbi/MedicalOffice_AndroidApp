package com.example.cabinetmedicinadefamiliefericita.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.service.AnalizaService;
import com.example.cabinetmedicinadefamiliefericita.database.service.ConsultatieService;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cabinetmedicinadefamiliefericita.R;

import java.util.List;

public class RaportConsultatieDialogFragment extends BottomSheetDialogFragment {
    private static final String CADRU_MEDICAL_KEY = "cadru_medical_key";

    private ConsultatieService consultatieService;
    private CadruMedical cadruMedical;

    private TextView dateMedicTv;
    private TextView evaluariInitialeTv;
    private TextView controaleTv;

    public static RaportConsultatieDialogFragment newInstance(CadruMedical cadruMedical) {
        final RaportConsultatieDialogFragment fragment = new RaportConsultatieDialogFragment();
        final Bundle args = new Bundle();

        args.putSerializable(CADRU_MEDICAL_KEY, cadruMedical);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_raport_consultatie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cadruMedical = (CadruMedical) getArguments().getSerializable(CADRU_MEDICAL_KEY);
        consultatieService = new ConsultatieService(getContext());

        dateMedicTv = view.findViewById(R.id.fragment_raport_consultatii_dateMedic);
        evaluariInitialeTv = view.findViewById(R.id.fragment_raport_consultatii_totalEvaluariInitiale);
        controaleTv = view.findViewById(R.id.fragment_raport_consultatii_totalControale);

        String tipMedic = DataUtils.getStringFromEnumName(cadruMedical.getTip(), getContext());
        String dateMedic = getString(R.string.raport_date_cadru_medical, cadruMedical.getNume(), tipMedic);
        dateMedicTv.setText(dateMedic);

        consultatieService.countByTip(countByTipFromDbCallback(evaluariInitialeTv, R.string.raport_evaluari_initiale), TipConsultatie.EvaluareInitiala, cadruMedical.getId());
        consultatieService.countByTip(countByTipFromDbCallback(controaleTv, R.string.raport_controale), TipConsultatie.Control, cadruMedical.getId());
    }

    private Callback<Integer> countByTipFromDbCallback(final TextView textView, final int stringResId) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != null) {
                    String textViewText = getString(stringResId, result);
                    textView.setText(textViewText);
                }
            }
        };
    }
}