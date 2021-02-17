package com.example.cabinetmedicinadefamiliefericita.enums;

import android.content.Context;

import com.example.cabinetmedicinadefamiliefericita.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public enum TipConsultatie {
    EvaluareInitiala,
    Control;

    public static List<String> toSpinnerList (Context context){
        List<String> spinnerItems = new ArrayList<String>();

        // fiecare enum de aici functioneaza si ca identificator in res>strings.xml
        // iar aici facem o lista cu textul asociat fiecaruia pentru a le afisa frumos
        // in spinner
        for (TipConsultatie t : TipConsultatie.values()) {
            String enumResString = DataUtils.getStringFromEnumName(t, context);
            spinnerItems.add(enumResString);
        }

        return spinnerItems;
    }

    public static TipConsultatie fromSpinnerSelection(Context context, String spinnerSelectionValue) {
        // ce s-a selectat din spinner este de fapt o valoare din res>strings.xml
        // asociata cu unul din enumurile de aici
        // deci trecem prin toate enumurile de aici si vedem care are valoarea egala
        // cu cea primita ca parametru
        for(TipConsultatie t : TipConsultatie.values()) {
            String enumResString = DataUtils.getStringFromEnumName(t, context);
            if (enumResString.equals(spinnerSelectionValue)) {
                return t;
            }
        }

        // daca nu am gasit nimic, atunci punem prima valoare ca default
        return TipConsultatie.EvaluareInitiala;
    }

}
