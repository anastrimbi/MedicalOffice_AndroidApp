package com.example.cabinetmedicinadefamiliefericita.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cabinetmedicinadefamiliefericita.R;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.service.AnalizaService;
import com.example.cabinetmedicinadefamiliefericita.enums.TesteAnaliza;
import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RaportAnalizaDialogFragment extends BottomSheetDialogFragment {
    private static final String CADRU_MEDICAL_KEY = "cadru_medical_key";

    private AnalizaService analizaService;
    private CadruMedical cadruMedical;

    private TextView dateMedicTv;
    private TextView sangeTv;
    private TextView urinaTv;
    private TextView exudatTv;

    public static RaportAnalizaDialogFragment newInstance(CadruMedical cadruMedical) {
        final RaportAnalizaDialogFragment fragment = new RaportAnalizaDialogFragment();
        final Bundle args = new Bundle();

        args.putSerializable(CADRU_MEDICAL_KEY, cadruMedical);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_raport_analiza, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cadruMedical = (CadruMedical) getArguments().getSerializable(CADRU_MEDICAL_KEY);
        analizaService = new AnalizaService(getContext());

        dateMedicTv = view.findViewById(R.id.fragment_raport_analize_dateMedic);
        sangeTv = view.findViewById(R.id.fragment_raport_analize_totalSange);
        urinaTv = view.findViewById(R.id.fragment_raport_analize_totalUrina);
        exudatTv = view.findViewById(R.id.fragment_raport_analize_totalExudat);

        String tipMedic = DataUtils.getStringFromEnumName(cadruMedical.getTip(), getContext());
        String dateMedic = getString(R.string.raport_date_cadru_medical, cadruMedical.getNume(), tipMedic);
        dateMedicTv.setText(dateMedic);

        analizaService.countByTest(countByTestFromDbCallback(sangeTv, R.string.raport_sange), TesteAnaliza.Sange, cadruMedical.getId());
        analizaService.countByTest(countByTestFromDbCallback(urinaTv, R.string.raport_urina), TesteAnaliza.Urina, cadruMedical.getId());
        analizaService.countByTest(countByTestFromDbCallback(exudatTv, R.string.raport_exudat_faringian), TesteAnaliza.ExudatFaringian, cadruMedical.getId());
    }

    private Callback<Integer> countByTestFromDbCallback(final TextView textView, final int stringResId) {
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