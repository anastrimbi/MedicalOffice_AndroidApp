package com.example.cabinetmedicinadefamiliefericita.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cabinetmedicinadefamiliefericita.asyncTask.Callback;
import com.example.cabinetmedicinadefamiliefericita.settings.AppSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabase {
    public static final String SETTINGS_TABLE_NAME = "settings";

    private DatabaseReference database;
    private static FirebaseDatabase firebaseDatabase;

    private FirebaseDatabase() {
        database = com.google.firebase.database.FirebaseDatabase.getInstance().getReference(SETTINGS_TABLE_NAME);
    }

    public static FirebaseDatabase getInstance() {
        if (firebaseDatabase == null) {
            synchronized (FirebaseDatabase.class) {
                if (firebaseDatabase == null) {
                    firebaseDatabase = new FirebaseDatabase();
                }
            }
        }
        return firebaseDatabase;
    }

    public void upsert(AppSettings appSettings) {
        if (appSettings == null) {
            return;
        }

        //verificam daca obiectul appSettings nu are id
        // daca obiectul deja are cheie, facem update, nu insert
        if (appSettings.getId() == null || appSettings.getId().trim().isEmpty()) {
            // la nivel de database, avem metoda push() => forteaza adaugarea in tabela a cheii (fara ce se afla in interior)
            // daca apelez getKey() dupa push(), imi returneaza sub forma de String valoarea cheii
            String id = database.push().getKey();
            // id-ul returnat din firebase il atasez campului id, pentru a evita ulterior inserarea multipla
            appSettings.setId(id);
        }

        // popularea in firebase cu toate valorile se face astfel: ne pozitionam in root (database), ii cautam copilul cu id-ul generat mai devreme
        // operatia de mai jos => se duce pe nodul Settings si cauta un copil cu valoarea
        // Firebase e o BD neomogena . Daca scriem database.setValue(coach) => creeaza toate campurile frati cu celelalte inregistrari existente
        // prin pozitionarea fata de child, mutam toate valorile sub umbrela unui copil din nodul principal
        database.child(appSettings.getId()).setValue(appSettings);
    }

    public void attachDataChangeEventListener(final Callback<AppSettings> callback, final String childId) {
        // eveniment declansat automat de fiecare data cand se produc modificari asupra nodului Settings
        database.child(childId).addListenerForSingleValueEvent(new ValueEventListener() {
            // daca totul se produce cu succes, se apeleaza metoda onDataChange()
            // DataSnapshot == clasa pusa la dispozitie de Firebase pentru a vedea tot ce se afla in nodul/copilul respectiv
            // aici, vedem tot ce e sub nodul root Settings
            // sunt multe aplicatii care se conecteaza la Firebase si e nevoie de un mod comun de comunicare
            // aici, inseram obiecte AppSettings. Nu putea sa ne dea un obiect AppSettings,
            // dar ne da un DataSnapshot pe care il convertim la informatiile pe care le stocam in aplicatie
            // ma astept sa am o lista de AppSettings
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppSettings appSettings = snapshot.getValue(AppSettings.class);
                //trimitem rezultatul catre activitatea prin intermediul callback-ului
                callback.runResultOnUiThread(appSettings);
            }

            // daca sunt probleme, se ajunge aici
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("FirebaseService", "Data is not available");
            }
        });
    }
}
