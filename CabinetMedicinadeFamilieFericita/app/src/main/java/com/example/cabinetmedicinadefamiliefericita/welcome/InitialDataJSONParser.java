package com.example.cabinetmedicinadefamiliefericita.welcome;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.cabinetmedicinadefamiliefericita.database.model.Analiza;
import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.database.model.CadruMedical;
import com.example.cabinetmedicinadefamiliefericita.database.model.Clinica;
import com.example.cabinetmedicinadefamiliefericita.database.model.Consultatie;
import com.example.cabinetmedicinadefamiliefericita.database.service.AnalizaService;
import com.example.cabinetmedicinadefamiliefericita.database.service.CadruMedicalService;
import com.example.cabinetmedicinadefamiliefericita.database.service.ClinicaService;
import com.example.cabinetmedicinadefamiliefericita.database.service.ConsultatieService;
import com.example.cabinetmedicinadefamiliefericita.database.service.PacientService;
import com.example.cabinetmedicinadefamiliefericita.enums.Gen;
import com.example.cabinetmedicinadefamiliefericita.enums.TesteAnaliza;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;
import com.example.cabinetmedicinadefamiliefericita.database.model.Pacient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InitialDataJSONParser {
    public static final String CLINICA = "clinica";
    public static final String NUME = "nume";
    public static final String DESCRIERE = "descriere";
    public static final String ADRESA = "adresa";
    public static final String CONTACT = "contact";
    public static final String CADRE_MEDICALE = "cadreMedicale";
    public static final String ID = "id";
    public static final String TIP = "tip";
    public static final String GEN = "gen";
    public static final String POZA = "poza";
    public static final String CONSULTATII = "consultatii";
    public static final String ANALIZE = "analize";
    public static final String PACIENT = "pacient";
    public static final String CNP = "cnp";
    public static final String VARSTA = "varsta";
    public static final String ID_MEDIC = "idMedic";
    public static final String ID_PACIENT = "idPacient";
    public static final String DATA_ORA = "dataOra";
    public static final String ID_ASISTENT = "idAsistent";
    public static final String TEST = "test";

    public static void fromJson(String json, Context context) {
        try {
            JSONObject object = new JSONObject(json);
            citireDateClinica(object, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void citireDateClinica(JSONObject object, Context context) throws JSONException {
        List<CadruMedical> listaCadreMedicale = new ArrayList<CadruMedical>();
        List<Pacient> listaPacienti = new ArrayList<Pacient>();
        List<Consultatie> listaConsultatii = new ArrayList<Consultatie>();
        List<Analiza> listaAnalize = new ArrayList<Analiza>();

        // clinica in JSON e doar una, deci nu necesita iteratie, deci preluam datele ei aici
        JSONObject clinicaJson = object.getJSONObject(CLINICA);
        Clinica clinica = creareClinicaDinJson(clinicaJson);

        // trecem la urmatorul nivel pt care avem nevoie de iteratieeeee
        JSONArray cadreMedicaleJsonArray = clinicaJson.getJSONArray(CADRE_MEDICALE);

        for (int i = 0; i < cadreMedicaleJsonArray.length(); i++) {
            // primul nivel: CadruMedical
            JSONObject cadruMedicalJson = cadreMedicaleJsonArray.getJSONObject(i);
            CadruMedical cadruMedical = creareCadruMedicalDinJson(cadruMedicalJson);
            listaCadreMedicale.add(cadruMedical); // salvez cadrul medical in lista

            boolean hasConsultatii = cadruMedicalJson.has(CONSULTATII);
            JSONArray consultatiiAnalizeJsonArray = cadruMedicalJson.getJSONArray(hasConsultatii ? CONSULTATII : ANALIZE);

            for (int j = 0; j < consultatiiAnalizeJsonArray.length(); j++) {
                JSONObject consultatieAnalizaJson = consultatiiAnalizeJsonArray.getJSONObject(j);

                JSONObject pacientJson = consultatieAnalizaJson.getJSONObject(PACIENT);
                Pacient pacient = crearePacientDinJson(pacientJson);
                listaPacienti.add(pacient); // salvez pacientul in lista

                if (hasConsultatii) {
                    Consultatie consultatie = creareConsultatieDinJson(consultatieAnalizaJson);
                    listaConsultatii.add(consultatie); // salvez consultatia in lista
                } else {
                    Analiza analiza = creareAnalizaDinJson(consultatieAnalizaJson);
                    listaAnalize.add(analiza); // salvez analiza in lista
                }
            }
        }

        // Populam Room Database cu datele noastre din JSON -- pentru ca vrem sa le facem secvential
        // ca sa nu avem probleme ca se introduce ceva care necesita foreign key, dar tabelul referentiat
        // sa nu fie inca populat, asa ca trimite toate datele din callback in callback pana adaugam datele
        // in ordinea care trebuie: clinica, cadreMedicale, pacienti, analize si consultatii
        adaugaClinicaInDB(clinica, listaCadreMedicale, listaPacienti, listaAnalize, listaConsultatii, context);
    }

    private static Clinica creareClinicaDinJson(JSONObject clinicaJson) throws JSONException {
        long id = clinicaJson.getLong(ID);
        String nume = clinicaJson.getString(NUME);
        String descriere = clinicaJson.getString(DESCRIERE);
        String adresa = clinicaJson.getString(ADRESA);
        String contact = clinicaJson.getString(CONTACT);

        return new Clinica(id, nume, descriere, adresa, contact);
    }

    private static CadruMedical creareCadruMedicalDinJson(JSONObject cadruMedicalJson) throws JSONException {
        long id = cadruMedicalJson.getLong(ID);
        String nume = cadruMedicalJson.getString(NUME);
        TipCadreMedicale tip = TipCadreMedicale.valueOf(cadruMedicalJson.getString(TIP));
        Gen gen = Gen.valueOf(cadruMedicalJson.getString(GEN));
        String poza = cadruMedicalJson.getString(POZA);

        return new CadruMedical(id, nume, tip, gen, poza);
    }

    private static Pacient crearePacientDinJson(JSONObject pacientJson) throws JSONException {
        long id = pacientJson.getLong(ID);
        String nume = pacientJson.getString(NUME);
        long cnp = pacientJson.getLong(CNP);
        Gen gen = Gen.valueOf(pacientJson.getString(GEN));
        int varsta = pacientJson.getInt(VARSTA);

        return new Pacient(id, nume, cnp, varsta, gen);
    }

    private static Consultatie creareConsultatieDinJson(JSONObject consultatieJson) throws JSONException {
        int id = consultatieJson.getInt(ID);
        int idMedic = consultatieJson.getInt(ID_MEDIC);
        int idPacient = consultatieJson.getInt(ID_PACIENT);
        long dataOra = consultatieJson.getLong(DATA_ORA);
        TipConsultatie tip = TipConsultatie.valueOf(consultatieJson.getString(TIP));

        return new Consultatie(id, idMedic, idPacient, dataOra, tip);
    }

    private static Analiza creareAnalizaDinJson(JSONObject analizaJson) throws JSONException {
        long id = analizaJson.getLong(ID);
        int idAsistent = analizaJson.getInt(ID_ASISTENT);
        int idPacient = analizaJson.getInt(ID_PACIENT);
        long dataOra = analizaJson.getLong(DATA_ORA);
        TesteAnaliza test = TesteAnaliza.valueOf(analizaJson.getString(TEST));

        return new Analiza(id, idAsistent, idPacient, dataOra, test);
    }

    private static void adaugaClinicaInDB(Clinica clinica, List<CadruMedical> listaCadreMedicale, List<Pacient> listaPacienti, List<Analiza> listaAnalize, List<Consultatie> listaConsultatii, Context context) {
        ClinicaService clinicaService = new ClinicaService(context);
        clinicaService.insert(insertClinicaIntoDbCallback(listaCadreMedicale, listaPacienti, listaAnalize, listaConsultatii, context), clinica);
    }

    private static Callback<Clinica> insertClinicaIntoDbCallback(final List<CadruMedical> listaCadreMedicale, final List<Pacient> listaPacienti, final List<Analiza> listaAnalize, final List<Consultatie> listaConsultatii, final Context context) {
        return new Callback<Clinica>() {
            @Override
            public void runResultOnUiThread(Clinica result) {
                if (result != null) {
                    Toast.makeText(context, "Date clinica introduse din JSON:\n" + result.getNume(), Toast.LENGTH_SHORT).show();
                    adaugaCadreMedicaleInDB(listaCadreMedicale, listaPacienti, listaAnalize, listaConsultatii, context);
                }
            }
        };
    }

    private static void adaugaCadreMedicaleInDB(List<CadruMedical> listaCadreMedicale, List<Pacient> listaPacienti, List<Analiza> listaAnalize, List<Consultatie> listaConsultatii, Context context) {
        CadruMedicalService cadruMedicalService = new CadruMedicalService(context);
        cadruMedicalService.insertAll(insertListaCadreMedicaleIntoDbCallback(listaPacienti, listaAnalize, listaConsultatii, context), listaCadreMedicale);
    }

    private static Callback<List<CadruMedical>> insertListaCadreMedicaleIntoDbCallback(final List<Pacient> listaPacienti, final List<Analiza> listaAnalize, final List<Consultatie> listaConsultatii, final Context context) {
        return new Callback<List<CadruMedical>>() {
            @Override
            public void runResultOnUiThread(List<CadruMedical> listResult) {
                if (listResult != null) {
                    List<String> listaNumeCadreMedicale = new ArrayList<>();
                    for (int i = 0; i < listResult.size(); i++) {
                        listaNumeCadreMedicale.add(listResult.get(i).getNume());
                    }
                    String numeCadreMedicale = TextUtils.join(", ", listaNumeCadreMedicale);

                    Toast.makeText(context, "Cadre medicale adaugate din JSON:\n" + numeCadreMedicale, Toast.LENGTH_SHORT).show();
                    adaugaPacientiInDB(listaPacienti, listaAnalize, listaConsultatii, context);
                }
            }
        };
    }

    private static void adaugaPacientiInDB(List<Pacient> listaPacienti, List<Analiza> listaAnalize, List<Consultatie> listaConsultatii, Context context) {
        PacientService pacientService = new PacientService(context);
        pacientService.insertAll(insertListaPacientiIntoDbCallback(listaAnalize, listaConsultatii, context), listaPacienti);
    }

    private static Callback<List<Pacient>> insertListaPacientiIntoDbCallback(final List<Analiza> listaAnalize, final List<Consultatie> listaConsultatii, final Context context) {
        return new Callback<List<Pacient>>() {
            @Override
            public void runResultOnUiThread(List<Pacient> listResult) {
                if (listResult != null) {
                    List<String> listaNumePacienti = new ArrayList<>();
                    for (int i = 0; i < listResult.size(); i++) {
                        listaNumePacienti.add(listResult.get(i).getNume());
                    }
                    String numePacienti = TextUtils.join(", ", listaNumePacienti);

                    Toast.makeText(context, "Pacienti adaugati din JSON:\n" + numePacienti, Toast.LENGTH_SHORT).show();
                    adaugaAnalizeInDB(listaAnalize, listaConsultatii, context);
                }
            }
        };
    }

    private static void adaugaAnalizeInDB(List<Analiza> listaAnalize, List<Consultatie> listaConsultatii, Context context) {
        AnalizaService analizaService = new AnalizaService(context);
        analizaService.insertAll(insertListaAnalizeIntoDbCallback(listaConsultatii, context), listaAnalize);
    }

    private static Callback<List<Analiza>> insertListaAnalizeIntoDbCallback(final List<Consultatie> listaConsultatii, final Context context) {
        return new Callback<List<Analiza>>() {
            @Override
            public void runResultOnUiThread(List<Analiza> listResult) {
                if (listResult != null) {
                    List<String> listaTesteAnalize = new ArrayList<>();
                    for (int i = 0; i < listResult.size(); i++) {
                        listaTesteAnalize.add(listResult.get(i).getTest().name());
                    }
                    String numeTesteAnalize = TextUtils.join(", ", listaTesteAnalize);

                    Toast.makeText(context, "Analize adaugate din JSON:\n" + numeTesteAnalize, Toast.LENGTH_SHORT).show();
                    adaugaConsultatiiInDB(listaConsultatii, context);
                }
            }
        };
    }

    private static void adaugaConsultatiiInDB(List<Consultatie> listaConsultatii, Context context) {
        ConsultatieService consultatieService = new ConsultatieService(context);
        consultatieService.insertAll(insertListaConsultatiiIntoDbCallback(context), listaConsultatii);
    }

    private static Callback<List<Consultatie>> insertListaConsultatiiIntoDbCallback(final Context context) {
        return new Callback<List<Consultatie>>() {
            @Override
            public void runResultOnUiThread(List<Consultatie> listResult) {
                if (listResult != null) {
                    List<String> listaTipConsultatii = new ArrayList<>();
                    for (int i = 0; i < listResult.size(); i++) {
                        listaTipConsultatii.add(listResult.get(i).getTip().name());
                    }
                    String numeTipConsultatii = TextUtils.join(", ", listaTipConsultatii);

                    Toast.makeText(context, "Consultatii adaugate din JSON:\n" + numeTipConsultatii, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


}
